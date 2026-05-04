package com.equabli.collectprism.approach.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PartitionResult {

    /** Accounts that were enriched successfully — ready to be saved. */
    private final List<AccountEnrichment> successful;

    /** Account IDs that failed enrichment — will be reset to RAW in bulk. */
    private final List<Long> failedIds;

    /** Convenience: true if at least one account failed in this partition. */
    public boolean hasFailures() {
        return !failedIds.isEmpty();
    }
}
