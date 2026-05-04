package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.OperationRequest;
import com.equabli.domain.QueueReason;
import com.equabli.domain.helpers.CommonUtils;

public class OperationRequestValidation {

	public static void mandatoryValidation(OperationRequest or, Map<String, Object> validationMap) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(or.getClientId())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getClientAccountNumber())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getOperationRequestType())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getRequestSource())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isLongNullOrZero(or.getRequestSourceId())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getRequestNumber())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getRequestStatus())) {
				validationMap.put("isOperationRequestValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(or.getQueueReason())) {
				or.setQueueReason(QueueReason.QUEUEREASON_NA);
				or.setQueueReasonIds(QueueReason.confQueueReason.get(QueueReason.QUEUEREASON_NA).getQueueReasonId());
			}
			if(CommonUtils.isIntegerNullOrZero(or.getPriority())) {
				or.setPriority(2);
			}
		}
	}

	public static void lookUpValidation(OperationRequest or, Map<String, Object> validationMap) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(or.getClientId()) && or.getClient() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getOperationRequestType()) && or.getOperationRequestTypeMap() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getQueueReason()) && CommonUtils.isIntegerNullOrZero(or.getQueueReasonIds())) {
				or.setQueueReason(QueueReason.QUEUEREASON_OTR);
				or.setQueueReasonIds(QueueReason.confQueueReason.get(QueueReason.QUEUEREASON_OTR).getQueueReasonId());
			}
			if(!CommonUtils.isStringNullOrBlank(or.getRequestSource()) && or.getRequestSourceLookUp() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getRequestStatus()) && or.getRequestStatusLookUp() == null) {
				validationMap.put("isOperationRequestValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void standardize(OperationRequest or) {
		if(!CommonUtils.isStringNullOrBlank(or.getClientAccountNumber())) {
			or.setClientAccountNumber(or.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(or.getRequestSource())) {
			or.setRequestSource(or.getRequestSource().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(or.getRequestStatus())) {
			or.setRequestStatus(or.getRequestStatus().toUpperCase());
		}
	}

	public static void referenceUpdation(OperationRequest or) {
		if(or.getAccountIds() != null && !CommonUtils.isLongNull(or.getAccountIds())) {
			or.setAccountId(or.getAccountIds());
		}
		if(or.getConsumerIds() != null && !CommonUtils.isLongNull(or.getConsumerIds())) {
			or.setConsumerId(or.getConsumerIds());
		}
		if(or.getOperationRequestTypeMap() != null) {
			or.setOperationRequesttypeId(or.getOperationRequestTypeMap().getOperationRequestTypeId());
		}
		if(!CommonUtils.isIntegerNullOrZero(or.getQueueReasonIds())) {
			or.setNewQueueReasonId(or.getQueueReasonIds());
		}
		if(!CommonUtils.isIntegerNullOrZero(or.getQueueIds())) {
			or.setQueueId(or.getQueueIds());
		}
		if(!CommonUtils.isIntegerNullOrZero(or.getQueueReasonsIds())) {
			or.setQueueReasonId(or.getQueueReasonsIds());
		}
		if(!CommonUtils.isIntegerNullOrZero(or.getQueueStatusIds())) {
			or.setQueueStatusId(or.getQueueStatusIds());
		}
	}

	public static void misingRefCheck(OperationRequest or, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(or.getAccountId())) {
				validationMap.put("isOperationRequestValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isLongNull(or.getClientConsumerNumber()) && CommonUtils.isLongNull(or.getConsumerId())) {
				validationMap.put("isOperationRequestValidated", false);
				break misingRefCheck;
			}
			if(CommonUtils.isIntegerNullOrZero(or.getOperationRequesttypeId())) {
				validationMap.put("isOperationRequestValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isStringNullOrBlank(or.getQueueReason()) && CommonUtils.isIntegerNullOrZero(or.getNewQueueReasonId())) {
				validationMap.put("isOperationRequestValidated", false);
				break misingRefCheck;
			}
		}
	}
}