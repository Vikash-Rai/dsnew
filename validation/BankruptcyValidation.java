package com.equabli.collectprism.validation;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import java.util.List;
import java.util.Map;

public class BankruptcyValidation {

    public static void mandatoryValidation(Bankruptcy bankruptcy, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(bankruptcy.getRecordType())) {
            if (errWarMessagesList.contains("E12306")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12306"));
            } else if (errWarMessagesList.contains("W12306")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12306"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(bankruptcy.getClientAccountNumber())) {
            if (errWarMessagesList.contains("E12302")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12302"));
            } else if (errWarMessagesList.contains("W12302")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12302"));
            }
        }
        if (CommonUtils.isIntegerNullOrZero(bankruptcy.getClientId())) {
            if (errWarMessagesList.contains("E12301")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12301"));
            } else if (errWarMessagesList.contains("W12301")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12301"));
            }
        }
        if (CommonUtils.isDateNull(bankruptcy.getDtBankruptcyFilling())) {
            if (errWarMessagesList.contains("E12303")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12303"));
            } else if (errWarMessagesList.contains("W12303")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12303"));
            }
        }
        if (CommonUtils.isDateNull(bankruptcy.getDtBankruptcyReport())) {
            if (errWarMessagesList.contains("E12304")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12304"));
            } else if (errWarMessagesList.contains("W12304")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12304"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(bankruptcy.getChannel())) {
            if (errWarMessagesList.contains("E12305")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12305"));
            } else if (errWarMessagesList.contains("W12305")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12305"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(bankruptcy.getModeOfReceipt())) {
            if (errWarMessagesList.contains("E12307")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12307"));
            } else if (errWarMessagesList.contains("W12307")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12307"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyChapter())) {
            if (errWarMessagesList.contains("E12308")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E12308"));
            } else if (errWarMessagesList.contains("W12308")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E12308"));
            }
        }
    }

    public static void lookUpValidation(Bankruptcy bankruptcy, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (!CommonUtils.isIntegerNullOrZero(bankruptcy.getClientId()) && bankruptcy.getClient() == null) {
            if (errWarMessagesList.contains("E22301")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22301"));
            } else if (errWarMessagesList.contains("W22301")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22301"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyChapter()) && bankruptcy.getBankruptcyChapterLookUp() == null) {
            if (errWarMessagesList.contains("E22302")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22302"));
            } else if (errWarMessagesList.contains("W22302")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22302"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyType()) && bankruptcy.getBankruptcyTypeLookUp() == null) {
            if (errWarMessagesList.contains("E22303")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22303"));
            } else if (errWarMessagesList.contains("W22303")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22303"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getChannel()) && bankruptcy.getChannelLookUp() == null) {
            if (errWarMessagesList.contains("E22304")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22304"));
            } else if (errWarMessagesList.contains("W22304")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22304"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getModeOfReceipt()) && bankruptcy.getModeOfReceiptLookUp() == null) {
            if (errWarMessagesList.contains("E22305")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22305"));
            } else if (errWarMessagesList.contains("W22305")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22305"));
            }
        }
		if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyCourtState()) && bankruptcy.getCountryState() == null) {
            if (errWarMessagesList.contains("E22306")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22306"));
            } else if (errWarMessagesList.contains("W22306")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22306"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyPetitionStatus()) && bankruptcy.getBankruptcyPetitionStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22307")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22307"));
            } else if (errWarMessagesList.contains("W22307")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22307"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyProcessStatus()) && bankruptcy.getBankruptcyProcessStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22308")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22308"));
            } else if (errWarMessagesList.contains("W22308")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22308"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getObjectionStatus()) && bankruptcy.getObjectionStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22309")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22309"));
            } else if (errWarMessagesList.contains("W22309")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22309"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getAutomaticStayStatus()) && bankruptcy.getAutomaticStayStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22310")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22310"));
            } else if (errWarMessagesList.contains("W22310")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22310"));
            }
        }
		if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyStatus()) && bankruptcy.getBankruptcyStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22311")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E22311"));
            } else if (errWarMessagesList.contains("W22311")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E22311"));
            }
        }
    }

    public static void standardize(Bankruptcy bankruptcy) {
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getClientAccountNumber())) {
            bankruptcy.setClientAccountNumber(bankruptcy.getClientAccountNumber().toUpperCase());
        }
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyCaseNumber())) {
            bankruptcy.setBankruptcyCaseNumber(bankruptcy.getBankruptcyCaseNumber().toUpperCase());
        }
		if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyStatus())) {
			bankruptcy.setBankruptcyStatus(bankruptcy.getBankruptcyStatus().toUpperCase());
		}
    }

    public static void referenceUpdation(Bankruptcy bankruptcy) {
        if (bankruptcy.getAccountIds() != null && !CommonUtils.isLongNull(bankruptcy.getAccountIds())) {
            bankruptcy.setAccountId(bankruptcy.getAccountIds());
        }
        if (bankruptcy.getConsumerAttorneyIds() != null && !CommonUtils.isLongNull(bankruptcy.getConsumerAttorneyIds())) {
        	bankruptcy.setEquabliAttorneyId(bankruptcy.getConsumerAttorneyIds());
        }
        if (bankruptcy.getConsumerTrusteeIds() != null && !CommonUtils.isLongNull(bankruptcy.getConsumerTrusteeIds())) {
        	bankruptcy.setEquabliTrusteeId(bankruptcy.getConsumerTrusteeIds());
        }
        if(bankruptcy.getConsumerIds() != null && !CommonUtils.isLongNull(bankruptcy.getConsumerIds())) {
            bankruptcy.setConsumerId(bankruptcy.getConsumerIds());
        }
    }

    public static void misingRefCheck(Bankruptcy bankruptcy, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (CommonUtils.isLongNull(bankruptcy.getAccountId())) {
            if (errWarMessagesList.contains("E42301")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E42301"));
            } else if (errWarMessagesList.contains("W42301")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E42301"));
            }
        }
        if (!CommonUtils.isLongNull(bankruptcy.getClientAttorneyId()) && CommonUtils.isLongNull(bankruptcy.getEquabliAttorneyId())) {
            if (errWarMessagesList.contains("E42302")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E42302"));
            } else if (errWarMessagesList.contains("W42302")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E42302"));
            }
        }
        if (!CommonUtils.isLongNull(bankruptcy.getClientTrusteeId()) && CommonUtils.isLongNull(bankruptcy.getEquabliTrusteeId())) {
            if (errWarMessagesList.contains("E42303")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E42303"));
            } else if (errWarMessagesList.contains("W42303")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E42303"));
            }
        }
        if(!CommonUtils.isLongNull(bankruptcy.getClientConsumerNumber()) && CommonUtils.isLongNull(bankruptcy.getConsumerId())) {
            if(errWarMessagesList.contains("E42304")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E42304"));
            } else if(errWarMessagesList.contains("W42304")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E42304"));
            }
        }
    }

    public static void businessRule(Bankruptcy bankruptcy, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(bankruptcy.getRecordType()) && !bankruptcy.getRecordType().equalsIgnoreCase("bankruptcy")) {
            if (errWarMessagesList.contains("E72306")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E72306"));
            } else if (errWarMessagesList.contains("W72306")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E72306"));
            }
        }
        if (!CommonUtils.isDateNull(bankruptcy.getDtBankruptcyReport()) && !CommonUtils.isDateNull(bankruptcy.getDtBankruptcyFilling()) 
        		&& bankruptcy.getDtBankruptcyReport().isBefore(bankruptcy.getDtBankruptcyFilling())) {
            if (errWarMessagesList.contains("E72301")) {
                validationMap.put("isBankruptcyValidated", false);
                bankruptcy.addErrWarJson(new ErrWarJson("e", "E72301"));
            } else if (errWarMessagesList.contains("W72301")) {
                bankruptcy.addErrWarJson(new ErrWarJson("w", "E72301"));
            }
        }
		if(!CommonUtils.isDoubleNull(bankruptcy.getAmtTotalBankruptcy()) && bankruptcy.getAmtTotalBankruptcy() > 0) {
			Double amtTotal = (!CommonUtils.isDoubleNull(bankruptcy.getAmtTotalBankruptcy()) && bankruptcy.getAmtTotalBankruptcy() > 0) ? bankruptcy.getAmtTotalBankruptcy() :  0.00;
			Double amtPrincipal = (!CommonUtils.isDoubleNull(bankruptcy.getAmtPrincipalBankruptcy()) && bankruptcy.getAmtPrincipalBankruptcy() > 0) ? bankruptcy.getAmtPrincipalBankruptcy() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(bankruptcy.getAmtInterestBankruptcy()) && bankruptcy.getAmtInterestBankruptcy() > 0) ? bankruptcy.getAmtInterestBankruptcy() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(bankruptcy.getAmtFeeAmountBankruptcy()) && bankruptcy.getAmtFeeAmountBankruptcy() > 0) ? bankruptcy.getAmtFeeAmountBankruptcy() :  0.00;

			String amtTotalBankruptcy = CommonConstants.dfFormat.format(amtTotal);
			String amtCalBankruptcy = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee);

			if(!amtTotalBankruptcy.equalsIgnoreCase(amtCalBankruptcy)) {
	            if (errWarMessagesList.contains("E72302")) {
	                validationMap.put("isBankruptcyValidated", false);
	                bankruptcy.addErrWarJson(new ErrWarJson("e", "E72302"));
	            } else if (errWarMessagesList.contains("W72302")) {
	                bankruptcy.addErrWarJson(new ErrWarJson("w", "E72302"));
	            }
	        }
		}
    }
}
