package com.equabli.collectprism.validation;

import java.util.Map;

import com.equabli.collectprism.entity.CoTOwner;
import com.equabli.domain.helpers.CommonUtils;

public class CoTOwnerValidation {

    public static void mandatoryValidation(CoTOwner cotOwner, Map<String, Object> validationMap) {
        mandatoryValidation: {
            if (CommonUtils.isStringNullOrBlank(cotOwner.getRecordType())) {
                validationMap.put("isCoTOwnerValidated", false);
                break mandatoryValidation;
            }
            if (CommonUtils.isIntegerNullOrZero(cotOwner.getClientId())) {
                validationMap.put("isCoTOwnerValidated", false);
                break mandatoryValidation;
            }
            if (CommonUtils.isLongNullOrZero(cotOwner.getExternalSystemId())) {
                validationMap.put("isCoTOwnerValidated", false);
                break mandatoryValidation;
            }
            if (CommonUtils.isStringNullOrBlank(cotOwner.getOwnerName())) {
                validationMap.put("isCoTOwnerValidated", false);
                break mandatoryValidation;
            }
            if (CommonUtils.isStringNullOrBlank(cotOwner.getAddress1())) {
                validationMap.put("isCoTOwnerValidated", false);
                break mandatoryValidation;
            }
        }
    }

    public static void lookUpValidation(CoTOwner coTOwner, Map<String, Object> validationMap) {
        lookUpValidation: {
            if (!CommonUtils.isIntegerNullOrZero(coTOwner.getClientId()) && coTOwner.getClient() == null) {
                validationMap.put("isCoTOwnerValidated", false);
                break lookUpValidation;
            }
        }
    }

    public static void standardize(CoTOwner coTOwner) {
		if(!CommonUtils.isStringNullOrBlank(coTOwner.getRecordType())) {
			coTOwner.setRecordType(coTOwner.getRecordType().toLowerCase());
		}
	}
}