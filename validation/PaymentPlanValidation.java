package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class PaymentPlanValidation {

	public static void mandatoryValidation(PaymentPlan pp, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(pp.getClientId())) {
				validationMap.put("isPaymentPlanValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(pp.getClientAccountNumber())) {
				validationMap.put("isPaymentPlanValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(PaymentPlan pp, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(pp.getClientId()) && pp.getClient() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentMethod()) && pp.getPaymentMethodLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanStatus()) && pp.getPaymentPlanStatusLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanType()) && pp.getPaymentPlanTypeLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanInterval()) && pp.getPaymentPlanIntervalLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanReason()) && pp.getPaymentPlanReasonLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanBrokenReason()) && pp.getPaymentPlanBrokenReasonLookUp() == null) {
				validationMap.put("isPaymentPlanValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(PaymentPlan pp) {
		if(!CommonUtils.isStringNullOrBlank(pp.getClientAccountNumber())) {
			pp.setClientAccountNumber(pp.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pp.getPaymentMethod())) {
			pp.setPaymentMethod(pp.getPaymentMethod().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanStatus())) {
			pp.setPaymentPlanStatus(pp.getPaymentPlanStatus().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pp.getPaymentPlanType())) {
			pp.setPaymentPlanType(pp.getPaymentPlanType().toUpperCase());
		}
	}

	public static void referenceUpdation(PaymentPlan pp) {
		if(pp.getAccountIds() != null && !CommonUtils.isLongNull(pp.getAccountIds())) {
			pp.setAccountId(pp.getAccountIds());
		}
	}

	public static void misingRefCheck(PaymentPlan pp, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(pp.getAccountId())) {
				validationMap.put("isPaymentPlanValidated", false);
				break misingRefCheck;
			}
		}
	}

	public static void businessRule(PaymentPlan pp, Map<String, Object> validationMap, Double sumOfAllPaymentSchedule, 
			Integer countOfAllPaymentSchedule, List<String> errWarMessagesList) {
		businessRule:{
			if(pp.getPctDiscount() != null && (pp.getPctDiscount() < 0 || pp.getPctDiscount() > 100)) {
				validationMap.put("isPaymentPlanValidated", false);
				break businessRule;
			}
			
			if(errWarMessagesList.contains("E70702")) {
				if(!CommonUtils.isDoubleNull(pp.getPaymentSettlementAmt()) && !CommonUtils.isDoubleNull(sumOfAllPaymentSchedule)) {
					if(!pp.getPaymentSettlementAmt().equals(sumOfAllPaymentSchedule)) {
						validationMap.put("isPaymentPlanValidated", false);
						pp.addErrWarJson(new ErrWarJson("e", "E70702"));
						break businessRule;
					}
				}
			}
			
			if(errWarMessagesList.contains("E70701")) {
				if(!CommonUtils.isIntegerNull(pp.getPaymentCount()) && !CommonUtils.isIntegerNull(countOfAllPaymentSchedule)) {
					if(!pp.getPaymentCount().equals(countOfAllPaymentSchedule)) {
						validationMap.put("isPaymentPlanValidated", false);
						pp.addErrWarJson(new ErrWarJson("e", "E70701"));
						break businessRule;
					}
				}
			}
			
			
		}
	}
}