package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;

/**
 * Contract for all enrichment handlers.
 *
 * Handlers run in PARALLEL (not a chain) — the Chain-of-Responsibility
 * pattern (setNext) was deliberately removed. Each handler writes to
 * its own disjoint set of Account fields; no ordering is required.
 *
 * See enrichSingleAccount() in PlacementEnrichment for how handlers
 * are composed via CompletableFuture.allOf().
 */
public interface EnrichmentService {
    void enrich(AccountEnrichment account, EnrichmentContext ctx);
}