package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.AccountUcc;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class AccountUccValidation {

	public static void mandatoryValidation(AccountUcc accUcc, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(accUcc.getRecordType())) {
				validationMap.put("isAccountUccValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accUcc.getClientAccountNumber())) {
				validationMap.put("isAccountUccValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(accUcc.getClientId())) {
				validationMap.put("isAccountUccValidated", false);
				break mandatoryValidation;
			}
			
			
		}
	}

	public static void lookUpValidation(AccountUcc accUcc, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(accUcc.getClientId()) && accUcc.getClient() == null) {
				validationMap.put("isAccountUccValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(AccountUcc accUcc) {
		if(!CommonUtils.isStringNullOrBlank(accUcc.getRecordType())) {
			accUcc.setRecordType(accUcc.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(accUcc.getClientAccountNumber())) {
			accUcc.setClientAccountNumber(accUcc.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(AccountUcc accUcc) {
		if(accUcc.getAccountIds() != null && !CommonUtils.isLongNull(accUcc.getAccountIds())) {
			accUcc.setAccountId(accUcc.getAccountIds());
		}
	}

	public static void misingRefCheck(AccountUcc accUcc, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(accUcc.getClientAccountNumber()) && CommonUtils.isLongNull(accUcc.getAccountId())) {
				validationMap.put("isAccountUccValidated", false);
				break misingRefCheck;
			}
		}
	}
}