package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class AdjustmentValidation {

	public static void mandatoryValidation(Adjustment ad, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(CommonUtils.isStringNullOrBlank(ad.getRecordType())) {
            if (errWarMessagesList.contains("E13306")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13306"));
            } else if (errWarMessagesList.contains("W13306")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13306"));
            }
		}
		if(CommonUtils.isStringNullOrBlank(ad.getClientAccountNumber())) {
            if (errWarMessagesList.contains("E13302")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13302"));
            } else if (errWarMessagesList.contains("W13302")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13302"));
            }
		}
		if(CommonUtils.isIntegerNullOrZero(ad.getClientId())) {
            if (errWarMessagesList.contains("E13301")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13301"));
            } else if (errWarMessagesList.contains("W13301")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13301"));
            }
		}
		if(CommonUtils.isStringNullOrBlank(ad.getAdjustmentType())) {
            if (errWarMessagesList.contains("E13307")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13307"));
            } else if (errWarMessagesList.contains("W13307")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13307"));
            }
		}
		if(CommonUtils.isDoubleNull(ad.getAmtAdjustment()) || ad.getAmtAdjustment() <= 0) {
            if (errWarMessagesList.contains("E13308")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13308"));
            } else if (errWarMessagesList.contains("W13308")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13308"));
            }
		}
		if(CommonUtils.isDateNull(ad.getAdjustmentDate())) {
            if (errWarMessagesList.contains("E13303")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E13303"));
            } else if (errWarMessagesList.contains("W13303")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E13303"));
            }
		}
	}

	public static void lookUpValidation(Adjustment ad, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(ad.getClientId()) && ad.getClient() == null) {
            if (errWarMessagesList.contains("E23301")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E23301"));
            } else if (errWarMessagesList.contains("W23301")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E23301"));
            }
		}
		if(!CommonUtils.isIntegerNullOrZero(ad.getPartnerId()) && ad.getPartner() == null) {
            if (errWarMessagesList.contains("E23302")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E23302"));
            } else if (errWarMessagesList.contains("W23302")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E23302"));
            }
		}
		if(!CommonUtils.isStringNullOrBlank(ad.getAdjustmentType()) && ad.getAdjustmentTypeLookUp() == null) {
            if (errWarMessagesList.contains("E23303")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E23303"));
            } else if (errWarMessagesList.contains("W23303")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E23303"));
            }
		}
	}

	public static void standardize(Adjustment ad) {
		if(!CommonUtils.isStringNullOrBlank(ad.getClientAccountNumber())) {
			ad.setClientAccountNumber(ad.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(ad.getAdjustmentType())) {
			ad.setAdjustmentType(ad.getAdjustmentType().toUpperCase());
		}
	}

	public static void referenceUpdation(Adjustment ad) {
		if(ad.getAccountIds() != null && !CommonUtils.isLongNull(ad.getAccountIds())) {
			ad.setAccountId(ad.getAccountIds());
		}
	}

	public static void misingRefCheck(Adjustment ad, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(CommonUtils.isLongNull(ad.getAccountId())) {
            if (errWarMessagesList.contains("E43301")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E43301"));
            } else if (errWarMessagesList.contains("W43301")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E43301"));
            }
		}
	}

	public static void businessRule(Adjustment ad, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		Double otherFees = (!CommonUtils.isDoubleNull(ad.getAmtOtherfee()) && ad.getAmtOtherfee() > 0) ? ad.getAmtOtherfee() :  0.00;
		Double interest = (!CommonUtils.isDoubleNull(ad.getAmtInterest()) && ad.getAmtInterest() > 0) ? ad.getAmtInterest() :  0.00;
		Double principal = (!CommonUtils.isDoubleNull(ad.getAmtPrincipal()) && ad.getAmtPrincipal() > 0) ? ad.getAmtPrincipal() :  0.00;
		Double lateFees = (!CommonUtils.isDoubleNull(ad.getAmtLatefee()) && ad.getAmtLatefee() > 0) ? ad.getAmtLatefee() :  0.00;
		Double courtcost = (!CommonUtils.isDoubleNull(ad.getAmtCourtcost()) && ad.getAmtCourtcost() > 0) ? ad.getAmtCourtcost() :  0.00;
		Double attorneyFees = (!CommonUtils.isDoubleNull(ad.getAmtAttorneyfee()) && ad.getAmtAttorneyfee() > 0) ? ad.getAmtAttorneyfee() :  0.00;

		if(!CommonUtils.isStringNullOrBlank(ad.getRecordType()) && !ad.getRecordType().equalsIgnoreCase("adjustment")) {
            if (errWarMessagesList.contains("E73306")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E73306"));
            } else if (errWarMessagesList.contains("W73306")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E73306"));
            }
		}
		if(!CommonUtils.isDoubleNull(ad.getAmtAdjustment()) && ad.getAmtAdjustment() > 0) {
			Double amtAdjustment = (!CommonUtils.isDoubleNull(ad.getAmtAdjustment()) && ad.getAmtAdjustment() > 0) ? ad.getAmtAdjustment() :  0.00;
			Double amtPrincipal = (!CommonUtils.isDoubleNull(ad.getAmtPrincipal()) && ad.getAmtPrincipal() > 0) ? ad.getAmtPrincipal() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(ad.getAmtInterest()) && ad.getAmtInterest() > 0) ? ad.getAmtInterest() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(ad.getAmtLatefee()) && ad.getAmtLatefee() > 0) ? ad.getAmtLatefee() :  0.00;
			Double amtOtherfee = (!CommonUtils.isDoubleNull(ad.getAmtOtherfee()) && ad.getAmtOtherfee() > 0) ? ad.getAmtOtherfee() :  0.00;
			Double amtCourtcost = (!CommonUtils.isDoubleNull(ad.getAmtCourtcost()) && ad.getAmtCourtcost() > 0) ? ad.getAmtCourtcost() :  0.00;
			Double amtAttorneyfee = (!CommonUtils.isDoubleNull(ad.getAmtAttorneyfee()) && ad.getAmtAttorneyfee() > 0) ? ad.getAmtAttorneyfee() :  0.00;

			String amtTotal = CommonConstants.dfFormat.format(amtAdjustment);
			String amtCal = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee + amtOtherfee + amtCourtcost + amtAttorneyfee);

			if(!amtTotal.equalsIgnoreCase(amtCal)) {
	            if (errWarMessagesList.contains("E73301")) {
	                validationMap.put("isAdjustmentValidated", false);
	                ad.addErrWarJson(new ErrWarJson("e", "E73301"));
	            } else if (errWarMessagesList.contains("W73301")) {
	            	ad.addErrWarJson(new ErrWarJson("w", "E73301"));
	            }
			}
		}

		if(ad.getAccountIds() != null && !CommonUtils.isLongNull(ad.getAccountIds())) {
			if(!CommonUtils.isStringNullOrBlank(ad.getAdjustmentType()) && ad.getAdjustmentType().equalsIgnoreCase(CommonConstants.ADJUSTMENT_TYPE_BALANCE_ADJUSTMENT)) {
				if(!CommonUtils.isDoubleNull(ad.getAmtCurrentbalance())) {
					ad.setAmtCurrentbalance(ad.getAmtCurrentbalance() + ad.getAmtAdjustment());
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPrincipalCurrentbalance())) {
					ad.setAmtPrincipalCurrentbalance(ad.getAmtPrincipalCurrentbalance() + principal);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtInterestCurrentbalance())) {
					ad.setAmtInterestCurrentbalance(ad.getAmtInterestCurrentbalance() + interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtLatefeeCurrentbalance())) {
					ad.setAmtLatefeeCurrentbalance(ad.getAmtLatefeeCurrentbalance() + lateFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtCourtCostCurrentbalance())) {
					ad.setAmtCourtCostCurrentbalance(ad.getAmtCourtCostCurrentbalance() + courtcost);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtAttorneyFeeCurrentBalance())) {
					ad.setAmtAttorneyFeeCurrentBalance(ad.getAmtAttorneyFeeCurrentBalance() + attorneyFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtOtherfeeCurrentbalance())) {
					ad.setAmtOtherfeeCurrentbalance(ad.getAmtOtherfeeCurrentbalance() + otherFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffPrinciple())) {
					ad.setAmtPreChargeOffPrinciple(ad.getAmtPreChargeOffPrinciple() + principal);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffInterest())) {
					ad.setAmtPreChargeOffInterest(ad.getAmtPreChargeOffInterest() + interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffFee())) {
					ad.setAmtPreChargeOffFee(ad.getAmtPreChargeOffFee() + (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffInterest())) {
					ad.setAmtPostChargeOffInterest(ad.getAmtPostChargeOffInterest() + interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffFees())) {
					ad.setAmtPostChargeOffFees(ad.getAmtPostChargeOffFees() + (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffCredit())) {
					ad.setAmtPostChargeOffCredit(ad.getAmtPostChargeOffCredit() - ad.getAmtAdjustment());
				}
			} else {
				if(!CommonUtils.isDoubleNull(ad.getAmtCurrentbalance())) {
					ad.setAmtCurrentbalance(ad.getAmtCurrentbalance() - ad.getAmtAdjustment());
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPrincipalCurrentbalance())) {
					ad.setAmtPrincipalCurrentbalance(ad.getAmtPrincipalCurrentbalance() - principal);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtInterestCurrentbalance())) {
					ad.setAmtInterestCurrentbalance(ad.getAmtInterestCurrentbalance() - interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtLatefeeCurrentbalance())) {
					ad.setAmtLatefeeCurrentbalance(ad.getAmtLatefeeCurrentbalance() - lateFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtCourtCostCurrentbalance())) {
					ad.setAmtCourtCostCurrentbalance(ad.getAmtCourtCostCurrentbalance() - courtcost);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtAttorneyFeeCurrentBalance())) {
					ad.setAmtAttorneyFeeCurrentBalance(ad.getAmtAttorneyFeeCurrentBalance() - attorneyFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtOtherfeeCurrentbalance())) {
					ad.setAmtOtherfeeCurrentbalance(ad.getAmtOtherfeeCurrentbalance() - otherFees);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffPrinciple())) {
					ad.setAmtPreChargeOffPrinciple(ad.getAmtPreChargeOffPrinciple() - principal);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffInterest())) {
					ad.setAmtPreChargeOffInterest(ad.getAmtPreChargeOffInterest() - interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffFee())) {
					ad.setAmtPreChargeOffFee(ad.getAmtPreChargeOffFee() - (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffInterest())) {
					ad.setAmtPostChargeOffInterest(ad.getAmtPostChargeOffInterest() - interest);
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffFees())) {
					ad.setAmtPostChargeOffFees(ad.getAmtPostChargeOffFees() - (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffCredit())) {
					ad.setAmtPostChargeOffCredit(ad.getAmtPostChargeOffCredit() + ad.getAmtAdjustment());
				}
			}
		}

		Double amtCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtCurrentbalance()) && ad.getAmtCurrentbalance() > 0) ? ad.getAmtCurrentbalance() :  0.00;
		Double amtPrincipalCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtPrincipalCurrentbalance()) && ad.getAmtPrincipalCurrentbalance() > 0) ? ad.getAmtPrincipalCurrentbalance() :  0.00;
		Double amtInterestCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtInterestCurrentbalance()) && ad.getAmtInterestCurrentbalance() > 0) ? ad.getAmtInterestCurrentbalance() :  0.00;
		Double amtOtherfeeCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtOtherfeeCurrentbalance()) && ad.getAmtOtherfeeCurrentbalance() > 0) ? ad.getAmtOtherfeeCurrentbalance() :  0.00;
		Double amtCourtCostCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtCourtCostCurrentbalance()) && ad.getAmtCourtCostCurrentbalance() > 0) ? ad.getAmtCourtCostCurrentbalance() :  0.00;
		Double amtAttorneyFeeCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtAttorneyFeeCurrentBalance()) && ad.getAmtAttorneyFeeCurrentBalance() > 0) ? ad.getAmtAttorneyFeeCurrentBalance() :  0.00;
		Double amtLatefeeCurrentbalance = (!CommonUtils.isDoubleNull(ad.getAmtLatefeeCurrentbalance()) && ad.getAmtLatefeeCurrentbalance() > 0) ? ad.getAmtLatefeeCurrentbalance() :  0.00;

		if (ad.getAccountIds() != null && !CommonUtils.isLongNull(ad.getAccountIds()) && ad.getDtChargeOff() == null) { // Pre Charge Off
			if(!CommonUtils.isDoubleNull(ad.getAmtCurrentbalance()) && ad.getAmtCurrentbalance() > 0) {
				Double amtPreChargeOffPrinciple = (!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffPrinciple()) && ad.getAmtPreChargeOffPrinciple() > 0) ? ad.getAmtPreChargeOffPrinciple() :  0.00;
				Double amtPreChargeOffInterest = (!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffInterest()) && ad.getAmtPreChargeOffInterest() > 0) ? ad.getAmtPreChargeOffInterest() :  0.00;
				Double amtPreChargeOffFee = (!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffFee()) && ad.getAmtPreChargeOffFee() > 0) ? ad.getAmtPreChargeOffFee() :  0.00;

				String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
				String amtCal = CommonConstants.dfFormat.format(amtPreChargeOffPrinciple + amtPreChargeOffInterest + amtPreChargeOffFee);

				if(!amtTotal.equalsIgnoreCase(amtCal)) {
		            if (errWarMessagesList.contains("E73302")) {
		                validationMap.put("isAdjustmentValidated", false);
		                ad.addErrWarJson(new ErrWarJson("e", "E73302"));
		            } else if (errWarMessagesList.contains("W73302")) {
		            	ad.addErrWarJson(new ErrWarJson("w", "E73302"));
		            }
				}
			}
		} else if (ad.getAccountIds() != null && !CommonUtils.isLongNull(ad.getAccountIds()) && ad.getDtChargeOff() != null) { // Charge Off
			if(!CommonUtils.isDoubleNull(ad.getAmtCurrentbalance()) && ad.getAmtCurrentbalance() > 0) {
				Double amtPreChargeOffBalance = (!CommonUtils.isDoubleNull(ad.getAmtPreChargeOffBalance()) && ad.getAmtPreChargeOffBalance() > 0) ? ad.getAmtPreChargeOffBalance() :  0.00;
				Double amtPostChargeOffInterest = (!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffInterest()) && ad.getAmtPostChargeOffInterest() > 0) ? ad.getAmtPostChargeOffInterest() :  0.00;
				Double amtPostChargeOffFees = (!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffFees()) && ad.getAmtPostChargeOffFees() > 0) ? ad.getAmtPostChargeOffFees() :  0.00;
				Double amtPostChargeOffPayment = (!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffPayment()) && ad.getAmtPostChargeOffPayment() > 0) ? ad.getAmtPostChargeOffPayment() :  0.00;
				Double amtPostChargeOffCredit = (!CommonUtils.isDoubleNull(ad.getAmtPostChargeOffCredit()) && ad.getAmtPostChargeOffCredit() != 0) ? ad.getAmtPostChargeOffCredit() :  0.00;

				String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
				String amtCal = CommonConstants.dfFormat.format(amtPreChargeOffBalance + amtPostChargeOffInterest + amtPostChargeOffFees - (amtPostChargeOffPayment + amtPostChargeOffCredit));

				if(!amtTotal.equalsIgnoreCase(amtCal)) {
		            if (errWarMessagesList.contains("E73303")) {
		                validationMap.put("isAdjustmentValidated", false);
		                ad.addErrWarJson(new ErrWarJson("e", "E73303"));
		            } else if (errWarMessagesList.contains("W73303")) {
		            	ad.addErrWarJson(new ErrWarJson("w", "E73303"));
		            }
				}
			}
		}
		String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
		String amtCal = CommonConstants.dfFormat.format(amtPrincipalCurrentbalance + amtInterestCurrentbalance + amtOtherfeeCurrentbalance + amtCourtCostCurrentbalance + amtAttorneyFeeCurrentbalance + amtLatefeeCurrentbalance);

		if(!amtTotal.equalsIgnoreCase(amtCal)) {
            if (errWarMessagesList.contains("E73304")) {
                validationMap.put("isAdjustmentValidated", false);
                ad.addErrWarJson(new ErrWarJson("e", "E73304"));
            } else if (errWarMessagesList.contains("W73304")) {
            	ad.addErrWarJson(new ErrWarJson("w", "E73304"));
            }
		}
	}
}