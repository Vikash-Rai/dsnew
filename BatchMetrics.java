package com.equabli.collectprism;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BatchMetrics {
    public long claimMs;
    public long fetchMs;
    public long enrichMs;
    public long saveMs;
    public long totalMs;

    public int batchSize;

    @Override
    public String toString() {
        return String.format(
                "Batch(size=%d) total=%dms | claim=%d | fetch=%d | enrich=%d | save=%d",
                batchSize, totalMs, claimMs, fetchMs, enrichMs, saveMs
        );
    }
}
