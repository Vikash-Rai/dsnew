package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.exception.EnrichmentHandlerException;
import com.equabli.collectprism.entity.SolAccountDTO;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.domain.*;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.SOLCalculation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * This handler reads exclusively from EnrichmentContext — zero entity
 * association navigation, zero lazy-load risk, zero DB calls during enrichment.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SolEnrichmentServiceImpl extends AbstractEnrichmentService {

    @Override
    public void enrich(AccountEnrichment account, EnrichmentContext ctx) {
        try {
            if (isSkipStatus(account)) {
                account.setErrShortName(null);
                account.setErrCodeJson(null);
                return;
            }

            if (shouldMarkSuspected(account)) {
                markSuspectedSafe(account, ctx);
                ctx.getValidationMap().put("needsSuspectedPropagation", Boolean.TRUE);
                return;
            }

            // STEP 1: Set SOLWAIT
            setRecordStatusSafe(account,
                    ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SOLWAIT).getRecordStatusId(),
                    ctx);

            // STEP 2: Build SOL input — all values from ctx, zero DB calls
            Integer solMonth             = 0;
            Integer clientConfiguredDays = 0;

            SOLCalulation solCalulation = new SOLCalulation();
            setSolCalculation(account, ctx, solCalulation);

            SolAccountDTO dto = ctx.getSolDto();
            if (dto != null) {
                solCalulation.setStateCode(dto.getStateCode());
                if (dto.getCountryStateId() != null)
                    solCalulation.setCountryStateId(dto.getCountryStateId().intValue());
                if (dto.getSolMonth() != null)
                    solMonth = dto.getSolMonth().intValue();
                if (dto.getSolDay() != null)
                    clientConfiguredDays = dto.getSolDay().intValue();
            }

            Response<StatuteOfLimitation> response = SOLCalculation.solCalculation(
                    account.getAccountId(), solCalulation,
                    solMonth, clientConfiguredDays, new Response<>());

            StatuteOfLimitation sol = response.getResponse();

            // STEP 3: Apply SOL result
            if (sol != null && sol.getIsPrimaryStateExists()) {
                if (CommonUtils.isDateNull(sol.getCurrentSOLDate())) {
                    updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                            sol.getCurrentSOLDate(), true, false, account, ctx);

                } else if (CommonUtils.isObjectNull(sol.getCalculatedSOLDate())) {
                    updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                            sol.getCurrentSOLDate(), false, true, account, ctx);

                } else {
                    int cmp = sol.getCalculatedSOLDate().compareTo(sol.getCurrentSOLDate());
                    if (cmp > 0)
                        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                                sol.getCurrentSOLDate(), false, true, account, ctx);
                    else if (cmp < 0)
                        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                                sol.getCurrentSOLDate(), false, false, account, ctx); // SOL conflict — stay SOLWAIT
                    else
                        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                                sol.getCurrentSOLDate(), true, false, account, ctx);
                }

            } else if (sol != null) {
                updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(),
                        sol.getCurrentSOLDate(), false, true, account, ctx);
            } else {
                setRecordStatusSafe(account,
                        ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
                        ctx);
                //log.info("SOL is not calculated");
            }

            // STEP 4: Assign queue
            assignQueue(account, ctx);

        } catch (Exception e) {
            log.warn("SOL enrichment failed — accountId={} — {}", account.getAccountId(), e.getMessage(), e);
            throw new EnrichmentHandlerException(
                    "SOL enrichment failed for accountId=" + account.getAccountId(), e);
        }
    }

    // HELPERS

    private boolean isSkipStatus(AccountEnrichment account) {
        Integer status = account.getRecordStatusId();
        if (status == null) return false;
        return status.equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
                || status.equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SOLWAIT).getRecordStatusId());
    }

    private boolean shouldMarkSuspected(AccountEnrichment account) {
        return account.getRecordStatusId().equals(
                ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
                || (!CommonUtils.isObjectNull(account.getErrCodeJson())
                && DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(account.getErrCodeJson()));
    }

    private void assignQueue(AccountEnrichment account, EnrichmentContext ctx) {
        if (!CommonUtils.isStringNullOrBlank(ctx.getPartnerType())
                && CommonConstants.PARTNER_TYPE_HOLDING_UNIT.equals(ctx.getPartnerType())) {

            account.setQueueId(Queue.confQueue.get(Queue.QUEUE_DNP).getQueueId());
            account.setQueueStatusId(
                    QueueStatus.confQueueStatus.get(QueueStatus.QUEUESTATUS_DNP).getQueueStatusId());
        } else {
            account.setQueueId(Queue.confQueue.get(Queue.QUEUE_PRP).getQueueId());
            account.setQueueStatusId(
                    QueueStatus.confQueueStatus.get(QueueStatus.QUEUESTATUS_OPN).getQueueStatusId());
        }
        account.setQueueReasonId(
                QueueReason.confQueueReason.get(QueueReason.QUEUEREASON_NA).getQueueReasonId());
    }

    public static void updateSOLDateDetailsInAccount(
            LocalDate equabliDate, LocalDate clientDate,
            boolean useEquabliDate, boolean useClientDate,
            AccountEnrichment account, EnrichmentContext ctx) {

        if (!useEquabliDate && !useClientDate) return; // SOL conflict — stay SOLWAIT

        if (useEquabliDate) {
            account.setSolDate(equabliDate);
            account.setEquabliSolDate(equabliDate);
            account.setRecordStatusId(
                    ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
        } else {
            account.setSolDate(clientDate);
            account.setRecordStatusId(
                    ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
        }
    }

    /**
     * Populates SOLCalculation from account scalars + EnrichmentContext cached fields.
     * Zero entity association navigation - everything comes from plain columns or Caffeine.
     */
    private static void setSolCalculation(AccountEnrichment account,
                                         EnrichmentContext ctx,
                                         SOLCalulation solCalulation) {
        solCalulation.setEquabliAccountId(account.getAccountId());
        solCalulation.setProductId(account.getProductId());
        solCalulation.setLastPaymentDate(account.getLastPaymentDate());
        solCalulation.setChargeOffDate(account.getChargeOffDate());
        solCalulation.setSOLDate(account.getSolDate());
        solCalulation.setClientId(account.getClientId());
        solCalulation.setPartnerId(account.getPartnerId());
        solCalulation.setDtDelinquency(account.getDelinquencyDate());
        solCalulation.setDtClientStatute(account.getClientSolDate());
        solCalulation.setDtEquabliStatute(account.getEquabliSolDate());

        // All three from Caffeine — zero DB cost after first load per unique key
        solCalulation.setClientCode(ctx.getClientShortName());
        solCalulation.setDebtCategoryId(ctx.getProductDebtCategoryId());
        if (ctx.getCycleDay() != null) {
            solCalulation.setCycleDay(ctx.getCycleDay());
        }
    }
}