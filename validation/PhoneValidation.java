package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Phone;
import com.equabli.collectprism.entity.ScrubWarning;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;

public class PhoneValidation {

	public static void mandatoryValidation(Account account, Consumer consumer, Phone phone, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10301") || errWarMessagesList.contains("W10301")) && CommonUtils.isIntegerNullOrZero(phone.getClientId())) {
			if(errWarMessagesList.contains("E10301")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E10301");
				phone.addErrWarJson(new ErrWarJson("e", "E10301"));
			} else if(errWarMessagesList.contains("W10301")) {
				phone.addErrWarJson(new ErrWarJson("w", "E10301"));
			}
		}
		if((errWarMessagesList.contains("E10302") || errWarMessagesList.contains("W10302")) && CommonUtils.isStringNullOrBlank(phone.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10302")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E10302");
				phone.addErrWarJson(new ErrWarJson("e", "E10302"));
			} else if(errWarMessagesList.contains("W10302")) {
				phone.addErrWarJson(new ErrWarJson("w", "E10302"));
			}
		}
		if((errWarMessagesList.contains("E10318") || errWarMessagesList.contains("W10318")) && CommonUtils.isLongNull(phone.getClientConsumerNumber())) {
			if(errWarMessagesList.contains("E10318")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E10318");
				phone.addErrWarJson(new ErrWarJson("e", "E10318"));
			} else if(errWarMessagesList.contains("W10318")) {
				phone.addErrWarJson(new ErrWarJson("w", "E10318"));
			}
		}
		if((errWarMessagesList.contains("E10328") || errWarMessagesList.contains("W10328")) && CommonUtils.isStringNullOrBlank(phone.getPhone())) {
			if(errWarMessagesList.contains("E10328")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E10328");
				phone.addErrWarJson(new ErrWarJson("e", "E10328"));
			} else if(errWarMessagesList.contains("W10328")) {
				phone.addErrWarJson(new ErrWarJson("w", "E10328"));
			}
		}
	}

	public static void lookUpValidation(Account account, Consumer consumer, Phone phone, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20301") || errWarMessagesList.contains("W20301")) && !CommonUtils.isIntegerNullOrZero(phone.getClientId()) && phone.getClient() == null) {
			if(errWarMessagesList.contains("E20301")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E20301");
				phone.addErrWarJson(new ErrWarJson("e", "E20301"));
			} else if(errWarMessagesList.contains("W20301")) {
				phone.addErrWarJson(new ErrWarJson("w", "E20301"));
			}
		}
		if((errWarMessagesList.contains("E20329") || errWarMessagesList.contains("W20329")) && !CommonUtils.isStringNullOrBlank(phone.getPhoneType()) && phone.getPhoneTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20329")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E20329");
				phone.addErrWarJson(new ErrWarJson("e", "E20329"));
			} else if(errWarMessagesList.contains("W20329")) {
				phone.addErrWarJson(new ErrWarJson("w", "E20329"));
			}
		}
		if((errWarMessagesList.contains("E20330") || errWarMessagesList.contains("W20330")) && !CommonUtils.isStringNullOrBlank(phone.getPhoneStatus()) && phone.getPhoneStatusLookUp() == null) {
			if(errWarMessagesList.contains("E20330")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E20330");
				phone.addErrWarJson(new ErrWarJson("e", "E20330"));
			} else if(errWarMessagesList.contains("W20330")) {
				phone.addErrWarJson(new ErrWarJson("w", "E20330"));
			}
		}
	}

	public static void standardize(Phone phone) {
		if(!CommonUtils.isStringNullOrBlank(phone.getClientAccountNumber())) {
			phone.setClientAccountNumber(phone.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(phone.getPhoneType())) {
			phone.setPhoneType(phone.getPhoneType().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(phone.getPhoneStatus())) {
			phone.setPhoneStatus(phone.getPhoneStatus().toUpperCase());
		}
	}

	public static void deDupValidation(Account account, Consumer consumer, Phone phone, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E30328") || errWarMessagesList.contains("W30328")) && phone.getPhoneNoDeDup() > 1) {
			if(errWarMessagesList.contains("E30328")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E30328");
				phone.addErrWarJson(new ErrWarJson("e", "E30328"));
			} else if(errWarMessagesList.contains("W30328")) {
				phone.addErrWarJson(new ErrWarJson("w", "E30328"));
			}
		}
		if(phone.getIsPrimary() != null && phone.getIsPrimary() && phone.getIsPrimaryDeDup() > 1) {
			if(consumer.getPhoneId() == null && phone.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId())) {
				consumer.setPhoneId(phone.getPhoneId());
			} else {
				phone.setIsPrimary(false);
			}
		}
	}

	public static void referenceUpdation(Account account, Consumer consumer, Phone phone, Integer phoneCount) {
		if(!CommonUtils.isLongNull(account.getAccountId())) {
			phone.setAccountId(account.getAccountId());
		}
		if(!CommonUtils.isLongNull(consumer.getConsumerId())) {
			phone.setConsumerId(consumer.getConsumerId());
		}
		if(CommonUtils.isBooleanNull(phone.getIsPrimary())) {
			if(phoneCount == 1) {
				phone.setIsPrimary(true);
			} else {
				phone.setIsPrimary(false);
			}
		}
	}

	public static void misingRefCheck(Account account, Consumer consumer, Phone phone, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E40316") || errWarMessagesList.contains("W40316")) && CommonUtils.isLongNull(phone.getAccountId())) {
			if(errWarMessagesList.contains("E40316")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E40316");
				phone.addErrWarJson(new ErrWarJson("e", "E40316"));
			} else if(errWarMessagesList.contains("W40316")) {
				phone.addErrWarJson(new ErrWarJson("w", "E40316"));
			}
		}
		if((errWarMessagesList.contains("E40327") || errWarMessagesList.contains("W40327")) && CommonUtils.isLongNull(phone.getConsumerId())) {
			if(errWarMessagesList.contains("E40327")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E40327");
				phone.addErrWarJson(new ErrWarJson("e", "E40327"));
			} else if(errWarMessagesList.contains("W40327")) {
				phone.addErrWarJson(new ErrWarJson("w", "E40327"));
			}
		}
	}

	public static void businessRule(Phone phone, Map<String, Object> validationMap, List<ScrubWarning> scrubWarningList, Integer appId, Integer recordSourceId, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E70301") || errWarMessagesList.contains("W70301")) && !CommonUtils.isStringNullOrBlank(phone.getPhone()) && phone.getPhone().length() != 10) {
			if(errWarMessagesList.contains("E70301")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E70301");
				phone.addErrWarJson(new ErrWarJson("e", "E70301"));
			} else if(errWarMessagesList.contains("W70301")) {
				phone.addErrWarJson(new ErrWarJson("w", "E70301"));
			}
		}
		if((errWarMessagesList.contains("E70302") || errWarMessagesList.contains("W70302")) && !CommonUtils.isStringNullOrBlank(phone.getPhone()) && !CommonUtils.onlyDigits(phone.getPhone())) {
			if(errWarMessagesList.contains("E70302")) {
				validationMap.put("isPhoneValidated", false);
				phone.setErrShortName("E70302");
				phone.addErrWarJson(new ErrWarJson("e", "E70302"));
			} else if(errWarMessagesList.contains("W70302")) {
				phone.addErrWarJson(new ErrWarJson("w", "E70302"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(phone.getPhoneType()) && phone.getPhoneType().equalsIgnoreCase("H") 
				&& ((phone.getIsConsent() == null || !phone.getIsConsent()) 
					|| (phone.getSmsConsent() == null || !phone.getSmsConsent()) 
					|| (phone.getAutoDialConsent() == null || !phone.getAutoDialConsent()))) {
			phone.setIsConsent(true);
			phone.setSmsConsent(true);
			phone.setAutoDialConsent(true);
		}
		if(CommonUtils.isStringNullOrBlank(phone.getPhoneNoTimeZone())) {
			ScrubWarning.setScrubWarning(validationMap, "data.phone", phone.getPhoneId().toString(), "W70301", scrubWarningList, appId, recordSourceId);
		}
	}
}