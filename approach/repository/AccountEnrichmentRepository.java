package com.equabli.collectprism.approach.repository;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Dedicated repository for the enrichment pipeline.
 * Uses AccountEnrichment - the lean entity that skips
 * all unused @JoinFormula lookups present on the full Account entity.
 */
@Repository
public interface AccountEnrichmentRepository
        extends JpaRepository<AccountEnrichment, Long> {

    @Query("""
        SELECT a FROM AccountEnrichment a
        WHERE a.accountId IN :ids
    """)
    List<AccountEnrichment> findForEnrichment(@Param("ids") List<Long> ids);
}
