package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;

import com.equabli.config.SqsMessageSender;
import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.entity.PaymentBucketConfig;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.feign.DataScrubEnrichmentServiceCommunication;

public class PaymentValidation {
	
	public static void mandatoryValidation(Payment pay, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10901") || errWarMessagesList.contains("W10901")) && CommonUtils.isIntegerNullOrZero(pay.getClientId())) {
			if(errWarMessagesList.contains("E10901")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E10901"));
			} else if(errWarMessagesList.contains("W10901")) {
				pay.addErrWarJson(new ErrWarJson("w", "E10901"));
			}
		}
		if((errWarMessagesList.contains("E10902") || errWarMessagesList.contains("W10902")) && CommonUtils.isStringNullOrBlank(pay.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10902")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E10902"));
			} else if(errWarMessagesList.contains("W10902")) {
				pay.addErrWarJson(new ErrWarJson("w", "E10902"));
			}
		}
		if((errWarMessagesList.contains("E10981") || errWarMessagesList.contains("W10981")) && CommonUtils.isDateNull(pay.getPaymentDate())) {
			if(errWarMessagesList.contains("E10981")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E10981"));
			} else if(errWarMessagesList.contains("W10981")) {
				pay.addErrWarJson(new ErrWarJson("w", "E10981"));
			}
		}
		if((errWarMessagesList.contains("E10959") || errWarMessagesList.contains("W10959")) && CommonUtils.isStringNullOrBlank(pay.getPaymentStatus())) {
			if(errWarMessagesList.contains("E10959")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E10959"));
			} else if(errWarMessagesList.contains("W10959")) {
				pay.addErrWarJson(new ErrWarJson("w", "E10959"));
			}
		}
		if((errWarMessagesList.contains("E10958") || errWarMessagesList.contains("W10958")) && CommonUtils.isStringNullOrBlank(pay.getPaymentType())) {
			if(errWarMessagesList.contains("E10958")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E10958"));
			} else if(errWarMessagesList.contains("W10958")) {
				pay.addErrWarJson(new ErrWarJson("w", "E10958"));
			}
		}
		if((errWarMessagesList.contains("E70903") || errWarMessagesList.contains("W70903")) && CommonUtils.isDoubleNull(pay.getAmtPayment()) || pay.getAmtPayment() <= 0) {
			if(errWarMessagesList.contains("E70903")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70903"));
			} else if(errWarMessagesList.contains("W70903")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70903"));
			}
		}
		if((errWarMessagesList.contains("E70908") || errWarMessagesList.contains("W70908")) && (CommonUtils.isDoubleNull(pay.getAmtPrincipal()) || pay.getAmtPrincipal() <= 0)
				&& (CommonUtils.isDoubleNull(pay.getAmtInterest()) || pay.getAmtInterest() <= 0)
				&& (CommonUtils.isDoubleNull(pay.getAmtOtherfee()) || pay.getAmtOtherfee() <= 0)) {
			if(errWarMessagesList.contains("E70908")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70908"));
			} else if(errWarMessagesList.contains("W70908")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70908"));
			}
		}
	}

	public static void lookUpValidation(Payment pay, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20901") || errWarMessagesList.contains("W20901")) && !CommonUtils.isIntegerNullOrZero(pay.getClientId()) && pay.getClient() == null) {
			if(errWarMessagesList.contains("E20901")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20901"));
			} else if(errWarMessagesList.contains("W20901")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20901"));
			}
		}
		if((errWarMessagesList.contains("E20960") || errWarMessagesList.contains("W20960")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentBrokenReason()) && pay.getPaymentBrokenReasonLookUp() == null) {
			if(errWarMessagesList.contains("E20960")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20960"));
			} else if(errWarMessagesList.contains("W20960")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20960"));
			}
		}
		if((errWarMessagesList.contains("E20953") || errWarMessagesList.contains("W20953")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentMethod()) && pay.getPaymentMethodLookUp() == null) {
			if(errWarMessagesList.contains("E20953")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20953"));
			} else if(errWarMessagesList.contains("W20953")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20953"));
			}
		}
		if((errWarMessagesList.contains("E20958") || errWarMessagesList.contains("W20958")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20958")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20958"));
			} else if(errWarMessagesList.contains("W20958")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20958"));
			}
		}
		if((errWarMessagesList.contains("E20959") || errWarMessagesList.contains("W20959")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentStatus()) && pay.getPaymentStatusLookUp() == null) {
			if(errWarMessagesList.contains("E20959")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20959"));
			} else if(errWarMessagesList.contains("W20959")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20959"));
			}
		}
		if((errWarMessagesList.contains("E20993") || errWarMessagesList.contains("W20993")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentSettlementType()) && pay.getPaymentSettlementTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20993")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E20993"));
			} else if(errWarMessagesList.contains("W20993")) {
				pay.addErrWarJson(new ErrWarJson("w", "E20993"));
			}
		}
	}

	public static void standardize(Payment pay) {
		if(!CommonUtils.isStringNullOrBlank(pay.getClientAccountNumber())) {
			pay.setClientAccountNumber(pay.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentMethod())) {
			pay.setPaymentMethod(pay.getPaymentMethod().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType())) {
			pay.setPaymentType(pay.getPaymentType().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentStatus())) {
			pay.setPaymentStatus(pay.getPaymentStatus().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentBrokenReason())) {
			pay.setPaymentBrokenReason(pay.getPaymentBrokenReason().toUpperCase());
		}
	}

	public static void referenceUpdation(Payment pay, DataScrubEnrichmentServiceCommunication serviceCommunication, String authHeader, Boolean isPartnerCommission) {
		if(pay.getPaymentPlan() != null && !CommonUtils.isLongNull(pay.getPaymentPlan().getPaymentPlanId())) {
			pay.setPaymentPlanId(pay.getPaymentPlan().getPaymentPlanId());
		}
		if(pay.getAccountIds() != null && !CommonUtils.isLongNull(pay.getAccountIds())) {
			pay.setAccountId(pay.getAccountIds());
		}
		
		if (!CommonUtils.isBooleanNull(isPartnerCommission) && isPartnerCommission && !CommonUtils.isIntegerNull(pay.getPartnerId())) {
			if (pay.getPartnerId().equals(pay.getPartnerIds())) {
				pay.setPctPartnerCommission(pay.getAccPctPartnerCommission());
			}else {
				if (!CommonUtils.isStringNullOrBlank(pay.getPaymentType())
						&& pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
					Map<String,Object> headers = new HashMap<>();
					Map<String,Object> requestBody = new HashMap<>();
					requestBody.put("clientId", pay.getClientId());
					requestBody.put("partnerId", pay.getPartnerId());
					requestBody.put("accountId", pay.getAccountId());
					headers.put(HttpHeaders.AUTHORIZATION, authHeader);		
	          Response<Map<String,Object>> commissionDetails =  serviceCommunication.getCommission(headers, requestBody);
	          if (commissionDetails != null && commissionDetails.getValidation() && commissionDetails.getResponse()!= null && commissionDetails.getResponse().get("commission") != null) {
	        	    double commission = Double.parseDouble(commissionDetails.getResponse().get("commission").toString());
	        	    pay.setPctPartnerCommission(commission);
	        	}else {
	        		pay.setPctPartnerCommission(null);
	        	}
				}
			}
		}else {
			pay.setPctPartnerCommission(null);
		}
		
	

	}

	public static void misingRefCheck(Payment pay, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E40916") || errWarMessagesList.contains("W40916")) && CommonUtils.isLongNull(pay.getAccountId())) {
			if(errWarMessagesList.contains("E40916")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E40916"));
			} else if(errWarMessagesList.contains("W40916")) {
				pay.addErrWarJson(new ErrWarJson("w", "E40916"));
			}
		}
	}
	
	public static Boolean checkPaymentBucketsCorrectOrNot(Payment pay) {
		Boolean isPaymentBucketValid = false;
		{
			if(!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) || !CommonUtils.isDoubleNull(pay.getAmtOtherfee()) || !CommonUtils.isDoubleNull(pay.getAmtInterest()) ||
					!CommonUtils.isDoubleNull(pay.getAmtLatefee()) || !CommonUtils.isDoubleNull(pay.getAmtCourtcost()) || !CommonUtils.isDoubleNull(pay.getAmtAttorneyfee()) ) {
				Double principal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) && pay.getAmtPrincipal() > 0) ? pay.getAmtPrincipal() : 0.00;
				Double otherFees = (!CommonUtils.isDoubleNull(pay.getAmtOtherfee()) && pay.getAmtOtherfee() > 0) ? pay.getAmtOtherfee() : 0.00;
				Double interest = (!CommonUtils.isDoubleNull(pay.getAmtInterest()) && pay.getAmtInterest() > 0) ? pay.getAmtInterest() : 0.00;
				Double lateFees = (!CommonUtils.isDoubleNull(pay.getAmtLatefee()) && pay.getAmtLatefee() > 0) ? pay.getAmtLatefee() : 0.00;
				Double courtcost = (!CommonUtils.isDoubleNull(pay.getAmtCourtcost()) && pay.getAmtCourtcost() > 0) ? pay.getAmtCourtcost() : 0.00;
				Double attorneyFees = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyfee()) && pay.getAmtAttorneyfee() > 0) ? pay.getAmtAttorneyfee() : 0.00;
				Double amtPayment = (!CommonUtils.isDoubleNull(pay.getAmtPayment()) && pay.getAmtPayment() > 0) ? pay.getAmtPayment() : 0.00;
				if(amtPayment == (principal + otherFees + interest + lateFees + courtcost + attorneyFees) ) {
					isPaymentBucketValid = true;
				}
			}
			
		}
		return isPaymentBucketValid;
	}
	
	
	public static void weightedPaymentDistribution(Double principal, Double otherFees, Double interest, Double lateFees,  Double courtCost, Double attorneyFees, Payment pay, 
			Double accountAmtPrincipal, Double accountAmtInterest, Double accountAmtOtherfee, Double accountAmtLatefee, Double accountAmtCourtCost, Double accountAmtAttorneyFee) {
		
		principal = Math.round(
		        ((accountAmtPrincipal / pay.getAmtCurrentbalance()) * pay.getAmtPayment()) * 100.0
				) / 100.0;
		interest = Math.round(
				((accountAmtInterest/pay.getAmtCurrentbalance())*pay.getAmtPayment())* 100.0
				) / 100.0;
		otherFees = Math.round(
				((accountAmtOtherfee/pay.getAmtCurrentbalance())*pay.getAmtPayment())* 100.0
				) / 100.0;
		lateFees = Math.round(
				((accountAmtLatefee/pay.getAmtCurrentbalance())*pay.getAmtPayment())* 100.0
				) / 100.0;
		
		courtCost = Math.round(
				((accountAmtCourtCost/pay.getAmtCurrentbalance())*pay.getAmtPayment())* 100.0
				) / 100.0;
		attorneyFees =  Math.round(
				((accountAmtAttorneyFee/pay.getAmtCurrentbalance())*pay.getAmtPayment())* 100.0
				) / 100.0;
		
		// check for difference and assign to the last populated value
		Double diff = pay.getAmtPayment() - (principal + interest + otherFees + lateFees + courtCost + attorneyFees) ;
		if(diff != 0.0) {
			if (attorneyFees != 0) {
			    attorneyFees += diff;
			} else if (courtCost != 0) {
			    courtCost += diff;
			} else if (lateFees != 0) {
			    lateFees += diff;
			} else if (otherFees != 0) {
			    otherFees += diff;
			} else if (interest != 0) {
			    interest += diff;
			} else {
			    principal += diff;
			}
		}
		pay.setAmtOtherfee(otherFees);
		pay.setAmtInterest(interest);
		pay.setAmtPrincipal(principal);
		pay.setAmtLatefee(lateFees);
		pay.setAmtCourtcost(courtCost);
		pay.setAmtAttorneyfee(attorneyFees);
		
	}
	
	public static void sequentialPaymentDistribution(Double principal, Double otherFees, Double interest, Double lateFees, Double attorneyFee, Double courtCost, Payment pay, 
			Double accountAmtPrincipal, Double accountAmtInterest, Double accountAmtOtherfee, Double accountAmtLateFee, Double accountAmtAttorneyFee, Double accountAmtCourtCost) {
		
		if(pay.getClient().getShortName().equals(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
			otherFees = pay.getAmtPayment() >= accountAmtOtherfee ? accountAmtOtherfee : pay.getAmtPayment();
			pay.setAmtOtherfee(otherFees);

			interest = (pay.getAmtPayment() - otherFees) >= accountAmtInterest ? accountAmtInterest : (pay.getAmtPayment() - otherFees);
			pay.setAmtInterest(interest);

			principal = (pay.getAmtPayment() - (otherFees + interest)) >= accountAmtPrincipal ? accountAmtPrincipal : (pay.getAmtPayment() - (otherFees + interest));
			pay.setAmtPrincipal(principal);

			lateFees = (pay.getAmtPayment() - (otherFees + interest + principal)) >= accountAmtLateFee ? accountAmtLateFee : (pay.getAmtPayment() - (otherFees + interest + principal)) ;
			pay.setAmtLatefee(lateFees);
//			lateFees = pay.getAmtPayment() - (otherFees + interest + principal);
			
			attorneyFee = (pay.getAmtPayment() - (otherFees + interest + principal + lateFees)) >= accountAmtAttorneyFee ? accountAmtAttorneyFee : (pay.getAmtPayment() - (otherFees + interest + principal + lateFees));
			pay.setAmtAttorneyfee(attorneyFee);
			
			courtCost = pay.getAmtPayment() - (otherFees + interest + principal + lateFees + attorneyFee);
			pay.setAmtCourtcost(courtCost);
			
		}else {
			Boolean isLateFee = !CommonUtils.isDoubleNull(accountAmtLateFee)
					&& accountAmtLateFee > 0 ? true : false;
			Boolean isInterest = !CommonUtils.isDoubleNull(accountAmtInterest)
					&& accountAmtInterest > 0 ? true : false;
			Boolean isPrincipal = !CommonUtils.isDoubleNull(accountAmtPrincipal)
					&& accountAmtPrincipal > 0 ? true : false;
			Boolean isOtherFee = !CommonUtils.isDoubleNull(accountAmtOtherfee)
					&& accountAmtOtherfee > 0 ? true : false;
			Boolean isAttorneyFee = !CommonUtils.isDoubleNull(accountAmtAttorneyFee)
					&& accountAmtAttorneyFee > 0 ? true : false;
			Boolean isCourtCost = !CommonUtils.isDoubleNull(accountAmtCourtCost)
					&& accountAmtCourtCost > 0 ? true : false;
			lateFees = isLateFee ? (pay.getAmtPayment() >= accountAmtLateFee ? accountAmtLateFee : pay.getAmtPayment()) :0;
			interest = isInterest ? ((pay.getAmtPayment() - lateFees) >= accountAmtInterest ? accountAmtInterest
					: (pay.getAmtPayment() - lateFees)) : 0 ;

			principal = isPrincipal ? ((pay.getAmtPayment() - (lateFees + interest)) >= accountAmtPrincipal ? accountAmtPrincipal
							: (pay.getAmtPayment() - (lateFees + interest))) : 0;

			otherFees = isOtherFee ? ((pay.getAmtPayment() - (lateFees + interest + principal)) >= accountAmtOtherfee ? accountAmtOtherfee
							: (pay.getAmtPayment() - (lateFees + interest + principal))) : 0;
					
			attorneyFee = isAttorneyFee ? ((pay.getAmtPayment() - (otherFees + interest + principal + lateFees)) >= accountAmtAttorneyFee
							? accountAmtAttorneyFee
							: (pay.getAmtPayment() - (otherFees + interest + principal + lateFees))) : 0;	
			courtCost =  isCourtCost ? ((pay.getAmtPayment() - (otherFees + interest + principal + lateFees + attorneyFee))) : 0;
			Double diff = pay.getAmtPayment() - (principal + interest + otherFees + lateFees + courtCost + attorneyFee);
			
			if (diff != 0.0) {
				if (isCourtCost) {
					courtCost += diff;
				} else if (isAttorneyFee) {
					attorneyFee += diff;
				} else if (isOtherFee) {
					otherFees += diff;
				} else if (isPrincipal) {
					principal += diff;
				} else if (isInterest) {
					interest += diff;
				} else {
					lateFees += diff;
				}
			}
			pay.setAmtLatefee(lateFees);
			pay.setAmtInterest(interest);
			pay.setAmtPrincipal(principal);
			pay.setAmtOtherfee(otherFees);
			pay.setAmtAttorneyfee(attorneyFee);
			pay.setAmtCourtcost(courtCost);
		}
		
		
		
	}
	
	public static void businessRule(Payment pay, Map<String, Object> validationMap, List<String> errWarMessagesList, AdjustmentRepository adjustmentRepository,List<Map<String,Object>> listOfDateJudement, SqsMessageSender sqsMessageSender, List<PaymentBucketConfig> paymentBucketConfigLs) {
//		Boolean paymentItemization = false;
//		if(pay.getClient() != null && pay.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
//			if(ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_CL).getRecordSourceId().equals(pay.getRecordSourceId())) {
//				paymentItemization = true;
//			}
//		} else {
//			paymentItemization = true;
//		}

		Double principal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) && pay.getAmtPrincipal() > 0) ? pay.getAmtPrincipal() : 0.00;
		Double otherFees = (!CommonUtils.isDoubleNull(pay.getAmtOtherfee()) && pay.getAmtOtherfee() > 0) ? pay.getAmtOtherfee() : 0.00;
		Double interest = (!CommonUtils.isDoubleNull(pay.getAmtInterest()) && pay.getAmtInterest() > 0) ? pay.getAmtInterest() : 0.00;
		Double lateFees = (!CommonUtils.isDoubleNull(pay.getAmtLatefee()) && pay.getAmtLatefee() > 0) ? pay.getAmtLatefee() : 0.00;
		Double courtcost = (!CommonUtils.isDoubleNull(pay.getAmtCourtcost()) && pay.getAmtCourtcost() > 0) ? pay.getAmtCourtcost() : 0.00;
		Double attorneyFees = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyfee()) && pay.getAmtAttorneyfee() > 0) ? pay.getAmtAttorneyfee() : 0.00;

		if((errWarMessagesList.contains("E70904") || errWarMessagesList.contains("W70904")) && pay.getIsNSFDeDup() > 1) {
			if(errWarMessagesList.contains("E70904")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70904"));
			} else if(errWarMessagesList.contains("W70904")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70904"));
			}
		}
		if(pay.getAccountIds() != null && !CommonUtils.isLongNull(pay.getAccountIds())) {
			Double amtPrincipal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) && pay.getAmtPrincipal() > 0) ? pay.getAmtPrincipal() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(pay.getAmtInterest()) && pay.getAmtInterest() > 0) ? pay.getAmtInterest() :  0.00;
			Double amtOtherfee = (!CommonUtils.isDoubleNull(pay.getAmtOtherfee()) && pay.getAmtOtherfee() > 0) ? pay.getAmtOtherfee() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(pay.getAmtLatefee()) && pay.getAmtLatefee() > 0) ? pay.getAmtLatefee() :  0.00;

			Double accountAmtOtherfee = (!CommonUtils.isDoubleNull(pay.getAmtOtherfeeCurrentbalance()) && pay.getAmtOtherfeeCurrentbalance() > 0) ? pay.getAmtOtherfeeCurrentbalance() :  0.00;
			Double accountAmtInterest = (!CommonUtils.isDoubleNull(pay.getAmtInterestCurrentbalance()) && pay.getAmtInterestCurrentbalance() > 0) ? pay.getAmtInterestCurrentbalance() :  0.00;
			Double accountAmtPrincipal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipalCurrentbalance()) && pay.getAmtPrincipalCurrentbalance() > 0) ? pay.getAmtPrincipalCurrentbalance() :  0.00;
			Double accountAmtLatefee = (!CommonUtils.isDoubleNull(pay.getAmtLatefeeCurrentbalance()) && pay.getAmtLatefeeCurrentbalance() > 0) ? pay.getAmtLatefeeCurrentbalance() :  0.00;
			Double accountAmtCourtCost = (!CommonUtils.isDoubleNull(pay.getAmtCourtCostCurrentbalance()) && pay.getAmtCourtCostCurrentbalance() > 0) ? pay.getAmtCourtCostCurrentbalance() :  0.00;
			Double accountAmtAttorneyFee = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyFeeCurrentBalance()) && pay.getAmtAttorneyFeeCurrentBalance() > 0) ? pay.getAmtAttorneyFeeCurrentBalance() :  0.00;

//			if(paymentItemization) {
//				if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
//					otherFees = pay.getAmtPayment() >= accountAmtOtherfee ? accountAmtOtherfee : pay.getAmtPayment();
//					pay.setAmtOtherfee(otherFees);
//
//					interest = (pay.getAmtPayment() - otherFees) >= accountAmtInterest ? accountAmtInterest : (pay.getAmtPayment() - otherFees);
//					pay.setAmtInterest(interest);
//
//					principal = (pay.getAmtPayment() - (otherFees + interest)) >= accountAmtPrincipal ? accountAmtPrincipal : (pay.getAmtPayment() - (otherFees + interest));
//					pay.setAmtPrincipal(principal);
//
//					lateFees = pay.getAmtPayment() - (otherFees + interest + principal);
//					pay.setAmtLatefee(lateFees);
//				}
//			}
			
			{// New logic for payment bucket distribution
				if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
					if(!CommonUtils.isObjectNull(paymentBucketConfigLs) && paymentBucketConfigLs.size() > 0){

						List<PaymentBucketConfig> clientConfigLs = PaymentBucketConfig.getClientBucketConfig(paymentBucketConfigLs);
						List<PaymentBucketConfig> eqConfigLs = PaymentBucketConfig.getEQBucketConfig(paymentBucketConfigLs);
						
						Boolean bucketDistributed = false;
						
						if(!CommonUtils.isObjectNull(clientConfigLs) && clientConfigLs.size() > 0){
							
							PaymentBucketConfig config_BUCKET_DIS_BUCKET = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(clientConfigLs, "BUCKET_DIS_BUCKET");
							if(!CommonUtils.isObjectNull(config_BUCKET_DIS_BUCKET)) {
//								if(checkPaymentBucketsCorrectOrNot(pay)) {
////									// no change in buckets
//									bucketDistributed = true;
//								}
								switch (config_BUCKET_DIS_BUCKET.getConfiguredValue().toLowerCase()) {
								case "bucket" -> {
									if(checkPaymentBucketsCorrectOrNot(pay)) {
//										// no change in buckets
										bucketDistributed = true;
									}
								}
						        case "weighteddistribution" -> {
						        	 weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
							                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
						            bucketDistributed = true;
						        }
						        case "sequentialdistribution" -> {
						        	sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
						                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
						            bucketDistributed = true;
						        }
						        default -> {
						            // optional: handle unknown distribution type
						        }
						    }
							} 
							if(!bucketDistributed && !CommonUtils.isObjectNull(pay.getActivePaymentPlan()) && clientConfigLs.stream().anyMatch(config -> "BUCKET_DIS_PPLAN".equals(config.getPaymentDistributionType()))) {
								PaymentBucketConfig config = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(clientConfigLs, "BUCKET_DIS_PPLAN");
									
								if (config != null && config.getConfiguredValue() != null) {
								    switch (config.getConfiguredValue().toLowerCase()) {
								        case "weighteddistribution" -> {
								            weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
								                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
								            bucketDistributed = true;
								        }
								        case "sequentialdistribution" -> {
								            sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
								                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
								            bucketDistributed = true;
								        }
								        default -> {
								            // optional: handle unknown distribution type
								        }
								    }
								}
//								if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//									weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//									bucketDistributed = true;
//								}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//									bucketDistributed = true;
//								}
							}else if(!bucketDistributed && CommonUtils.isObjectNull(pay.getActivePaymentPlan()) && clientConfigLs.stream().anyMatch(config -> "BUCKET_DIS_NOPPLAN".equals(config.getPaymentDistributionType()))){
								PaymentBucketConfig config = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(clientConfigLs, "BUCKET_DIS_NOPPLAN");
								
								if (config != null && config.getConfiguredValue() != null) {
								    switch (config.getConfiguredValue().toLowerCase()) {
								        case "weighteddistribution" -> {
								        	 weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
									                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
								            bucketDistributed = true;
								        }
								        case "sequentialdistribution" -> {
								        	sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
								                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
								            bucketDistributed = true;
								        }
								        default -> {
								            // optional: handle unknown distribution type
								        }
								    }
								}
//								if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//									weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//									bucketDistributed = true;
//								}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//									bucketDistributed = true;
//								}
							}
							
						} 
						if(!bucketDistributed && !CommonUtils.isObjectNull(eqConfigLs) && eqConfigLs.size() > 0) {
							
							PaymentBucketConfig config_BUCKET_DIS_BUCKET = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(eqConfigLs, "BUCKET_DIS_BUCKET");
							if(!CommonUtils.isObjectNull(config_BUCKET_DIS_BUCKET)) {
//								if(checkPaymentBucketsCorrectOrNot(pay)) {
////									// no change in buckets
//									bucketDistributed = true;
//								}
								
								//
								switch (config_BUCKET_DIS_BUCKET.getConfiguredValue().toLowerCase()) {
								case "bucket" -> {
									if(checkPaymentBucketsCorrectOrNot(pay)) {
//										// no change in buckets
										bucketDistributed = true;
									}
								}
						        case "weighteddistribution" -> {
						        	 weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
							                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
						            bucketDistributed = true;
						        }
						        case "sequentialdistribution" -> {
						        	sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
						                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
						            bucketDistributed = true;
						        }
						        default -> {
						            // optional: handle unknown distribution type
						        }
						    }
								//
							} 
							if(!bucketDistributed && !CommonUtils.isObjectNull(pay.getActivePaymentPlan()) && eqConfigLs.stream().anyMatch(config -> "BUCKET_DIS_PPLAN".equals(config.getPaymentDistributionType()))) {
								PaymentBucketConfig config = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(eqConfigLs, "BUCKET_DIS_PPLAN");
										
								if (config != null && config.getConfiguredValue() != null) {
								    switch (config.getConfiguredValue().toLowerCase()) {
								        case "weighteddistribution" -> {
								        	 weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
									                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
								            bucketDistributed = true;
								        }
								        case "sequentialdistribution" -> {
								        	sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
								                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
								            bucketDistributed = true;
								        }
								        default -> {
								            // optional: handle unknown distribution type
								        }
								    }
								}
								
//								if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//									weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//									bucketDistributed = true;
//								}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//									bucketDistributed = true;
//								}
							}else if(!bucketDistributed && CommonUtils.isObjectNull(pay.getActivePaymentPlan()) && eqConfigLs.stream().anyMatch(config -> "BUCKET_DIS_NOPPLAN".equals(config.getPaymentDistributionType()))){
								PaymentBucketConfig config = PaymentBucketConfig.getPaymentBucketConfigFromPaymentDistributionType(eqConfigLs, "BUCKET_DIS_NOPPLAN");
								
								if (config != null && config.getConfiguredValue() != null) {
								    switch (config.getConfiguredValue().toLowerCase()) {
								        case "weighteddistribution" -> {
								        	 weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, courtcost, attorneyFees,
									                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtCourtCost, accountAmtAttorneyFee);
								            bucketDistributed = true;
								        }
								        case "sequentialdistribution" -> {
								        	sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
								                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
								            bucketDistributed = true;
								        }
								        default -> {
								            // optional: handle unknown distribution type
								        }
								    }
								}
//								if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//									weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//									bucketDistributed = true;
//								}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//									bucketDistributed = true;
//								}
							}
						}else if(!bucketDistributed){
							sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
				                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
							bucketDistributed = true;
						}
//						if(paymentBucketConfigLs.stream().anyMatch(config -> "BUCKET_DIS_BUCKET".equals(config.getPaymentDistributionType()))) {
//							if(checkPaymentBucketsCorrectOrNot(pay)) {
//								// no change in buckets
//							}else if(!CommonUtils.isObjectNull(pay.getPaymentPlan()) && paymentBucketConfigLs.stream().anyMatch(config -> "BUCKET_DIS_PPLAN".equals(config.getPaymentDistributionType())) ){
//								PaymentBucketConfig config = paymentBucketConfigLs.stream()
//										.filter(c -> "BUCKET_DIS_PPLAN".equals(c.getPaymentDistributionType()))
//										.findFirst()
//										.orElse(null);
//								if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//									weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//								}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//								}else {
//									sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//								}
//							}
//						}else if(!CommonUtils.isObjectNull(pay.getPaymentPlan()) && paymentBucketConfigLs.stream().anyMatch(config -> "BUCKET_DIS_PPLAN".equals(config.getPaymentDistributionType())) ){
//							PaymentBucketConfig config = paymentBucketConfigLs.stream()
//									.filter(c -> "BUCKET_DIS_PPLAN".equals(c.getPaymentDistributionType()))
//									.findFirst()
//									.orElse(null);
//							if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//								weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//							}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//								sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//							}else {
//								sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//							}
//						}else if(CommonUtils.isObjectNull(pay.getPaymentPlan()) && paymentBucketConfigLs.stream().anyMatch(config -> "BUCKET_DIS_NOPPLAN".equals(config.getPaymentDistributionType())) ){
//							PaymentBucketConfig config = paymentBucketConfigLs.stream()
//									.filter(c -> "BUCKET_DIS_NOPPLAN".equals(c.getPaymentDistributionType()))
//									.findFirst()
//									.orElse(null);
//							if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("WeightedDistribution")) {
//								weightedPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee);
//							}else if(!CommonUtils.isObjectNull(config) && config.getConfiguredValue().equalsIgnoreCase("SequentialDistribution")) {
//								sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//							}else {
//								sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//							}
//						}else {
//							sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees, pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee);
//						}
					}else{
						sequentialPaymentDistribution(amtPrincipal, otherFees, amtInterest, lateFees,attorneyFees, courtcost,
			                    pay, accountAmtPrincipal, accountAmtInterest, accountAmtOtherfee, accountAmtLatefee, accountAmtAttorneyFee,accountAmtCourtCost);
					}
				}
				
				
				{// Sequential Distribution
//					if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
//						otherFees = pay.getAmtPayment() >= accountAmtOtherfee ? accountAmtOtherfee : pay.getAmtPayment();
//						pay.setAmtOtherfee(otherFees);
//
//						interest = (pay.getAmtPayment() - otherFees) >= accountAmtInterest ? accountAmtInterest : (pay.getAmtPayment() - otherFees);
//						pay.setAmtInterest(interest);
//
//						principal = (pay.getAmtPayment() - (otherFees + interest)) >= accountAmtPrincipal ? accountAmtPrincipal : (pay.getAmtPayment() - (otherFees + interest));
//						pay.setAmtPrincipal(principal);
//
//						lateFees = pay.getAmtPayment() - (otherFees + interest + principal);
//						pay.setAmtLatefee(lateFees);
//					}
					
				}
				{// weighted Distribution
//					principal = (accountAmtPrincipal/pay.getAmtCurrentbalance())*pay.getAmtPayment();
//					interest = (accountAmtInterest/pay.getAmtCurrentbalance())*pay.getAmtPayment();
//					otherFees = (accountAmtOtherfee/pay.getAmtCurrentbalance())*pay.getAmtPayment();
//					lateFees = (accountAmtLatefee/pay.getAmtCurrentbalance())*pay.getAmtPayment();
//					
//					pay.setAmtOtherfee(otherFees);
//					pay.setAmtInterest(interest);
//					pay.setAmtPrincipal(principal);
//					pay.setAmtLatefee(lateFees);
					
				}
				
			}

			if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtInterest > 0 && amtOtherfee <= 0 && accountAmtOtherfee > 0) {
				if(errWarMessagesList.contains("E70901")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70901"));
				} else if(errWarMessagesList.contains("W70901")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70901"));
				}
			}
			if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtPrincipal > 0 && ((amtInterest <= 0 && accountAmtInterest > 0) || (amtOtherfee <= 0 && accountAmtOtherfee > 0))) {
				if(errWarMessagesList.contains("E70901")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70901"));
				} else if(errWarMessagesList.contains("W70901")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70901"));
				}
			}
			if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtLatefee > 0 && ((amtPrincipal <= 0 && accountAmtPrincipal > 0) || (amtInterest <= 0 && accountAmtInterest > 0) || (amtOtherfee <= 0 && accountAmtOtherfee > 0))) {
				if(errWarMessagesList.contains("E70901")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70901"));
				} else if(errWarMessagesList.contains("W70901")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70901"));
				}
			}

			if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
				if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtOtherfee > 0 && accountAmtOtherfee <= 0) {
					if(errWarMessagesList.contains("E70901")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70901"));
					} else if(errWarMessagesList.contains("W70901")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70901"));
					}
				} 
				if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtInterest > 0 && accountAmtInterest <= 0) {
					if(errWarMessagesList.contains("E70901")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70901"));
					} else if(errWarMessagesList.contains("W70901")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70901"));
					}
				} 
				if((errWarMessagesList.contains("E70901") || errWarMessagesList.contains("W70901")) && amtPrincipal > 0 && accountAmtPrincipal <= 0) {
					if(errWarMessagesList.contains("E70901")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70901"));
					} else if(errWarMessagesList.contains("W70901")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70901"));
					}
				}
			}

			Double amtBalance = (!CommonUtils.isDoubleNull(pay.getAmtBalance())) ? pay.getAmtBalance() :  0.00;
			Double amtPayment = (!CommonUtils.isDoubleNull(pay.getAmtPayment())) ? pay.getAmtPayment() :  0.00;
			Double accCurrentBalance = (!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance())) ? pay.getAmtCurrentbalance() :  0.00;

			String amtBal = CommonConstants.dfFormat.format(amtBalance);
			String amtCal1 = "";
			if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
				amtCal1 = CommonConstants.dfFormat.format((accCurrentBalance) - (amtPayment));
			} else {
				amtCal1 = CommonConstants.dfFormat.format((accCurrentBalance) + (amtPayment));
			}

			if((errWarMessagesList.contains("E70902") || errWarMessagesList.contains("W70902")) && !amtBal.equalsIgnoreCase(amtCal1)) {
				try {
					if(pay.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME) &&
							ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_CL).getRecordSourceId().equals(pay.getRecordSourceId())){
						Double amtDiff = Double.valueOf(amtBal) - Double.valueOf(amtCal1);
						Adjustment adjustment = null;
						Double adPrincpal = 0.0;
						Double adInterest = 0.0;
						Double adFees = 0.0;
						if( (amtDiff > 0) && (1>amtDiff) ) {
							if(principal > 0) {
								adPrincpal = amtDiff;
							}else if(interest > 0) {
								adInterest = amtDiff;
							}else if(otherFees > 0) {
								adFees = amtDiff;
							}
							adjustment = new Adjustment("adjustment", pay.getClientId(), pay.getAccountId(), "BA", LocalDate.now(), amtDiff, adPrincpal, adInterest, adFees, null, null, null, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
							adjustmentRepository.insertIntoAdjustment(adjustment.getRecordType(), adjustment.getClientId(), pay.getClientAccountNumber(), adjustment.getAccountId(), adjustment.getAdjustmentType(), adjustment.getAdjustmentDate(), adjustment.getAmtAdjustment(), adjustment.getAmtPrincipal(), adjustment.getAmtInterest(), adjustment.getAmtLatefee(), adjustment.getRecordStatusId());
							Long adjustmentId = adjustmentRepository.getAdjustmentId();
							String clientCode = adjustmentRepository.getClientCode(pay.getClientId());
//							sqsMessageSender.sendMessage(clientCode,"AJ",adjustmentId);
							
						}else if((amtDiff < 0) &&amtDiff>-1)  {
							if(principal > 0) {
								adPrincpal = -1*amtDiff;
							}else if(interest > 0) {
								adInterest = -1*amtDiff;
							}else if(otherFees > 0) {
								adFees = -1*amtDiff;
							}
							adjustment = new Adjustment("adjustment", pay.getClientId(), pay.getAccountId(), "CR", LocalDate.now(), (-1*amtDiff), adPrincpal, adInterest, adFees, null, null, null, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
							adjustmentRepository.insertIntoAdjustment(adjustment.getRecordType(), adjustment.getClientId(),pay.getClientAccountNumber(), adjustment.getAccountId(), adjustment.getAdjustmentType(), adjustment.getAdjustmentDate(), adjustment.getAmtAdjustment(), adjustment.getAmtPrincipal(), adjustment.getAmtInterest(), adjustment.getAmtLatefee(), adjustment.getRecordStatusId());
						
							Long adjustmentId = adjustmentRepository.getAdjustmentId();
							String clientCode = adjustmentRepository.getClientCode(pay.getClientId());
//							sqsMessageSender.sendMessage(clientCode,"AJ",adjustmentId);
						
						}else {
							if(errWarMessagesList.contains("E70902")) {
								validationMap.put("isPaymentValidated", false);
								pay.addErrWarJson(new ErrWarJson("e", "E70902"));
							} else if(errWarMessagesList.contains("W70902")) {
								pay.addErrWarJson(new ErrWarJson("w", "E70902"));
							}
						}
					}else {
						if(errWarMessagesList.contains("E70902")) {
							validationMap.put("isPaymentValidated", false);
							pay.addErrWarJson(new ErrWarJson("e", "E70902"));
						} else if(errWarMessagesList.contains("W70902")) {
							pay.addErrWarJson(new ErrWarJson("w", "E70902"));
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NSF)) {
			if((errWarMessagesList.contains("E70905") || errWarMessagesList.contains("W70905")) && CommonUtils.isLongNull(pay.getParentPaymentIds())) {
				if(errWarMessagesList.contains("E70905")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70905"));
				} else if(errWarMessagesList.contains("W70905")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70905"));
				}
			}
			if(!CommonUtils.isDateNull(pay.getDtChargeOff()) && !CommonUtils.isDateNull(pay.getPaymentDate()) &&
					pay.getDtChargeOff().isAfter(pay.getPaymentDate()) ) {
				pay.setAmtPrincipal(pay.getAmtPayment());
				pay.setAmtInterest(0.0);
				pay.setAmtLatefee(0.0);
				pay.setAmtOtherfee(0.0);
				pay.setAmtCourtcost(0.0);
				pay.setAmtAttorneyfee(0.0);
			}else if (!CommonUtils.isLongNull(pay.getParentPaymentIds())){
				pay.setAmtPrincipal(pay.getParentPrincipal());
				pay.setAmtInterest(pay.getParentInterest());
				pay.setAmtOtherfee(pay.getParentOtherFees());
				pay.setAmtLatefee(pay.getParentLateFees());
				pay.setAmtCourtcost(pay.getParentCourtCost());
				pay.setAmtAttorneyfee(pay.getParentAttorneyFees());
			}else {
				pay.setAmtPrincipal(pay.getAmtPayment());
				pay.setAmtInterest(0.0);
				pay.setAmtLatefee(0.0);
				pay.setAmtOtherfee(0.0);
				pay.setAmtCourtcost(0.0);
				pay.setAmtAttorneyfee(0.0);
			}
			if((errWarMessagesList.contains("E70906") || errWarMessagesList.contains("W70906")) && !CommonUtils.isLongNull(pay.getParentPaymentIds()) && !pay.getParentInterest().equals(interest) && !pay.getParentPrincipal().equals(principal) && !pay.getParentOtherFees().equals(otherFees) && !pay.getParentLateFees().equals(lateFees)) {
				if(errWarMessagesList.contains("E70906")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70906"));
				} else if(errWarMessagesList.contains("W70906")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70906"));
				}
			}
			pay.setParentPaymentId(pay.getParentPaymentIds());
		}
		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NSF)) {
			Double amtPrincipal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) ) ? pay.getAmtPrincipal() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(pay.getAmtInterest()) ) ? pay.getAmtInterest() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(pay.getAmtLatefee()) ) ? pay.getAmtLatefee() :  0.00;
			Double amtOtherfee = (!CommonUtils.isDoubleNull(pay.getAmtOtherfee()) ) ? pay.getAmtOtherfee() :  0.00;
			Double amtCourtcost = (!CommonUtils.isDoubleNull(pay.getAmtCourtcost()) ) ? pay.getAmtCourtcost() :  0.00;
			Double amtAttorneyfee = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyfee()) ) ? pay.getAmtAttorneyfee() :  0.00;
			String totalAmtPayment = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee + amtOtherfee + amtCourtcost + amtAttorneyfee);
			String amtPayment =  CommonConstants.dfFormat.format(pay.getAmtPayment());
			
			if(!totalAmtPayment.equalsIgnoreCase(amtPayment)) {
				pay.setAmtPrincipal(pay.getAmtPayment());
				pay.setAmtInterest(0.0);
				pay.setAmtLatefee(0.0);
				pay.setAmtOtherfee(0.0);
			}
		}
		if(!CommonUtils.isDoubleNull(pay.getAmtPayment()) && pay.getAmtPayment() > 0) {
			Double amtPayment = (!CommonUtils.isDoubleNull(pay.getAmtPayment()) && pay.getAmtPayment() > 0) ? pay.getAmtPayment() :  0.00;
			Double amtPrincipal = (!CommonUtils.isDoubleNull(pay.getAmtPrincipal()) && pay.getAmtPrincipal() > 0) ? pay.getAmtPrincipal() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(pay.getAmtInterest()) && pay.getAmtInterest() > 0) ? pay.getAmtInterest() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(pay.getAmtLatefee()) && pay.getAmtLatefee() > 0) ? pay.getAmtLatefee() :  0.00;
			Double amtOtherfee = (!CommonUtils.isDoubleNull(pay.getAmtOtherfee()) && pay.getAmtOtherfee() > 0) ? pay.getAmtOtherfee() :  0.00;
			Double amtCourtcost = (!CommonUtils.isDoubleNull(pay.getAmtCourtcost()) && pay.getAmtCourtcost() > 0) ? pay.getAmtCourtcost() :  0.00;
			Double amtAttorneyfee = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyfee()) && pay.getAmtAttorneyfee() > 0) ? pay.getAmtAttorneyfee() :  0.00;

			String amtTotal = CommonConstants.dfFormat.format(amtPayment);
			String amtCal = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee + amtOtherfee + amtCourtcost + amtAttorneyfee);

			if((errWarMessagesList.contains("E70915") || errWarMessagesList.contains("W70915")) && !amtTotal.equalsIgnoreCase(amtCal)) {
				if(errWarMessagesList.contains("E70915")) {
					validationMap.put("isPaymentValidated", false);
					pay.addErrWarJson(new ErrWarJson("e", "E70915"));
				} else if(errWarMessagesList.contains("W70915")) {
					pay.addErrWarJson(new ErrWarJson("w", "E70915"));
				}
			}
		}
		if((errWarMessagesList.contains("E70907") || errWarMessagesList.contains("W70907")) && !CommonUtils.isStringNullOrBlank(pay.getPaymentSettlementType()) && !(pay.getPaymentSettlementType().equalsIgnoreCase(CommonConstants.PAYMENT_SETTLEMENT_TYPE_PF)
				|| pay.getPaymentSettlementType().equalsIgnoreCase(CommonConstants.PAYMENT_SETTLEMENT_TYPE_SF) || pay.getPaymentSettlementType().equalsIgnoreCase(CommonConstants.PAYMENT_SETTLEMENT_TYPE_NN))) {
			if(errWarMessagesList.contains("E70907")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70907"));
			} else if(errWarMessagesList.contains("W70907")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70907"));
			}
		}

		if(pay.getAccountIds() != null && !CommonUtils.isLongNull(pay.getAccountIds())) {
			if(listOfDateJudement.size() == 0  || (listOfDateJudement.size() > 0 && 
					!CommonUtils.isObjectNull(listOfDateJudement.get(0).get("dtjudgement")) && (pay.getPaymentDate().isAfter(LocalDate.parse(listOfDateJudement.get(0).get("dtjudgement").toString()))))) {
			if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL)) {
				if(!CommonUtils.isDoubleNull(pay.getAmtOtherfeeCurrentbalance())) {
					pay.setAmtOtherfeeCurrentbalance(pay.getAmtOtherfeeCurrentbalance() - otherFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtInterestCurrentbalance())) {
					pay.setAmtInterestCurrentbalance(pay.getAmtInterestCurrentbalance() - interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPrincipalCurrentbalance())) {
					pay.setAmtPrincipalCurrentbalance(pay.getAmtPrincipalCurrentbalance() - principal);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance())) {
					pay.setAmtCurrentbalance(pay.getAmtCurrentbalance() - pay.getAmtPayment());
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtLatefeeCurrentbalance())) {
					pay.setAmtLatefeeCurrentbalance(pay.getAmtLatefeeCurrentbalance() - lateFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtCourtCostCurrentbalance())) {
					pay.setAmtCourtCostCurrentbalance(pay.getAmtCourtCostCurrentbalance() - courtcost);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtAttorneyFeeCurrentBalance())) {
					pay.setAmtAttorneyFeeCurrentBalance(pay.getAmtAttorneyFeeCurrentBalance() - attorneyFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffPrinciple())) {
					pay.setAmtPreChargeOffPrinciple(pay.getAmtPreChargeOffPrinciple() - principal);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffInterest())) {
					pay.setAmtPreChargeOffInterest(pay.getAmtPreChargeOffInterest() - interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffFee())) {
					pay.setAmtPreChargeOffFee(pay.getAmtPreChargeOffFee() - (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffInterest())) {
					pay.setAmtPostChargeOffInterest(pay.getAmtPostChargeOffInterest() - interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffFees())) {
					pay.setAmtPostChargeOffFees(pay.getAmtPostChargeOffFees() - (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffPayment())) {
					pay.setAmtPostChargeOffPayment(pay.getAmtPostChargeOffPayment() + pay.getAmtPayment());
				}
			} else {
				if(!CommonUtils.isDoubleNull(pay.getAmtOtherfeeCurrentbalance())) {
					pay.setAmtOtherfeeCurrentbalance(pay.getAmtOtherfeeCurrentbalance() + otherFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtInterestCurrentbalance())) {
					pay.setAmtInterestCurrentbalance(pay.getAmtInterestCurrentbalance() + interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPrincipalCurrentbalance())) {
					pay.setAmtPrincipalCurrentbalance(pay.getAmtPrincipalCurrentbalance() + principal);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance())) {
					pay.setAmtCurrentbalance(pay.getAmtCurrentbalance() + pay.getAmtPayment());
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtLatefeeCurrentbalance())) {
					pay.setAmtLatefeeCurrentbalance(pay.getAmtLatefeeCurrentbalance() + lateFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtCourtCostCurrentbalance())) {
					pay.setAmtCourtCostCurrentbalance(pay.getAmtCourtCostCurrentbalance() + courtcost);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtAttorneyFeeCurrentBalance())) {
					pay.setAmtAttorneyFeeCurrentBalance(pay.getAmtAttorneyFeeCurrentBalance() + attorneyFees);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffPrinciple())) {
					pay.setAmtPreChargeOffPrinciple(pay.getAmtPreChargeOffPrinciple() + principal);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffInterest())) {
					pay.setAmtPreChargeOffInterest(pay.getAmtPreChargeOffInterest() + interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffFee())) {
					pay.setAmtPreChargeOffFee(pay.getAmtPreChargeOffFee() + (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffInterest())) {
					pay.setAmtPostChargeOffInterest(pay.getAmtPostChargeOffInterest() + interest);
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffFees())) {
					pay.setAmtPostChargeOffFees(pay.getAmtPostChargeOffFees() + (otherFees + lateFees + courtcost + attorneyFees));
				}
				if(!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffPayment())) {
					pay.setAmtPostChargeOffPayment(pay.getAmtPostChargeOffPayment() - pay.getAmtPayment());
				}
			}
			}
			pay.setPaymentAmtCurrentBalance(pay.getAmtCurrentbalance());
		}
		if(!CommonUtils.isIntegerNullOrZero(pay.getPartnerId())) {
			if(!pay.getPartnerId().equals(pay.getPartnerIds())) {
				if((errWarMessagesList.contains("E70910") || errWarMessagesList.contains("W70910")) && !CommonUtils.isDateNull(pay.getCotDtFrom()) && pay.getCotDtFrom().isAfter(pay.getPaymentDate())) {
					if(errWarMessagesList.contains("E70910")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70910"));
					} else if(errWarMessagesList.contains("W70910")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70910"));
					}
				}
				if((errWarMessagesList.contains("E70909") || errWarMessagesList.contains("W70909")) && !CommonUtils.isDateNull(pay.getCotDtTill()) && pay.getCotDtTill().isBefore(pay.getPaymentDate())) {
					if(errWarMessagesList.contains("E70909")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70909"));
					} else if(errWarMessagesList.contains("W70909")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70909"));
					}
				}
				if((errWarMessagesList.contains("E70911") || errWarMessagesList.contains("W70911")) && CommonUtils.isDateNull(pay.getCotDtFrom()) && CommonUtils.isDateNull(pay.getCotDtTill())) {
					if(errWarMessagesList.contains("E70911")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70911"));
					} else if(errWarMessagesList.contains("W70911")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70911"));
					}
				}
			}
		}

		Double amtCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance()) && pay.getAmtCurrentbalance() > 0) ? pay.getAmtCurrentbalance() :  0.00;
		Double amtPrincipalCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtPrincipalCurrentbalance()) && pay.getAmtPrincipalCurrentbalance() > 0) ? pay.getAmtPrincipalCurrentbalance() :  0.00;
		Double amtInterestCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtInterestCurrentbalance()) && pay.getAmtInterestCurrentbalance() > 0) ? pay.getAmtInterestCurrentbalance() :  0.00;
		Double amtOtherfeeCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtOtherfeeCurrentbalance()) && pay.getAmtOtherfeeCurrentbalance() > 0) ? pay.getAmtOtherfeeCurrentbalance() :  0.00;
		Double amtCourtCostCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtCourtCostCurrentbalance()) && pay.getAmtCourtCostCurrentbalance() > 0) ? pay.getAmtCourtCostCurrentbalance() :  0.00;
		Double amtAttorneyFeeCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtAttorneyFeeCurrentBalance()) && pay.getAmtAttorneyFeeCurrentBalance() > 0) ? pay.getAmtAttorneyFeeCurrentBalance() :  0.00;
		Double amtLatefeeCurrentbalance = (!CommonUtils.isDoubleNull(pay.getAmtLatefeeCurrentbalance()) && pay.getAmtLatefeeCurrentbalance() > 0) ? pay.getAmtLatefeeCurrentbalance() :  0.00;

		if (pay.getAccountIds() != null && !CommonUtils.isLongNull(pay.getAccountIds()) && pay.getDtChargeOff() == null) { // Pre Charge Off
			if(!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance()) && pay.getAmtCurrentbalance() > 0) {
				Double amtPreChargeOffPrinciple = (!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffPrinciple()) && pay.getAmtPreChargeOffPrinciple() > 0) ? pay.getAmtPreChargeOffPrinciple() :  0.00;
				Double amtPreChargeOffInterest = (!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffInterest()) && pay.getAmtPreChargeOffInterest() > 0) ? pay.getAmtPreChargeOffInterest() :  0.00;
				Double amtPreChargeOffFee = (!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffFee()) && pay.getAmtPreChargeOffFee() > 0) ? pay.getAmtPreChargeOffFee() :  0.00;

				String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
				String amtCal = CommonConstants.dfFormat.format(amtPreChargeOffPrinciple + amtPreChargeOffInterest + amtPreChargeOffFee);

				if((errWarMessagesList.contains("E70912") || errWarMessagesList.contains("W70912")) && !amtTotal.equalsIgnoreCase(amtCal)) {
					if(errWarMessagesList.contains("E70912")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70912"));
					} else if(errWarMessagesList.contains("W70912")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70912"));
					}
				}
			}
		} else if (pay.getAccountIds() != null && !CommonUtils.isLongNull(pay.getAccountIds()) && pay.getDtChargeOff() != null) { // Charge Off
			if(!CommonUtils.isDoubleNull(pay.getAmtCurrentbalance()) && pay.getAmtCurrentbalance() > 0) {
				Double amtPreChargeOffBalance = (!CommonUtils.isDoubleNull(pay.getAmtPreChargeOffBalance()) && pay.getAmtPreChargeOffBalance() > 0) ? pay.getAmtPreChargeOffBalance() :  0.00;
				Double amtPostChargeOffInterest = (!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffInterest()) && pay.getAmtPostChargeOffInterest() > 0) ? pay.getAmtPostChargeOffInterest() :  0.00;
				Double amtPostChargeOffFees = (!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffFees()) && pay.getAmtPostChargeOffFees() > 0) ? pay.getAmtPostChargeOffFees() :  0.00;
				Double amtPostChargeOffPayment = (!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffPayment()) && pay.getAmtPostChargeOffPayment() > 0) ? pay.getAmtPostChargeOffPayment() :  0.00;
				Double amtPostChargeOffCredit = (!CommonUtils.isDoubleNull(pay.getAmtPostChargeOffCredit()) && pay.getAmtPostChargeOffCredit() > 0) ? pay.getAmtPostChargeOffCredit() :  0.00;

				String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
				String amtCal = CommonConstants.dfFormat.format(amtPreChargeOffBalance + amtPostChargeOffInterest + amtPostChargeOffFees - (amtPostChargeOffPayment + amtPostChargeOffCredit));

				if((errWarMessagesList.contains("E70913") || errWarMessagesList.contains("W70913")) && !amtTotal.equalsIgnoreCase(amtCal)) {
					if(errWarMessagesList.contains("E70913")) {
						validationMap.put("isPaymentValidated", false);
						pay.addErrWarJson(new ErrWarJson("e", "E70913"));
					} else if(errWarMessagesList.contains("W70913")) {
						pay.addErrWarJson(new ErrWarJson("w", "E70913"));
					}
				}
			}
		}
		String amtTotal = CommonConstants.dfFormat.format(amtCurrentbalance);
		String amtCal = CommonConstants.dfFormat.format(amtPrincipalCurrentbalance + amtInterestCurrentbalance + amtOtherfeeCurrentbalance + amtCourtCostCurrentbalance + amtAttorneyFeeCurrentbalance + amtLatefeeCurrentbalance);

		if((errWarMessagesList.contains("E70914") || errWarMessagesList.contains("W70914")) && !amtTotal.equalsIgnoreCase(amtCal)) {
			if(errWarMessagesList.contains("E70914")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70914"));
			} else if(errWarMessagesList.contains("W70914")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70914"));
			}
		}

		if((errWarMessagesList.contains("E70916") || errWarMessagesList.contains("W70916")) && pay.getIsParentPaymentExist() > 1) {
			if(errWarMessagesList.contains("E70916")) {
				validationMap.put("isPaymentValidated", false);
				pay.addErrWarJson(new ErrWarJson("e", "E70916"));
			} else if(errWarMessagesList.contains("W70916")) {
				pay.addErrWarJson(new ErrWarJson("w", "E70916"));
			}
		}
	}
}