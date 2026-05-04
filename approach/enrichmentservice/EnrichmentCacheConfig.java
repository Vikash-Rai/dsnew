package com.equabli.collectprism.approach.enrichmentservice;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Exposes 4 typed Caffeine Cache beans for the enrichment pipeline.
 *
 * ════════════════════════════════════════════════════════════════════
 * WHY THIS REPLACES THE PREVIOUS CacheManagerCustomizer APPROACH
 * ════════════════════════════════════════════════════════════════════
 * The previous design used @Cacheable + CacheManagerCustomizer, which has
 * two fundamental problems:
 *
 *   1. STAMPEDE: Spring's @Cacheable AOP proxy does not coalesce concurrent
 *      misses. If 8 threads miss the same key simultaneously, all 8 independently
 *      execute the DB query. The logs confirmed exactly 8 concurrent misses per
 *      key on every cold start.
 *
 *   2. NULL CACHING: @Cacheable does not cache null returns by default.
 *      unless="false" was added as a workaround, but it relies on Spring's
 *      proxy intercepting the return — which is unreliable for self-invocation
 *      and can be bypassed in edge cases.
 *
 * ════════════════════════════════════════════════════════════════════
 * THE FIX: Direct Caffeine Cache beans
 * ════════════════════════════════════════════════════════════════════
 * Caffeine's native Cache.get(key, loader) DOES coalesce concurrent misses:
 *   - If N threads simultaneously miss the same key, Caffeine allows exactly
 *     ONE thread to execute the loader function.
 *   - All other N-1 threads block on the same future and receive the result
 *     once the single loader call completes.
 *   - This is guaranteed by Caffeine's internal ConcurrentHashMap + computeIfAbsent
 *     semantics — not a configuration option, it is the default behaviour.
 *
 * NULL VALUE CACHING:
 *   Caffeine.get(key, loader) does NOT cache null loader return values — it
 *   treats null as "entry absent" and re-invokes the loader on next access.
 *   To cache null results, all 4 caches use Cache<K, Optional<V>>:
 *     - Loader wraps result in Optional.ofNullable(result)
 *     - Optional.empty() IS a non-null value → Caffeine caches it normally
 *     - Caller unwraps with cache.get(...).orElse(null)
 *   This is the standard pattern for null-safe Caffeine caching.
 *
 * ════════════════════════════════════════════════════════════════════
 * COMPOUND KEYS
 * ════════════════════════════════════════════════════════════════════
 * cycleDayCache and partnerTypeCache use Cache<String, Optional<V>> with
 * key = clientId + ":" + productId (or partnerId).
 * The colon separator guarantees no collision: "12:3" ≠ "1:23" because
 * the integer before the colon is always fully bounded by it.
 *
 * ════════════════════════════════════════════════════════════════════
 * @EnableCaching IS INTENTIONALLY ABSENT
 * ════════════════════════════════════════════════════════════════════
 * We no longer use @Cacheable. Spring's caching AOP proxy is not needed.
 * If @EnableCaching exists elsewhere in the application, it has no effect
 * on this pipeline (there are no @Cacheable annotations left to intercept).
 *
 * CACHE SIZING:
 *   clientShortNameCache     — max 500  (unique clients in the system)
 *   productDebtCategoryCache — max 500  (unique products)
 *   cycleDayCache            — max 2500 (up to 500 clients × 500 products)
 *   partnerTypeCache         — max 2500 (up to 500 clients × 500 partners)
 *
 * NO EXPIRY — conf.client, conf.product, conf.custom_appconfig_value, and
 * conf.map_clientpartner are configuration tables that do not change during
 * an enrichment run. Cache is valid for the JVM lifetime.
 *
 * recordStats() is enabled on all 4 caches.
 * Disable in production if the minor overhead is unwanted — but keep it for
 * at least the first few deployments to verify hit rates via logCacheStats().
 */
@Configuration
@Slf4j
public class EnrichmentCacheConfig {

    /**
     * Caches client.short_name (String) keyed by clientId (Integer).
     * Optional<String> wrapper allows caching null (no client found).
     */
    @Bean
    public Cache<Integer, Optional<String>> clientShortNameCache() {
        Cache<Integer, Optional<String>> cache = Caffeine.newBuilder()
                .maximumSize(500)
                .recordStats()
                .build();
        log.info("Enrichment cache created: clientShortNameCache (maxSize=500)");
        return cache;
    }

    /**
     * Caches product.debtcategory_id (Integer) keyed by productId (Integer).
     * Optional<Integer> wrapper allows caching null (no product found).
     */
    @Bean
    public Cache<Integer, Optional<Integer>> productDebtCategoryCache() {
        Cache<Integer, Optional<Integer>> cache = Caffeine.newBuilder()
                .maximumSize(500)
                .recordStats()
                .build();
        log.info("Enrichment cache created: productDebtCategoryCache (maxSize=500)");
        return cache;
    }

    /**
     * Caches CYCLE_DAYS customAppConfigValueId (Integer) keyed by
     * compound String "clientId:productId".
     * Optional<Integer> wrapper allows caching null (no CYCLE_DAYS config).
     */
    @Bean
    public Cache<String, Optional<Integer>> cycleDayCache() {
        Cache<String, Optional<Integer>> cache = Caffeine.newBuilder()
                .maximumSize(2500)
                .recordStats()
                .build();
        log.info("Enrichment cache created: cycleDayCache (maxSize=2500)");
        return cache;
    }

    /**
     * Caches partner_type (String) keyed by compound String "clientId:partnerId".
     * Optional<String> wrapper allows caching null (no active partner mapping).
     */
    @Bean
    public Cache<String, Optional<String>> partnerTypeCache() {
        Cache<String, Optional<String>> cache = Caffeine.newBuilder()
                .maximumSize(2500)
                .recordStats()
                .build();
        log.info("Enrichment cache created: partnerTypeCache (maxSize=2500)");
        return cache;
    }
}