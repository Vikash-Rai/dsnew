package com.equabli.collectprism.approach.builders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.approach.EnrichmentTransactionService;
import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.entity.AccountUpdateResult;
import com.equabli.collectprism.entity.Ledger;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EnablingAccount {

    private final EnrichmentTransactionService txService;
    private static final int BATCH_SIZE = 1000;
    private static final int THREAD_POOL_SIZE = 10;
    private static final long EXECUTOR_TIMEOUT_MINUTES = 10;

    /**
     * Entry point: processes enriched results
     * 
     * 
     */
    
    EnablingAccount(EnrichmentTransactionService txService){
    	this.txService=txService;
    }
    
    @Transactional
    public void persistFromResults(List<Long> claimedIds,
            List<AccountEnrichment> successful) {

        txService.persistBatchWithRetry(
                claimedIds,
                successful,
                status(ConfRecordStatus.RAW));

  
    }

private Integer status(String shortName) {
    return ConfRecordStatus.confRecordStatus
            .get(shortName)
            .getRecordStatusId();
}
    /**
     * Batch processing
     */

}