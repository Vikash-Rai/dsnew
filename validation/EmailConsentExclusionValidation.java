package com.equabli.collectprism.validation;

import java.time.LocalDateTime;
import java.util.Map;

import com.equabli.collectprism.entity.EmailConsentExclusion;
import com.equabli.domain.helpers.CommonUtils;

public class EmailConsentExclusionValidation {

	public static void mandatoryValidation(EmailConsentExclusion ece, Map<String, Object> validationMap) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(ece.getRecordType())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(ece.getClientId())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(ece.getClientAccountNumber())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(ece.getEmailAddress())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(ece.getWeekDayNo())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(ece.getTmUtcFrom())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(ece.getTmUtcTill())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void referenceUpdation(EmailConsentExclusion ece, Integer recordSourceId) {
		if(ece.getAccountIds() != null && !CommonUtils.isLongNull(ece.getAccountIds())) {
			ece.setAccountId(ece.getAccountIds());
		}
		if(ece.getConsumerIds() != null && !CommonUtils.isLongNull(ece.getConsumerIds())) {
			ece.setConsumerId(ece.getConsumerIds());
		}
		if(ece.getEmailDetails() != null && ece.getEmailDetails().getEmailId() != null && !CommonUtils.isLongNull(ece.getEmailDetails().getEmailId())) {
			ece.setEmailId(ece.getEmailDetails().getEmailId());
			ece.getEmailDetails().setConsent(true);
			ece.getEmailDetails().setConsentDate(LocalDateTime.now());
			ece.getEmailDetails().setConsentSourceId(recordSourceId);
		}
	}

	public static void standardize(EmailConsentExclusion ece) {
		if(!CommonUtils.isStringNullOrBlank(ece.getEmailAddress())) {
			ece.setEmailAddress(ece.getEmailAddress().toLowerCase());
		}
	}

	public static void misingRefCheck(EmailConsentExclusion ece, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(ece.getAccountId())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break misingRefCheck;
			}
			if(CommonUtils.isLongNull(ece.getEmailId())) {
				validationMap.put("isEmailConsentExclusionValidated", false);
				break misingRefCheck;
			}
		}
	}
}