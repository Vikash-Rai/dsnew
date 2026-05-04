package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.OperationResponse;
import com.equabli.domain.helpers.CommonUtils;

public class OperationResponseValidation {

	public static void mandatoryValidation(OperationResponse or, Map<String, Object> validationMap) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(or.getClientId())) {
				validationMap.put("isOperationResponseValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getClientAccountNumber())) {
				validationMap.put("isOperationResponseValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getResponseSource())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isLongNullOrZero(or.getResponseSourceId())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getRequestNumber())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getResponseStatus())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(OperationResponse or, Map<String, Object> validationMap) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(or.getClientId()) && or.getClient() == null) {
				validationMap.put("isOperationResponseValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getResponseSource()) && or.getResponseSourceLookUp() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getResponseStatus()) && or.getResponseStatusLookUp() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(OperationResponse or) {
		if(!CommonUtils.isStringNullOrBlank(or.getClientAccountNumber())) {
			or.setClientAccountNumber(or.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(or.getResponseSource())) {
			or.setResponseSource(or.getResponseSource().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(or.getResponseStatus())) {
			or.setResponseStatus(or.getResponseStatus().toUpperCase());
		}
	}

	public static void referenceUpdation(OperationResponse or) {
		if(or.getAccountIds() != null && !CommonUtils.isLongNull(or.getAccountIds())) {
			or.setAccountId(or.getAccountIds());
		}
		if(or.getConsumerIds() != null && !CommonUtils.isLongNull(or.getConsumerIds())) {
			or.setConsumerId(or.getConsumerIds());
		}
		if(or.getOperationRequest() != null) {
			or.setOperationRequestId(or.getOperationRequest().getOperationRequestId());
		}
	}

	public static void misingRefCheck(OperationResponse or, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(or.getAccountId())) {
				validationMap.put("isOperationResponseValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isLongNull(or.getClientConsumerNumber()) && CommonUtils.isLongNull(or.getConsumerId())) {
				validationMap.put("isOperationResponseValidated", false);
				break misingRefCheck;
			}
			if(CommonUtils.isLongNull(or.getOperationRequestId())) {
				validationMap.put("isOperationResponseValidated", false);
				break misingRefCheck;
			}
		}
	}
}