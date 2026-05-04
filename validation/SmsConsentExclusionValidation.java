package com.equabli.collectprism.validation;

import java.time.LocalDateTime;
import java.util.Map;

import com.equabli.collectprism.entity.SmsConsentExclusion;
import com.equabli.domain.helpers.CommonUtils;

public class SmsConsentExclusionValidation {

	public static void mandatoryValidation(SmsConsentExclusion sce, Map<String, Object> validationMap) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(sce.getRecordType())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(sce.getClientId())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(sce.getClientAccountNumber())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(sce.getPhone())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(sce.getWeekDayNo())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(sce.getTmUtcFrom())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isTimeNull(sce.getTmUtcTill())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void referenceUpdation(SmsConsentExclusion sce, Integer recordSourceId) {
		if(sce.getAccountIds() != null && !CommonUtils.isLongNull(sce.getAccountIds())) {
			sce.setAccountId(sce.getAccountIds());
		}
		if(sce.getConsumerIds() != null && !CommonUtils.isLongNull(sce.getConsumerIds())) {
			sce.setConsumerId(sce.getConsumerIds());
		}
		if(sce.getPhoneDetails() != null && sce.getPhoneDetails().getPhoneId() != null && !CommonUtils.isLongNull(sce.getPhoneDetails().getPhoneId())) {
			sce.setPhoneId(sce.getPhoneDetails().getPhoneId());
			sce.getPhoneDetails().setSmsConsent(true);
			sce.getPhoneDetails().setSmsConsentDate(LocalDateTime.now());
			sce.getPhoneDetails().setSmsConsentSourceId(recordSourceId);
		}
	}

	public static void misingRefCheck(SmsConsentExclusion sce, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(sce.getAccountId())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break misingRefCheck;
			}
			if(CommonUtils.isLongNull(sce.getPhoneId())) {
				validationMap.put("isSmsConsentExclusionValidated", false);
				break misingRefCheck;
			}
		}
	}
}