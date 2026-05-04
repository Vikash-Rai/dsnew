package com.equabli.collectprism.approach.builders;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.equabli.collectprism.approach.enrichmentservice.EnrichmentCacheService;
import com.equabli.collectprism.approach.entity.AccountEnrichment;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Address;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.SolAccountDTO;
import com.equabli.collectprism.entity.AccountUpdateResult;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.domain.Response;
import com.equabli.domain.SOLCalulation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.SOLCalculation;
import com.equabli.domain.StatuteOfLimitation;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

/**
 * Component for processing SOL (Statute of Limitations) calculations for
 * accounts. Utilizes Java 21 virtual threads for efficient batch processing.
 * 
 * Design: Uses account IDs throughout processing. Collects SOL calculation
 * results and applies them to Account entities, which are then passed to the
 * caller for persistence in a single batch operation.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SolCalculation {

	@Autowired
	AccountRepository accountRepository;
	
	@Autowired
	EnrichmentCacheService cacheService;

	private static final int DEFAULT_BATCH_SIZE = 1000;
	private static final long EXECUTOR_TIMEOUT_MINUTES = 5;
	private static final int ZERO = 0;
	private static final int DB_CONCURRENCY_LIMIT = 20;
	private final Semaphore semaphore = new Semaphore(DB_CONCURRENCY_LIMIT);

	/**
	 * Process accounts using virtual threads with batch support. Updates are
	 * applied to Account entities for caller to persist.
	 *
	 * @return List of updated Account entities
	 */
	public List<AccountEnrichment> processAccountsBatchesWithVirtualThreads(List<AccountEnrichment> accountDetails,Map<Long, SolAccountDTO> solDtoMap) {
		return processAccountsBatchesWithVirtualThreads(accountDetails, DEFAULT_BATCH_SIZE,solDtoMap);
	}

	/**
	 * Process accounts in batches using virtual threads. Collects SOL calculations
	 * and applies them to Account objects. Caller is responsible for persisting the
	 * updated accounts.
	 *
	 * @param accountIds List of account IDs to process
	 * @param batchSize  Size of each batch
	 * @return List of updated Account entities with SOL data
	 */
	public List<AccountEnrichment> processAccountsBatchesWithVirtualThreads(List<AccountEnrichment> accountIds, int batchSize,Map<Long, SolAccountDTO> solDtoMap) {

		if (accountIds == null || accountIds.isEmpty()) {
			log.warn("No accounts to process");
			return null;
		}

		log.info("Starting batch processing with virtual threads: {} accounts (batch size: {})", accountIds.size(),
				batchSize);

		// Thread-safe map to collect SOL calculation results indexed by account ID
		//Map<Long, AccountUpdateResult> updateResults = Collections.synchronizedMap(new LinkedHashMap<>());

		try {
			for (int i = 0; i < accountIds.size(); i += batchSize) {
				int end = Math.min(i + batchSize, accountIds.size());
				List<AccountEnrichment> batch = accountIds.subList(i, end);
				processBatch(batch, end, accountIds.size(),solDtoMap);
			}
		} catch (Exception e) {
			log.error("Error during batch processing", e);
		}

		log.info("Completed batch processing with virtual threads: {} accounts processed", accountIds);

		return accountIds;
	}

	/**
	 * Process individual batch using virtual threads. Collects SOL calculation
	 * results without persisting.
	 */
	private List<AccountEnrichment> processBatch(List<AccountEnrichment> batchAccountIds, int endIndex, int totalAccounts,Map<Long, SolAccountDTO> solDtoMap) {

		List<AccountEnrichment> updatedAccounts =  new ArrayList<>();
		try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
			// Submit processing tasks for each account, using semaphore to limit concurrency
			batchAccountIds.forEach(account -> executor.submit(() -> {
				try {
					processSingleAccount(account,solDtoMap);
					updatedAccounts.add(account);
				} catch (Exception e) {
					log.error("Error processing account {}", account.getAccountId(), e);
					//updateResults.put(account.getAccountId(), AccountUpdateResult.failed(e.getMessage()));
				} finally {
					semaphore.release();  // Release semaphore once done
				}
			}));

			waitForExecutorTermination(executor);
			log.info("Processed batch: {}/{} accounts", endIndex, totalAccounts);

		} catch (Exception e) {
			log.error("Batch execution failed", e);
		}
		return updatedAccounts;
	}

	/**
	 * Process a single account by ID. Collects the SOL calculation result for later
	 * application to Account entity.
	 */
	private AccountEnrichment processSingleAccount(AccountEnrichment account,Map<Long, SolAccountDTO> solDtoMap) {
		try {
			// AccountUpdateResult result = AccountUpdateResult.getOrCreate(updateResults, account.getAccountId());
			Integer solMonth = 0;
			Integer clientConfiguredDays = 0;
			Response<StatuteOfLimitation> response = new  Response<StatuteOfLimitation>();

			if (!shouldProcess(account)) {
				return account;
			}
 
			SolAccountDTO dto =solDtoMap.get(account.getAccountId());
			SOLCalulation solCalculation = buildSol(account);
			processSOLCalculation(account,solCalculation,dto.getSolMonth(),dto.getSolDay(),response);
//			int solMonth = Optional.ofNullable(account.getSolMonth()).orElse(0);
//			int clientDays = Optional.ofNullable(account.getSolDay()).orElse(0);
//			Response<StatuteOfLimitation> response = SOLCalculation.solCalculation(account.getAccountId(),
//					solCalculation, solMonth, clientDays, new Response<>());
//			StatuteOfLimitation sol = response.getResponse();
//
//			if (sol == null) {
//				updateResults.put(account.getAccountId(), AccountUpdateResult.failed("SOL null"));
//				return;
//			}
//
//			 if (sol != null) {
//			        result.setCurrentSOLDate(sol.getCurrentSOLDate());
//			        result.setCalculatedSOLDate(sol.getCalculatedSOLDate());
//			        result.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
//			    }
//			 
//			 applySOLResultToAccount(result);
//			
//			updateResults.put(account.getAccountId(), result);

		} catch (Exception e) {
			log.error("Error processing account {}", account.getAccountId(), e);
			//updateResults.put(account.getAccountId(), AccountUpdateResult.failed(e.getMessage()));
		}
		return account;
	}
	
	public void processSOLCalculation(AccountEnrichment accountEnrichment,SOLCalulation solCalulation,Integer solMonth , 
			Integer clientConfiguredDays,Response<StatuteOfLimitation> response) {
//	    for (Consumer consumer : accountEnrichment.getConsumer()) {
//	        if (isContactTypePD(consumer)) {
//	            for (Address address : consumer.getAddress()) {
//	                if (isPrimaryAddress(address)) {
//	                    handlePrimaryAddress(address,solMonth,clientConfiguredDays,solCalulation);
//	                } else {
//	                    handleSecondaryAddress(address,solMonth);
//	                }
//	            }
//	        }
//	    }

	    // Perform SOL calculation after iterating through consumers and addresses
	    response = SOLCalculation.solCalculation(
	            accountEnrichment.getAccountId(),
	            solCalulation,
	            solMonth,
	            clientConfiguredDays,
	            response
	    );

	    handleSOLResponse(response, accountEnrichment);
	}

	private boolean isContactTypePD(Consumer consumer) {
	    return consumer.getContactTypeLookUp() != null &&
	            "PD".equalsIgnoreCase(consumer.getContactTypeLookUp().getKeycode());
	}

	private boolean isPrimaryAddress(Address address) {
	    return address.getIsPrimary() != null && address.getIsPrimary();
	}

	private void handlePrimaryAddress(Address address,Integer solMonth, Integer clientConfiguredDays,SOLCalulation solCalulation) {
	    solCalulation.setStateCode(address.getStateCode());
	    solCalulation.setCountryStateId(
	            address.getCountryState() != null ? address.getCountryState().getCountryStateId() : null
	    );

	    log.debug("client_id#" + address.getClientId() + "#client_account_number#" + address.getClientAccountNumber() +
	            "#state_code#" + address.getStateCode());

	    if (address.getStatutesOfLimitation() != null) {
	        solMonth = address.getStatutesOfLimitation().getSolMonth();
	        log.debug("solMonth#" + solMonth);
	    }

	    if (address.getClientStatutesOfLimitation() != null) {
	        clientConfiguredDays = address.getClientStatutesOfLimitation().getSolDay();
	        log.debug("clientConfiguredDays#" + clientConfiguredDays);
	    }
	}

	private void handleSecondaryAddress(Address address,Integer solMonth) {
	    if (address.getStatutesOfLimitationMinMonthForProduct() != null) {
	        solMonth = address.getStatutesOfLimitationMinMonthForProduct().getSolMonth();
	        log.debug("solMonth calculated for min#" + solMonth);
	    }
	}

	private void handleSOLResponse(Response<StatuteOfLimitation> response, AccountEnrichment accountEnrichment) {
	    StatuteOfLimitation sol = response.getResponse();

	    if (sol != null && sol.getIsPrimaryStateExists()) {
	        log.debug("Calculated SOL Date: " + sol.getCalculatedSOLDate());

	        if (CommonUtils.isDateNull(sol.getCurrentSOLDate())) {
	            log.info("Current SOL Date is null");
	            updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, accountEnrichment);
	        } else if (CommonUtils.isObjectNull(sol.getCalculatedSOLDate())) {
	            updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, accountEnrichment);
	        } else {
	            compareAndUpdateSOLDates(sol, accountEnrichment);
	        }
	    } else if (sol != null) {
	        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, accountEnrichment);
	    } else {
	        accountEnrichment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
	        log.info("SOL is not calculated");
	    }
	}

	private void compareAndUpdateSOLDates(StatuteOfLimitation sol, AccountEnrichment accountEnrichment) {
	    Integer dateCompare = sol.getCalculatedSOLDate().compareTo(sol.getCurrentSOLDate());
	    if (dateCompare > 0) {
	        log.info("Equabli SOL Date occurs after Client SOL Date");
	        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, accountEnrichment);
	    } else if (dateCompare < 0) {
	        log.info("Equabli SOL Date occurs before Client SOL Date");
	        // Handle SOL conflict scenario
	        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, false, accountEnrichment);
	    } else {
	        log.info("Both SOL dates are equal");
	        updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, accountEnrichment);
	    }
	}

	private void updateSOLDateDetailsInAccount(LocalDate calculatedSOLDate, LocalDate currentSOLDate, boolean updateEquabliDateInAccount, boolean updateClientDateInAccount, AccountEnrichment accountEnrichment) {
	    // Implement the update logic here, potentially involving setting fields or calling an update method.
	    // Example:
		if (Boolean.TRUE.equals(updateEquabliDateInAccount)) {
			accountEnrichment.setSolDate(calculatedSOLDate);
			setAccountToEnabled(accountEnrichment);
		} else if (Boolean.TRUE.equals(updateClientDateInAccount)) {
			accountEnrichment.setSolDate(currentSOLDate);
			setAccountToEnabled(accountEnrichment);
		}

		if (!CommonUtils.isDateNull(calculatedSOLDate)) {
			accountEnrichment.setEquabliSolDate(calculatedSOLDate);
		}
	    // Set other relevant flags or attributes in accountEnrichment if needed.
	    log.info("Updated SOL Date for account " + accountEnrichment.getAccountId());
	}
	

	private void setAccountToEnabled(AccountEnrichment account) {
		Integer enabledStatusId = ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId();
		account.setRecordStatusId(enabledStatusId);
	}

	/**
	 * Build SOL calculation object from account DTO.
	 */
	private SOLCalulation buildSol(AccountEnrichment account) {
		// Load minimal account data needed for SOL calculation
//		Account fullAccount = accountRepository.findById(account.accountId())
//				.orElseThrow(() -> new IllegalArgumentException("Account not found: " + account.accountId()));

		SOLCalulation solCalculation = new SOLCalulation();
		setSolCalculation(account, solCalculation);
		return solCalculation;
	}

	/**
	 * Set SOL calculation fields from account data.
	 */
	private void setSolCalculation(AccountEnrichment account, SOLCalulation solCalculation) {
		solCalculation.setEquabliAccountId(account.getAccountId());
		solCalculation.setProductId(account.getProductId());
		solCalculation.setLastPaymentDate(account.getLastPaymentDate());
		solCalculation.setChargeOffDate(account.getChargeOffDate());
		solCalculation.setSOLDate(account.getSolDate());
		solCalculation.setClientId(account.getClientId());
		solCalculation.setPartnerId(account.getPartnerId());
		solCalculation.setDtDelinquency(account.getDelinquencyDate());
		solCalculation.setDtClientStatute(account.getClientSolDate());
		solCalculation.setDtEquabliStatute(account.getEquabliSolDate());
		
		solCalculation.setClientCode(
	                cacheService.getClientShortName(account.getClientId()));
		solCalculation.setDebtCategoryId(
	                cacheService.getProductDebtCategoryId(account.getProductId()));
		solCalculation.setCycleDay(
	                cacheService.getCycleDayConfigValueId(account.getClientId(), account.getProductId()));
		// Commenting this code , this shoud be fetched from cache
//		if(account.getClient() != null)
//			solCalculation.setClientCode(account.getClient().getShortName());
//		if(account.getProduct() != null)
//			solCalculation.setDebtCategoryId(account.getProduct().getDebtCategoryId());
//		if(account.getCustomAppConfigValue() != null)
//			solCalculation.setCycleDay(account.getCustomAppConfigValue().getCustomAppConfigValueId());
	}

	/**
	 * Extract SOL data from account DTO and calculation object.
	 */

	/**
	 * Check if account should be processed.
	 */
	private boolean shouldProcess(AccountEnrichment account) {
		try {

			if (isAccountInValidStatus(account)) {
				log.debug("Account {} already in valid status, skipping", account.getAccountId());
				return false;
			}

			return shouldProcessAccount(account);
		} catch (Exception e) {
			log.error("Error checking if account should be processed: {}", account.getAccountId(), e);
			return false;
		}
	}

	/**
	 * Check if account is in valid status for processing.
	 */
	private boolean isAccountInValidStatus(AccountEnrichment account) {
		Integer statusId = account.getRecordStatusId();
		Integer enabledStatusId = ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId();
		Integer solwaitStatusId = ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SOLWAIT).getRecordStatusId();

		return statusId.equals(enabledStatusId) || statusId.equals(solwaitStatusId);
	}

	/**
	 * Check if account should be processed based on status and error codes.
	 */
	private boolean shouldProcessAccount(AccountEnrichment account) {
		Integer statusId = account.getRecordStatusId();
		Integer suspectedStatusId = ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED)
				.getRecordStatusId();

		return !statusId.equals(suspectedStatusId) && (CommonUtils.isObjectNull(account.getErrCodeJson())
				|| !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(account.getErrCodeJson()));
	}

	/**
	 * Wait for executor to terminate with timeout.
	 */
	private void waitForExecutorTermination(ExecutorService executor) throws InterruptedException {
		executor.shutdown();

		boolean terminated = executor.awaitTermination(EXECUTOR_TIMEOUT_MINUTES, TimeUnit.MINUTES);

		if (!terminated) {
			log.warn("Executor shutdown timeout exceeded, forcing immediate shutdown");
			List<Runnable> remainingTasks = executor.shutdownNow();
			log.warn("Number of tasks that were not executed: {}", remainingTasks.size());
		}
	}

	/**
	 * Data transfer object for SOL information.
	 * 
	 * /** Result of SOL calculation for a single account. Immutable value object
	 * for thread-safe result collection.
	 */
	
}