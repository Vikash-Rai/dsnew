package com.equabli.collectprism.validation;

import com.equabli.collectprism.entity.AccountTag;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

import java.util.Map;

public class AccountTagValidation {

	public static void mandatoryValidation(AccountTag accountTag, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(accountTag.getRecordType())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accountTag.getClientAccountNumber())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(accountTag.getClientId())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accountTag.getEntityShortName())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accountTag.getTagValue())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accountTag.getTagName())) {
				validationMap.put("isAccountTagValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(AccountTag accountTag, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(accountTag.getClientId()) && accountTag.getClient() == null) {
				validationMap.put("isAccountTagValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accountTag.getEntityShortName()) && accountTag.getEntityName() == null) {
				validationMap.put("isAccountTagValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accountTag.getTagName()) && accountTag.getEntityAttributeId() == null) {
				validationMap.put("isAccountTagValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(AccountTag accountTag) {
		if(!CommonUtils.isStringNullOrBlank(accountTag.getRecordType())) {
			accountTag.setRecordType(accountTag.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(accountTag.getClientAccountNumber())) {
			accountTag.setClientAccountNumber(accountTag.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(AccountTag accountTag) {
		if(accountTag.getAccountIds() != null && !CommonUtils.isLongNull(accountTag.getAccountIds())) {
			accountTag.setAccountId(accountTag.getAccountIds());
		}
		if(!CommonUtils.isStringNullOrBlank(accountTag.getEntityName()) && accountTag.getEntityName().equalsIgnoreCase("Consumer")) {
			if(CommonUtils.isLongNull(accountTag.getRefEntityId()) 
					&& accountTag.getConsumerId() != null && !CommonUtils.isLongNull(accountTag.getConsumerId())) {
				accountTag.setRefEntityId(accountTag.getConsumerId());
			}
			if(CommonUtils.isStringNullOrBlank(accountTag.getRefEntityNumber()) 
					&& accountTag.getClientConsumerNumber() != null && !CommonUtils.isLongNull(accountTag.getClientConsumerNumber())) {
				accountTag.setRefEntityNumber(accountTag.getClientConsumerNumber().toString());
			}
		}
	}

	public static void misingRefCheck(AccountTag accountTag, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(accountTag.getClientAccountNumber()) && CommonUtils.isLongNull(accountTag.getAccountId())) {
				validationMap.put("isAccountTagValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isStringNullOrBlank(accountTag.getEntityName()) && accountTag.getEntityName().equalsIgnoreCase("Consumer")) {
				if(CommonUtils.isLongNull(accountTag.getRefEntityId())) {
					validationMap.put("isAccountTagValidated", false);
					break misingRefCheck;
				}
				if(CommonUtils.isStringNullOrBlank(accountTag.getRefEntityNumber())) {
					validationMap.put("isAccountTagValidated", false);
					break misingRefCheck;
				}
			}
		}
	}
}