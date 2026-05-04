package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.entity.ScrubWarning;
import com.equabli.collectprism.entity.SolAccountDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Per-account enrichment context passed to all handlers.
 *
 * ROUND 1: solDto              — replaced Consumer graph (6,660 subqueries)
 * ROUND 2: clientShortName,    — replaced 3 @JoinFormula @ManyToOne associations
 *          productDebtCategoryId,   (client, product, customAppConfigValue)
 *          cycleDay
 * ROUND 3: partnerType         — replaced @Formula on AccountEnrichment
 *
 * THREAD SAFETY FIX:
 *   errWarMessagesList was initialized as a plain ArrayList passed in from outside.
 *   Four handlers run in parallel via StructuredTaskScope — any handler calling
 *   ctx.getErrWarMessagesList().add(...) from a virtual thread caused concurrent
 *   mutation of a non-thread-safe list.
 *
 *   Fix: constructor always wraps into CopyOnWriteArrayList regardless of what
 *   the caller passes in. The field type stays List<String> (interface) so callers
 *   don't need to change. CopyOnWriteArrayList is correct here because:
 *     - Writes (add) are rare — only on validation failure paths
 *     - Reads (contains, iterate) are frequent — every handler may check codes
 *     - CopyOnWriteArrayList gives thread-safe reads with zero locking cost,
 *       and thread-safe writes by copying the backing array on each mutation
 *
 *   scrubWarnings already used CopyOnWriteArrayList — now consistent.
 *   validationMap already used ConcurrentHashMap — no change needed.
 *   accountLock (ReentrantLock) — no change needed.
 */
@Getter
@Setter
public class EnrichmentContext {

    // ── Thread-safe collections ───────────────────────────────────────────

    /**
     * FIX: Was assigned directly from constructor parameter (plain ArrayList).
     * Now always wrapped in CopyOnWriteArrayList to guarantee thread-safe
     * concurrent reads and writes from parallel handler virtual threads.
     */
    private final List<String> errWarMessagesList;

    /** Already thread-safe — CopyOnWriteArrayList. No change. */
    @Setter(AccessLevel.NONE)
    private List<ScrubWarning> scrubWarnings;

    /** Already thread-safe — ConcurrentHashMap. No change. */
    private final Map<String, Boolean> validationMap;

    /** Guards AccountEnrichment.recordStatusId across concurrent handler writes. */
    private final ReentrantLock accountLock;

    // ── Immutable context (set once before handlers run) ─────────────────

    private final String  updatedBy;
    private final Integer appId;
    private final Integer recordSourceId;

    // ── SOL pre-fetch (ROUND 1) ───────────────────────────────────────────

    private SolAccountDTO solDto;

    // ── Cached config from Caffeine (ROUND 2) ────────────────────────────

    /**
     * client.shortName — from Caffeine.
     * Used by SolEnrichmentServiceImpl, PreChargeOffEnrichmentServiceImpl.
     */
    private String clientShortName;

    /**
     * product.debtCategoryId — from Caffeine.
     * Used by SolEnrichmentServiceImpl.
     */
    private Integer productDebtCategoryId;

    /**
     * CYCLE_DAYS customAppConfigValueId — from Caffeine. Null = no config.
     * Used by SolEnrichmentServiceImpl.
     */
    private Integer cycleDay;

    // ── Cached partner_type (ROUND 3) ────────────────────────────────────

    /**
     * partner_type string for this account's (clientId, partnerId).
     * Null means no active partner mapping → treated as non-HOLDING_UNIT in assignQueue().
     * NOTE: Currently always null because partner_id is null in DB for all accounts.
     *       Cache is wired correctly — no code change needed here.
     */
    private String partnerType;

    // ── Mutable enrichment state ──────────────────────────────────────────

    private boolean primaryDebtorFound;

    // ── Constructor ───────────────────────────────────────────────────────

    /**
     * @param errWarMessages Pre-populated error/warning codes for this account.
     *                       Wrapped into CopyOnWriteArrayList regardless of input type —
     *                       callers do not need to provide a thread-safe list.
     */
    public EnrichmentContext(List<String> errWarMessages,
                             String updatedBy,
                             Integer appId,
                             Integer recordSourceId) {

        // FIX: wrap in CopyOnWriteArrayList — thread-safe for concurrent handler access
        this.errWarMessagesList = new CopyOnWriteArrayList<>(
                errWarMessages != null ? errWarMessages : List.of());

        this.updatedBy          = updatedBy;
        this.appId              = appId;
        this.recordSourceId     = recordSourceId;
        this.validationMap      = new ConcurrentHashMap<>();
        this.scrubWarnings      = new CopyOnWriteArrayList<>();
        this.primaryDebtorFound = false;
        this.validationMap.put("isAccountValidated", Boolean.TRUE);
        this.accountLock        = new ReentrantLock();
    }

    // ── Convenience methods ───────────────────────────────────────────────

    /**
     * Thread-safe — delegates to CopyOnWriteArrayList.contains().
     * Safe to call from any parallel handler virtual thread.
     */
    public boolean contains(String code) {
        return errWarMessagesList.contains(code);
    }

    /**
     * Thread-safe — delegates to CopyOnWriteArrayList.add().
     * Any handler can add a warning code without synchronization.
     */
    public void addErrWarMessage(String code) {
        errWarMessagesList.add(code);
    }

    /**
     * Thread-safe — delegates to CopyOnWriteArrayList.addAll().
     */
    public void addAllErrWarMessages(Collection<String> codes) {
        errWarMessagesList.addAll(codes);
    }

    public boolean isAccountValid() {
        return Boolean.TRUE.equals(
                validationMap.getOrDefault("isAccountValidated", Boolean.TRUE));
    }

    public void markInvalid() {
        validationMap.put("isAccountValidated", Boolean.FALSE);
    }
}