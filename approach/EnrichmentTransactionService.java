package com.equabli.collectprism.approach;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.entity.LedgerEnrichment;
import com.equabli.collectprism.approach.repository.AccountEnrichmentRepository;
import com.equabli.collectprism.approach.repository.LedgerEnrichmentRepository;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.domain.entity.ConfRecordStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Handles all DB writes for the enrichment pipeline via JdbcTemplate.
 *
 * ════════════════════════════════════════════════════════════════════
 * FIX: @Transactional retry bug in persistBatchWithRetry
 * ════════════════════════════════════════════════════════════════════
 * PROBLEM (original code):
 *   persistBatchWithRetry() was annotated @Transactional at the method level.
 *   Spring's AOP proxy wraps the ENTIRE method in a single transaction.
 *   If attempt 1 throws DataAccessException, Spring marks that transaction
 *   for rollback BEFORE control returns to the catch block.
 *   Attempts 2 and 3 then execute inside the same already-marked-rollback
 *   transaction — they are guaranteed to fail with
 *   "Transaction marked as rollback-only" regardless of what they do.
 *   The retry loop was effectively a no-op.
 *
 * FIX:
 *   Remove @Transactional from persistBatchWithRetry().
 *   Use TransactionTemplate to wrap EACH attempt in its own independent
 *   transaction. Attempt 1 fails → its transaction rolls back cleanly →
 *   attempt 2 starts a fresh transaction → genuinely independent retry.
 *
 * TransactionTemplate is configured with:
 *   - PROPAGATION_REQUIRES_NEW: each attempt is always a new transaction,
 *     never nested inside a caller's transaction.
 *   - timeout = 30s: prevents indefinite lock waits when PostgreSQL row-level
 *     locks from a slow concurrent writer hold up the batch UPDATE.
 *
 * ════════════════════════════════════════════════════════════════════
 * PERFORMANCE: JdbcTemplate batchUpdate vs Hibernate saveAll
 * ════════════════════════════════════════════════════════════════════
 * batchInsertLedgers  → single JDBC batch INSERT, no IDENTITY key roundtrip
 * batchUpdateAccounts → single JDBC batch UPDATE, no Hibernate merge/re-SELECT
 *
 * The batch size parameter in jdbcTemplate.batchUpdate(sql, list, list.size(), setter)
 * is set to list.size() — the entire list is sent in ONE network round trip.
 * saveChunkSize in PlacementEnrichmentProcessor controls how large that list is.
 * With saveChunkSize=500 (set in application.properties), one 500-account chunk
 * = one round trip for UPDATEs + one round trip for INSERTs = 2 total per chunk.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EnrichmentTransactionService {

    private final AccountRepository            accountRepository;
    private final LedgerEnrichmentRepository   ledgerRepository;
    private final AccountEnrichmentRepository  enrichmentRepository;
    private final JdbcTemplate                 jdbcTemplate;
    private final PlatformTransactionManager   transactionManager;


    @Transactional
    public List<Long> claimAndMarkInProgress(int rawStatusId,
                                             int inProgressStatusId,
                                             int batchSize) {
        List<Long> ids = accountRepository.claimRawAccountIds(rawStatusId, batchSize);
        if (!ids.isEmpty()) {
            accountRepository.updateStatus(ids, inProgressStatusId);
            log.info("Claimed {} accounts RAW → INPROGRESS", ids.size());
        }
        return ids;
    }

    // ── Save ──────────────────────────────────────────────────────────────

    /**
     * Persists a batch of enriched accounts with up to 3 independent retries.
     *
     * NOT annotated @Transactional — each attempt manages its own transaction
     * via TransactionTemplate so that a failed attempt does not poison
     * subsequent attempts with a rollback-only flag.
     */
    public void persistBatchWithRetry(List<Long> claimedIds,
                                      List<AccountEnrichment> successful,
                                      int rawStatusId) {
        int maxRetries = 3;
        Exception lastException = null;

        // Each TransactionTemplate instance is independent — a fresh transaction
        // is started for every attempt.
        TransactionTemplate txTemplate = new TransactionTemplate(transactionManager);
        txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        txTemplate.setTimeout(30); // 30-second timeout — prevents indefinite lock waits

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                final int currentAttempt = attempt;
                txTemplate.executeWithoutResult(status -> {
                    // Ledger INSERTs — single JDBC batch, no generated-key roundtrip
                    List<LedgerEnrichment> ledgers = successful.stream()
                            .map(AccountEnrichment::getLedger)
                            .filter(Objects::nonNull)
                            .toList();

                    if (!ledgers.isEmpty()) {
                        batchInsertLedgers(ledgers);
                    }

                    // Account UPDATEs — single JDBC batch, bypasses Hibernate entirely
                    batchUpdateAccounts(successful);

                    // SUSPECTED propagation — 4 bulk native UPDATEs, zero entity loading
                    propagateSuspectedStatus(successful);

                    log.debug("Persist attempt {}/{} committed — {} accounts",
                            currentAttempt, maxRetries, successful.size());
                });

                return; // committed successfully

            } catch (Exception ex) {
                // DataAccessException or transaction timeout — transaction already rolled back
                lastException = ex;
                log.warn("Persist attempt {}/{} failed and rolled back: {}",
                        attempt, maxRetries, ex.getMessage());
                if (attempt < maxRetries) sleepQuietly(200L * attempt);
            }
        }

        // All retries exhausted — reset claimed accounts back to RAW
        try {
            // Use a separate short transaction for the reset - must NOT fail silently
            TransactionTemplate resetTx = new TransactionTemplate(transactionManager);
            resetTx.setTimeout(10);
            resetTx.executeWithoutResult(status ->
                    accountRepository.updateStatus(claimedIds, rawStatusId));
            log.warn("Reset {} accounts back to RAW after {} failed persist attempts",
                    claimedIds.size(), maxRetries);
        } catch (Exception rollbackEx) {
            log.error("CRITICAL: Could not reset {} accounts to RAW after persist failure. "
                            + "Manual intervention needed. IDs: {}",
                    claimedIds.size(), claimedIds, rollbackEx);
        }

        throw new RuntimeException(
                "Persist failed after " + maxRetries + " attempts for "
                        + successful.size() + " accounts", lastException);
    }

    // ── Batch INSERT ──────────────────────────────────────────────────────

    /**
     * Batch INSERT for LedgerEnrichment via JdbcTemplate.
     *
     * The entire list is sent as a single JDBC batch (batchSize = ledgers.size()).
     * With reWriteBatchedInserts=true in the JDBC URL (application.properties),
     * PostgreSQL rewrites this into a multi-row VALUES INSERT:
     *   INSERT INTO data.ledger (...) VALUES (...), (...), (...), ...
     * reducing N network round trips to 1.
     *
     * ledgerId is not returned — it's @Transient on AccountEnrichment
     * and not referenced after save.
     */
    private void batchInsertLedgers(List<LedgerEnrichment> ledgers) {
        String sql = """
                INSERT INTO data.ledger (
                    client_id, account_id, dt_transaction, transaction_id, transaction_type,
                    dt_ledger, amt_ledger, amt_principal, amt_interest, amt_latefee,
                    amt_otherfee, amt_courtcost, amt_attorneyfee,
                    record_status_id, created_by, updated_by, record_source_id, app_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, ledgers, ledgers.size(), (ps, ledger) -> {
            ps.setObject(1,  ledger.getClientId());
            ps.setObject(2,  ledger.getAccountId());
            ps.setObject(3,  ledger.getTransactionDate() != null
                    ? Date.valueOf(ledger.getTransactionDate()) : null);
            ps.setObject(4,  ledger.getTransactionId());
            ps.setObject(5,  ledger.getTransactionType());
            ps.setObject(6,  ledger.getLedgerDate() != null
                    ? Date.valueOf(ledger.getLedgerDate()) : null);
            ps.setObject(7,  ledger.getAmtLedger());
            ps.setObject(8,  ledger.getAmtPrincipal());
            ps.setObject(9,  ledger.getAmtInterest());
            ps.setObject(10, ledger.getAmtLatefee());
            ps.setObject(11, ledger.getAmtOtherfee());
            ps.setObject(12, ledger.getAmtCourtcost());
            ps.setObject(13, ledger.getAmtAttorneyfee());
            ps.setObject(14, ledger.getRecordStatusId());
            ps.setObject(15, ledger.getCreatedBy());
            ps.setObject(16, ledger.getUpdatedBy());
            ps.setObject(17, ledger.getRecordSourceId());
            ps.setObject(18, ledger.getAppId());
        });

        log.debug("Batch-inserted {} ledger records", ledgers.size());
    }

    // ── Batch UPDATE ──────────────────────────────────────────────────────

    /**
     * Batch UPDATE for AccountEnrichment via JdbcTemplate.
     *
     * Bypasses Hibernate entirely — no merge(), no re-SELECT, no dirty tracking,
     * no @JoinFormula subqueries. The entire list is sent as a single JDBC batch.
     *
     * With reWriteBatchedInserts=true in the JDBC URL, PostgreSQL's driver
     * also rewrites batched UPDATE/DELETE statements to minimise round trips.
     *
     * SET clause contains only the columns enrichment handlers write.
     * Immutable columns (client_id, client_account_number, product_id,
     * dtm_utc_create) are excluded — @DynamicUpdate on the entity
     * provides a second layer of protection at the Hibernate level, though
     * this path bypasses Hibernate entirely.
     */
    private void batchUpdateAccounts(List<AccountEnrichment> accounts) {
        String sql = """
                UPDATE data.account SET
                    record_status_id               = ?,
                    dt_statute                     = ?,
                    dt_equabli_statute             = ?,
                    queue_id                       = ?,
                    queuestatus_id                 = ?,
                    queuereason_id                 = ?,
                    err_short_name                 = ?,
                    dt_currentbalance              = ?,
                    amt_currentbalance             = ?,
                    amt_principal_currentbalance   = ?,
                    amt_interest_currentbalance    = ?,
                    amt_latefee_currentbalance     = ?,
                    amt_otherfee_currentbalance    = ?,
                    amt_courtcost_currentbalance   = ?,
                    amt_attorneyfee_currentbalance = ?,
                    amt_pre_charge_off_balance     = ?,
                    amt_pre_charge_off_principle   = ?,
                    amt_pre_charge_off_interest    = ?,
                    amt_pre_charge_off_fee         = ?,
                    updated_by                     = ?,
                    app_id                         = ?,
                    record_source_id               = ?
                WHERE account_id = ?
                """;

        jdbcTemplate.batchUpdate(sql, accounts, accounts.size(), (ps, account) -> {
            ps.setObject(1,  account.getRecordStatusId());
            ps.setObject(2,  account.getSolDate() != null
                    ? Date.valueOf(account.getSolDate()) : null);
            ps.setObject(3,  account.getEquabliSolDate() != null
                    ? Date.valueOf(account.getEquabliSolDate()) : null);
            ps.setObject(4,  account.getQueueId());
            ps.setObject(5,  account.getQueueStatusId());
            ps.setObject(6,  account.getQueueReasonId());
            ps.setObject(7,  account.getErrShortName());
            ps.setObject(8,  account.getCurrentbalanceDate() != null
                    ? Date.valueOf(account.getCurrentbalanceDate()) : null);
            ps.setObject(9,  account.getAmtCurrentbalance());
            ps.setObject(10, account.getAmtPrincipalCurrentbalance());
            ps.setObject(11, account.getAmtInterestCurrentbalance());
            ps.setObject(12, account.getAmtLatefeeCurrentbalance());
            ps.setObject(13, account.getAmtOtherfeeCurrentbalance());
            ps.setObject(14, account.getAmtCourtcostCurrentbalance());
            ps.setObject(15, account.getAmtAttorneyfeeCurrentbalance());
            ps.setObject(16, account.getAmtPreChargeOffBalance());
            ps.setObject(17, account.getAmtPreChargeOffPrinciple());
            ps.setObject(18, account.getAmtPreChargeOffInterest());
            ps.setObject(19, account.getAmtPreChargeOffFees());
            ps.setObject(20, account.getUpdatedBy());
            ps.setObject(21, account.getAppId());
            ps.setObject(22, account.getRecordSourceId());
            ps.setObject(23, account.getAccountId());
        });

        log.debug("Batch-updated {} accounts", accounts.size());
    }

    // ── SUSPECTED propagation ─────────────────────────────────────────────

    /**
     * Propagates SUSPECTED status to consumer hierarchy via 4 bulk native UPDATEs.
     * Executes 4 SQL statements regardless of suspected account count.
     * Zero entity loading — pure SQL.
     */
    private void propagateSuspectedStatus(List<AccountEnrichment> accounts) {
        Integer suspectedStatusId = ConfRecordStatus.confRecordStatus
                .get(ConfRecordStatus.SUSPECTED).getRecordStatusId();

        List<String> suspectedAccountNumbers = accounts.stream()
                .filter(a -> suspectedStatusId.equals(a.getRecordStatusId()))
                .map(AccountEnrichment::getClientAccountNumber)
                .filter(Objects::nonNull)
                .toList();

        if (suspectedAccountNumbers.isEmpty()) return;

        log.info("Propagating SUSPECTED status for {} accounts", suspectedAccountNumbers.size());
        accountRepository.updateConsumerToSuspectedByList(suspectedAccountNumbers, suspectedStatusId);
        accountRepository.updateAddressToSuspectedByList(suspectedAccountNumbers, suspectedStatusId);
        accountRepository.updatePhoneToSuspectedByList(suspectedAccountNumbers, suspectedStatusId);
        accountRepository.updateEmailToSuspectedByList(suspectedAccountNumbers, suspectedStatusId);
    }

    // ── Recovery ──────────────────────────────────────────────────────────

    @Transactional
    public void recoverStuckInProgress(int inProgressStatusId, int rawStatusId,
                                       LocalDateTime threshold) {
        int recovered = accountRepository
                .recoverStaleRecords(inProgressStatusId, rawStatusId, threshold);
        if (recovered > 0) {
            log.warn("Recovered {} stuck INPROGRESS → RAW", recovered);
        }
    }

    // ── Utilities ─────────────────────────────────────────────────────────

    private void sleepQuietly(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Persist retry sleep interrupted");
        }
    }
}