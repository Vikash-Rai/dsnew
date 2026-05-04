package com.equabli.collectprism.approach.repository;

import com.equabli.collectprism.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for conf.client.
 * Used exclusively by EnrichmentCacheServiceImpl to load client.short_name
 * once per clientId, then cache it in Caffeine as a scalar String.
 *
 * FIX: Added findShortNameById() projection query.
 * The old approach loaded the full Client entity just to call .getShortName().
 * This projection fetches only the one column that enrichment actually needs,
 * keeping the Caffeine heap footprint minimal (String vs full entity object graph).
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    /**
     * Returns only client.short_name for the given clientId.
     * Used by EnrichmentCacheServiceImpl.getClientShortName().
     */
    @Query("SELECT c.shortName FROM Client c WHERE c.clientId = :clientId")
    Optional<String> findShortNameById(@Param("clientId") Integer clientId);
}