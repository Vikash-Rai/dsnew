package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.exception.EnrichmentHandlerException;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PreChargeOffEnrichmentServiceImpl extends AbstractEnrichmentService {

    @Override
    public void enrich(AccountEnrichment account, EnrichmentContext ctx) {
        try {
            if (CommonUtils.isObjectNull(account.getChargeOffDate())) {
                account.setAmtPreChargeOffBalance(account.getAmtAssigned());
                account.setAmtPreChargeOffPrinciple(account.getAmtPrincipalAssigned());
                account.setAmtPreChargeOffInterest(account.getAmtInterestAssigned());

                if (CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME
                        .equals(ctx.getClientShortName())) {

                    Double sum =
                            (CommonUtils.isDoubleNull(account.getAmtLatefeeAssigned())    ? 0.00 : account.getAmtLatefeeAssigned())
                                    + (CommonUtils.isDoubleNull(account.getAmtOtherfeeAssigned())   ? 0.00 : account.getAmtOtherfeeAssigned())
                                    + (CommonUtils.isDoubleNull(account.getAmtCourtcostAssigned())  ? 0.00 : account.getAmtCourtcostAssigned())
                                    + (CommonUtils.isDoubleNull(account.getAmtAttorneyfeeAssigned())? 0.00 : account.getAmtAttorneyfeeAssigned());

                    account.setAmtPreChargeOffFees(sum);
                } else {
                    account.setAmtPreChargeOffFees(
                            CommonUtils.isDoubleNull(account.getAmtLatefeeAssigned())
                                    ? 0.00
                                    : account.getAmtLatefeeAssigned());
                }
            }
        } catch (Exception e) {
            log.warn("setPreChargeOffBuckets failed for accountId={} — {}",
                    account.getAccountId(), e.getMessage(), e);
            throw new EnrichmentHandlerException(
                    "PreChargeOff calculation failed for accountId=" + account.getAccountId(), e);
        }
    }
}