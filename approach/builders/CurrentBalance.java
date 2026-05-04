package com.equabli.collectprism.approach.builders;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.AccountUpdateResult;
import com.equabli.collectprism.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CurrentBalance {

    @Autowired
    AccountRepository accountRepository;

    private static final int DEFAULT_BATCH_SIZE = 1000;
    private static final long EXECUTOR_TIMEOUT_MINUTES = 10;

    public List<AccountEnrichment> enrichWithAccountIds(List<AccountEnrichment> accountIds) {
        try {
            log.info("Current balance enrichment starting for {} accounts", accountIds.size());
      
            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < accountIds.size(); i += DEFAULT_BATCH_SIZE) {
                    int end = Math.min(i + DEFAULT_BATCH_SIZE, accountIds.size());
                    List<AccountEnrichment> batch = accountIds.subList(i, end);
                    executor.submit(() -> processBatch(batch));
                }

                waitForExecutorTermination(executor);
            }

            log.info("Current balance enrichment completed: {} accounts", accountIds.size());

        } catch (Exception e) {
            log.error("Current balance enrichment failed", e);
        }

        return accountIds;
    }

    private void processBatch(List<AccountEnrichment> batch) {
        for (AccountEnrichment acc : batch) {
            try {
            	acc.setCurrentbalanceDate(LocalDate.now());
            	acc.setAmtCurrentbalance(acc.getAmtAssigned());
            	acc.setAmtPrincipalCurrentbalance(acc.getAmtPrincipalAssigned());
            	acc.setAmtInterestCurrentbalance(acc.getAmtInterestAssigned());
            	acc.setAmtLatefeeCurrentbalance(acc.getAmtLatefeeAssigned());
            	acc.setAmtOtherfeeCurrentbalance(acc.getAmtOtherfeeAssigned());
            	acc.setAmtCourtcostCurrentbalance(acc.getAmtCourtcostAssigned());
            	acc.setAmtAttorneyfeeCurrentbalance(acc.getAmtAttorneyfeeAssigned());
                //results.put(acc.getAccountId(), accUpdate);

            } catch (Exception e) {
                log.error("Error processing current balance for accountId={}", acc.getAccountId(), e);
            }
        }
    }

    private void waitForExecutorTermination(ExecutorService executor) throws InterruptedException {
        executor.shutdown();

        if (!executor.awaitTermination(EXECUTOR_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
            log.warn("Executor timeout, forcing shutdown");
            executor.shutdownNow();
        }
    }
}