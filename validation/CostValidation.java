package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.Cost;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class CostValidation {

	public static void mandatoryValidation(Cost cost, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(cost.getRecordType())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(cost.getClientAccountNumber())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(cost.getClientId())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(cost.getCostType())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isDateNull(cost.getDtCost())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isDoubleNull(cost.getAmtCost())) {
				validationMap.put("isCostValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(Cost cost, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(cost.getClientId()) && cost.getClient() == null) {
				validationMap.put("isCostValidated", false);
				break lookUpValidation;
			}
			
			if(!CommonUtils.isStringNullOrBlank(cost.getCostType()) && cost.getCostTypeLookUp() == null) {
				validationMap.put("isCostValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(Cost cost) {
		if(!CommonUtils.isStringNullOrBlank(cost.getRecordType())) {
			cost.setRecordType(cost.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(cost.getClientAccountNumber())) {
			cost.setClientAccountNumber(cost.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(Cost cost) {
		if(cost.getAccountIds() != null && !CommonUtils.isLongNull(cost.getAccountIds())) {
			cost.setAccountId(cost.getAccountIds());
		}
	}

	public static void misingRefCheck(Cost cost, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(cost.getClientAccountNumber()) && CommonUtils.isLongNull(cost.getAccountId())) {
				validationMap.put("isCostValidated", false);
				break misingRefCheck;
			}
		}
	}

	public static void businessRule(Cost cost, Map<String, Object> validationMap) {
		businessRule:{			
			if(!CommonUtils.isDateNull(cost.getDtCost()) && !CommonUtils.isDateNull(cost.getPartnerPlacementDate())) {
				if(cost.getDtCost().compareTo(cost.getPartnerPlacementDate()) < 0 ? true : false) {
					validationMap.put("isCostValidated", false);
					break businessRule;
				}
			}

			if(!CommonUtils.isDoubleNull(cost.getAmtCost()) && cost.getAmtCost() > 0) {
				Double amtCost = (!CommonUtils.isDoubleNull(cost.getAmtCost()) && cost.getAmtCost() > 0) ? cost.getAmtCost() :  0.00;
				Double amtPrincipal = (!CommonUtils.isDoubleNull(cost.getAmtPrincipal()) && cost.getAmtPrincipal() > 0) ? cost.getAmtPrincipal() :  0.00;
				Double amtInterest = (!CommonUtils.isDoubleNull(cost.getAmtInterest()) && cost.getAmtInterest() > 0) ? cost.getAmtInterest() :  0.00;
				Double amtLatefee = (!CommonUtils.isDoubleNull(cost.getAmtLatefee()) && cost.getAmtLatefee() > 0) ? cost.getAmtLatefee() :  0.00;
				Double amtOtherfee = (!CommonUtils.isDoubleNull(cost.getAmtOtherfee()) && cost.getAmtOtherfee() > 0) ? cost.getAmtOtherfee() :  0.00;
				Double amtCourtcost = (!CommonUtils.isDoubleNull(cost.getAmtCourtcost()) && cost.getAmtCourtcost() > 0) ? cost.getAmtCourtcost() :  0.00;
				Double amtAttorneyfee = (!CommonUtils.isDoubleNull(cost.getAmtAttorneyfee()) && cost.getAmtAttorneyfee() > 0) ? cost.getAmtAttorneyfee() :  0.00;

				String amtTotal = CommonConstants.dfFormat.format(amtCost);
				String amtCal = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee + amtOtherfee + amtCourtcost + amtAttorneyfee);

				if(!amtTotal.equalsIgnoreCase(amtCal)) {
					validationMap.put("isCostValidated", false);
					break businessRule;
				}
			}
		}
	}

}
