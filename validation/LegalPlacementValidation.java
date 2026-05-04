package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.LegalPlacement;
import com.equabli.domain.helpers.CommonUtils;

public class LegalPlacementValidation {

	public static void mandatoryValidation(LegalPlacement lp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(lp.getRecordType())) {
			if(errWarMessagesList.contains("E11906")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E11906"));
			} else if(errWarMessagesList.contains("W11906")) {
				lp.addErrWarJson(new ErrWarJson("w", "E11906"));
			}
        }
		if(CommonUtils.isIntegerNullOrZero(lp.getClientId())) {
			if(errWarMessagesList.contains("E11901")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E11901"));
			} else if(errWarMessagesList.contains("W11901")) {
				lp.addErrWarJson(new ErrWarJson("w", "E11901"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(lp.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11902")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E11902"));
			} else if(errWarMessagesList.contains("W11902")) {
				lp.addErrWarJson(new ErrWarJson("w", "E11902"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(lp.getCaseNumber())) {
			if(errWarMessagesList.contains("E11903")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E11903"));
			} else if(errWarMessagesList.contains("W11903")) {
				lp.addErrWarJson(new ErrWarJson("w", "E11903"));
			}
		}
	}

	public static void lookUpValidation(LegalPlacement lp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(lp.getClientId()) && lp.getClient() == null) {
			if(errWarMessagesList.contains("E21901")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E21901"));
			} else if(errWarMessagesList.contains("W21901")) {
				lp.addErrWarJson(new ErrWarJson("w", "E21901"));
			}
		}
        if (!CommonUtils.isStringNullOrBlank(lp.getJudgmentStatus()) && lp.getJudgmentStatusLookUp() == null) {
			if(errWarMessagesList.contains("E21902")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E21902"));
			} else if(errWarMessagesList.contains("W21902")) {
				lp.addErrWarJson(new ErrWarJson("w", "E21902"));
			}
        }
        if (!CommonUtils.isStringNullOrBlank(lp.getJudgmentSuitOutcome()) && lp.getJudgmentSuitOutcomeLookUp() == null) {
			if(errWarMessagesList.contains("E21903")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E21903"));
			} else if(errWarMessagesList.contains("W21903")) {
				lp.addErrWarJson(new ErrWarJson("w", "E21903"));
			}
        }
	}

	public static void standardize(LegalPlacement lp) {
		if(!CommonUtils.isStringNullOrBlank(lp.getClientAccountNumber())) {
			lp.setClientAccountNumber(lp.getClientAccountNumber().toUpperCase());
		}
	}

	public static void deDupValidation(LegalPlacement lp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(lp.getCaseNumberDeDup() > 1) {
			if(errWarMessagesList.contains("E31901")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E31901"));
			} else if(errWarMessagesList.contains("W31901")) {
				lp.addErrWarJson(new ErrWarJson("w", "E31901"));
			}
		}
	}

	public static void referenceUpdation(LegalPlacement lp) {
		if(lp.getAccountIds() != null && !CommonUtils.isLongNull(lp.getAccountIds())) {
			lp.setAccountId(lp.getAccountIds());
		}
        if (lp.getConsumerIds() != null && !CommonUtils.isLongNull(lp.getConsumerIds())) {
        	lp.setConsumerId(lp.getConsumerIds());
        }
        if(lp.getSuitStatus() ==  null) {
        	lp.setSuitStatus(true);
        }
        if(lp.getDocIdCount() != null && lp.getDocIdCount() > 0) {
        	lp.setIsJudgmentAvailable(true);
        }
	}

	public static void misingRefCheck(LegalPlacement lp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(CommonUtils.isLongNull(lp.getAccountId())) {
			if(errWarMessagesList.contains("E41901")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E41901"));
			} else if(errWarMessagesList.contains("W41901")) {
				lp.addErrWarJson(new ErrWarJson("w", "E41901"));
			}
		}
        if (CommonUtils.isLongNull(lp.getConsumerId())) {
			if(errWarMessagesList.contains("E41902")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E41902"));
			} else if(errWarMessagesList.contains("W41902")) {
				lp.addErrWarJson(new ErrWarJson("w", "E41902"));
			}
        }
	}

	public static void businessRule(LegalPlacement lp, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(lp.getRecordType()) && !lp.getRecordType().equalsIgnoreCase("legal")) {
			if(errWarMessagesList.contains("E71906")) {
				validationMap.put("isGarnishmentValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71906"));
			} else if(errWarMessagesList.contains("W71906")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71906"));
			}
        }
		if (!CommonUtils.isStringNullOrBlank(lp.getCaseNumber()) && (lp.getCaseNumber().length() < 5 || lp.getCaseNumber().length() > 50)) {
			if(errWarMessagesList.contains("E71901")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71901"));
			} else if(errWarMessagesList.contains("W71901")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71901"));
			}
		}
		if(lp.getAmtPlacedLegal() != null && lp.getAmtPlacedLegal() < 0) {
			if(errWarMessagesList.contains("E71902")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71902"));
			} else if(errWarMessagesList.contains("W71902")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71902"));
			}
		}
		if(lp.getDtPlaced() != null && (lp.getDtPlaced().isAfter(LocalDate.now()) 
				|| (lp.getDtOriginalAccountOpen() != null && !lp.getDtPlaced().isAfter(lp.getDtOriginalAccountOpen()))
				|| (lp.getDtDelinquency() != null && !lp.getDtPlaced().isAfter(lp.getDtDelinquency())))) {
			if(errWarMessagesList.contains("E71903")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71903"));
			} else if(errWarMessagesList.contains("W71903")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71903"));
			}
		}
		if(lp.getDtSuitFiled() != null && (lp.getDtSuitFiled().isAfter(LocalDate.now()) 
				|| (lp.getDtOriginalAccountOpen() != null && !lp.getDtSuitFiled().isAfter(lp.getDtOriginalAccountOpen()))
				|| (lp.getDtDelinquency() != null && !lp.getDtSuitFiled().isAfter(lp.getDtDelinquency())))) {
			if(errWarMessagesList.contains("E71904")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71904"));
			} else if(errWarMessagesList.contains("W71904")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71904"));
			}
		}
		if(lp.getDtSuitFiled() != null && (StringUtils.isEmpty(lp.getCourtSuitFiled()) || lp.getCourtSuitFiled() == null)) {
			if(errWarMessagesList.contains("E71905")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71905"));
			} else if(errWarMessagesList.contains("W71905")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71905"));
			}
		}
		if(lp.getDtServed() != null && (lp.getDtServed().isAfter(LocalDate.now()) 
				|| (lp.getDtPlaced() != null && !lp.getDtServed().isAfter(lp.getDtPlaced())))) {
			if(errWarMessagesList.contains("E71907")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71907"));
			} else if(errWarMessagesList.contains("W71907")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71907"));
			}
		}
		if (lp.getAmtCourtCost() != null && lp.getAmtCourtCost() < 0) {
			if(errWarMessagesList.contains("E71908")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71908"));
			} else if(errWarMessagesList.contains("W71908")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71908"));
			}
		}
		if (lp.getAmtSuitDebt() != null && lp.getAmtSuitDebt() < 0) {
			if(errWarMessagesList.contains("E71909")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71909"));
			} else if(errWarMessagesList.contains("W71909")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71909"));
			}
		}
		Double amtJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgement()) && lp.getAmtJudgement() > 0) ? lp.getAmtJudgement() : 0.00;
		Double amtJudgementPrincipal = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentPrincipal()) && lp.getAmtJudgmentPrincipal() > 0) ? lp.getAmtJudgmentPrincipal() : 0.00;
		Double amtJudgementInterest = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentInterest()) && lp.getAmtJudgmentInterest() > 0) ? lp.getAmtJudgmentInterest() : 0.00;
		Double amtJudgementFees = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentFees()) && lp.getAmtJudgmentFees() > 0) ? lp.getAmtJudgmentFees() : 0.00;
		Double amtJudgementAttorney = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentAttorney()) && lp.getAmtJudgmentAttorney() > 0) ? lp.getAmtJudgmentAttorney() : 0.00;
		Double amtJudgementOther = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentOther()) && lp.getAmtJudgmentOther() > 0) ? lp.getAmtJudgmentOther() : 0.00;

		if ((lp.getDtJudgment() != null && (CommonUtils.isDoubleNull(lp.getAmtJudgement()) || lp.getAmtJudgement() == 0))
				|| (lp.getAmtJudgement() != null && lp.getAmtJudgement() < 0)
				|| (lp.getAmtJudgement() != null && amtJudgement != (amtJudgementPrincipal + amtJudgementInterest + amtJudgementFees + amtJudgementAttorney +amtJudgementOther))) {
			if(errWarMessagesList.contains("E71910")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71910"));
			} else if(errWarMessagesList.contains("W71910")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71910"));
			}
		}
		if (lp.getAmtJudgmentPrincipal() != null && lp.getAmtJudgmentPrincipal() < 0) {
			if(errWarMessagesList.contains("E71911")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71911"));
			} else if(errWarMessagesList.contains("W71911")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71911"));
			}
		}
		if (lp.getAmtJudgmentInterest() != null && lp.getAmtJudgmentInterest() < 0) {
			if(errWarMessagesList.contains("E71912")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71912"));
			} else if(errWarMessagesList.contains("W71912")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71912"));
			}
		}
		if (lp.getAmtJudgmentFees() != null && lp.getAmtJudgmentFees() < 0) {
			if(errWarMessagesList.contains("E71913")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71913"));
			} else if(errWarMessagesList.contains("W71913")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71913"));
			}
		}
		if (lp.getAmtJudgmentAttorney() != null && lp.getAmtJudgmentAttorney() < 0) {
			if(errWarMessagesList.contains("E71914")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71914"));
			} else if(errWarMessagesList.contains("W71914")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71914"));
			}
		}
		if (lp.getAmtJudgmentOther() != null && lp.getAmtJudgmentOther() < 0) {
			if(errWarMessagesList.contains("E71915")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71915"));
			} else if(errWarMessagesList.contains("W71915")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71915"));
			}
		}
		if(lp.getPctJudgmentInterestRate() != null && (lp.getPctJudgmentInterestRate() < 0 || lp.getPctJudgmentInterestRate() > 1)) {
			if(errWarMessagesList.contains("E71916")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71916"));
			} else if(errWarMessagesList.contains("W71916")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71916"));
			}
		}
		if(lp.getDtJudgment() != null && (lp.getDtJudgment().isAfter(LocalDate.now()) 
				|| (lp.getDtSuitFiled() != null && !lp.getDtJudgment().isAfter(lp.getDtSuitFiled()))
				|| (lp.getDtServed() != null && !lp.getDtJudgment().isAfter(lp.getDtServed())))) {
			if(errWarMessagesList.contains("E71917")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71917"));
			} else if(errWarMessagesList.contains("W71917")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71917"));
			}
		}
		if (lp.getDtJudgmentExpiration() != null && lp.getDtJudgment() != null && (lp.getDtJudgmentExpiration().isBefore(lp.getDtJudgment())
				|| lp.getDtJudgmentExpiration().isEqual(lp.getDtJudgment()))) {
			if(errWarMessagesList.contains("E71918")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71918"));
			} else if(errWarMessagesList.contains("W71918")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71918"));
			}
		}
		if (lp.getDtJudgmentRenewal() != null && lp.getDtJudgment() != null && (lp.getDtJudgmentRenewal().isBefore(lp.getDtJudgment())
				|| lp.getDtJudgmentRenewal().isEqual(lp.getDtJudgment()))) {
			if(errWarMessagesList.contains("E71919")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71919"));
			} else if(errWarMessagesList.contains("W71919")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71919"));
			}
		}
		if (lp.getDtJudgment() != null && (lp.getCountyJudgment() == null || StringUtils.isEmpty(lp.getCountyJudgment()))) {
			if(errWarMessagesList.contains("E71920")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71920"));
			} else if(errWarMessagesList.contains("W71920")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71920"));
			}
		}
		if(lp.getDtAttorneyInvoice() != null && (lp.getDtAttorneyInvoice().isAfter(LocalDate.now()) 
				|| (lp.getDtDelinquency() != null && !lp.getDtAttorneyInvoice().isAfter(lp.getDtDelinquency()))
				|| (lp.getDtPlaced() != null && !lp.getDtAttorneyInvoice().isAfter(lp.getDtPlaced())))) {
			if(errWarMessagesList.contains("E71921")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71921"));
			} else if(errWarMessagesList.contains("W71921")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71921"));
			}
		}
		if (lp.getDtJudgment() != null && (lp.getJudgmentDocket() == null || StringUtils.isEmpty(lp.getJudgmentDocket()))) {
			if(errWarMessagesList.contains("E71922")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71922"));
			} else if(errWarMessagesList.contains("W71922")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71922"));
			}
		}
		if (lp.getDtSuitFiled() != null && (lp.getSuitDocket() == null && StringUtils.isEmpty(lp.getSuitDocket()))) {
			if(errWarMessagesList.contains("E71923")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71923"));
			} else if(errWarMessagesList.contains("W71923")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71923"));
			}
		}
		if (lp.getJudgmentSuitAnswerFiledDate() != null && ((lp.getDtSuitFiled() != null && lp.getJudgmentSuitAnswerFiledDate().isBefore(lp.getDtSuitFiled()))
				|| lp.getJudgmentSuitAnswerFiledDate().isAfter(LocalDate.now()))) {
			if(errWarMessagesList.contains("E71924")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71924"));
			} else if(errWarMessagesList.contains("W71924")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71924"));
			}
		}
		if (lp.getJudgmentSuitDismissalDate() != null && ((lp.getDtSuitFiled() != null && lp.getJudgmentSuitDismissalDate().isBefore(lp.getDtSuitFiled()))
				|| lp.getJudgmentSuitDismissalDate().isAfter(LocalDate.now()))) {
			if(errWarMessagesList.contains("E71925")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71925"));
			} else if(errWarMessagesList.contains("W71925")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71925"));
			}
		}
		if (lp.getJudgmentLastServiceAttempt() != null && ((lp.getDtSuitFiled() != null && lp.getJudgmentLastServiceAttempt().isBefore(lp.getDtSuitFiled()))
				|| lp.getJudgmentLastServiceAttempt().isAfter(LocalDate.now()))) {
			if(errWarMessagesList.contains("E71926")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71926"));
			} else if(errWarMessagesList.contains("W71926")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71926"));
			}
		}
		if (lp.getJudgmentSatisfactionDate() != null && ((lp.getDtSuitFiled() != null && lp.getJudgmentSatisfactionDate().isBefore(lp.getDtSuitFiled()))
				|| lp.getJudgmentSatisfactionDate().isAfter(LocalDate.now()))) {
			if(errWarMessagesList.contains("E71927")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71927"));
			} else if(errWarMessagesList.contains("W71927")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71927"));
			}
		}
		if (lp.getJudgmentStipulationDate() != null && lp.getJudgmentStipulationDate().isAfter(LocalDate.now())) {
			if(errWarMessagesList.contains("E71928")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71928"));
			} else if(errWarMessagesList.contains("W71928")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71928"));
			}
		}
		if (lp.getTotalPostJudgementInterest() != null && lp.getTotalPostJudgementInterest() < 0) {
			if(errWarMessagesList.contains("E71929")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71929"));
			} else if(errWarMessagesList.contains("W71929")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71929"));
			}
		}
		if (lp.getTotalPostJudgementCredits() != null && lp.getTotalPostJudgementCredits() < 0) {
			if(errWarMessagesList.contains("E71930")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71930"));
			} else if(errWarMessagesList.contains("W71930")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71930"));
			}
		}
		if (lp.getTotalPostJudgementFees() != null && lp.getTotalPostJudgementFees() < 0) {
			if(errWarMessagesList.contains("E71931")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71931"));
			} else if(errWarMessagesList.contains("W71931")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71931"));
			}
		}
		if (lp.getTotalPostJudgementPayments() != null && lp.getTotalPostJudgementPayments() < 0) {
			if(errWarMessagesList.contains("E71932")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71932"));
			} else if(errWarMessagesList.contains("W71932")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71932"));
			}
		}
		if(lp.getLegalPlacementIdCount() != null && lp.getLegalPlacementIdCount() > 0) {
			if(errWarMessagesList.contains("E71933")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71933"));
			} else if(errWarMessagesList.contains("W71933")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71933"));
			}
		}
		if(lp.getLegalLienIdCount() != null && lp.getLegalLienIdCount() > 0) {
			if(errWarMessagesList.contains("E71934")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71934"));
			} else if(errWarMessagesList.contains("W71934")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71934"));
			}
		}
		if(lp.getLegalGarnishmentIdCount() != null && lp.getLegalGarnishmentIdCount() > 0) {
			if(errWarMessagesList.contains("E71935")) {
				validationMap.put("isLegalPlacementValidated", false);
				lp.addErrWarJson(new ErrWarJson("e", "E71935"));
			} else if(errWarMessagesList.contains("W71935")) {
				lp.addErrWarJson(new ErrWarJson("w", "E71935"));
			}
		}
	}
}