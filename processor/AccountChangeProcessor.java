package com.equabli.collectprism.processor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.AccountChange;
import com.equabli.collectprism.repository.AccountAdditionalInfoRepository;
import com.equabli.collectprism.repository.AccountChangeRepository;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

@Component
public class AccountChangeProcessor {

	@Autowired
	private AccountRepository debtRepository;

	@Autowired
	private AccountChangeRepository changeRepository;
	
	@Autowired
	private AccountAdditionalInfoRepository additionalInfoRepository;

	int batchSize = 50;

	public Response<Map<String, Object>> processChanges() {
		Response<Map<String, Object>> responseResult = new Response<>();

		List<AccountChange> changes = changeRepository.findByRecordStatusId(
				ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId(), null);
		List<Account> updatedAccounts = new ArrayList<>();
		// Group changes by client account number
		Map<String, List<AccountChange>> groupedChanges = changes.stream()
				.collect(Collectors.groupingBy(AccountChange::getClientAccountNumber));

		try (StructuredTaskScope<Void> taskScope = new StructuredTaskScope<>()) {
			// Create subtasks within the task scope
			for (Map.Entry<String, List<AccountChange>> entry : groupedChanges.entrySet()) {
				String clientAccountNumber = entry.getKey();
				List<AccountChange> accountChanges = entry.getValue();

				// Submit each task to the task scope
				taskScope.fork(() -> {
					try {
						Account debt = debtRepository.getAccountByClientAccountNumber(clientAccountNumber);
						if (debt == null) {
							throw new RuntimeException("Account not found");
						}

						for (AccountChange change : accountChanges) {
							applyChange(debt, change);
						}

						updatedAccounts.add(debt);

						for (int i = 0; i < updatedAccounts.size(); i += batchSize) {
							int end = Math.min(i + batchSize, updatedAccounts.size());
							List<Account> batch = updatedAccounts.subList(i, end);
							debtRepository.saveAll(batch);
							debtRepository.flush(); // forces batch execution
						}

						changes.forEach(c -> c.setRecordStatusId(ConfRecordStatus.confRecordStatus
								.get(ConfRecordStatus.SUCCESSFUL).getRecordStatusId()));
						
						
					} catch (Exception e) {
						// Mark all changes as FAILED in case of an error
						for (AccountChange change : accountChanges) {
							change.setRecordStatusId(
									ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.FAILED).getRecordStatusId());
						}
						responseResult.setValidation(false);
						System.err.println("Failed for account: " + clientAccountNumber + " | " + e.getMessage());
					}

					// Save changes after processing
					changeRepository.saveAll(accountChanges);
					return null; // Explicitly return null for Void
				});
			}

			// Wait for all tasks to finish and collect results
			taskScope.join(); // Wait for all subtasks to finish

			responseResult.setValidation(true);
			responseResult.setMessage(CommonConstants.PROCESS_SUCESSFULL_MESSAGE);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Restore the interrupt status
			responseResult.setValidation(false);
			responseResult.setMessage("Error during concurrent processing.");
			System.err.println("Error: " + e.getMessage());
		}

		return responseResult;
	}

	private void applyChange(Account debt, AccountChange change) {

		Set<String> blankFields = parseBlankOrNull(change.getBlankOrNull());

		// Apply updates field by field
		updateField(blankFields, "currentLenderCreditor", change.getCurrentLenderCreditor(),
				debt::setCurrentLenderCreditor);

		updateField(blankFields, "chargeOffDate", change.getChargeOffDate(), debt::setChargeOffDate);

		updateField(blankFields, "debtStage", change.getDebtStage(), debt::setDebtStage);

		
//		updateField(blankFields, "preChargeOffClientBucket", change.getPreChargeOffClientBucket(),
//				debt::setPreChargeOffClientBucket);

		updateField(blankFields, "clientSOLDate", change.getClientSOLDate(), debt::setClientSolDate);

		updateField(blankFields, "subpoolId", change.getSubpoolId(), debt::setSubpoolId);

		updateField(blankFields, "additionalAccountNumber", change.getAdditionalAccountNumber(),
				debt::setAdditionalAccountNumber);

		updateField(blankFields, "originalLenderCreditor", change.getOriginalLenderCreditor(),
				debt::setOriginalLenderCreditor);

		updateField(blankFields, "delinquencyDate", change.getDelinquencyDate(), debt::setDelinquencyDate);

		updateField(blankFields, "debtDueDate", change.getDebtDueDate(), debt::setDebtDueDate);
		
		if(!CommonUtils.isStringNullOrBlank(change.getPreChargeOffClientBucket())){
			updatePreChargeOffBucket(change);
		}

		// ---- Special handling: debtStatus ----
		if (blankFields.contains("debtStatus")) {
			debt.setDebtStatus(null);
		} else if (isNotBlank(change.getDebtStatus())) {
			debt.setDebtStatus(change.getDebtStatus());

			// Archive logic
			if ("AR".equals(change.getDebtStatus())) {
				debt.setArchive(false);
				debt.setDtmUtcArchieve(LocalDateTime.now(ZoneOffset.UTC));
			}
		}
	}

	private boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

	private Set<String> parseBlankOrNull(String blankOrNull) {

		if (blankOrNull == null || blankOrNull.trim().isEmpty()) {
			return Collections.emptySet();
		}
		return Arrays.stream(blankOrNull.split(",")).map(String::trim).collect(Collectors.toSet());
	}

	private <T> void updateField(Set<String> blankFields, String fieldName, T newValue, Consumer<T> setter) {

		if (blankFields.contains(fieldName)) {
			setter.accept(null);
			return;
		}

		if (newValue == null) {
			return; // do not overwrite
		}

		if (newValue instanceof String && ((String) newValue).trim().isEmpty()) {
			return; // skip empty string
		}

		setter.accept(newValue);
	}

	private void updatePreChargeOffBucket(AccountChange change) {

	    additionalInfoRepository.updatePreChargeOffClientBucket(
	            change.getClientId(),
	            change.getClientAccountNumber(),
	            change.getPreChargeOffClientBucket()
	    );
	}

}
