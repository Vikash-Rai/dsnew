package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Employer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.EntityUtil;

public class EmployerValidation {

	public static void mandatoryValidation(Employer emp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E11401") || errWarMessagesList.contains("W11401")) && CommonUtils.isIntegerNullOrZero(emp.getClientId())) {
			if(errWarMessagesList.contains("E11401")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E11401"));
			} else if(errWarMessagesList.contains("W11401")) {
				emp.addErrWarJson(new ErrWarJson("w", "E11401"));
			}
		}
		if((errWarMessagesList.contains("E11402") || errWarMessagesList.contains("W11402")) && CommonUtils.isStringNullOrBlank(emp.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11402")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E11402"));
			} else if(errWarMessagesList.contains("W11402")) {
				emp.addErrWarJson(new ErrWarJson("w", "E11402"));
			}
		}
		if((errWarMessagesList.contains("E71401") || errWarMessagesList.contains("W71401")) && CommonUtils.isLongNull(emp.getClientConsumerNumber()) && CommonUtils.isStringNullOrBlank(emp.getConsumerIdentificationNumber())) {
			if(errWarMessagesList.contains("E71401")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E71401"));
			} else if(errWarMessagesList.contains("W71401")) {
				emp.addErrWarJson(new ErrWarJson("w", "E71401"));
			}
		}
		if((errWarMessagesList.contains("E11475") || errWarMessagesList.contains("W11475")) && CommonUtils.isStringNullOrBlank(emp.getFullName())) {
			if(errWarMessagesList.contains("E11475")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E11475"));
			} else if(errWarMessagesList.contains("W11475")) {
				emp.addErrWarJson(new ErrWarJson("w", "E11475"));
			}
		}
	}

	public static void lookUpValidation(Employer emp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E21401") || errWarMessagesList.contains("W21401")) && !CommonUtils.isIntegerNullOrZero(emp.getClientId()) && emp.getClient() == null) {
			if(errWarMessagesList.contains("E21401")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E21401"));
			} else if(errWarMessagesList.contains("W21401")) {
				emp.addErrWarJson(new ErrWarJson("w", "E21401"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getZip()) && emp.getCountryZip() == null) {
			emp.setZip(null);
		}
		if((errWarMessagesList.contains("E21497") || errWarMessagesList.contains("W21497")) && !CommonUtils.isStringNullOrBlank(emp.getEmployerStatus()) && emp.getEmployerStatusLookUp() == null) {
			if(errWarMessagesList.contains("E21497")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E21497"));
			} else if(errWarMessagesList.contains("W21497")) {
				emp.addErrWarJson(new ErrWarJson("w", "E21497"));
			}
		}
		if((errWarMessagesList.contains("E21498") || errWarMessagesList.contains("W21498")) && !CommonUtils.isStringNullOrBlank(emp.getConsumerWagepayFrequency()) && emp.getConsumerWagepayFrequencyLookUp() == null) {
			if(errWarMessagesList.contains("E21498")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E21498"));
			} else if(errWarMessagesList.contains("W21498")) {
				emp.addErrWarJson(new ErrWarJson("w", "E21498"));
			}
		}
	}

	public static void standardize(Employer emp) {
		if(!CommonUtils.isStringNullOrBlank(emp.getClientAccountNumber())) {
			emp.setClientAccountNumber(emp.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getFullName())) {
			emp.setFullName(EntityUtil.capitailizeWord(emp.getFullName()));
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getAddress1())) {
			emp.setAddress1(EntityUtil.capitailizeWord(emp.getAddress1()));
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getAddress2())) {
			emp.setAddress2(emp.getAddress2().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getStateCode())) {
			emp.setStateCode(emp.getStateCode().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(emp.getCity())) {
			emp.setCity(EntityUtil.capitailizeWord(emp.getCity()));
		}
	}

	public static void deDupValidation(Employer emp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E31419") || errWarMessagesList.contains("W31419")) && emp.getClientEmployerNumberDeDup() > 1) {
			if(errWarMessagesList.contains("E31419")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E31419"));
			} else if(errWarMessagesList.contains("W31419")) {
				emp.addErrWarJson(new ErrWarJson("w", "E31419"));
			}
		}
	}

	public static void referenceUpdation(Employer emp) {
		if(emp.getAccountIds() != null && !CommonUtils.isLongNull(emp.getAccountIds())) {
			emp.setAccountId(emp.getAccountIds());
		}
		if(emp.getConsumerIdsByConsumerNumber() != null && !CommonUtils.isLongNull(emp.getConsumerIdsByConsumerNumber())) {
			emp.setConsumerId(emp.getConsumerIdsByConsumerNumber());
		}
		else if(emp.getConsumerIdsByIdentificationNumber() != null && !CommonUtils.isLongNull(emp.getConsumerIdsByIdentificationNumber())) {
			emp.setConsumerId(emp.getConsumerIdsByIdentificationNumber());
		}
	}

	public static void misingRefCheck(Employer emp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E71403") || errWarMessagesList.contains("W71403")) && CommonUtils.isLongNull(emp.getAccountId()) && CommonUtils.isLongNull(emp.getConsumerId())) {
			if(errWarMessagesList.contains("E71403")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E71403"));
			} else if(errWarMessagesList.contains("W71403")) {
				emp.addErrWarJson(new ErrWarJson("w", "E71403"));
			}
		}
		if((errWarMessagesList.contains("E71403") || errWarMessagesList.contains("W71403")) && !CommonUtils.isStringNullOrBlank(emp.getClientAccountNumber()) && CommonUtils.isLongNull(emp.getAccountId())) {
			if(errWarMessagesList.contains("E71403")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E71403"));
			} else if(errWarMessagesList.contains("W71403")) {
				emp.addErrWarJson(new ErrWarJson("w", "E71403"));
			}
		}
		if((errWarMessagesList.contains("E71403") || errWarMessagesList.contains("W71403")) && (!CommonUtils.isLongNull(emp.getClientConsumerNumber()) || !CommonUtils.isStringNullOrBlank(emp.getConsumerIdentificationNumber()))
				&& CommonUtils.isLongNull(emp.getConsumerId())) {
			if(errWarMessagesList.contains("E71403")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E71403"));
			} else if(errWarMessagesList.contains("W71403")) {
				emp.addErrWarJson(new ErrWarJson("w", "E71403"));
			}
		}
	}

	public static void businessRule(Employer emp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E71402") || errWarMessagesList.contains("W71402")) && !CommonUtils.isStringNullOrBlank(emp.getPhone()) && !CommonUtils.onlyDigits(emp.getPhone())) {
			if(errWarMessagesList.contains("E71402")) {
				validationMap.put("isEmployerValidated", false);
				emp.addErrWarJson(new ErrWarJson("e", "E71402"));
			} else if(errWarMessagesList.contains("W71402")) {
				emp.addErrWarJson(new ErrWarJson("w", "E71402"));
			}
		}
	}
}