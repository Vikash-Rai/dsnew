package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ChargeOffAccount;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class ChargeOffAccountValidation {
	
	public static void mandatoryValidation(ChargeOffAccount ChargeOffAccount, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E11703") || errWarMessagesList.contains("W11703")) && CommonUtils.isStringNullOrBlank(ChargeOffAccount.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11703")) {
				validationMap.put("isChargeOffAccountValidated", false);
				ChargeOffAccount.addErrWarJson(new ErrWarJson("e", "E11703"));
			} else if(errWarMessagesList.contains("W11703")) {
				ChargeOffAccount.addErrWarJson(new ErrWarJson("w", "E11703"));
			}
		}
		if((errWarMessagesList.contains("E11708") || errWarMessagesList.contains("W11708")) && CommonUtils.isDateNull(ChargeOffAccount.getChargeOffDate())) {
			if(errWarMessagesList.contains("E11708")) {
				validationMap.put("isChargeOffAccountValidated", false);
				ChargeOffAccount.addErrWarJson(new ErrWarJson("e", "E11708"));
			} else if(errWarMessagesList.contains("W11708")) {
				ChargeOffAccount.addErrWarJson(new ErrWarJson("w", "E11708"));
			}
		}
	}
	
	public static void lookUpValidation(ChargeOffAccount ChargeOffAccount, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(ChargeOffAccount.getClientId()) && ChargeOffAccount.getClient() == null) {
			validationMap.put("isChargeOffAccountValidated", false);
		}
	}
	
	public static void standardize(ChargeOffAccount ChargeOffAccount) {
		if(!CommonUtils.isStringNullOrBlank(ChargeOffAccount.getClientAccountNumber())) {
			ChargeOffAccount.setClientAccountNumber(ChargeOffAccount.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(ChargeOffAccount ChargeOffAccount) {
		if(ChargeOffAccount.getAccountIds() != null && !CommonUtils.isLongNull(ChargeOffAccount.getAccountIds())) {
			ChargeOffAccount.setAccountId(ChargeOffAccount.getAccountIds());
		}
		
	}

	public static void misingRefCheck(ChargeOffAccount ChargeOffAccount, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E41702") || errWarMessagesList.contains("W41702")) && !CommonUtils.isStringNullOrBlank(ChargeOffAccount.getClientAccountNumber()) && CommonUtils.isLongNull(ChargeOffAccount.getAccountId())) {
			if(errWarMessagesList.contains("E41702")) {
				validationMap.put("isChargeOffAccountValidated", false);
				ChargeOffAccount.addErrWarJson(new ErrWarJson("e", "E41702"));
			} else if(errWarMessagesList.contains("W41702")) {
				ChargeOffAccount.addErrWarJson(new ErrWarJson("w", "E41702"));
			}
		}
	}

	public static void businessRule(ChargeOffAccount ChargeOffAccount, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E71706") || errWarMessagesList.contains("W71706")) && !CommonUtils.isDoubleNull(ChargeOffAccount.getAmtPreChargeOffPrincipal()) && ChargeOffAccount.getAmtPreChargeOffPrincipal() <= 0) {
			if(errWarMessagesList.contains("E71706")) {
				validationMap.put("isChargeOffAccountValidated", false);
				ChargeOffAccount.addErrWarJson(new ErrWarJson("e", "E71706"));
			} else if(errWarMessagesList.contains("W71706")) {
				ChargeOffAccount.addErrWarJson(new ErrWarJson("w", "E71706"));
			}
		
		}
		if((errWarMessagesList.contains("E71712") || errWarMessagesList.contains("W71712")) && !CommonUtils.isDoubleNull(ChargeOffAccount.getAmtPreChargeOffBalance()) && ChargeOffAccount.getAmtPreChargeOffBalance() <= 0) {
			if(errWarMessagesList.contains("E71712")) {
				validationMap.put("isChargeOffAccountValidated", false);
				ChargeOffAccount.addErrWarJson(new ErrWarJson("e", "E71712"));
			} else if(errWarMessagesList.contains("W71712")) {
				ChargeOffAccount.addErrWarJson(new ErrWarJson("w", "E71712"));
			}
		}
	}
}