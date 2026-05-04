package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.CreditScore;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class CreditScoreValidation {

	public static void mandatoryValidation(CreditScore creditScore, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(creditScore.getRecordType())) {
				validationMap.put("isCreditScoreValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(creditScore.getClientAccountNumber())) {
				validationMap.put("isCreditScoreValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(creditScore.getClientId())) {
				validationMap.put("isCreditScoreValidated", false);
				break mandatoryValidation;
			}
			
		}
	}

	public static void lookUpValidation(CreditScore creditScore, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(creditScore.getClientId()) && creditScore.getClient() == null) {
				validationMap.put("isCreditScoreValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(creditScore.getCreditScoreProvider()) && creditScore.getCreditScoreProviderLookUp() == null) {
				validationMap.put("isCreditScoreValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(CreditScore creditScore) {
		if(!CommonUtils.isStringNullOrBlank(creditScore.getRecordType())) {
			creditScore.setRecordType(creditScore.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(creditScore.getClientAccountNumber())) {
			creditScore.setClientAccountNumber(creditScore.getClientAccountNumber().toUpperCase());
		}
		if(creditScore.getConsumerIds() != null && !CommonUtils.isLongNull(creditScore.getConsumerIds())) {
			creditScore.setConsumerId(creditScore.getConsumerIds());
		}
	}

	public static void referenceUpdation(CreditScore creditScore) {
		if(creditScore.getAccountIds() != null && !CommonUtils.isLongNull(creditScore.getAccountIds())) {
			creditScore.setAccountId(creditScore.getAccountIds());
		}
	}

	public static void misingRefCheck(CreditScore creditScore, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(creditScore.getClientAccountNumber()) && CommonUtils.isLongNull(creditScore.getAccountId())) {
				validationMap.put("isCreditScoreValidated", false);
				break misingRefCheck;
			}
		}
	}
}
