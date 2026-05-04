package com.equabli.collectprism.approach.repository;

import com.equabli.collectprism.approach.entity.LedgerEnrichment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LedgerEnrichmentRepository extends JpaRepository<LedgerEnrichment, Long> {

}