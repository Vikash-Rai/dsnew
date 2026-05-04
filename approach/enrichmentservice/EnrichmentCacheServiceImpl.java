package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.repository.ClientRepository;
import com.equabli.collectprism.approach.repository.CustomAppConfigValueRepository;
import com.equabli.collectprism.approach.repository.ProductRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Caffeine-backed cache service for enrichment pipeline configuration data.
 *
 * ════════════════════════════════════════════════════════════════════
 * STAMPEDE FIX: @Cacheable → cache.get(key, loader)
 * ════════════════════════════════════════════════════════════════════
 * Root cause confirmed in logs (both Run 1 and Run 2):
 *   8 virtual threads simultaneously logged "Cache MISS [clientId=1]"
 *   then 8 threads logged "Cache POPULATED [clientId=1]"
 *   → 8 separate DB queries fired for the same key.
 *
 * Why @Cacheable caused this:
 *   Spring's @Cacheable AOP proxy checks the cache before calling the method.
 *   If N threads all miss at the same nanosecond, all N independently pass
 *   the cache check (all see "absent"), all N call the underlying method,
 *   and all N write the result. No coalescing, no blocking, pure stampede.
 *
 * Why cache.get(key, loader) fixes this:
 *   Caffeine's internal implementation uses ConcurrentHashMap.computeIfAbsent
 *   semantics. Exactly ONE thread executes the loader; all others block on
 *   the same computation and receive the result when it completes. This is
 *   not configurable — it is the fundamental contract of Caffeine's load path.
 *
 * ════════════════════════════════════════════════════════════════════
 * NULL VALUE CACHING: Optional<V> wrapper
 * ════════════════════════════════════════════════════════════════════
 * Caffeine.get(key, loader) returns null (and does NOT cache the entry)
 * when the loader returns null. To cache "no result found" as a stable
 * entry, all 4 caches store Optional<V>:
 *
 *   Present value:  Optional.of("ABC")    → cached, unwrapped to "ABC"
 *   Absent value:   Optional.empty()      → cached, unwrapped to null
 *
 * Pattern in each method:
 *   return cache.get(key, k -> Optional.ofNullable(dbLookup(k)))
 *               .orElse(null);
 *
 * ════════════════════════════════════════════════════════════════════
 * WHAT EACH CACHE REPLACES
 * ════════════════════════════════════════════════════════════════════
 * getClientShortName()       → @JoinFormula: (select cl.short_name ...)
 * getProductDebtCategoryId() → @JoinFormula: (select prd.debtcategory_id ...)
 * getCycleDayConfigValueId() → @JoinFormula: (select cav... custom_appconfig_value ...)
 * getPartnerType()           → @Formula:     (select mcp.partner_type ...)
 *
 * ════════════════════════════════════════════════════════════════════
 * LOGGING STRATEGY
 * ════════════════════════════════════════════════════════════════════
 * DB miss    → INFO  (first load per key; after fix this happens exactly once
 *                     per unique key per JVM lifetime, not N times)
 * Cache hit  → no log (suppress to avoid flooding; enable DEBUG if needed)
 * Stats      → INFO every 5 min via @Scheduled + at job end via logFinalCacheStats()
 *
 * Expected log pattern after this fix:
 *   BEFORE: "Cache MISS clientId=1" × 8, "Cache POPULATED clientId=1" × 8
 *   AFTER:  "Cache MISS clientId=1" × 1, "Cache POPULATED clientId=1" × 1
 */
@Service
@Slf4j
public class EnrichmentCacheServiceImpl implements EnrichmentCacheService {

    private final ClientRepository                  clientRepository;
    private final ProductRepository                 productRepository;
    private final CustomAppConfigValueRepository    customAppConfigValueRepository;
    private final JdbcTemplate                      jdbcTemplate;

    // Typed Cache beans injected directly — no CacheManager, no Spring AOP proxy
    private final Cache<Integer, Optional<String>>  clientShortNameCache;
    private final Cache<Integer, Optional<Integer>> productDebtCategoryCache;
    private final Cache<String,  Optional<Integer>> cycleDayCache;
    private final Cache<String,  Optional<String>>  partnerTypeCache;

    public EnrichmentCacheServiceImpl(
            ClientRepository clientRepository,
            ProductRepository productRepository,
            CustomAppConfigValueRepository customAppConfigValueRepository,
            JdbcTemplate jdbcTemplate,
            @Qualifier("clientShortNameCache")     Cache<Integer, Optional<String>>  clientShortNameCache,
            @Qualifier("productDebtCategoryCache") Cache<Integer, Optional<Integer>> productDebtCategoryCache,
            @Qualifier("cycleDayCache")            Cache<String,  Optional<Integer>> cycleDayCache,
            @Qualifier("partnerTypeCache")         Cache<String,  Optional<String>>  partnerTypeCache) {

        this.clientRepository               = clientRepository;
        this.productRepository              = productRepository;
        this.customAppConfigValueRepository = customAppConfigValueRepository;
        this.jdbcTemplate                   = jdbcTemplate;
        this.clientShortNameCache           = clientShortNameCache;
        this.productDebtCategoryCache       = productDebtCategoryCache;
        this.cycleDayCache                  = cycleDayCache;
        this.partnerTypeCache               = partnerTypeCache;
    }

    // ── Cache-aware lookups ───────────────────────────────────────────────

    /**
     * Returns client.short_name for the given clientId.
     *
     * cache.get(key, loader):
     *   - First call per clientId: ONE DB query fires, result wrapped in Optional
     *     and stored. All concurrent misses on the same key block until this
     *     single load completes — zero stampede.
     *   - Subsequent calls: nanosecond Caffeine heap lookup, zero DB cost.
     *   - "No client" result: Optional.empty() is cached — re-lookup never fires.
     */
    @Override
    public String getClientShortName(Integer clientId) {
        if (clientId == null) return null;
        return clientShortNameCache.get(clientId, id -> {
            log.info("Cache MISS [clientShortNameCache] clientId={} — loading from DB", id);
            String value = clientRepository.findShortNameById(id).orElse(null);
            log.info("Cache POPULATED [clientShortNameCache] clientId={} shortName={}", id, value);
            return Optional.ofNullable(value);
        }).orElse(null);
    }

    /**
     * Returns product.debtcategory_id for the given productId.
     *
     * Same stampede-safe, null-safe pattern as getClientShortName().
     */
    @Override
    public Integer getProductDebtCategoryId(Integer productId) {
        if (productId == null) return null;
        return productDebtCategoryCache.get(productId, id -> {
            log.info("Cache MISS [productDebtCategoryCache] productId={} — loading from DB", id);
            Integer value = productRepository.findDebtCategoryIdById(id).orElse(null);
            log.info("Cache POPULATED [productDebtCategoryCache] productId={} debtCategoryId={}", id, value);
            return Optional.ofNullable(value);
        }).orElse(null);
    }

    /**
     * Returns CYCLE_DAYS customAppConfigValueId for (clientId, productId).
     * Null if no CYCLE_DAYS config exists for that combination.
     *
     * Compound key: "clientId:productId"
     * Colon separator guarantees no collision: "12:3" ≠ "1:23" because
     * integer digits are always fully bounded by the colon on both sides.
     */
    @Override
    public Integer getCycleDayConfigValueId(Integer clientId, Integer productId) {
        if (clientId == null || productId == null) return null;
        String key = clientId + ":" + productId;
        return cycleDayCache.get(key, k -> {
            log.info("Cache MISS [cycleDayCache] clientId={} productId={} — loading from DB",
                    clientId, productId);
            Integer value = customAppConfigValueRepository
                    .findCycleDays(clientId, productId)
                    .map(cav -> cav.getCustomAppConfigValueId())
                    .orElse(null);
            log.info("Cache POPULATED [cycleDayCache] clientId={} productId={} cycleDayId={}",
                    clientId, productId, value);
            return Optional.ofNullable(value);
        }).orElse(null);
    }

    /**
     * Returns partner_type for (clientId, partnerId).
     * Null if no active mapping in conf.map_clientpartner.
     *
     * Compound key: "clientId:partnerId"
     * Null IS cached as Optional.empty() — accounts with no partner mapping
     * will never re-hit the DB after the first lookup.
     *
     * Uses JdbcTemplate directly — no MapClientPartner @Entity needed since
     * the query returns a single scalar String.
     */
    @Override
    public String getPartnerType(Integer clientId, Integer partnerId) {
        if (clientId == null || partnerId == null) return null;
        String key = clientId + ":" + partnerId;
        return partnerTypeCache.get(key, k -> {
            log.info("Cache MISS [partnerTypeCache] clientId={} partnerId={} — loading from DB",
                    clientId, partnerId);

            String sql = """
                    SELECT mcp.partner_type
                    FROM conf.map_clientpartner mcp
                    JOIN conf.partner pr ON mcp.partner_id = pr.partner_id
                    WHERE mcp.client_id        = ?
                      AND pr.partner_id        = ?
                      AND pr.record_status_id  = conf.df_record_status('ENABLED')
                      AND mcp.record_status_id = conf.df_record_status('ENABLED')
                    LIMIT 1
                    """;

            List<String> results = jdbcTemplate.queryForList(sql, String.class, clientId, partnerId);
            String value = results.isEmpty() ? null : results.get(0);
            log.info("Cache POPULATED [partnerTypeCache] clientId={} partnerId={} partnerType={}",
                    clientId, partnerId, value);
            return Optional.ofNullable(value);
        }).orElse(null);
    }

    public void logCacheStats() {
        StringBuilder sb = new StringBuilder("Enrichment cache stats:\n");
        appendStats(sb, "clientShortNameCache",     clientShortNameCache.stats());
        appendStats(sb, "productDebtCategoryCache", productDebtCategoryCache.stats());
        appendStats(sb, "cycleDayCache",            cycleDayCache.stats());
        appendStats(sb, "partnerTypeCache",          partnerTypeCache.stats());
        log.info(sb.toString());
    }

    /**
     * Call from PlacementEnrichmentProcessor.doEnrichment() at job completion
     * for a final per-run cache stats snapshot.
     */
    public void logFinalCacheStats() {
        log.info("=== FINAL enrichment cache stats (end of run) ===");
        logCacheStats();
    }

    private void appendStats(StringBuilder sb, String name, CacheStats stats) {
        sb.append(String.format(
                "  %-30s — hits=%-6d  misses=%-4d  hitRate=%.2f%%  evictions=%d%n",
                name,
                stats.hitCount(),
                stats.missCount(),
                stats.hitRate() * 100,
                stats.evictionCount()));
    }
}