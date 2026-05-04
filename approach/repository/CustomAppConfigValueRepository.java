package com.equabli.collectprism.approach.repository;

import com.equabli.collectprism.entity.CustomAppConfigValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for conf.custom_appconfig_value.
 *
 * findCycleDays() replicates the @JoinFormula that was on AccountEnrichment:
 *
 *   (select cav.custom_appconfig_value_id
 *    from conf.custom_appconfig_value cav
 *    join conf.appconfig_name an on cav.appconfig_name_id = an.appconfig_name_id
 *      and an.short_name = 'CYCLE_DAYS'
 *    where cav.client_id = client_id
 *      and cav.reference_id = product_id
 *      and cav.reference_type = 'PRODUCT')
 *
 * This query fires ONCE per unique (clientId, productId) combination,
 * then the result is held in Caffeine forever (no TTL — CYCLE_DAYS config
 * does not change while enrichment is running).
 *
 * Subsequent calls for the same (clientId, productId) return from Caffeine
 * in nanoseconds — zero DB round trips.
 */
@Repository
public interface CustomAppConfigValueRepository extends JpaRepository<CustomAppConfigValue, Integer> {

    @Query(value = """
        SELECT cav.*
        FROM conf.custom_appconfig_value cav
        JOIN conf.appconfig_name an
          ON cav.appconfig_name_id = an.appconfig_name_id
         AND an.short_name = 'CYCLE_DAYS'
        WHERE cav.client_id    = :clientId
          AND cav.reference_id = :productId
          AND cav.reference_type = 'PRODUCT'
        LIMIT 1
        """, nativeQuery = true)
    Optional<CustomAppConfigValue> findCycleDays(
            @Param("clientId")  Integer clientId,
            @Param("productId") Integer productId);
}