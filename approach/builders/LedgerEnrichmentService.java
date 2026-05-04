package com.equabli.collectprism.approach.builders;

import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.entity.LedgerEnrichment;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.AccountUpdateResult;
import com.equabli.collectprism.entity.Ledger;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.domain.entity.ConfApp;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * LedgerEnrichment processes ledger data for accounts using account IDs.
 * Loads accounts only when needed, applies enrichment, and returns updated entities.
 */
@Component
@Slf4j
public class LedgerEnrichmentService {
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	AccountRepository accountRepository;

	/**
	 * Enrich ledger data for accounts by ID.
	 * Loads account data, applies ledger enrichment, and returns updated accounts.
	 *
	 * @param accountIds List of account IDs to enrich
	 * @return List of updated Account entities with ledger data
	 */
	public List<AccountEnrichment> enrichWithAccountIds(
	        List<AccountEnrichment> accountIds, String updatedBy, Integer appId, Integer recordSourceId) {
	    try {
	        log.info("Ledger enrichment starting for {} accounts", accountIds.size());
	        for (AccountEnrichment account : accountIds) {
	        	 account.setLedger(
	                     LedgerEnrichment.setLedger(
	                             account,
	                             updatedBy,
	                             recordSourceId,
	                             appId));
	     
	        }

	        log.info("Ledger enrichment completed for {} accounts", accountIds.size());
	        return accountIds;

	    } catch (Exception e) {
	        log.error("Ledger enrichment failed", e);
	        return accountIds;
	    }
	}

	/**
	 * Enrich ledger data for Account objects (legacy support).
	 * Kept for backward compatibility.
	 *
	 * @param accounts List of Account entities to enrich
	 * @return List of updated Account entities with ledger data
	 */
	public List<Account> enrich(List<Account> accounts) {
		try {
			log.info("Ledger enrichment (legacy): {} accounts on thread: {}", 
				accounts.size(), Thread.currentThread().getName());

			// Extract request details for audit trail
			//Map<String, Object> requestDetailsMap = extractRequestDetails();
			String updatedBy = "tst";
			Integer appId = 1;
			Integer recordSourceId = 1;

			// Apply ledger enrichment to each account
			accounts.forEach(account ->
					account.setLedger(Ledger.setLedger(account, updatedBy, recordSourceId, appId))
			);

			log.info("Ledger enrichment completed for {} accounts", accounts.size());
			return accounts;

		} catch (Exception e) {
			log.error("Ledger enrichment failed", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Extract request details from HttpServletRequest headers.
	 * Falls back to default ECP_AP/ECP_BAT if not provided in request.
	 */
//	private Map<String, Object> extractRequestDetails() {
//		Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
//
//		if (!requestDetailsMap.containsKey(CommonConstants.APP_ID)) {
//			log.warn("APP_ID not in request headers, using defaults");
//			requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(
//					ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(),
//					ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId()
//			);
//		}
//
//		return requestDetailsMap;
//	}
}