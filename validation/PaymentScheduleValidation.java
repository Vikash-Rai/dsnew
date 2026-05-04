package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.PaymentSchedule;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class PaymentScheduleValidation {

	public static void mandatoryValidation(PaymentSchedule ps, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(ps.getClientId())) {
				validationMap.put("isPaymentScheduleValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(ps.getClientAccountNumber())) {
				validationMap.put("isPaymentScheduleValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(ps.getPaymentScheduleStatus())) {
				validationMap.put("isPaymentScheduleValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(PaymentSchedule ps, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(ps.getClientId()) && ps.getClient() == null) {
				validationMap.put("isPaymentScheduleValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(ps.getPaymentMethod()) && ps.getPaymentMethodLookUp() == null) {
				validationMap.put("isPaymentScheduleValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(ps.getPaymentScheduleStatus()) && ps.getPaymentScheduleStatusLookUp() == null) {
				validationMap.put("isPaymentScheduleValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(PaymentSchedule ps) {
		if(!CommonUtils.isStringNullOrBlank(ps.getClientAccountNumber())) {
			ps.setClientAccountNumber(ps.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(ps.getPaymentMethod())) {
			ps.setPaymentMethod(ps.getPaymentMethod().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(ps.getPaymentScheduleStatus())) {
			ps.setPaymentScheduleStatus(ps.getPaymentScheduleStatus().toUpperCase());
		}
	}

	public static void referenceUpdation(PaymentSchedule ps) {
		if(ps.getAccountIds() != null && !CommonUtils.isLongNull(ps.getAccountIds())) {
			ps.setAccountId(ps.getAccountIds());
		}
	}

	public static void misingRefCheck(PaymentSchedule ps, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(ps.getAccountId())) {
				validationMap.put("isPaymentScheduleValidated", false);
				break misingRefCheck;
			}
		}
	}
}