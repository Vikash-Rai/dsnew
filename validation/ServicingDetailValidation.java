package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.ServicingDetail;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class ServicingDetailValidation {

	public static void mandatoryValidation(ServicingDetail sd, Map<String, Object> validationMap, CacheableService cacheableService) {
		mandatoryValidation:{
			if(CommonUtils.isIntegerNullOrZero(sd.getClientId())) {
				validationMap.put("isServicingDetailValidated", false);
				break mandatoryValidation;
			}
			if(CommonUtils.isStringNullOrBlank(sd.getClientAccountNumber()) && CommonUtils.isLongNull(sd.getClientConsumerNumber())) {
				validationMap.put("isServicingDetailValidated", false);
				break mandatoryValidation;
			}
		}
	}

	public static void lookUpValidation(ServicingDetail sd, Map<String, Object> validationMap, CacheableService cacheableService) {
		lookUpValidation:{
			if(!CommonUtils.isIntegerNullOrZero(sd.getClientId()) && sd.getClient() == null) {
				validationMap.put("isServicingDetailValidated", false);
				break lookUpValidation;
			}
		}
	}

	public static void referenceUpdation(ServicingDetail sd) {
		if(sd.getAccountIds() != null && !CommonUtils.isLongNull(sd.getAccountIds())) {
			sd.setAccountId(sd.getAccountIds());
		}
		if(sd.getConsumerIds() != null && !CommonUtils.isLongNull(sd.getConsumerIds())) {
			sd.setConsumerId(sd.getConsumerIds());
		}
	}

	public static void misingRefCheck(ServicingDetail sd, Map<String, Object> validationMap) {
		misingRefCheck:{
			if(CommonUtils.isLongNull(sd.getAccountId()) && CommonUtils.isLongNull(sd.getConsumerId())) {
				validationMap.put("isServicingDetailValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isStringNullOrBlank(sd.getClientAccountNumber()) && CommonUtils.isLongNull(sd.getAccountId())) {
				validationMap.put("isServicingDetailValidated", false);
				break misingRefCheck;
			}
			if(!CommonUtils.isLongNull(sd.getClientConsumerNumber()) && CommonUtils.isLongNull(sd.getConsumerId())) {
				validationMap.put("isServicingDetailValidated", false);
				break misingRefCheck;
			}
		}
	}
}