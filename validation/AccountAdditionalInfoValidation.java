package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.AccountAdditionalInfo;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class AccountAdditionalInfoValidation {

	public static void mandatoryValidation(AccountAdditionalInfo accountAdditionalInfo, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(CommonUtils.isStringNullOrBlank(accountAdditionalInfo.getClientAccountNumber())) {
			validationMap.put("isAccountAdditionalInfoValidated", false);
		}
		if(CommonUtils.isIntegerNullOrZero(accountAdditionalInfo.getClientId())) {
			validationMap.put("isAccountAdditionalInfoValidated", false);
		}
	}

	public static void lookUpValidation(AccountAdditionalInfo accountAdditionalInfo, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(accountAdditionalInfo.getClientId()) && accountAdditionalInfo.getClient() == null) {
			validationMap.put("isAccountAdditionalInfoValidated", false);
		}
	}

	public static void standardize(AccountAdditionalInfo accountAdditionalInfo) {
		if(!CommonUtils.isStringNullOrBlank(accountAdditionalInfo.getClientAccountNumber())) {
			accountAdditionalInfo.setClientAccountNumber(accountAdditionalInfo.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(AccountAdditionalInfo accountAdditionalInfo) {
		if(accountAdditionalInfo.getAccountIds() != null && !CommonUtils.isLongNull(accountAdditionalInfo.getAccountIds())) {
			accountAdditionalInfo.setAccountId(accountAdditionalInfo.getAccountIds());
		}
	}

	public static void misingRefCheck(AccountAdditionalInfo accountAdditionalInfo, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(!CommonUtils.isStringNullOrBlank(accountAdditionalInfo.getClientAccountNumber()) && CommonUtils.isLongNull(accountAdditionalInfo.getAccountId())) {
			validationMap.put("isAccountAdditionalInfoValidated", false);
		}
	}
}