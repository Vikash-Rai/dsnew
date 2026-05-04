package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.entity.LedgerEnrichment;
import org.springframework.stereotype.Component;

/**
 * FIX: Removed unused @Order import.
 * LedgerServiceImpl runs in PARALLEL with CurrentBalanceService
 * inside enrichSingleAccount() — they write to disjoint Account fields
 * (Ledger vs BalanceBuckets), so no ordering is needed or applied.
 */
@Component
public class LedgerEnrichmentServiceImpl extends AbstractEnrichmentService {

    @Override
    public void enrich(AccountEnrichment account, EnrichmentContext ctx) {
        account.setLedger(
                LedgerEnrichment.setLedger(
                        account,
                        ctx.getUpdatedBy(),
                        ctx.getRecordSourceId(),
                        ctx.getAppId()));
    }
}