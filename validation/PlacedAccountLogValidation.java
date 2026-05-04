package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.PlacedAccountLog;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class PlacedAccountLogValidation {

	public static void mandatoryValidation(PlacedAccountLog ad, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(ad.getClientId())) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(ad.getPartnerId())) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(ad.getClientAccountNumber())) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break mandatoryValidation;
			}
//			if(CommonUtils.isLongNullOrZero(ad.getJobScheduleId())) {
//				validationMap.put("isPlacedAccountLogValidated", false);
//				break mandatoryValidation;
//			}
			if(CommonUtils.isDateNull(ad.getConfirmDate())) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(PlacedAccountLog ad, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(ad.getClientId()) && ad.getClient() == null) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isIntegerNullOrZero(ad.getPartnerId()) && ad.getPartner() == null) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(PlacedAccountLog ad) {
		if(!CommonUtils.isStringNullOrBlank(ad.getClientAccountNumber())) {
			ad.setClientAccountNumber(ad.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(PlacedAccountLog ad) {
		if(CommonUtils.isLongNull(ad.getAccountId()) && ad.getAccountIds() != null && !CommonUtils.isLongNull(ad.getAccountIds())) {
			ad.setAccountId(ad.getAccountIds());
		}
	}

	public static void misingRefCheck(PlacedAccountLog ad, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(ad.getAccountId()) || !ad.getAccountId().equals(ad.getAccountIds())) {
				validationMap.put("isPlacedAccountLogValidated", false);
				break misingRefCheck;
			}
		}
	}
}