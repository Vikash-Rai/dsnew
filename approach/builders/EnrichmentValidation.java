package com.equabli.collectprism.approach.builders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import com.equabli.domain.entity.ConfApp;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.equabli.datascrubbing.rule.ValidationRule;
import com.equabli.collectprism.approach.EnrichmentTransactionService;
import com.equabli.collectprism.approach.PlacementEnrichmentProcessor;
import com.equabli.collectprism.approach.enrichmentservice.CurrentBalanceEnrichmentServiceImpl;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import com.equabli.collectprism.approach.enrichmentservice.LedgerEnrichmentServiceImpl;
import com.equabli.collectprism.approach.enrichmentservice.PreChargeOffEnrichmentServiceImpl;
import com.equabli.collectprism.approach.enrichmentservice.SolEnrichmentServiceImpl;
import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.approach.repository.AccountEnrichmentRepository;
import com.equabli.collectprism.approach.validationhandler.ValidationResult;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.AccountUpdateResult;
import com.equabli.collectprism.entity.SolAccountDTO;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AddressRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.repository.EmailRepository;
import com.equabli.collectprism.repository.PhoneRepository;
import com.equabli.domain.entity.ConfRecordStatus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EnrichmentValidation implements ValidationRule{
	
	 @Autowired
     AccountRepository accountRepository;
	 
	 @Autowired
	 ConsumerRepository consumerRepository;
	 
	 @Autowired
	 AddressRepository addressRepository;
	 
	 @Autowired
	 PhoneRepository phoneRepository;
	 
	 @Autowired
	 EmailRepository emailRepository;
	 
	 @Autowired
	 SolCalculation solCalculation;
	 
	 @Autowired
	 EnablingAccount enablingAccount;
	 
	 @Autowired
	 LedgerEnrichmentService ledgerEnrichment;

     @Autowired
     CurrentBalance currentBalance;
     
     @Autowired
     AccountEnrichmentRepository accountEnrichmentRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private PlacementEnrichmentProcessor placementEnrichmentProcessor;

    @Value("${enrichment.engine:v1}")
    private String enrichmentEngine;
	 
	private static final int THREADS = 5;
    private static final int BATCH_SIZE = 1000;
    private final ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    
    public List<ValidationResult> execute() {
		List<ValidationResult> response = new ArrayList<>();
		while (true) {

			// ── Y JUNCTION ────────────────────────────────────────
			// enrichment.engine=PlacementEnrichmentProcessor → PlacementEnrichmentProcessor path
			// enrichment.engine=legacy → existing doAccountEnrichmentProcess path
			// ─────────────────────────────────────────────────────
			ValidationResult accountBatch;

			if ("v1".equalsIgnoreCase(enrichmentEngine)) {
				log.info("Delegating account enrichment to PlacementEnrichmentProcessor");
				accountBatch = executePlacementEnrichmentProcessor();
			} else {
				List<Long> accountsIds = accountRepository.claimRawAccountIds(
						ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId(), BATCH_SIZE);

				accountBatch = doAccountEnrichmentProcess(accountsIds);
			}
			ValidationResult resultBatch = processBatch();
			response.add(resultBatch);
			response.add(accountBatch);

			if (!accountBatch.isPassed()) {
				log.error("Batch failed: {}", accountBatch.getMessage());
			} else if (!resultBatch.isPassed()) {
				log.error("Batch failed: {}", resultBatch.getMessage());
			} else {
				log.info("Batch processed successfully");
			}
			return response;
		}

	}

	private ValidationResult processBatch() {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

			var consumerTask = scope.fork(this::doConsumerEnrichment);
			var emailTask = scope.fork(this::doEmailEnrichment);
			var phoneTask = scope.fork(this::doPhoneEnrichment);
			var addressTask = scope.fork(this::doAddressEnrichment);

			scope.join();
			scope.throwIfFailed();

			List<ValidationResult> results = List.of(consumerTask.get(), emailTask.get(), phoneTask.get(),
					addressTask.get());

			// ✅ Return first failure or success
			return results.stream().filter(r -> !r.isPassed()).findFirst().orElse(ValidationResult.passed());

		} catch (Exception e) {
			log.error("Batch execution failed", e);
			return ValidationResult.failed("BATCH_ERROR", e.getMessage());
		}
	}

//	private void processLoop() {
//        while (true) {
//            // 🔒 STEP 1: Claim records (VERY IMPORTANT)
//            List<Account> accountIds = accountRepository.clam(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId(),BATCH_SIZE);
//            if (accountIds == null || accountIds.isEmpty()) {
//                break;
//            }
//            try {
//                // 🔥 Parallel enrichment inside batch (optional)
//
//            	//account Enrihcment only  - doa
//            	doAccountEnrichment(accountIds);
//              
//            } catch (Exception e) {
//                System.err.println("Error processing batch: " + e.getMessage());
//            }
//        }
//    }

	private void doAccountEnrichment(List<Long> accounts) {
		// if(SolProcess)
		doAccountEnrichmentProcess(accounts);
	}

	// Main method to process account enrichment
    public ValidationResult doAccountEnrichmentProcess(List<Long> accountIds) {
        // Step 1: Fetch account details in batches
    	long t1 = System.currentTimeMillis();
    	log.info("Fetched {} started in {} ms", t1);
        List<AccountEnrichment> rawAccountsDetails = fetchAccountEnrichmentsInBatches(accountIds);
        log.info("Fetched {} changes in {} ms",
                System.currentTimeMillis() - t1);
        // Step 2: Fetch user/app/request details
        
       long t = System.currentTimeMillis();
       Map<Long, SolAccountDTO> solDtoMap = placementEnrichmentProcessor.fetchSolDtoMap(accountIds);
        
        Map<String, Object> requestDetailsMap = fetchRequestDetails();
        String updatedBy = getUpdatedBy(requestDetailsMap);
        Integer appId = getAppId(requestDetailsMap);
        Integer recordSourceId = getRecordSourceId(requestDetailsMap);

        // Step 3: Run parallel tasks for enrichment processing
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<List<AccountEnrichment>>> subtasks = new ArrayList<>();

            // Fork parallel tasks for enrichment steps
            subtasks.add(scope.fork(() -> doSolCalculation(rawAccountsDetails,solDtoMap)));
            subtasks.add(scope.fork(() -> doLedgerProcess(rawAccountsDetails, updatedBy, appId, recordSourceId)));
            subtasks.add(scope.fork(() -> doCurrentBalanceHandling(rawAccountsDetails)));
            subtasks.add(scope.fork(() -> doEnabling(rawAccountsDetails, accountIds)));

            scope.join();
            scope.throwIfFailed();

            // Return successful validation result if all tasks complete successfully
            return ValidationResult.passed();

        } catch (Exception e) {
            log.error("Account enrichment process failed", e);
            return ValidationResult.failed("ACCOUNT_BATCH_ERROR", e.getMessage());
        }
    }
    // Fetch details from HTTP request
    private Map<String, Object> fetchRequestDetails() {
        return CommonUtils.getDetailsFromRequestHeaders(request);
    }

    // Method to fetch account enrichments in batches
    private List<AccountEnrichment> fetchAccountEnrichmentsInBatches(List<Long> accountIds) {
        List<List<Long>> batches = batchAccountIds(accountIds, 100);
        List<AccountEnrichment> allEnrichedAccounts = new ArrayList<>();

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            List<StructuredTaskScope.Subtask<List<AccountEnrichment>>> subtasks = new ArrayList<>();

            for (List<Long> batch : batches) {
                subtasks.add(scope.fork(() -> accountEnrichmentRepository.findForEnrichment(batch)));
            }

            scope.join();
            scope.throwIfFailed();

            // Collect the results from all subtasks
            for (StructuredTaskScope.Subtask<List<AccountEnrichment>> subtask : subtasks) {
                allEnrichedAccounts.addAll(subtask.get());
            }

        } catch (Exception e) {
            log.error("Error fetching account enrichments", e);
        }

        return allEnrichedAccounts;
    }

    // Helper method to split account IDs into smaller batches
    private List<List<Long>> batchAccountIds(List<Long> accountIds, int batchSize) {
        List<List<Long>> batches = new ArrayList<>();
        for (int i = 0; i < accountIds.size(); i += batchSize) {
            batches.add(accountIds.subList(i, Math.min(i + batchSize, accountIds.size())));
        }
        return batches;
    }
    

    // Extract 'updatedBy' from request details
    private String getUpdatedBy(Map<String, Object> requestDetailsMap) {
        return requestDetailsMap.get(CommonConstants.USER_KEY).toString();
    }

    // Extract App ID from request details
    private Integer getAppId(Map<String, Object> requestDetailsMap) {
        if (!requestDetailsMap.containsKey(CommonConstants.APP_ID)) {
            requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(
                    ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(),
                    ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId()
            );
        }
        return Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString());
    }

    // Extract Record Source ID from request details
    private Integer getRecordSourceId(Map<String, Object> requestDetailsMap) {
        return Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString());
    }

    
	private void merge(Map<Long, AccountUpdateResult> target, Map<Long, AccountUpdateResult> source) {

		source.forEach((accountId, incoming) -> {

			target.merge(accountId, incoming, (existing, newVal) -> {
				existing.merge(newVal); // your merge logic
				return existing;
			});

		});
	}
	
	  
	/**
	 * Merge enriched accounts from multiple enrichment tasks. If an account appears
	 * in multiple task results, keeps the last (most recently updated) version.
	 */
	private Map<Long, Account> mergeEnrichedAccounts(List<Account> allEnrichedAccounts) {
		Map<Long, Account> mergedMap = new LinkedHashMap<>();

		for (Account account : allEnrichedAccounts) {
			mergedMap.put(account.getAccountId(), account);
		}

		log.info("Merged {} enriched accounts ({} unique)", allEnrichedAccounts.size(), mergedMap.size());

		return mergedMap;
	}

	private ValidationResult doConsumerEnrichment() {
		try {
			consumerRepository.updateConsumer();
			log.info("Consumer enrichment running: " + Thread.currentThread().getName());
			sleep();
			return ValidationResult.passed();
		} catch (Exception e) {
			log.error("Consumer enrichment failed", e);
			return ValidationResult.failed("CONSUMER_ERROR", e.getMessage());
		}
	}

	private ValidationResult doEmailEnrichment() {
		try {
			emailRepository.updateEmail();
			log.info("Email enrichment running: " + Thread.currentThread().getName());
			sleep();
			return ValidationResult.passed();
		} catch (Exception e) {
			log.error("Email enrichment failed", e);
			return ValidationResult.failed("EMAIL_ERROR", e.getMessage());
		}
	}

	private ValidationResult doPhoneEnrichment() {
		try {
			phoneRepository.updatePhone();
			log.info("Phone enrichment running: " + Thread.currentThread().getName());
			sleep();
			return ValidationResult.passed();
		} catch (Exception e) {
			log.error("Phone enrichment failed", e);
			return ValidationResult.failed("PHONE_ERROR", e.getMessage());
		}
	}

	private ValidationResult doAddressEnrichment() {
		try {
			addressRepository.updateAddress();
			log.info("Address enrichment running: " + Thread.currentThread().getName());
			sleep();
			return ValidationResult.passed();
		} catch (Exception e) {
			log.error("Address enrichment failed", e);
			return ValidationResult.failed("ADDRESS_ERROR", e.getMessage());
		}
	}

	private List<AccountEnrichment> doSolCalculation(
			List<AccountEnrichment> accountIds,Map<Long, SolAccountDTO> solDtoMap) {
		try {
			log.info("SOL Calculation starting for {} accounts on thread: {}", accountIds.size(),
					Thread.currentThread().getName());

			List<AccountEnrichment> updatedResult = solCalculation
					.processAccountsBatchesWithVirtualThreads(accountIds,solDtoMap);

			log.info("SOL Calculation completed for {} accounts", updatedResult);
			return updatedResult;

		} catch (Exception e) {
			log.error("SOL Calculation failed", e);
			return new ArrayList<>() ;// ✅ Return empty list on error, not null
		}
	}

	// ✅ CORRECT - Returns List<Account>
	private List<AccountEnrichment> doLedgerProcess(List<AccountEnrichment> accountIds,String updatedBy, Integer appId, Integer recordSourceId) {
		try {
			log.info("Ledger enrichment starting for {} accounts on thread: {}", accountIds.size(),
					Thread.currentThread().getName());

			List<AccountEnrichment> updateLedger = ledgerEnrichment.enrichWithAccountIds(accountIds,updatedBy,appId,recordSourceId);

			log.info("Ledger enrichment completed for {} accounts", updateLedger.size());
			return updateLedger;

		} catch (Exception e) {
			log.error("Ledger enrichment failed", e);
			 return new ArrayList<>() ; // ✅ Return empty list on error
		}
	}

	// ✅ CORRECT - Returns List<Account>
	private List<AccountEnrichment> doCurrentBalanceHandling(List<AccountEnrichment> accountIds) {
		try {
			log.info("Current balance handling starting for {} accounts on thread: {}", accountIds.size(),
					Thread.currentThread().getName());
			List<AccountEnrichment> updatedAccounts = currentBalance.enrichWithAccountIds(accountIds);
			log.info("Current balance handling completed for {} accounts", updatedAccounts.size());
			return updatedAccounts;

		} catch (Exception e) {
			log.error("Current balance handling failed", e);
			return new ArrayList<>(); // ✅ Return empty list on error
		}
	}

	// ✅ CORRECT - Returns List<Account>
	private List<AccountEnrichment> doEnabling(List<AccountEnrichment> updatedAccounts, List<Long> accountIds) {
		try {
			log.info("Account enabling starting for {} accounts on thread: {}", updatedAccounts.size(),
					Thread.currentThread().getName());

			enablingAccount.persistFromResults(accountIds,updatedAccounts);

			log.info("Account enabling completed for {} accounts", updatedAccounts.size());

			return updatedAccounts;
		} catch (Exception e) {
			log.error("Account enabling failed", e);
			// ✅ Return empty list on error
			return new ArrayList<>();
		}
	}

	private void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void shutdown() {
		executor.shutdown();
	}

	public static void main(String[] args) {
		EnrichmentValidation service = new EnrichmentValidation();
		service.execute();
		service.shutdown();
	}

    private ValidationResult executePlacementEnrichmentProcessor() {
        Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
        String updatedBy = requestDetailsMap.get(CommonConstants.USER_KEY).toString();
        if(!requestDetailsMap.containsKey(CommonConstants.APP_ID)) {
            requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
        }
        Integer appId = Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString());
        Integer recordSourceId = Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString());
        try {
            log.info("Calling doEnrichment");
            placementEnrichmentProcessor.doEnrichment(updatedBy, appId, recordSourceId);
            return ValidationResult.passed();
        } catch (Exception e) {
            log.error("PlacementEnrichmentProcessor failed", e);
            return ValidationResult.failed("PLACEMENR_ENRICHMENT_ERROR", e.getMessage());
        }
    }
    
     
}
