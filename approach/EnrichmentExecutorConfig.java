package com.equabli.collectprism.approach;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executor configuration for the enrichment pipeline.
 *
 * TWO EXECUTORS — each with a distinct purpose:
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ batchWorkerExecutor  (platform threads, fixed pool = workerCount)       │
 * │                                                                         │
 * │  Used by: PlacementEnrichmentV2.runWorkerLoop()                         │
 * │  Why platform threads?                                                  │
 * │    Worker loops are long-running (hours for 10M records). Virtual       │
 * │    threads are designed for short-lived tasks. A long-lived virtual     │
 * │    thread ties up a carrier thread for its entire lifetime, which       │
 * │    defeats the purpose of virtual threads. Platform threads are the     │
 * │    correct choice for long-running, persistent workers.                 │
 * │                                                                         │
 * │  Pool size = workerCount (default 5).                                   │
 * │  Each worker needs exactly one platform thread for its lifetime.        │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * ┌─────────────────────────────────────────────────────────────────────────┐
 * │ enrichmentExecutor  (virtual threads, unbounded)                        │
 * │                                                                         │
 * │  Used by: processBatch() partition tasks, enrichSingleAccount() handler │
 * │  Why virtual threads?                                                   │
 * │    Partition tasks and handler tasks are short-lived (< 100ms each).   │
 * │    They block on CPU-light enrichment logic. Virtual threads are cheap  │
 * │    to create (no OS thread needed), so running thousands concurrently   │
 * │    is fine. Ledger + Balance for 1000 accounts = 2000 tiny tasks        │
 * │    running simultaneously — virtual threads handle this naturally.      │
 * └─────────────────────────────────────────────────────────────────────────┘
 *
 * HIKARI POOL SIZING GUIDE:
 *
 *   Each worker loop does 3 sequential DB operations per batch:
 *     (1) claimAndMarkInProgress  — 1 connection, ~5ms
 *     (2) findAccountsWithAllRelations — 1 connection, ~50–200ms
 *     (3) persistAndFinalize      — 1 connection, ~50–100ms
 *
 *   These are SEQUENTIAL within a worker, so peak simultaneous connections = workerCount.
 *   Virtual thread partition tasks do NO DB work (enrichment is in-memory).
 *
 *   Recommended pool size:
 *     spring.datasource.hikari.maximum-pool-size = workerCount + 2
 *
 *   Example for workerCount=5:
 *     spring.datasource.hikari.maximum-pool-size=7
 *
 *   Setting it too high wastes DB connections. Setting it too low makes workers
 *   wait on connection acquisition and kills your parallelism benefit.
 */
@Configuration
public class EnrichmentExecutorConfig {

    /**
     * Platform thread pool for long-running worker loops.
     * Pool size = workerCount so each worker gets exactly one dedicated thread.
     */
    @Bean("batchWorkerExecutor")
    public ExecutorService batchWorkerExecutor(
            @Value("${enrichment.worker.count:5}") int workerCount) {

        return Executors.newFixedThreadPool(
                workerCount,
                Thread.ofPlatform()
                        .name("batch-worker-", 0)   // names: batch-worker-0, batch-worker-1 ...
                        .daemon(false)               // keep JVM alive until workers finish
                        .factory());
    }

    /**
     * Virtual thread executor for short partition/handler tasks.
     * Unbounded — each submitted task gets its own virtual thread.
     * Java 21+ only.
     */
    @Bean("enrichmentExecutor")
    public ExecutorService enrichmentExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}