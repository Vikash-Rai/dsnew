package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.domain.entity.ConfRecordStatus;

/**
 * FIX 5 continued: setRecordStatusSafe now gets the lock from ctx
 * instead of requiring a separate parameter.
 *
 * This removes the "lock created but not passed" bug in v6.
 * Every handler subclass calls setRecordStatusSafe(account, statusId, ctx)
 * and the lock is always the correct per-account instance.
 */
public abstract class AbstractEnrichmentService implements EnrichmentService {

    @Override
    public abstract void enrich(AccountEnrichment account, EnrichmentContext ctx);

    /**
     * Thread-safe write for recordStatusId.
     * Lock is taken from ctx — guaranteed to be the correct per-account lock.
     */
    protected void setRecordStatusSafe(AccountEnrichment account,
                                       Integer statusId,
                                       EnrichmentContext ctx) {
        ctx.getAccountLock().lock();
        try {
            account.setRecordStatusId(statusId);
        } finally {
            ctx.getAccountLock().unlock();
        }
    }

    protected void markSuspectedSafe(AccountEnrichment account, EnrichmentContext ctx) {
        setRecordStatusSafe(
                account,
                ConfRecordStatus.confRecordStatus
                        .get(ConfRecordStatus.SUSPECTED)
                        .getRecordStatusId(),
                ctx
        );
    }
}