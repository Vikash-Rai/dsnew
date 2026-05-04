package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.PartnerAssignment;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class PartnerAssignmentValidation {

	public static void mandatoryValidation(PartnerAssignment partnerAssignment, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isStringNullOrBlank(partnerAssignment.getRecordType())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(partnerAssignment.getClientAccountNumber())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(partnerAssignment.getClientId())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isIntegerNullOrZero(partnerAssignment.getPartnerId())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isDateNull(partnerAssignment.getDtPartnerAssignment())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(PartnerAssignment partnerAssignment, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(partnerAssignment.getClientId()) && partnerAssignment.getClient() == null) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isIntegerNullOrZero(partnerAssignment.getPartnerId()) && partnerAssignment.getPartner() == null) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(PartnerAssignment partnerAssignment) {
		if(!CommonUtils.isStringNullOrBlank(partnerAssignment.getRecordType())) {
			partnerAssignment.setRecordType(partnerAssignment.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(partnerAssignment.getClientAccountNumber())) {
			partnerAssignment.setClientAccountNumber(partnerAssignment.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(PartnerAssignment partnerAssignment) {
		if(partnerAssignment.getAccountIds() != null && !CommonUtils.isLongNull(partnerAssignment.getAccountIds())) {
			partnerAssignment.setAccountId(partnerAssignment.getAccountIds());
		}
	}

	public static void misingRefCheck(PartnerAssignment partnerAssignment, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(!CommonUtils.isStringNullOrBlank(partnerAssignment.getClientAccountNumber()) && CommonUtils.isLongNull(partnerAssignment.getAccountId())) {
				validationMap.put("isPartnerAssignmentValidated", false);
				break misingRefCheck;
			}
		}
	}

}