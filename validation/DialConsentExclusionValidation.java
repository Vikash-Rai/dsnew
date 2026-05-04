package com.equabli.collectprism.validation;

import java.time.LocalDateTime;
import java.util.Map;

import com.equabli.collectprism.entity.DialConsentExclusion;
import com.equabli.domain.helpers.CommonUtils;

public class DialConsentExclusionValidation {

	public static void mandatoryValidation(DialConsentExclusion dce, Map<String, Object> validationMap) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(dce.getRecordType())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(dce.getClientId())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(dce.getClientAccountNumber())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(dce.getPhone())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(dce.getWeekDayNo())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(dce.getTmUtcFrom())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(dce.getTmUtcTill())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void referenceUpdation(DialConsentExclusion dce, Integer recordSourceId) {
		if(dce.getAccountIds() != null && !CommonUtils.isLongNull(dce.getAccountIds())) {
			dce.setAccountId(dce.getAccountIds());
		}
		if(dce.getConsumerIds() != null && !CommonUtils.isLongNull(dce.getConsumerIds())) {
			dce.setConsumerId(dce.getConsumerIds());
		}
		if(dce.getPhoneDetails() != null && dce.getPhoneDetails().getPhoneId() != null && !CommonUtils.isLongNull(dce.getPhoneDetails().getPhoneId())) {
			dce.setPhoneId(dce.getPhoneDetails().getPhoneId());
			dce.getPhoneDetails().setAutoDialConsent(true);
			dce.getPhoneDetails().setAutoDialConsentDate(LocalDateTime.now());
			dce.getPhoneDetails().setAutoDialConsentSourceId(recordSourceId);
		}
	}

	public static void misingRefCheck(DialConsentExclusion dce, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(dce.getAccountId())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break misingRefCheck;
			}
			if(CommonUtils.isLongNull(dce.getPhoneId())) {
				validationMap.put("isDialConsentExclusionValidated", false);
				break misingRefCheck;
			}
		}
	}
}