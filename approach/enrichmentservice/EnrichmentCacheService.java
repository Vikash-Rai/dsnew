package com.equabli.collectprism.approach.enrichmentservice;

/**
 * Cache contract for the enrichment pipeline.
 *
 * FIX: Methods now return scalars instead of full JPA entities.
 *
 * BEFORE:
 *   Client  getClient(Integer clientId)   → cached whole entity, only .getShortName() was used
 *   Product getProduct(Integer productId) → cached whole entity, only .getDebtCategoryId() was used
 *
 * AFTER:
 *   String  getClientShortName(Integer clientId)       → caches exactly what is needed
 *   Integer getProductDebtCategoryId(Integer productId) → caches exactly what is needed
 *
 * This removes the import dependency on Client and Product entities from the
 * cache service layer, reduces GC pressure (no entity object graphs held in
 * Caffeine), and makes the intent of each cache unambiguous.
 *
 * Callers (PlacementEnrichmentProcessor) no longer need to extract the scalar
 * from the returned entity — they receive the value directly.
 */
public interface EnrichmentCacheService {

    /**
     * Returns client.short_name for the given clientId.
     * Null if no enabled client found.
     */
    String getClientShortName(Integer clientId);

    /**
     * Returns product.debtcategory_id for the given productId.
     * Null if no enabled product found.
     */
    Integer getProductDebtCategoryId(Integer productId);

    /**
     * Returns CYCLE_DAYS customAppConfigValueId for (clientId, productId).
     * Null if no CYCLE_DAYS config exists for that combination.
     */
    Integer getCycleDayConfigValueId(Integer clientId, Integer productId);

    /**
     * Returns partner_type string for (clientId, partnerId).
     * Null if no active partner mapping exists — treated as non-HOLDING_UNIT
     * in SolEnrichmentServiceImpl.assignQueue().
     */
    String getPartnerType(Integer clientId, Integer partnerId);
}