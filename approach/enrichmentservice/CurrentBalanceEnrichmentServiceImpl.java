package com.equabli.collectprism.approach.enrichmentservice;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class CurrentBalanceEnrichmentServiceImpl extends AbstractEnrichmentService {

    @Override
    public void enrich(AccountEnrichment account, EnrichmentContext ctx) {
        account.setCurrentbalanceDate(LocalDate.now());
        account.setAmtCurrentbalance(account.getAmtAssigned());
        account.setAmtPrincipalCurrentbalance(account.getAmtPrincipalAssigned());
        account.setAmtInterestCurrentbalance(account.getAmtInterestAssigned());
        account.setAmtLatefeeCurrentbalance(account.getAmtLatefeeAssigned());
        account.setAmtOtherfeeCurrentbalance(account.getAmtOtherfeeAssigned());
        account.setAmtCourtcostCurrentbalance(account.getAmtCourtcostAssigned());
        account.setAmtAttorneyfeeCurrentbalance(account.getAmtAttorneyfeeAssigned());
    }
}