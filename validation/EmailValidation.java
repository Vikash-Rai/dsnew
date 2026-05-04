package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.Email;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.ScrubWarning;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;

public class EmailValidation {

	public static void mandatoryValidation(Account account, Email email, Map<String, Object> validationMap, List<String> errWarMessagesList ) {
		if((errWarMessagesList.contains("E10501") || errWarMessagesList.contains("W10501")) && CommonUtils.isIntegerNullOrZero(email.getClientId())) {
			if(errWarMessagesList.contains("E10501")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E10501");
				email.addErrWarJson(new ErrWarJson("e", "E10501"));
			} else if(errWarMessagesList.contains("W10501")) {
				email.addErrWarJson(new ErrWarJson("w", "E10501"));
			}
		}
		if((errWarMessagesList.contains("E10502") || errWarMessagesList.contains("W10502")) && CommonUtils.isStringNullOrBlank(email.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10502")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E10502");
				email.addErrWarJson(new ErrWarJson("e", "E10502"));
			} else if(errWarMessagesList.contains("W10502")) {
				email.addErrWarJson(new ErrWarJson("w", "E10502"));
			}
		}
		if((errWarMessagesList.contains("E10518") || errWarMessagesList.contains("W10518")) && CommonUtils.isLongNull(email.getClientConsumerNumber())) {
			if(errWarMessagesList.contains("E10518")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E10518");
				email.addErrWarJson(new ErrWarJson("e", "E10518"));
			} else if(errWarMessagesList.contains("W10518")) {
				email.addErrWarJson(new ErrWarJson("w", "E10518"));
			}
		}
		if((errWarMessagesList.contains("E10542") || errWarMessagesList.contains("W10542")) && CommonUtils.isStringNullOrBlank(email.getEmailAddress())) {
			if(errWarMessagesList.contains("E10542")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E10542");
				email.addErrWarJson(new ErrWarJson("e", "E10542"));
			} else if(errWarMessagesList.contains("W10542")) {
				email.addErrWarJson(new ErrWarJson("w", "E10542"));
			}
		}
	}

	public static void lookUpValidation(Account account, Email email, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20501") || errWarMessagesList.contains("W20501")) && !CommonUtils.isIntegerNullOrZero(email.getClientId()) && email.getClient() == null) {
			if(errWarMessagesList.contains("E20501")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E20501");
				email.addErrWarJson(new ErrWarJson("e", "E20501"));
			} else if(errWarMessagesList.contains("W20501")) {
				email.addErrWarJson(new ErrWarJson("w", "E20501"));
			}
		}
		if((errWarMessagesList.contains("E20592") || errWarMessagesList.contains("W20592")) && !CommonUtils.isStringNullOrBlank(email.getEmailStatus()) && email.getEmailStatusLookUp() == null) {
			if(errWarMessagesList.contains("E20592")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E20592");
				email.addErrWarJson(new ErrWarJson("e", "E20592"));
			} else if(errWarMessagesList.contains("W20592")) {
				email.addErrWarJson(new ErrWarJson("w", "E20592"));
			}
		}
	}

	public static void standardize(Email email) {
		if(!CommonUtils.isStringNullOrBlank(email.getClientAccountNumber())) {
			email.setClientAccountNumber(email.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(email.getEmailAddress())) {
			email.setEmailAddress(email.getEmailAddress().toLowerCase());
		}
	}

	public static void deDupValidation(Account account, Consumer consumer, Email email, Map<String, Object> validationMap, List<ScrubWarning> scrubWarningList, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E30542") || errWarMessagesList.contains("W30542")) && email.getEmailDeDup() > 1) {
			if(errWarMessagesList.contains("E30542")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E30542");
				email.addErrWarJson(new ErrWarJson("e", "E30542"));
			} else if(errWarMessagesList.contains("W30542")) {
				email.addErrWarJson(new ErrWarJson("w", "E30542"));
			}
		}
		if(email.getIsPrimary() != null && email.getIsPrimary() && email.getIsPrimaryDeDup() > 1) {
			if(consumer.getEmailId() == null && email.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId())) {
				consumer.setEmailId(email.getEmailId());
			} else {
				email.setIsPrimary(false);
			}
		}
	}

	public static void referenceUpdation(Account account, Consumer consumer, Email email, Integer emailCount) {
		if(!CommonUtils.isLongNull(account.getAccountId())) {
			email.setAccountId(account.getAccountId());
		}
		if(!CommonUtils.isLongNull(consumer.getConsumerId())) {
			email.setConsumerId(consumer.getConsumerId());
		}
		if(CommonUtils.isBooleanNull(email.getIsPrimary())) {
			if(emailCount == 1) {
				email.setIsPrimary(true);
			} else {
				email.setIsPrimary(false);
			}
		}
	}

	public static void misingRefCheck(Account account, Email email, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E40516") || errWarMessagesList.contains("W40516")) && CommonUtils.isLongNull(email.getAccountId())) {
			if(errWarMessagesList.contains("E40516")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E40516");
				email.addErrWarJson(new ErrWarJson("e", "E40516"));
			} else if(errWarMessagesList.contains("W40516")) {
				email.addErrWarJson(new ErrWarJson("w", "E40516"));
			}
		}
		if((errWarMessagesList.contains("E40527") || errWarMessagesList.contains("W40527")) && CommonUtils.isLongNull(email.getConsumerId())) {
			if(errWarMessagesList.contains("E40527")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E40527");
				email.addErrWarJson(new ErrWarJson("e", "E40527"));
			} else if(errWarMessagesList.contains("W40527")) {
				email.addErrWarJson(new ErrWarJson("w", "E40527"));
			}
		}
	}

	public static void businessRule(Email email, Map<String, Object> validationMap,  List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E70501") || errWarMessagesList.contains("W70501")) && !CommonUtils.isStringNullOrBlank(email.getEmailAddress()) && !CommonUtils.isValidEmail(email.getEmailAddress())) {
			if(errWarMessagesList.contains("E70501")) {
				validationMap.put("isEmailValidated", false);
				email.setErrShortName("E70501");
				email.addErrWarJson(new ErrWarJson("e", "E70501"));
			} else if(errWarMessagesList.contains("W70501")) {
				email.addErrWarJson(new ErrWarJson("w", "E70501"));
			}
		}
	}
}