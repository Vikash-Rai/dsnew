package com.equabli.collectprism.approach.enrichmentservice;

/**
 * Cache name constants for the enrichment pipeline Caffeine caches.
 *
 * FIX: Renamed ENRICHMENT_CLIENT_BY_ID → ENRICHMENT_CLIENT_SHORTNAME_BY_ID
 *      and ENRICHMENT_PRODUCT_BY_ID    → ENRICHMENT_PRODUCT_DEBTCATEGORY_BY_ID
 *
 * The caches now store scalars (String, Integer) instead of full JPA entities.
 * Naming them after what is actually cached makes this explicit and prevents
 * future callers from assuming the full entity is available.
 */
public final class EnrichmentCacheNames {

    private EnrichmentCacheNames() {}

    /**
     * Caches client.short_name (String) by clientId (Integer).
     * Was: full Client entity — only short_name was ever read from it.
     */
    public static final String ENRICHMENT_CLIENT_SHORTNAME_BY_ID = "enrichmentClientShortNameById";

    /**
     * Caches product.debtcategory_id (Integer) by productId (Integer).
     * Was: full Product entity — only debtCategoryId was ever read from it.
     */
    public static final String ENRICHMENT_PRODUCT_DEBTCATEGORY_BY_ID = "enrichmentProductDebtCategoryById";

    /**
     * Caches CYCLE_DAYS customAppConfigValueId (Integer) by compound key {clientId, productId}.
     * Null result is also cached — no config = null, which is valid.
     */
    public static final String ENRICHMENT_CYCLE_DAY_BY_CLIENT_PRODUCT = "enrichmentCycleDayByClientProduct";

    /**
     * Caches partner_type (String) by compound key {clientId, partnerId}.
     * Null result is also cached — no active partner mapping = null,
     * treated as non-HOLDING_UNIT path (QUEUE_PRP) in assignQueue().
     */
    public static final String ENRICHMENT_PARTNER_TYPE_BY_CLIENT_PARTNER = "enrichmentPartnerTypeByClientPartner";
}