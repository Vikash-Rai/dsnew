package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Deceased;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class DeceasedValidation {
	
	public static void mandatoryValidation(Deceased g, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(g.getRecordType())) {
			if(errWarMessagesList.contains("E12203")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12203"));
			} else if(errWarMessagesList.contains("W12203")) {
				g.addErrWarJson(new ErrWarJson("w", "E12203"));
			}
        }
		if(CommonUtils.isIntegerNullOrZero(g.getClientId())) {
			if(errWarMessagesList.contains("E12201")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12201"));
			} else if(errWarMessagesList.contains("W12201")) {
				g.addErrWarJson(new ErrWarJson("w", "E12201"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E12202")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12202"));
			} else if(errWarMessagesList.contains("W12202")) {
				g.addErrWarJson(new ErrWarJson("w", "E12202"));
			}
		}
		
		if(CommonUtils.isLongNullOrZero(g.getClientDecedentId())) {
			if(errWarMessagesList.contains("E12205")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12205"));
			} else if(errWarMessagesList.contains("W12205")) {
				g.addErrWarJson(new ErrWarJson("w", "E12205"));
			}
		}
		if(CommonUtils.isDateNull(g.getDtDeceased())) {
			if(errWarMessagesList.contains("E12209")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12209"));
			} else if(errWarMessagesList.contains("W12209")) {
				g.addErrWarJson(new ErrWarJson("w", "E12209"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getChannel())) {
			if(errWarMessagesList.contains("E12206")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12206"));
			} else if(errWarMessagesList.contains("W12206")) {
				g.addErrWarJson(new ErrWarJson("w", "E12206"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getMode())) {
			if(errWarMessagesList.contains("E12207")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12207"));
			} else if(errWarMessagesList.contains("W12207")) {
				g.addErrWarJson(new ErrWarJson("w", "E12207"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getDcStatus())) {
			if(errWarMessagesList.contains("E12208")) {
				validationMap.put("isDeceasedValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12208"));
			} else if(errWarMessagesList.contains("W12208")) {
				g.addErrWarJson(new ErrWarJson("w", "E12208"));
			}
		}
	}

	public static void lookUpValidation(Deceased deceased, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(deceased.getClientId()) && deceased.getClient() == null) {
			if(errWarMessagesList.contains("E12201")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E12201"));
			} else if(errWarMessagesList.contains("W12201")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E12201"));
			}
		}
        if (!CommonUtils.isLongNullOrZero(deceased.getClientDecedentId()) && deceased.getDecencentlookup() == null) {
			if(errWarMessagesList.contains("E22201")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E22201"));
			} else if(errWarMessagesList.contains("W22201")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E22201"));
			}
        }
        if (!CommonUtils.isStringNullOrBlank(deceased.getChannel()) && deceased.getComplianceChannelLookUp() == null) {
			if(errWarMessagesList.contains("E22202")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E22202"));
			} else if(errWarMessagesList.contains("W22202")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E22202"));
			}
        }
        if (!CommonUtils.isStringNullOrBlank(deceased.getDcStatus()) && deceased.getDcStatusLookUp() == null) {
			if(errWarMessagesList.contains("E22203")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E22203"));
			} else if(errWarMessagesList.contains("W22203")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E22203"));
			}
        }
        if (!CommonUtils.isStringNullOrBlank(deceased.getMode()) && deceased.getComplianceModeLookUp() == null) {
			if(errWarMessagesList.contains("E22204")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E22204"));
			} else if(errWarMessagesList.contains("W22204")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E22204"));
			}
        }
	}

	public static void standardize(Deceased deceased) {
		if(!CommonUtils.isStringNullOrBlank(deceased.getClientAccountNumber())) {
			deceased.setClientAccountNumber(deceased.getClientAccountNumber().toUpperCase());
		}
		
	}

	public static void referenceUpdation(Deceased deceased  ) {
		if(deceased.getAccountIds() != null && !CommonUtils.isLongNull(deceased.getAccountIds())) {
			deceased.setAccountId(deceased.getAccountIds());
		}

		  if (!CommonUtils.isLongNullOrZero(deceased.getClientDecedentId()) && deceased.getDecencentlookup() != null) {
			  deceased.setDecedent(deceased.getDecencentlookup());
	        }
	}

	public static void misingRefCheck(Deceased deceased, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(CommonUtils.isLongNull(deceased.getAccountId())) {
			if(errWarMessagesList.contains("E12204")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E12204"));
			} else if(errWarMessagesList.contains("W12204")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E12204"));
			}
		}
       
	}

	public static void businessRule(Deceased deceased, Map<String, Object> validationMap,List<String> errWarMessagesList) {
        if (!CommonUtils.isDoubleNull(deceased.getAmtDcPrincipal()) && deceased.getAmtDcPrincipal() < 0) {
			if(errWarMessagesList.contains("E72201")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72201"));
			} else if(errWarMessagesList.contains("W72201")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72201"));
			}
        }
		if(!CommonUtils.isDoubleNull(deceased.getAmtDcInterest()) && deceased.getAmtDcInterest() < 0) {
			if(errWarMessagesList.contains("E72202")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72202"));
			} else if(errWarMessagesList.contains("W72202")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72202"));
			}
		}
		if(!CommonUtils.isDoubleNull(deceased.getAmtDcFees()) && deceased.getAmtDcFees() < 0) {
			if(errWarMessagesList.contains("E72203")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72203"));
			} else if(errWarMessagesList.contains("W72203")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72203"));
			}
		}
		if (!CommonUtils.isDoubleNull(deceased.getAmtDcTotal()) && deceased.getAmtDcTotal() < 0) {
			if(errWarMessagesList.contains("E72204")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72204"));
			} else if(errWarMessagesList.contains("W72204")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72204"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateZip()) && deceased.getCountryZip() == null) {
			if(errWarMessagesList.contains("E72220")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72220"));
			} else if(errWarMessagesList.contains("W72220")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72220"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateFirmState()) && deceased.getCountryState() == null) {
			if(errWarMessagesList.contains("E72205")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72205"));
			} else if(errWarMessagesList.contains("W72205")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72205"));
			}
		}
		if((!CommonUtils.isStringNullOrBlank(deceased.getProbatePhone())) && (deceased.getProbatePhone().length() != 10 || !CommonUtils.onlyDigits(deceased.getProbatePhone())) ) {
			if(errWarMessagesList.contains("E72206")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72206"));
			} else if(errWarMessagesList.contains("W72206")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72206"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateEmail()) && !CommonUtils.isValidEmail(deceased.getProbateEmail())) {
			if(errWarMessagesList.contains("E72207")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72207"));
			} else if(errWarMessagesList.contains("W72207")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72207"));
			}
		}
		
		
		//probate firm 
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateFirmZip()) && deceased.getProbateCountryZip() == null) {
			if(errWarMessagesList.contains("E72221")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72221"));
			} else if(errWarMessagesList.contains("W72221")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72221"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateFirmState()) && deceased.getProbateCountryState() == null) {
			if(errWarMessagesList.contains("E72208")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72208"));
			} else if(errWarMessagesList.contains("W72208")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72208"));
			}
		}
		if((!CommonUtils.isStringNullOrBlank(deceased.getProbateFirmPhone())) && (deceased.getProbateFirmPhone().length() != 10 || !CommonUtils.onlyDigits(deceased.getProbateFirmPhone())) ) {
			if(errWarMessagesList.contains("E72209")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72209"));
			} else if(errWarMessagesList.contains("W72209")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72209"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getProbateEmail()) && !CommonUtils.isValidEmail(deceased.getProbateFirmEmail())) {
			if(errWarMessagesList.contains("E72210")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72210"));
			} else if(errWarMessagesList.contains("W72210")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72210"));
			}
		}
		
		//executor validation
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorZip()) && deceased.getExecutorCountryZip() == null) {
			if(errWarMessagesList.contains("E72211")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72211"));
			} else if(errWarMessagesList.contains("W72211")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72211"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorState()) && deceased.getExecutorCountryState() == null) {
			if(errWarMessagesList.contains("E72212")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72212"));
			} else if(errWarMessagesList.contains("W72212")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72212"));
			}
		}
		if((!CommonUtils.isStringNullOrBlank(deceased.getExecutorPhone())) && (deceased.getExecutorPhone().length() != 10 || !CommonUtils.onlyDigits(deceased.getExecutorPhone())) ) {
			if(errWarMessagesList.contains("E72213")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72213"));
			} else if(errWarMessagesList.contains("W72213")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72213"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorEmail()) && !CommonUtils.isValidEmail(deceased.getExecutorEmail())) {
			if(errWarMessagesList.contains("E72214")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72214"));
			} else if(errWarMessagesList.contains("W72214")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72214"));
			}
		}
		
		// executor firm validation
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorFirmZip()) && deceased.getExecutorFirmCountryZip() == null) {
			if(errWarMessagesList.contains("E72215")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72215"));
			} else if(errWarMessagesList.contains("W72215")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72215"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorFirmState()) && deceased.getExecutorFirmCountryState() == null) {
			if(errWarMessagesList.contains("E72216")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72216"));
			} else if(errWarMessagesList.contains("W72216")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72216"));
			}
		}
		if((!CommonUtils.isStringNullOrBlank(deceased.getExecutorFirmPhone())) && (deceased.getExecutorFirmPhone().length() != 10 || !CommonUtils.onlyDigits(deceased.getExecutorFirmPhone())) ) {
			if(errWarMessagesList.contains("E72217")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72217"));
			} else if(errWarMessagesList.contains("W72217")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72217"));
			}
		}
		
		if(!CommonUtils.isStringNullOrBlank(deceased.getExecutorFirmEmail()) && !CommonUtils.isValidEmail(deceased.getExecutorFirmEmail())) {
			if(errWarMessagesList.contains("E72218")) {
				validationMap.put("isDeceasedValidated", false);
				deceased.addErrWarJson(new ErrWarJson("e", "E72218"));
			} else if(errWarMessagesList.contains("W72218")) {
				deceased.addErrWarJson(new ErrWarJson("w", "E72218"));
			}
		}
		
	}

}
