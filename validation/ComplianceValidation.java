package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Compliance;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.ConfRegulatoryBody;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class ComplianceValidation {

	public static void mandatoryValidation(Compliance comp, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E11102") || errWarMessagesList.contains("W11102")) && CommonUtils.isStringNullOrBlank(comp.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11102")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11102"));
			} else if(errWarMessagesList.contains("W11102")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11102"));
			}
		}
		if((errWarMessagesList.contains("E11101") || errWarMessagesList.contains("W11101")) && CommonUtils.isIntegerNullOrZero(comp.getClientId())) {
			if(errWarMessagesList.contains("E11101")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11101"));
			} else if(errWarMessagesList.contains("W11101")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11101"));
			}
		}
		if((errWarMessagesList.contains("E11166") || errWarMessagesList.contains("W11166")) && CommonUtils.isStringNullOrBlank(comp.getComplianceMode())) {
			if(errWarMessagesList.contains("E11166")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11166"));
			} else if(errWarMessagesList.contains("W11166")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11166"));
			}
		}
		if((errWarMessagesList.contains("E71101") || errWarMessagesList.contains("W71101")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceMode()) && !CommonUtils.isAlpha(comp.getComplianceMode())) {
			if(errWarMessagesList.contains("E71101")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71101"));
			} else if(errWarMessagesList.contains("W71101")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71101"));
			}
		}
		if((errWarMessagesList.contains("E11177") || errWarMessagesList.contains("W11177")) && CommonUtils.isDateNull(comp.getReportedDate())) {
			if(errWarMessagesList.contains("E11177")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11177"));
			} else if(errWarMessagesList.contains("W11177")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11177"));
			}
		}
		if((errWarMessagesList.contains("E11178") || errWarMessagesList.contains("W11178")) && CommonUtils.isDateNull(comp.getFillingDate())) {
			if(errWarMessagesList.contains("E11178")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11178"));
			} else if(errWarMessagesList.contains("W11178")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11178"));
			}
		}
		if((errWarMessagesList.contains("E11163") || errWarMessagesList.contains("W11163")) && CommonUtils.isStringNullOrBlank(comp.getComplianceType())) {
			if(errWarMessagesList.contains("E11163")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11163"));
			} else if(errWarMessagesList.contains("W11163")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11163"));
			}
		}
		if((errWarMessagesList.contains("E11179") || errWarMessagesList.contains("W11179")) && comp.getComplianceSubtypeHierarcyCount() > 0 && CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype())) {
			if(errWarMessagesList.contains("E11179")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11179"));
			} else if(errWarMessagesList.contains("W11179")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11179"));
			}
		}
		if((errWarMessagesList.contains("E11180") || errWarMessagesList.contains("W11180")) && comp.getComplianceReasonHierarcyCount() > 0 && CommonUtils.isStringNullOrBlank(comp.getComplianceReason())) {
			if(errWarMessagesList.contains("E11180")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11180"));
			} else if(errWarMessagesList.contains("W11180")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11180"));
			}
		}
		if((errWarMessagesList.contains("E11164") || errWarMessagesList.contains("W11164")) && CommonUtils.isStringNullOrBlank(comp.getComplianceChannel())) {
			if(errWarMessagesList.contains("E11164")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E11164"));
			} else if(errWarMessagesList.contains("W11164")) {
				comp.addErrWarJson(new ErrWarJson("w", "E11164"));
			}
		}
	}

	public static void lookUpValidation(Compliance comp, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E21101") || errWarMessagesList.contains("W21101")) && !CommonUtils.isIntegerNullOrZero(comp.getClientId()) && comp.getClient() == null) {
			if(errWarMessagesList.contains("E21101")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21101"));
			} else if(errWarMessagesList.contains("W21101")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21101"));
			}
		}
		if((errWarMessagesList.contains("E21166") || errWarMessagesList.contains("W21166")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceMode()) && comp.getComplianceModeLookUp() == null) {
			if(errWarMessagesList.contains("E21166")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21166"));
			} else if(errWarMessagesList.contains("W21166")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21166"));
			}
		}
		if((errWarMessagesList.contains("E21163") || errWarMessagesList.contains("W21163")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceType()) && comp.getComplianceTypeLookUp() == null) {
			if(errWarMessagesList.contains("E21163")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21163"));
			} else if(errWarMessagesList.contains("W21163")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21163"));
			}
		}
		if((errWarMessagesList.contains("E21179") || errWarMessagesList.contains("W21179")) && comp.getComplianceSubtypeHierarcyCount() > 0 && !CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype()) && comp.getComplianceSubtypeCount() == 0) {
			if(errWarMessagesList.contains("E21179")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21179"));
			} else if(errWarMessagesList.contains("W21179")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21179"));
			}
		}
		if((errWarMessagesList.contains("E21180") || errWarMessagesList.contains("W21180")) && comp.getComplianceReasonHierarcyCount() > 0 && !CommonUtils.isStringNullOrBlank(comp.getComplianceReason()) && comp.getComplianceReasonCount() == 0) {
			if(errWarMessagesList.contains("E21180")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21180"));
			} else if(errWarMessagesList.contains("W21180")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21180"));
			}
		}
		if((errWarMessagesList.contains("E21179") || errWarMessagesList.contains("W21179")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype()) && !comp.getComplianceSubtype().equalsIgnoreCase("NA") && comp.getComplianceSubtypeHierarcyCount() == 0) {
			if(errWarMessagesList.contains("E21179")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21179"));
			} else if(errWarMessagesList.contains("W21179")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21179"));
			}
		}
		if((errWarMessagesList.contains("E21180") || errWarMessagesList.contains("W21180")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceReason()) && !comp.getComplianceReason().equalsIgnoreCase("NA") && comp.getComplianceReasonHierarcyCount() == 0) {
			if(errWarMessagesList.contains("E21180")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21180"));
			} else if(errWarMessagesList.contains("W21180")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21180"));
			}
		}
		if((errWarMessagesList.contains("E21164") || errWarMessagesList.contains("W21164")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceChannel()) && comp.getComplianceChannelLookUp() == null) {
			if(errWarMessagesList.contains("E21164")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21164"));
			} else if(errWarMessagesList.contains("W21164")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21164"));
			}
		}
		if((errWarMessagesList.contains("E21165") || errWarMessagesList.contains("W21165")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceStatus()) && comp.getComplianceStatusLookUp() == null) {
			if(errWarMessagesList.contains("E21165")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21165"));
			} else if(errWarMessagesList.contains("W21165")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21165"));
			}
		}
		if((errWarMessagesList.contains("E21195") || errWarMessagesList.contains("W21195")) && !CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName()) && comp.getRegulatoryBody() == null) {
			if(errWarMessagesList.contains("E21195")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21195"));
			} else if(errWarMessagesList.contains("W21195")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21195"));
			}
		}
		if((errWarMessagesList.contains("E21194") || errWarMessagesList.contains("W21194")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceType())
				&& comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_BANKRUPTCY) 
				&& !CommonUtils.isStringNullOrBlank(comp.getBankruptcyType()) && comp.getBankruptcyTypeLookUp() == null) {
			if(errWarMessagesList.contains("E21194")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21194"));
			} else if(errWarMessagesList.contains("W21194")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21194"));
			}
		}
		if((errWarMessagesList.contains("E21196") || errWarMessagesList.contains("W21196")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceAction()) && comp.getComplianceActionLookUp() == null) {
			if(errWarMessagesList.contains("E21196")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21196"));
			} else if(errWarMessagesList.contains("W21196")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21196"));
			}
		}
		if((errWarMessagesList.contains("E21182") || errWarMessagesList.contains("W21182")) && !CommonUtils.isStringNullOrBlank(comp.getDefendant()) && comp.getDefendantLookUp() == null) {
			if(errWarMessagesList.contains("E21182")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21182"));
			} else if(errWarMessagesList.contains("W21182")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21182"));
			}
		}
		if((errWarMessagesList.contains("E21183") || errWarMessagesList.contains("W21183")) && !CommonUtils.isStringNullOrBlank(comp.getAttorneyType()) && comp.getAttorneyTypeLookUp() == null) {
			if(errWarMessagesList.contains("E21183")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E21183"));
			} else if(errWarMessagesList.contains("W21183")) {
				comp.addErrWarJson(new ErrWarJson("w", "E21183"));
			}
		}
	}

	public static void standardize(Compliance comp) {
		if(!CommonUtils.isStringNullOrBlank(comp.getClientAccountNumber())) {
			comp.setClientAccountNumber(comp.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceMode())) {
			comp.setComplianceMode(comp.getComplianceMode().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceType())) {
			comp.setComplianceType(comp.getComplianceType().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype())) {
			comp.setComplianceSubtype(comp.getComplianceSubtype().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceReason())) {
			comp.setComplianceReason(comp.getComplianceReason().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceChannel())) {
			comp.setComplianceChannel(comp.getComplianceChannel().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceStatus())) {
			comp.setComplianceStatus(comp.getComplianceStatus().toUpperCase());
		}
	}

	public static void referenceUpdation(Compliance comp) {
		if(comp.getAccountIds() != null && !CommonUtils.isLongNull(comp.getAccountIds())) {
			comp.setAccountId(comp.getAccountIds());
		}
		if(comp.getConsumerIds() != null && !CommonUtils.isLongNull(comp.getConsumerIds())) {
			comp.setConsumerId(comp.getConsumerIds());
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName()) && comp.getRegulatoryBody() != null) {
			comp.setRegulatoryBodyId(comp.getRegulatoryBody().getRegulatoryBodyId());
		}
	}

	public static void misingRefCheck(Compliance comp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E71128") || errWarMessagesList.contains("W71128")) && CommonUtils.isLongNull(comp.getAccountId()) && CommonUtils.isLongNull(comp.getConsumerId())) {
			if(errWarMessagesList.contains("E71128")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71128"));
			} else if(errWarMessagesList.contains("W71128")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71128"));
			}
		}
		if((errWarMessagesList.contains("E71128") || errWarMessagesList.contains("W71128")) && !CommonUtils.isStringNullOrBlank(comp.getClientAccountNumber()) && CommonUtils.isLongNull(comp.getAccountId())) {
			if(errWarMessagesList.contains("E71128")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71128"));
			} else if(errWarMessagesList.contains("W71128")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71128"));
			}
		}
		if((errWarMessagesList.contains("E71128") || errWarMessagesList.contains("W71128")) && !CommonUtils.isLongNull(comp.getClientConsumerNumber()) && CommonUtils.isLongNull(comp.getConsumerId())) {
			if(errWarMessagesList.contains("E71128")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71128"));
			} else if(errWarMessagesList.contains("W71128")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71128"));
			}
		}
	}

	public static void businessRule(Compliance comp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(comp.getClient() != null && comp.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
			Double amtPrincipal = (!CommonUtils.isDoubleNull(comp.getAmtPrinciple()) && comp.getAmtPrinciple() > 0) ? comp.getAmtPrinciple() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(comp.getAmtInterest()) && comp.getAmtInterest() > 0) ? comp.getAmtInterest() :  0.00;
			Double amtFee = (!CommonUtils.isDoubleNull(comp.getAmtFee()) && comp.getAmtFee() > 0) ? comp.getAmtFee() :  0.00;

			comp.setAmtTotal(amtPrincipal + amtInterest + amtFee);

			if(!CommonUtils.isStringNullOrBlank(comp.getComplianceType()) 
					&& comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_DECEASED)) {
				comp.setEventDate(comp.getFillingDate());
			}
		}
		if((errWarMessagesList.contains("E71102") || errWarMessagesList.contains("W71102")) && !CommonUtils.isStringNullOrBlank(comp.getCaseFileNumber()) && CommonUtils.containsSpecialCharExceptHypen(comp.getCaseFileNumber())) {
			if(errWarMessagesList.contains("E71102")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71102"));
			} else if(errWarMessagesList.contains("W71102")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71102"));
			}
		}
		if((errWarMessagesList.contains("E71103") || errWarMessagesList.contains("W71103")) && !CommonUtils.isStringNullOrBlank(comp.getCaseFileNumber()) && comp.getCaseFileNumber().length() > 50) {
			if(errWarMessagesList.contains("E71103")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71103"));
			} else if(errWarMessagesList.contains("W71103")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71103"));
			}
		}
		if((errWarMessagesList.contains("E71104") || errWarMessagesList.contains("W71104")) && !CommonUtils.isStringNullOrBlank(comp.getCourtCity()) && CommonUtils.containsSpecialCharExceptHypenAndComma(comp.getCourtCity())) {
			if(errWarMessagesList.contains("E71104")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71104"));
			} else if(errWarMessagesList.contains("W71104")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71104"));
			}
		}
		if((errWarMessagesList.contains("E71105") || errWarMessagesList.contains("W71105")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceType()) && !comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_BANKRUPTCY)
				&& (!CommonUtils.isStringNullOrBlank(comp.getCaseFileNumber()) 
					|| !CommonUtils.isDoubleNull(comp.getAmtPrinciple()) && comp.getAmtPrinciple() > 0 
					|| !CommonUtils.isDoubleNull(comp.getAmtInterest()) && comp.getAmtInterest() > 0 
					|| !CommonUtils.isDoubleNull(comp.getAmtFee()) && comp.getAmtFee() > 0 
					|| !CommonUtils.isDoubleNull(comp.getAmtTotal()) && comp.getAmtTotal() > 0 
					|| !CommonUtils.isStringNullOrBlank(comp.getCourtCity()))) {
			if(errWarMessagesList.contains("E71105")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71105"));
			} else if(errWarMessagesList.contains("W71105")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71105"));
			}
		}
		if((errWarMessagesList.contains("E71106") || errWarMessagesList.contains("W71106")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceType()) && comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_BANKRUPTCY)) {
			if(!CommonUtils.isDoubleNull(comp.getAmtPrinciple()) && comp.getAmtPrinciple() > 0
				&& !CommonUtils.isDoubleNull(comp.getAmtInterest()) && comp.getAmtInterest() > 0
				&& !CommonUtils.isDoubleNull(comp.getAmtFee()) && comp.getAmtFee() > 0
				&& !CommonUtils.isDoubleNull(comp.getAmtTotal()) && comp.getAmtTotal() > 0) {

				String amtTotal = CommonConstants.dfFormat.format(comp.getAmtTotal());
				String amtCal = CommonConstants.dfFormat.format(comp.getAmtPrinciple() + comp.getAmtInterest() + comp.getAmtFee());

				if(!amtTotal.equalsIgnoreCase(amtCal)) {
					if(errWarMessagesList.contains("E71106")) {
						validationMap.put("isComplianceValidated", false);
						comp.addErrWarJson(new ErrWarJson("e", "E71106"));
					} else if(errWarMessagesList.contains("W71106")) {
						comp.addErrWarJson(new ErrWarJson("w", "E71106"));
					}
				}
			}
		}
		if((errWarMessagesList.contains("E71107") || errWarMessagesList.contains("W71107")) && !CommonUtils.isStringNullOrBlank(comp.getComplianceType()) && comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_LITIGIOUS)) {
			if(CommonUtils.isStringNullOrBlank(comp.getDefendant())) {
				if(errWarMessagesList.contains("E71107")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71107"));
				} else if(errWarMessagesList.contains("W71107")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71107"));
				}
			}
		}
		if((errWarMessagesList.contains("E71108") || errWarMessagesList.contains("W71108")) && !CommonUtils.isStringNullOrBlank(comp.getDescriptionReported()) && comp.getDescriptionReported().length() > 500) {
			if(errWarMessagesList.contains("E71108")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71108"));
			} else if(errWarMessagesList.contains("W71108")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71108"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getComplianceType()) 
			&& (comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_COMPLAINTS) 
				|| comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_DISPUTE))) {
			if(comp.getValidationRequired() == null) {
				comp.setValidationRequired(false);
			}
			if(!CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName())) {
				comp.setRegulatoryRequired(true);
			}
			if((errWarMessagesList.contains("E71109") || errWarMessagesList.contains("W71109")) && (!CommonUtils.isStringNullOrBlank(comp.getComplianceStatus()) && !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_SUBSTANTIATED) &&  !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_NOT_SUBSTANTIATED))
					&& CommonUtils.isDateNull(comp.getFirstSlaDate()) && !CommonUtils.isDateNull(comp.getLastSlaDate())) {
				if(errWarMessagesList.contains("E71109")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71109"));
				} else if(errWarMessagesList.contains("W71109")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71109"));
				}
			}
			if((errWarMessagesList.contains("E71110") || errWarMessagesList.contains("W71110")) && (!CommonUtils.isStringNullOrBlank(comp.getComplianceStatus()) && !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_SUBSTANTIATED) &&  !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_NOT_SUBSTANTIATED))
					&& !CommonUtils.isDateNull(comp.getFirstSlaDate()) && CommonUtils.isDateNull(comp.getLastSlaDate())) {
				if(errWarMessagesList.contains("E71110")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71110"));
				} else if(errWarMessagesList.contains("W71110")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71110"));
				}
			}
			if((errWarMessagesList.contains("E71111") || errWarMessagesList.contains("W71111")) && !CommonUtils.isDateNull(comp.getFirstSlaDate()) && !CommonUtils.isDateNull(comp.getLastSlaDate())
					&& comp.getFirstSlaDate().isAfter(comp.getLastSlaDate())) {
				if(errWarMessagesList.contains("E71111")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71111"));
				} else if(errWarMessagesList.contains("W71111")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71111"));
				}
			}

			LocalDate dtRaisedFirstSla = comp.getReportedDate();
			LocalDate dtRaisedLastSla = comp.getReportedDate();
			if(comp.getRegulatoryBody() != null) {
				dtRaisedFirstSla = dtRaisedFirstSla.plusDays(comp.getRegulatoryBody().getFirstSlaDays());
				dtRaisedLastSla = dtRaisedLastSla.plusDays(comp.getRegulatoryBody().getLastSlaDays());
			} else {
				dtRaisedFirstSla = dtRaisedFirstSla.plusDays(ConfRegulatoryBody.confRegulatoryBody.get(ConfRegulatoryBody.REGULATORYBODY_EQB).getFirstSlaDays());
				dtRaisedLastSla = dtRaisedLastSla.plusDays(ConfRegulatoryBody.confRegulatoryBody.get(ConfRegulatoryBody.REGULATORYBODY_EQB).getLastSlaDays());
			}

			if(!CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName()) && comp.getRegulatoryBody() != null 
					&& comp.getRegulatoryBody().getShortName().equalsIgnoreCase(CommonConstants.REGULATORY_BODY_CFPB)) {
				if((errWarMessagesList.contains("E71112") || errWarMessagesList.contains("W71112")) && !CommonUtils.isDateNull(comp.getFirstSlaDate()) && !comp.getFirstSlaDate().isEqual(dtRaisedFirstSla)) {
					if(errWarMessagesList.contains("E71112")) {
						validationMap.put("isComplianceValidated", false);
						comp.addErrWarJson(new ErrWarJson("e", "E71112"));
					} else if(errWarMessagesList.contains("W71112")) {
						comp.addErrWarJson(new ErrWarJson("w", "E71112"));
					}
				}
				if((errWarMessagesList.contains("E71113") || errWarMessagesList.contains("W71113")) && !CommonUtils.isDateNull(comp.getLastSlaDate()) && !comp.getLastSlaDate().isEqual(dtRaisedLastSla)) {
					if(errWarMessagesList.contains("E71113")) {
						validationMap.put("isComplianceValidated", false);
						comp.addErrWarJson(new ErrWarJson("e", "E71113"));
					} else if(errWarMessagesList.contains("W71113")) {
						comp.addErrWarJson(new ErrWarJson("w", "E71113"));
					}
				}
			}
			else if(!CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName())) {
				if((errWarMessagesList.contains("E71114") || errWarMessagesList.contains("W71114")) && !CommonUtils.isDateNull(comp.getFirstSlaDate()) && !CommonUtils.isDateNull(comp.getReportedDate())
						&& comp.getReportedDate().isAfter(comp.getFirstSlaDate())) {
					if(errWarMessagesList.contains("E71114")) {
						validationMap.put("isComplianceValidated", false);
						comp.addErrWarJson(new ErrWarJson("e", "E71114"));
					} else if(errWarMessagesList.contains("W71114")) {
						comp.addErrWarJson(new ErrWarJson("w", "E71114"));
					}
				}
			}
			if(CommonUtils.isDateNull(comp.getFirstSlaDate())) {
				comp.setFirstSlaDate(dtRaisedFirstSla);
			}
			if(CommonUtils.isDateNull(comp.getLastSlaDate())) {
				comp.setLastSlaDate(dtRaisedLastSla);
			}
		} else {
			if((errWarMessagesList.contains("E71115") || errWarMessagesList.contains("W71115")) && !CommonUtils.isStringNullOrBlank(comp.getRegulatoryBodyShortName())) {
				if(errWarMessagesList.contains("E71115")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71115"));
				} else if(errWarMessagesList.contains("W71115")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71115"));
				}
			}
			if((errWarMessagesList.contains("E71116") || errWarMessagesList.contains("W71116")) && (!CommonUtils.isStringNullOrBlank(comp.getComplianceStatus()) && !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_SUBSTANTIATED) &&  !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_NOT_SUBSTANTIATED))
					&& !CommonUtils.isDateNull(comp.getFirstSlaDate())) {
				if(errWarMessagesList.contains("E71116")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71116"));
				} else if(errWarMessagesList.contains("W71116")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71116"));
				}
			}
			if((errWarMessagesList.contains("E71117") || errWarMessagesList.contains("W71117")) && (!CommonUtils.isStringNullOrBlank(comp.getComplianceStatus()) && !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_SUBSTANTIATED) &&  !comp.getComplianceStatus().equals(CommonConstants.COMPLIANCE_STATUS_CLOSED_NOT_SUBSTANTIATED))
					&& !CommonUtils.isDateNull(comp.getLastSlaDate())) {
				if(errWarMessagesList.contains("E71117")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71117"));
				} else if(errWarMessagesList.contains("W71117")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71117"));
				}
			}
		}
		if((errWarMessagesList.contains("E71118") || errWarMessagesList.contains("W71118")) && !CommonUtils.isStringNullOrBlank(comp.getDescription()) && comp.getDescription().length() > 500) {
			if(errWarMessagesList.contains("E71118")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71118"));
			} else if(errWarMessagesList.contains("W71118")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71118"));
			}
		}
		if((errWarMessagesList.contains("E71119") || errWarMessagesList.contains("W71119")) && !CommonUtils.isStringNullOrBlank(comp.getRemark()) && comp.getRemark().length() > 250) {
			if(errWarMessagesList.contains("E71119")) {
				validationMap.put("isComplianceValidated", false);
				comp.addErrWarJson(new ErrWarJson("e", "E71119"));
			} else if(errWarMessagesList.contains("W71119")) {
				comp.addErrWarJson(new ErrWarJson("w", "E71119"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(comp.getConsumerIdentificationNumber())) {
			String ssn1 = comp.getConsumerIdentificationNumber().trim().substring(0,3);
			String ssn2 = comp.getConsumerIdentificationNumber().trim().substring(3,5);
			String ssn3 = comp.getConsumerIdentificationNumber().trim().substring(5);

			if((errWarMessagesList.contains("E71120") || errWarMessagesList.contains("W71120")) && comp.getConsumerIdentificationNumber().trim().length() != 9) {
				if(errWarMessagesList.contains("E71120")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71120"));
				} else if(errWarMessagesList.contains("W71120")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71120"));
				}
			}
			if((errWarMessagesList.contains("E71121") || errWarMessagesList.contains("W71121")) && !CommonUtils.onlyDigits(comp.getConsumerIdentificationNumber())) {
				if(errWarMessagesList.contains("E71121")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71121"));
				} else if(errWarMessagesList.contains("W71121")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71121"));
				}
			}
			if((errWarMessagesList.contains("E71122") || errWarMessagesList.contains("W71122")) && (!CommonUtils.isStringNullOrBlank(ssn1) && !CommonUtils.isStringNullOrBlank(ssn2) && !CommonUtils.isStringNullOrBlank(ssn3)
					&& (ssn1.equalsIgnoreCase("000") || ssn1.equalsIgnoreCase("666") || ssn2.equalsIgnoreCase("00") || ssn3.equalsIgnoreCase("0000"))) 
							|| CommonUtils.isSingleInteger(comp.getConsumerIdentificationNumber()) || CommonUtils.isConsecutiveNumber(comp.getConsumerIdentificationNumber())) {
				if(errWarMessagesList.contains("E71122")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71122"));
				} else if(errWarMessagesList.contains("W71122")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71122"));
				}
			}
		}
		if((!CommonUtils.isStringNullOrBlank(comp.getComplianceType()) && !comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_BANKRUPTCY) 
				&& !comp.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_DECEASED))) {
			if((errWarMessagesList.contains("E71123") || errWarMessagesList.contains("W71123")) && !CommonUtils.isDoubleNull(comp.getAmtEventBalance())) {
				if(errWarMessagesList.contains("E71123")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71123"));
				} else if(errWarMessagesList.contains("W71123")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71123"));
				}
			}
			if((errWarMessagesList.contains("E71124") || errWarMessagesList.contains("W71124")) && !CommonUtils.isBooleanNull(comp.getAttorneyAssignedRequired())) {
				if(errWarMessagesList.contains("E71124")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71124"));
				} else if(errWarMessagesList.contains("W71124")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71124"));
				}
			}
		}
		if(CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype()) 
				|| (!CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype()) && !comp.getComplianceSubtype().equalsIgnoreCase(CommonConstants.COMPLIANCE_SUB_TYPE_ATTORNEY))) {
			if((errWarMessagesList.contains("E71125") || errWarMessagesList.contains("W71125")) && !CommonUtils.isDateNull(comp.getAttorneyAssignedDate())) {
				if(errWarMessagesList.contains("E71125")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71125"));
				} else if(errWarMessagesList.contains("W71125")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71125"));
				}
			}
			if((errWarMessagesList.contains("E71126") || errWarMessagesList.contains("W71126")) && !CommonUtils.isStringNullOrBlank(comp.getAttorneyType())) {
				if(errWarMessagesList.contains("E71126")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71126"));
				} else if(errWarMessagesList.contains("W71126")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71126"));
				}
			}
			if((errWarMessagesList.contains("E71127") || errWarMessagesList.contains("W71127")) && !CommonUtils.isStringNullOrBlank(comp.getAttorneyFax())) {
				if(errWarMessagesList.contains("E71127")) {
					validationMap.put("isComplianceValidated", false);
					comp.addErrWarJson(new ErrWarJson("e", "E71127"));
				} else if(errWarMessagesList.contains("W71127")) {
					comp.addErrWarJson(new ErrWarJson("w", "E71127"));
				}
			}
		}
		if(comp.getIsComplianceSubtypeHierarcyNA() > 0 && CommonUtils.isStringNullOrBlank(comp.getComplianceSubtype())) {
			comp.setComplianceSubtype("NA");
		}
		if(comp.getIsComplianceReasonHierarcyNA() > 0 && CommonUtils.isStringNullOrBlank(comp.getComplianceReason())) {
			comp.setComplianceReason("NA");
		}
	}
}