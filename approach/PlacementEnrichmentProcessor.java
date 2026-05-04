package com.equabli.collectprism.approach;

import com.equabli.collectprism.approach.enrichmentservice.*;
import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.entity.PartitionResult;
import com.equabli.collectprism.approach.repository.AccountEnrichmentRepository;
import com.equabli.collectprism.entity.SolAccountDTO;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.domain.entity.ConfRecordStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 *  fetchSolDtoMap() — replaced Consumer graph (thousand subqueries).
 *  cache resolution for client, product, cycleDay — replaced 3 @JoinFormula.
 *  cache resolution for partnerType — replaced @Formula on AccountEnrichment.
 */
@Component
@Slf4j
public class PlacementEnrichmentProcessor {

    private final AccountRepository                   accountRepository;
    private final AccountEnrichmentRepository         enrichmentRepository;
    private final EnrichmentTransactionService        txService;
    private final EnrichmentCacheService              cacheService;
    private final LedgerEnrichmentServiceImpl         ledgerEnrichmentService;
    private final CurrentBalanceEnrichmentServiceImpl currentBalanceEnrichmentService;
    private final PreChargeOffEnrichmentServiceImpl   preChargeOffEnrichmentService;
    private final SolEnrichmentServiceImpl            solEnrichmentServiceImpl;
    private final ExecutorService                     batchWorkerExecutor;

    private final AtomicLong totalBatchTime = new AtomicLong();
    private final AtomicLong totalBatches   = new AtomicLong();

    @Value("${enrichment.batch.size}")              private int  batchSize;
    @Value("${enrichment.partition.size}")          private int  partitionSize;
    @Value("${enrichment.save.chunk.size}")         private int  saveChunkSize;
    @Value("${enrichment.worker.count}")            private int  workerCount;
    @Value("${enrichment.worker.empty-retries}")    private int  maxEmptyRetries;
    @Value("${enrichment.worker.retry-wait-ms}")    private long retryWaitMs;

    public PlacementEnrichmentProcessor(
            AccountRepository accountRepository,
            AccountEnrichmentRepository enrichmentRepository,
            EnrichmentTransactionService txService,
            EnrichmentCacheService cacheService,
            LedgerEnrichmentServiceImpl ledgerEnrichmentService,
            CurrentBalanceEnrichmentServiceImpl currentBalanceEnrichmentService,
            PreChargeOffEnrichmentServiceImpl preChargeOffEnrichmentService,
            SolEnrichmentServiceImpl solEnrichmentServiceImpl,
            @Qualifier("batchWorkerExecutor") ExecutorService batchWorkerExecutor) {

        this.accountRepository               = accountRepository;
        this.enrichmentRepository            = enrichmentRepository;
        this.txService                       = txService;
        this.cacheService                    = cacheService;
        this.ledgerEnrichmentService         = ledgerEnrichmentService;
        this.currentBalanceEnrichmentService = currentBalanceEnrichmentService;
        this.preChargeOffEnrichmentService   = preChargeOffEnrichmentService;
        this.solEnrichmentServiceImpl        = solEnrichmentServiceImpl;
        this.batchWorkerExecutor             = batchWorkerExecutor;
    }



    public void doEnrichment(String updatedBy, Integer appId, Integer recordSourceId) {
        OperatingSystemMXBean osBean =
                (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        int cores = osBean.getAvailableProcessors();
        long totalRam = osBean.getTotalPhysicalMemorySize();
        long freeRam = osBean.getFreePhysicalMemorySize();
        double cpuLoad = osBean.getSystemCpuLoad();

        log.info("Cores: " + cores);
        log.info("Total RAM: " + totalRam / (1024 * 1024) + " MB");
        log.info("Free RAM: " + freeRam / (1024 * 1024) + " MB");
        log.info("CPU Load: " + cpuLoad);

        log.info("Enrichment started — workers={} batchSize={} partitionSize={} saveChunkSize={}",
                workerCount, batchSize, partitionSize, saveChunkSize);

        List<CompletableFuture<Void>> workers = IntStream.range(0, workerCount)
                .mapToObj(workerId -> CompletableFuture.runAsync(
                        () -> runWorkerLoop(workerId, updatedBy, appId, recordSourceId),
                        batchWorkerExecutor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(workers.toArray(new CompletableFuture[0])).join();

        // Final cache stats snapshot for the completed run
        if (cacheService instanceof EnrichmentCacheServiceImpl impl) {
            impl.logFinalCacheStats();
        }

        log.info("Enrichment completed — all {} workers finished", workerCount);
    }

    // Worker loop
    private void runWorkerLoop(int workerId, String updatedBy, Integer appId, Integer recordSourceId) {
        log.info("Worker-{} started", workerId);
        int emptyRetries = 0;

        while (true) {
            long batchStart = System.currentTimeMillis();
            long claimMs, fetchMs, enrichMs, saveMs;

            // STEP 1: Claim
            long t = System.currentTimeMillis();
            List<Long> claimedIds = txService.claimAndMarkInProgress(
                    status(ConfRecordStatus.RAW),
                    status(ConfRecordStatus.INPROGRESS),
                    batchSize);
            claimMs = System.currentTimeMillis() - t;

            if (claimedIds.isEmpty()) {
                if (emptyRetries < maxEmptyRetries) {
                    emptyRetries++;
                    log.debug("Worker-{} empty (retry {}/{})", workerId, emptyRetries, maxEmptyRetries);
                    sleep(retryWaitMs);
                    continue;
                }
                log.info("Worker-{} no RAW accounts after {} retries — exiting", workerId, maxEmptyRetries);
                break;
            }
            emptyRetries = 0;

            // STEP 2a: Fetch AccountEnrichment — clean SELECT, zero subqueries
            t = System.currentTimeMillis();
            List<AccountEnrichment> accounts = enrichmentRepository.findForEnrichment(claimedIds);
            if (accounts == null) accounts = Collections.emptyList();
            fetchMs = System.currentTimeMillis() - t;

            if (accounts.isEmpty()) {
                log.warn("Worker-{} no entities for {} claimed IDs — reverting to RAW",
                        workerId, claimedIds.size());
                accountRepository.updateStatus(claimedIds, status(ConfRecordStatus.RAW));
                continue;
            }

            // STEP 2b: Pre-fetch SOL data — one batch SQL JOIN for stateCode + SOL months
            Map<Long, SolAccountDTO> solDtoMap = fetchSolDtoMap(claimedIds);

            // STEP 3: Enrich in parallel partitions
            t = System.currentTimeMillis();
            List<AccountEnrichment> successful =
                    processBatch(accounts, solDtoMap, updatedBy, appId, recordSourceId);
            enrichMs = System.currentTimeMillis() - t;

            // STEP 4: Save in chunks via JdbcTemplate — no Hibernate merge/re-SELECT
            t = System.currentTimeMillis();
            if (!successful.isEmpty()) saveInChunks(workerId, successful);
            saveMs = System.currentTimeMillis() - t;

            long totalMs = System.currentTimeMillis() - batchStart;
            totalBatchTime.addAndGet(totalMs);
            long count = totalBatches.incrementAndGet();

            log.info("Worker-{} Batch(size={}) total={}ms | claim={}ms | fetch={}ms | enrich={}ms | save={}ms | avgBatch={}ms",
                    workerId, claimedIds.size(), totalMs,
                    claimMs, fetchMs, enrichMs, saveMs,
                    count > 0 ? totalBatchTime.get() / count : 0);
        }
    }


    /**
     * SOL pre-fetch
     *
     * Calls the trimmed fetchSolAccounts query (5 columns only — stateCode,
     * countryStateId, solMonth, solDay, accountId).
     */
    public Map<Long, SolAccountDTO> fetchSolDtoMap(List<Long> accountIds) {
        try {
            return accountRepository.fetchSolAccounts(accountIds).stream()
                    .collect(Collectors.toMap(
                            SolAccountDTO::getAccountId,
                            Function.identity(),
                            (existing, replacement) -> existing));
        } catch (Exception e) {
            log.warn("fetchSolAccounts failed - SOL will use empty DTO: {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // Batch / partition processing
    private List<AccountEnrichment> processBatch(
            List<AccountEnrichment> accounts, Map<Long, SolAccountDTO> solDtoMap,
            String updatedBy, Integer appId, Integer recordSourceId) {

        // Plain lists — populated on main thread after scope.join(), no concurrent access
        List<AccountEnrichment> allSuccessful = new ArrayList<>();
        List<Long>              allFailedIds  = new ArrayList<>();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Fork one virtual-thread task per partition
            var tasks = partition(accounts, partitionSize).stream()
                    .map(p -> scope.fork(() ->
                            processPartition(p, solDtoMap, updatedBy, appId, recordSourceId)))
                    .collect(Collectors.toList());
            // Wait for ALL partitions to finish (or for shutdown if one fatally crashes)
            try {
                scope.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("processBatch interrupted — collecting partial results");
                // Fall through: collect whatever finished before the interrupt
            }
            // ── Collect results — runs on main thread, zero concurrency ──────
            for (var task : tasks) {
                if (task.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                    PartitionResult result = task.get();
                    allSuccessful.addAll(result.getSuccessful());
                    allFailedIds.addAll(result.getFailedIds());
                } else if (task.state() == StructuredTaskScope.Subtask.State.FAILED) {
                    // This branch is hit ONLY for catastrophic failures (OOM etc.)
                    // because processPartition() catches all normal exceptions itself.
                    // We cannot know which accounts were in this partition from here,
                    // so we log and accept that those accounts stay INPROGRESS.
                    // The stale-recovery job will reset them on the next scheduled run.
                    log.error("Partition task failed with unrecoverable exception: {}",
                            task.exception().getMessage(), task.exception());
                }
                // UNAVAILABLE state = task never started (scope already shut down before fork ran)
                // Same treatment: stale recovery will clean these up.
            }

        } catch (Exception e) {
            log.error("Unexpected scope-level error in processBatch — {} accounts may be lost",
                    accounts.size(), e);
        }

        // ── Single bulk DB update for ALL failed IDs (the key fix) ────────
        // Called OUTSIDE the scope, on the main thread, after all partitions complete.
        // One updateStatus() call for potentially hundreds of failed IDs:
        //   BEFORE: up to ceil(batchSize / partitionSize) separate DB calls
        //   AFTER:  exactly ONE DB call regardless of how many partitions had failures
        if (!allFailedIds.isEmpty()) {
            try {
                accountRepository.updateStatus(allFailedIds, status(ConfRecordStatus.RAW));
                log.warn("Reset {} failed accounts back to RAW", allFailedIds.size());
            } catch (Exception e) {
                log.error("CRITICAL: Could not reset {} failed accounts to RAW. " +
                                "IDs: {} — manual intervention needed",
                        allFailedIds.size(), allFailedIds, e);
            }
        }

        log.info("processBatch complete — success={} failed={}",
                allSuccessful.size(), allFailedIds.size());

        return allSuccessful;
    }

    private PartitionResult processPartition(
            List<AccountEnrichment> partition,
            Map<Long, SolAccountDTO> solDtoMap,
            String updatedBy,
            Integer appId,
            Integer recordSourceId) {

        List<AccountEnrichment> successful = new ArrayList<>(partition.size());
        List<Long>              failedIds  = new ArrayList<>();

        for (AccountEnrichment account : partition) {
            try {
                enrichSingleAccount(account, solDtoMap, updatedBy, appId, recordSourceId);
                successful.add(account);
            } catch (Exception ex) {
                log.error("Enrichment failed accountId={} — {}",
                        account.getAccountId(), ex.getMessage(), ex);
                failedIds.add(account.getAccountId());
            }
        }

        return new PartitionResult(successful, failedIds);
        // always returns, never throws — ShutdownOnFailure never triggers from here
    }

    /**
     * Populates EnrichmentContext with all data the handlers need.
     * Zero DB calls after the first batch — everything comes from Caffeine or the
     * pre-fetched solDtoMap.
     *
     * Context population:
     *   1. solDto                — solDtoMap lookup (pre-fetched, in-memory HashMap)
     *   2. clientShortName       — Caffeine scalar cache
     *   3. productDebtCategoryId — Caffeine scalar cache
     *   4. cycleDay              — Caffeine scalar cache
     *   5. partnerType           — Caffeine scalar cache
     */
    private void enrichSingleAccount(AccountEnrichment account,
                                     Map<Long, SolAccountDTO> solDtoMap,
                                     String updatedBy, Integer appId, Integer recordSourceId) throws Exception {

        EnrichmentContext ctx = new EnrichmentContext(
                new ArrayList<>(), updatedBy, appId, recordSourceId);

        // SOL address data (stateCode, countryStateId, solMonth, solDay)
        ctx.setSolDto(solDtoMap.get(account.getAccountId()));

        // All config from Caffeine; null is valid for every field
        ctx.setClientShortName(
                cacheService.getClientShortName(account.getClientId()));

        ctx.setProductDebtCategoryId(
                cacheService.getProductDebtCategoryId(account.getProductId()));

        ctx.setCycleDay(
                cacheService.getCycleDayConfigValueId(account.getClientId(), account.getProductId()));

        ctx.setPartnerType(
                cacheService.getPartnerType(account.getClientId(), account.getPartnerId()));

        // Run all four handlers in parallel on virtual threads
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            scope.fork(() -> { ledgerEnrichmentService.enrich(account, ctx);         return null; });
            scope.fork(() -> { currentBalanceEnrichmentService.enrich(account, ctx); return null; });
            scope.fork(() -> { preChargeOffEnrichmentService.enrich(account, ctx);   return null; });
            scope.fork(() -> { solEnrichmentServiceImpl.enrich(account, ctx);        return null; });

            try {
                scope.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new Exception("Interrupted during handler execution for accountId=" + account.getAccountId(), e);
            }
            scope.throwIfFailed(ex ->
                    new Exception("Handler failed for accountId=" + account.getAccountId(), ex));
        }

        account.setUpdatedBy(updatedBy);
        account.setAppId(appId);
        account.setRecordSourceId(recordSourceId);
    }

    // Chunked save
    private void saveInChunks(int workerId, List<AccountEnrichment> successful) {
        List<List<AccountEnrichment>> chunks = partition(successful, saveChunkSize);
        int idx = 0;
        for (List<AccountEnrichment> chunk : chunks) {
            List<Long> chunkIds = chunk.stream().map(AccountEnrichment::getAccountId).toList();
            try {
                txService.persistBatchWithRetry(chunkIds, chunk, status(ConfRecordStatus.RAW));
                log.debug("Worker saved chunk {}/{}", ++idx, chunks.size());
            } catch (Exception ex) {
                log.error("Chunk {}/{} failed — {} accounts requeued as RAW",
                        ++idx, chunks.size(), chunkIds.size(), ex);
            }
        }
    }

    // Utilities
    private Integer status(String shortName) {
        return ConfRecordStatus.confRecordStatus.get(shortName).getRecordStatusId();
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size)
            result.add(list.subList(i, Math.min(i + size, list.size())));
        return result;
    }

    private void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Worker sleep interrupted");
        }
    }
}