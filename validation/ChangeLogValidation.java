package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.dao.ChangeLogDao;
import com.equabli.collectprism.entity.ChangeLog;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class ChangeLogValidation {

	public static void mandatoryValidation(ChangeLog changeLog, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E11605") || errWarMessagesList.contains("W11605")) && CommonUtils.isStringNullOrBlank(changeLog.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11605")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E11605"));
			} else if(errWarMessagesList.contains("W11605")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E11605"));
			}
		}
		if((errWarMessagesList.contains("E21603") || errWarMessagesList.contains("W21603")) && CommonUtils.isIntegerNullOrZero(changeLog.getClientId())) {
			if(errWarMessagesList.contains("E21603")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E21603"));
			} else if(errWarMessagesList.contains("W21603")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E21603"));
			}
		}
		if((errWarMessagesList.contains("E11606") || errWarMessagesList.contains("W11606")) && CommonUtils.isStringNullOrBlank(changeLog.getEntityShortName())) {
			if(errWarMessagesList.contains("E11606")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E11606"));
			} else if(errWarMessagesList.contains("W11606")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E11606"));
			}
		}
		if((errWarMessagesList.contains("E11607") || errWarMessagesList.contains("W11607")) && CommonUtils.isStringNullOrBlank(changeLog.getAttributeName())) {
			if(errWarMessagesList.contains("E11607")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E11607"));
			} else if(errWarMessagesList.contains("W11607")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E11607"));
			}
		}
	}

	public static void lookUpValidation(ChangeLog changeLog, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E21603") || errWarMessagesList.contains("W21603")) && !CommonUtils.isIntegerNullOrZero(changeLog.getClientId()) && changeLog.getClient() == null) {
			if(errWarMessagesList.contains("E21603")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E21603"));
			} else if(errWarMessagesList.contains("W21603")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E21603"));
			}
		}
		if((errWarMessagesList.contains("E11606") || errWarMessagesList.contains("W11606")) && !CommonUtils.isStringNullOrBlank(changeLog.getEntityShortName()) && changeLog.getConfEntity() == null) {
			if(errWarMessagesList.contains("E11606")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E11606"));
			} else if(errWarMessagesList.contains("W11606")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E11606"));
			}
		}
		
		
		
	}

	public static void standardize(ChangeLog changeLog) {
		if(!CommonUtils.isStringNullOrBlank(changeLog.getClientAccountNumber())) {
			changeLog.setClientAccountNumber(changeLog.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(ChangeLog changeLog) {
		if(changeLog.getAccountIds() != null && !CommonUtils.isLongNull(changeLog.getAccountIds())) {
			changeLog.setAccountId(changeLog.getAccountIds());
		}
	}

	public static void misingRefCheck(ChangeLog changeLog, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E41604") || errWarMessagesList.contains("W41604")) && !CommonUtils.isStringNullOrBlank(changeLog.getClientAccountNumber()) && CommonUtils.isLongNull(changeLog.getAccountId())) {
			if(errWarMessagesList.contains("E41604")) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson("e", "E41604"));
			} else if(errWarMessagesList.contains("W41604")) {
				changeLog.addErrWarJson(new ErrWarJson("w", "E41604"));
			}
		}
	}

	public static void businessRule(ChangeLog changeLog, Map<String, Object> validationMap, List<String> errWarMessagesList,ChangeLogDao changeLogDao) {
		if(!CommonUtils.isStringNullOrBlank(changeLog.getEntityShortName() ) && !CommonUtils.isStringNullOrBlank(changeLog.getAttributeName())) {
			if(!CommonUtils.isObjectNull(changeLog.getEntityAttribute())) {
				if((errWarMessagesList.contains("E71601") || errWarMessagesList.contains("W71601")) && !( (!CommonUtils.isStringNullOrBlank(changeLog.getExternalSourceType()) && (changeLog.getExternalSourceType().equals("CL") && (changeLog.getEntityAttribute().getIsClientInbound() != null ? changeLog.getEntityAttribute().getIsClientInbound() : false ) )) ||
						(!CommonUtils.isStringNullOrBlank(changeLog.getExternalSourceType()) && (changeLog.getExternalSourceType().equals("PT") && (changeLog.getEntityAttribute().getIsPartnerInbound() != null ? changeLog.getEntityAttribute().getIsPartnerInbound() : false )  ))
						)) {
					if(errWarMessagesList.contains("E71601")) {
						validationMap.put("isChangeLogValidated", false);
						changeLog.addErrWarJson(new ErrWarJson("e", "E71601"));
					} else if(errWarMessagesList.contains("W71601")) {
						changeLog.addErrWarJson(new ErrWarJson("w", "E71601"));
					}
				}
			}else {
				if(errWarMessagesList.contains("E71601")) {
					validationMap.put("isChangeLogValidated", false);
					changeLog.addErrWarJson(new ErrWarJson("e", "E71601"));
				} else if(errWarMessagesList.contains("W71601")) {
					changeLog.addErrWarJson(new ErrWarJson("w", "E71601"));
				}
			}
			
			if(changeLog.getAttributeName().equalsIgnoreCase("dt_statute") &&  !CommonUtils.isObjectNull(changeLog.getExternalSourceType())
					&& changeLog.getExternalSourceType().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER) && changeLog.getSolDateConfiguration()) {
				LocalDate solDate = LocalDate.parse(changeLog.getNewValue()); 

				if( (!CommonUtils.isDateNull(changeLog.getLastPaymentDate())  && (solDate.isBefore(changeLog.getLastPaymentDate()) || solDate.isEqual(changeLog.getLastPaymentDate())))
						 || (!CommonUtils.isDateNull(changeLog.getDelinquencyDate()) && (solDate.isBefore(changeLog.getDelinquencyDate())
					|| solDate.isEqual(changeLog.getDelinquencyDate())))) {
					
					validationMap.put("isChangeLogValidated", false);
					changeLog.addErrWarJson(new ErrWarJson(errWarMessagesList.contains("E71602") ? "e" : "w", "E71602"));
				}
				
			}else if(changeLog.getAttributeName().equalsIgnoreCase("dt_statute") &&  !CommonUtils.isObjectNull(changeLog.getExternalSourceType())
					&& changeLog.getExternalSourceType().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER) && !changeLog.getSolDateConfiguration()) {
				
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson(errWarMessagesList.contains("E71603") ? "e" : "w", "E71603"));
				
			}else if (changeLog.getAttributeName().equalsIgnoreCase("dt_statute") &&  !CommonUtils.isObjectNull(changeLog.getExternalSourceType())
					&& changeLog.getExternalSourceType().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_CLIENT) && changeLog.getSolDateConfiguration()) {
			     validationMap.put("isChangeLogValidated", false);
			     changeLog.addErrWarJson(new ErrWarJson(errWarMessagesList.contains("E71604") ? "e" : "w", "E71604"));
		}
			if(changeLog.getAttributeName().equalsIgnoreCase("subpool_id") && (!isNumeric(changeLog.getNewValue()) || !changeLogDao.validSubPoolId(Long.parseLong(changeLog.getNewValue()),changeLog.getClientId()))) {
				validationMap.put("isChangeLogValidated", false);
				changeLog.addErrWarJson(new ErrWarJson(errWarMessagesList.contains("E71605") ? "e" : "w", "E71605"));
			}	
		}
		
	}
	
	 public static boolean isNumeric(String str) {
	        // Regular expression to check if the string is a valid number
	        return str != null && str.matches("-?\\d+");
	    }
	
}