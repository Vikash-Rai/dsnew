package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.AutoAccountInfo;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class AutoAccountInfoValidation {

	public static void mandatoryValidation(AutoAccountInfo autoAccountInfo, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E13202") || errWarMessagesList.contains("W13202")) && CommonUtils.isStringNullOrBlank(autoAccountInfo.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E13202")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E13202"));
			} else if(errWarMessagesList.contains("W13202")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E13202"));
			}
		}
		if((errWarMessagesList.contains("E13201") || errWarMessagesList.contains("W13201")) && CommonUtils.isIntegerNullOrZero(autoAccountInfo.getClientId())) {
			if(errWarMessagesList.contains("E13201")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E13201"));
			} else if(errWarMessagesList.contains("W13201")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E13201"));
			}
		}
		if((errWarMessagesList.contains("E13203") || errWarMessagesList.contains("W13203")) && CommonUtils.isStringNullOrBlank(autoAccountInfo.getRecordType())) {
			if(errWarMessagesList.contains("E13203")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E13203"));
			} else if(errWarMessagesList.contains("W13203")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E13203"));
			}
		}
	}

	public static void lookUpValidation(AutoAccountInfo autoAccountInfo, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E22603") || errWarMessagesList.contains("W22603")) && !CommonUtils.isIntegerNullOrZero(autoAccountInfo.getClientId()) && autoAccountInfo.getClient() == null) {
			if(errWarMessagesList.contains("E22603")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E22603"));
			} else if(errWarMessagesList.contains("W22603")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E22603"));
			}
		}
		if((errWarMessagesList.contains("E32201") || errWarMessagesList.contains("W32201")) && !CommonUtils.isStringNullOrBlank(autoAccountInfo.getRepossessionCaseStatus()) && autoAccountInfo.getRepossesssionCaseStatusLookUp() == null) {
			if(errWarMessagesList.contains("E32201")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E32201"));
			} else if(errWarMessagesList.contains("W32201")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E32201"));
			}
		}
	}

	public static void standardize(AutoAccountInfo autoAccountInfo) {
		if(!CommonUtils.isStringNullOrBlank(autoAccountInfo.getClientAccountNumber())) {
			autoAccountInfo.setClientAccountNumber(autoAccountInfo.getClientAccountNumber().toUpperCase());
		}
		if(CommonUtils.isBooleanNull(autoAccountInfo.getRepossessionEligible())) {
			autoAccountInfo.setRepossessionEligible(false);
		}
		if(CommonUtils.isBooleanNull(autoAccountInfo.getRepossession_sale_complete())) {
			autoAccountInfo.setRepossession_sale_complete(false);
		}
		if(CommonUtils.isBooleanNull(autoAccountInfo.getReposessionOverride())) {
			autoAccountInfo.setReposessionOverride(false);
		}
	}

	public static void referenceUpdation(AutoAccountInfo autoAccountInfo) {
		if(autoAccountInfo.getAccountIds() != null && !CommonUtils.isLongNull(autoAccountInfo.getAccountIds())) {
			autoAccountInfo.setAccountId(autoAccountInfo.getAccountIds());
		}
	}

	public static void misingRefCheck(AutoAccountInfo autoAccountInfo, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E42604") || errWarMessagesList.contains("E42604")) && !CommonUtils.isStringNullOrBlank(autoAccountInfo.getClientAccountNumber()) && CommonUtils.isLongNull(autoAccountInfo.getAccountId())) {
			if(errWarMessagesList.contains("E42604")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E42604"));
			} else if(errWarMessagesList.contains("W42604")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E42604"));
			}
		}
	}

	public static void businessRule(AutoAccountInfo autoAccountInfo, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		
		if((errWarMessagesList.contains("E73202") || errWarMessagesList.contains("E73202")) && !CommonUtils.isLongNullOrZero(autoAccountInfo.getRepossessionCaseId()) && Long.toString(autoAccountInfo.getRepossessionCaseId()).length() > 10) {
			if(errWarMessagesList.contains("E73202")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E73202"));
			} else if(errWarMessagesList.contains("W73202")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E73202"));
			}
		}
		if(!CommonUtils.isDoubleNull(autoAccountInfo.getAutoSalePrice()) && autoAccountInfo.getAutoSalePrice() < 0) {
			if(errWarMessagesList.contains("E73201")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E73201"));
			} else if(errWarMessagesList.contains("W73201")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E73201"));
			}
		}
		if ((errWarMessagesList.contains("E73203") || errWarMessagesList.contains("W73203")) && autoAccountInfo.getProduct() != null && !autoAccountInfo.getProduct().getShortName().equalsIgnoreCase("AL")) {
			if(errWarMessagesList.contains("E73203")) {
				validationMap.put("isAutoAccountInfoValidated", false);
				autoAccountInfo.addErrWarJson(new ErrWarJson("e", "E73203"));
			} else if(errWarMessagesList.contains("W73203")) {
				autoAccountInfo.addErrWarJson(new ErrWarJson("w", "E73203"));
			}
		}
	}
}