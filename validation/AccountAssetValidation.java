package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.AccountAsset;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class AccountAssetValidation {

	public static void mandatoryValidation(AccountAsset accAsset, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(accAsset.getRecordType())) {
				validationMap.put("isAccountAssetValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(accAsset.getClientAccountNumber())) {
				validationMap.put("isAccountAssetValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(accAsset.getClientId())) {
				validationMap.put("isAccountAssetValidated", false);
				break mandatoryValidation;
			}
			
		}
	}

	public static void lookUpValidation(AccountAsset accAsset, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(accAsset.getClientId()) && accAsset.getClient() == null) {
				validationMap.put("isAccountAssetValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accAsset.getAssetType()) && accAsset.getAssetTypeLookUp() == null) {
				validationMap.put("isAccountAssetValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accAsset.getAssetCategory()) && accAsset.getAssetCategoryLookUp() == null) {
				validationMap.put("isAccountAssetValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accAsset.getStateCode()) && accAsset.getCountryState() == null) {
				validationMap.put("isAccountAssetValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(accAsset.getGpsStatus()) && accAsset.getGpsStatusLookUp() == null) {
				validationMap.put("isAccountAssetValidated", false);
				break lookUpValidation;
			}
			
		}
	}

	public static void standardize(AccountAsset accAsset) {
		if(!CommonUtils.isStringNullOrBlank(accAsset.getRecordType())) {
			accAsset.setRecordType(accAsset.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(accAsset.getClientAccountNumber())) {
			accAsset.setClientAccountNumber(accAsset.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(AccountAsset accAsset) {
		if(accAsset.getAccountIds() != null && !CommonUtils.isLongNull(accAsset.getAccountIds())) {
			accAsset.setAccountId(accAsset.getAccountIds());
		}
	}

	public static void misingRefCheck(AccountAsset accAsset, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(accAsset.getClientAccountNumber()) && CommonUtils.isLongNull(accAsset.getAccountId())) {
				validationMap.put("isAccountAssetValidated", false);
				break misingRefCheck;
			}
		}
	}
}