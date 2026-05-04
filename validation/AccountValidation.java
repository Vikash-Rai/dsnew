package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class AccountValidation {

	public static void mandatoryValidation(Account account, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10101") || errWarMessagesList.contains("W10101")) && CommonUtils.isIntegerNullOrZero(account.getClientId())) {
			if(errWarMessagesList.contains("E10102")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E10101");
				account.addErrWarJson(new ErrWarJson("e", "E10101"));
			} else if(errWarMessagesList.contains("W10101")) {
				account.addErrWarJson(new ErrWarJson("w", "E10101"));
			}
		}
		if((errWarMessagesList.contains("E10102") || errWarMessagesList.contains("W10102")) && CommonUtils.isStringNullOrBlank(account.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10102")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E10102");
				account.addErrWarJson(new ErrWarJson("e", "E10102"));
			} else if(errWarMessagesList.contains("W10102")) {
				account.addErrWarJson(new ErrWarJson("w", "E10102"));
			}
		}
		if((errWarMessagesList.contains("E10104") || errWarMessagesList.contains("W10104")) && CommonUtils.isIntegerNullOrZero(account.getProductId())) {
			if(errWarMessagesList.contains("E10104")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E10104");
				account.addErrWarJson(new ErrWarJson("e", "E10104"));
			} else if(errWarMessagesList.contains("W10104")) {
				account.addErrWarJson(new ErrWarJson("w", "E10104"));
			}
		}
	}

	public static void lookUpValidation(Account account, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20101") || errWarMessagesList.contains("W20101")) && !CommonUtils.isIntegerNullOrZero(account.getClientId()) && account.getClient() == null) {
			if(errWarMessagesList.contains("E20101")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20101");
				account.addErrWarJson(new ErrWarJson("e", "E20101"));
			} else if(errWarMessagesList.contains("W20101")) {
				account.addErrWarJson(new ErrWarJson("w", "E20101"));
			}
		}
		if((errWarMessagesList.contains("E20104") || errWarMessagesList.contains("W20104")) && !CommonUtils.isIntegerNullOrZero(account.getProductId()) && account.getProduct() == null) {
			if(errWarMessagesList.contains("E20104")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20104");
				account.addErrWarJson(new ErrWarJson("e", "E20104"));
			} else if(errWarMessagesList.contains("W20104")) {
				account.addErrWarJson(new ErrWarJson("w", "E20104"));
			}
		}
		if((errWarMessagesList.contains("E20187") || errWarMessagesList.contains("W20187")) && !CommonUtils.isIntegerNullOrZero(account.getProductSubTypeId()) && (account.getProductSubTypeCount() == null || account.getProductSubTypeCount() == 0)) {
			if(errWarMessagesList.contains("E20187")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20187");
				account.addErrWarJson(new ErrWarJson("e", "E20187"));
			} else if(errWarMessagesList.contains("W20104")) {
				account.addErrWarJson(new ErrWarJson("w", "E20187"));
			}
		}
		if((errWarMessagesList.contains("E20118") || errWarMessagesList.contains("W20118")) && !CommonUtils.isStringNullOrBlank(account.getPortfolioCode()) && account.getPortfolioCodeLookup() == null) {
			if(errWarMessagesList.contains("E20118")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20118");
				account.addErrWarJson(new ErrWarJson("e", "E20118"));
			} else if(errWarMessagesList.contains("W20118")) {
				account.addErrWarJson(new ErrWarJson("w", "E20118"));
			}
		}
		if((errWarMessagesList.contains("E20106") || errWarMessagesList.contains("W20106")) && !CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20106")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20106");
				account.addErrWarJson(new ErrWarJson("e", "E20106"));
			} else if(errWarMessagesList.contains("W20106")) {
				account.addErrWarJson(new ErrWarJson("w", "E20106"));
			}
		}
		if((errWarMessagesList.contains("E20188") || errWarMessagesList.contains("W20188")) && !CommonUtils.isStringNullOrBlank(account.getDebtType()) && account.getDebtTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20188")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20188");
				account.addErrWarJson(new ErrWarJson("e", "E20188"));
			} else if(errWarMessagesList.contains("W20188")) {
				account.addErrWarJson(new ErrWarJson("w", "E20188"));
			}
		}
		if((errWarMessagesList.contains("E20189") || errWarMessagesList.contains("W20189")) && !CommonUtils.isStringNullOrBlank(account.getOriginalAccountApplicationType()) && account.getOriginalAccountApplicationTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20189")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20189");
				account.addErrWarJson(new ErrWarJson("e", "E20189"));
			} else if(errWarMessagesList.contains("W20189")) {
				account.addErrWarJson(new ErrWarJson("w", "E20189"));
			}
		}
		if((errWarMessagesList.contains("E20190") || errWarMessagesList.contains("W20190")) && !CommonUtils.isStringNullOrBlank(account.getSaleReviewStatus()) && account.getSaleReviewStatusLookUp() == null) {
			if(errWarMessagesList.contains("E20190")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E20190");
				account.addErrWarJson(new ErrWarJson("e", "E20190"));
			} else if(errWarMessagesList.contains("W20190")) {
				account.addErrWarJson(new ErrWarJson("w", "E20190"));
			}
		}
	}

	public static void standardize(Account account) {
		if(CommonUtils.isStringNullOrBlank(account.getProductAffinity()) && !CommonUtils.isStringNullOrBlank(account.getMerchantName())) {
			account.setProductAffinity(account.getMerchantName().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(account.getProductAffinity())) {
			account.setProductAffinity(account.getProductAffinity().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(account.getOriginalLenderCreditor())) {
			account.setOriginalLenderCreditor(account.getOriginalLenderCreditor().toUpperCase());
		}
		
			account.setAssignedDate(LocalDate.now());
		
	}

	public static void deDupValidation(Account account, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E30103") || errWarMessagesList.contains("W30103")) && !CommonUtils.isIntegerNullOrZero(account.getOriginalAccountNoDeDup())
				&& account.getOriginalAccountNoDeDup() > 1) {
			if(errWarMessagesList.contains("E30103")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E30103");
				account.addErrWarJson(new ErrWarJson("e", "E30103"));
			} else if(errWarMessagesList.contains("W30103")) {
				account.addErrWarJson(new ErrWarJson("w", "E30103"));
			}
		}
	}

	public static void businessRule(Account account, Map<String, Object> validationMap,  List<String> errWarMessagesList) {
		if(errWarMessagesList.contains("E70101") || errWarMessagesList.contains("W70101")) {
			if(!CommonUtils.isStringNullOrBlank(account.getClientAccountNumber()) && StringUtils.containsWhitespace(account.getClientAccountNumber())) {
				if(errWarMessagesList.contains("E70101")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70101");
					account.addErrWarJson(new ErrWarJson("e", "E70101"));
				} else if(errWarMessagesList.contains("W70101")) {
					account.addErrWarJson(new ErrWarJson("w", "E70101"));
				}
			}
			if(!CommonUtils.isStringNullOrBlank(account.getOriginalAccountNumber()) && StringUtils.containsWhitespace(account.getOriginalAccountNumber())) {
				if(errWarMessagesList.contains("E70101")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70101");
					account.addErrWarJson(new ErrWarJson("e", "E70101"));
				} else if(errWarMessagesList.contains("W70101")) {
					account.addErrWarJson(new ErrWarJson("w", "E70101"));
				}
			}
		}
		if(errWarMessagesList.contains("E70102") || errWarMessagesList.contains("W70102")) {
			if(!CommonUtils.isStringNullOrBlank(account.getClientAccountNumber()) && CommonUtils.containsSpecialChar(account.getClientAccountNumber())) {
				if(errWarMessagesList.contains("E70102")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70102");
					account.addErrWarJson(new ErrWarJson("e", "E70102"));
				} else if(errWarMessagesList.contains("W70102")) {
					account.addErrWarJson(new ErrWarJson("w", "E70102"));
				}
			}
			if(!CommonUtils.isStringNullOrBlank(account.getOriginalAccountNumber()) && CommonUtils.containsSpecialChar(account.getOriginalAccountNumber())) {
				if(errWarMessagesList.contains("E70102")) {
					validationMap.put("isAccountValidated", false);
					account.addErrWarJson(new ErrWarJson("e", "E70102"));
				} else if(errWarMessagesList.contains("W70102")) {
					account.addErrWarJson(new ErrWarJson("w", "E70102"));
				}
			}
		}
		if(errWarMessagesList.contains("E70103") || errWarMessagesList.contains("W70103")) {
			if(!CommonUtils.isStringNullOrBlank(account.getClientAccountNumber()) && !CommonUtils.onlyDigits(account.getClientAccountNumber())) {
				if(errWarMessagesList.contains("E70103")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70103");
					account.addErrWarJson(new ErrWarJson("e", "E70103"));
				} else if(errWarMessagesList.contains("W70103")) {
					account.addErrWarJson(new ErrWarJson("w", "E70103"));
				}
			}
			if(!CommonUtils.isStringNullOrBlank(account.getOriginalAccountNumber()) && !CommonUtils.onlyDigits(account.getOriginalAccountNumber())) {
				if(errWarMessagesList.contains("E70103")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70103");
					account.addErrWarJson(new ErrWarJson("e", "E70103"));
				} else if(errWarMessagesList.contains("W70103")) {
					account.addErrWarJson(new ErrWarJson("w", "E70103"));
				}
			}
		}
		if((errWarMessagesList.contains("E70105") || errWarMessagesList.contains("W70105")) && !CommonUtils.isStringNullOrBlank(account.getOriginalAccountNumber()) && account.getOriginalAccountNumber().length() < 5) {
			if(errWarMessagesList.contains("E70105")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70105");
				account.addErrWarJson(new ErrWarJson("e", "E70105"));
			} else if(errWarMessagesList.contains("W70105")) {
				account.addErrWarJson(new ErrWarJson("w", "E70105"));
			}
		}
		if((errWarMessagesList.contains("E70106") || errWarMessagesList.contains("W70106")) && !CommonUtils.isStringNullOrBlank(account.getOriginalAccountNumber()) && account.getOriginalAccountNumber().length() > 16) {
			if(errWarMessagesList.contains("E70106")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70106");
				account.addErrWarJson(new ErrWarJson("e", "E70106"));
			} else if(errWarMessagesList.contains("W70106")) {
				account.addErrWarJson(new ErrWarJson("w", "E70106"));
			}
		}
		if(!CommonUtils.isDateNull(account.getOriginalAccountOpenDate())) {
			if((errWarMessagesList.contains("E70107") || errWarMessagesList.contains("W70107")) && !CommonUtils.isDateNull(account.getDelinquencyDate())
					&& (account.getOriginalAccountOpenDate().isAfter(account.getDelinquencyDate()))) {
				if(errWarMessagesList.contains("E70107")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70107");
					account.addErrWarJson(new ErrWarJson("e", "E70107"));
				} else if(errWarMessagesList.contains("W70107")) {
					account.addErrWarJson(new ErrWarJson("w", "E70107"));
				}
			}
			if((errWarMessagesList.contains("E70108") || errWarMessagesList.contains("W70108")) && !CommonUtils.isDateNull(account.getChargeOffDate())
					&& (account.getOriginalAccountOpenDate().equals(account.getChargeOffDate()) || account.getOriginalAccountOpenDate().isAfter(account.getChargeOffDate()))) {
				if(errWarMessagesList.contains("E70108")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70108");
					account.addErrWarJson(new ErrWarJson("e", "E70108"));
				} else if(errWarMessagesList.contains("W70108")) {
					account.addErrWarJson(new ErrWarJson("w", "E70108"));
				}
			}
			if((errWarMessagesList.contains("E70109") || errWarMessagesList.contains("W70109")) && !CommonUtils.isDateNull(account.getLastPaymentDate())
					&& (account.getOriginalAccountOpenDate().isAfter(account.getLastPaymentDate()))) {
				if(errWarMessagesList.contains("E70109")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70109");
					account.addErrWarJson(new ErrWarJson("e", "E70109"));
				} else if(errWarMessagesList.contains("W70109")) {
					account.addErrWarJson(new ErrWarJson("w", "E70109"));
				}
			}
			if((errWarMessagesList.contains("E70110") || errWarMessagesList.contains("W70110")) && !CommonUtils.isDateNull(account.getLastPurchaseDate())
					&& (account.getOriginalAccountOpenDate().isAfter(account.getLastPurchaseDate()))) {
				if(errWarMessagesList.contains("E70110")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70110");
					account.addErrWarJson(new ErrWarJson("e", "E70110"));
				} else if(errWarMessagesList.contains("W70110")) {
					account.addErrWarJson(new ErrWarJson("w", "E70110"));
				}
			}
			if((errWarMessagesList.contains("E70111") || errWarMessagesList.contains("W70111")) && account.getOriginalAccountOpenDate().isAfter(LocalDate.now())) {
				if(errWarMessagesList.contains("E70111")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70111");
					account.addErrWarJson(new ErrWarJson("e", "E70111"));
				} else if(errWarMessagesList.contains("W70111")) {
					account.addErrWarJson(new ErrWarJson("w", "E70111"));
				}
			}
		}
		if((errWarMessagesList.contains("E70112") || errWarMessagesList.contains("W70112")) && !CommonUtils.isDoubleNull(account.getAmtPreChargeOffBalance()) && account.getAmtPreChargeOffBalance() < 0) {
			if(errWarMessagesList.contains("E70112")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70112");
				account.addErrWarJson(new ErrWarJson("e", "E70112"));
			} else if(errWarMessagesList.contains("W70112")) {
				account.addErrWarJson(new ErrWarJson("w", "E70112"));
			}
		}
		if((errWarMessagesList.contains("E70113") || errWarMessagesList.contains("W70113"))
				&& !CommonUtils.isDateNull(account.getChargeOffDate())
				&& !CommonUtils.isDoubleNull(account.getAmtPreChargeOffBalance())
				&& !CommonUtils.isDoubleNull(account.getAmtPostChargeOffInterest())
				&& !CommonUtils.isDoubleNull(account.getAmtPostChargeOffFee())
				&& !CommonUtils.isDoubleNull(account.getAmtAssigned())) {

			// As per story: EQCLCT-12115, Updated E70113 validation to allow post-charge-off payment and credit to be null and treated as zero while validating assigned amount.
			double postChargeOffPayment = CommonUtils.isDoubleNull(account.getAmtPostChargeOffPayment())
					? 0.0 : account.getAmtPostChargeOffPayment();
			double postChargeOffCredit = CommonUtils.isDoubleNull(account.getAmtPostChargeOffCredit())
					? 0.0 : account.getAmtPostChargeOffCredit();

			String amtAssigned = CommonConstants.dfFormat.format(account.getAmtAssigned());
			String amtCal = CommonConstants.dfFormat.format((account.getAmtPreChargeOffBalance() + account.getAmtPostChargeOffInterest() + account.getAmtPostChargeOffFee()) - (postChargeOffPayment  + postChargeOffCredit));

			if(!amtAssigned.equalsIgnoreCase(amtCal)) {
				if(errWarMessagesList.contains("E70113")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70113");
					account.addErrWarJson(new ErrWarJson("e", "E70113"));
				} else if(errWarMessagesList.contains("W70113")) {
					account.addErrWarJson(new ErrWarJson("w", "E70113"));
				}
			}
		}
		if((errWarMessagesList.contains("E70114") || errWarMessagesList.contains("W70114")) && !CommonUtils.isDoubleNull(account.getAmtPreChargeOffBalance()) && !CommonUtils.isDoubleNull(account.getAmtPreChargeOffPrinciple())
				&& !CommonUtils.isDoubleNull(account.getAmtPreChargeOffInterest()) && !CommonUtils.isDoubleNull(account.getAmtPreChargeOffFees())) {
			String amtPreChargeOffBalance = CommonConstants.dfFormat.format(account.getAmtPreChargeOffBalance());
			String amtCal = CommonConstants.dfFormat.format(account.getAmtPreChargeOffPrinciple() + account.getAmtPreChargeOffInterest() + account.getAmtPreChargeOffFees());

			if(!amtPreChargeOffBalance.equalsIgnoreCase(amtCal)) {
				if(errWarMessagesList.contains("E70114")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70114");
					account.addErrWarJson(new ErrWarJson("e", "E70114"));
				} else if(errWarMessagesList.contains("W70114")) {
					account.addErrWarJson(new ErrWarJson("w", "E70114"));
				}
			}
		}
		if((errWarMessagesList.contains("E70115") || errWarMessagesList.contains("W70115")) && !CommonUtils.isDateNull(account.getDelinquencyDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
				&& (account.getDelinquencyDate().isAfter(account.getChargeOffDate()))) {
			if(errWarMessagesList.contains("E70115")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70115");
				account.addErrWarJson(new ErrWarJson("e", "E70115"));
			} else if(errWarMessagesList.contains("W70115")) {
				account.addErrWarJson(new ErrWarJson("w", "E70115"));
			}
		}
		if((errWarMessagesList.contains("E70117") || errWarMessagesList.contains("W70117")) && !CommonUtils.isDateNull(account.getDelinquencyDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
				&& (ChronoUnit.DAYS.between(account.getDelinquencyDate(), account.getChargeOffDate()) > 250)) {
			if(errWarMessagesList.contains("E70117")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70117");
				account.addErrWarJson(new ErrWarJson("e", "E70117"));
			} else if(errWarMessagesList.contains("W70117")) {
				account.addErrWarJson(new ErrWarJson("w", "E70117"));
			}
		}
		if((errWarMessagesList.contains("E70118") || errWarMessagesList.contains("W70118")) && (!CommonUtils.isDateNull(account.getLastPaymentDate()) && (CommonUtils.isDoubleNull(account.getAmtLastPayment()) || account.getAmtLastPayment() <= 0))
				|| (CommonUtils.isDateNull(account.getLastPaymentDate()) && !CommonUtils.isDoubleNull(account.getAmtLastPayment()) && account.getAmtLastPayment() > 0)) {
			if(errWarMessagesList.contains("E70118")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70118");
				account.addErrWarJson(new ErrWarJson("e", "E70118"));
			} else if(errWarMessagesList.contains("W70118")) {
				account.addErrWarJson(new ErrWarJson("w", "E70118"));
			}
		}
		if((errWarMessagesList.contains("E70119") || errWarMessagesList.contains("W70119")) && CommonUtils.isDateNull(account.getLastPaymentDate()) && account.getProduct() != null && account.getProduct().getShortName().equalsIgnoreCase("CC")
				&& !CommonUtils.isDateNull(account.getDelinquencyDate()) && !CommonUtils.isDateNull(account.getChargeOffDate()) 
				&& (ChronoUnit.MONTHS.between(account.getDelinquencyDate(), account.getChargeOffDate()) > 6)) {
			if(errWarMessagesList.contains("E70119")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70119");
				account.addErrWarJson(new ErrWarJson("e", "E70119"));
			} else if(errWarMessagesList.contains("W70119")) {
				account.addErrWarJson(new ErrWarJson("w", "E70119"));
			}
		}
		if(!CommonUtils.isDateNull(account.getChargeOffDate())) {
			if((errWarMessagesList.contains("E70120") || errWarMessagesList.contains("W70120")) && !CommonUtils.isDateNull(account.getLastPaymentDate()) && !CommonUtils.isDateNull(account.getChargeOffDate()) && !CommonUtils.isDoubleNull(account.getAmtPostChargeOffPayment())
					&& account.getAmtPostChargeOffPayment() > 0 && account.getChargeOffDate().isAfter(account.getLastPaymentDate())) {
				if(errWarMessagesList.contains("E70120")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70120");
					account.addErrWarJson(new ErrWarJson("e", "E70120"));
				} else if(errWarMessagesList.contains("W70120")) {
					account.addErrWarJson(new ErrWarJson("w", "E70120"));
				}
			}
			if((errWarMessagesList.contains("E70121") || errWarMessagesList.contains("W70121")) && !CommonUtils.isDateNull(account.getLastPaymentDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
					&& (CommonUtils.isDoubleNull(account.getAmtPostChargeOffPayment()) || account.getAmtPostChargeOffPayment() <= 0)
					&& account.getLastPaymentDate().isAfter(account.getChargeOffDate())) {
				if(errWarMessagesList.contains("E70121")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70121");
					account.addErrWarJson(new ErrWarJson("e", "E70121"));
				} else if(errWarMessagesList.contains("W70121")) {
					account.addErrWarJson(new ErrWarJson("w", "E70121"));
				}
			}
		}
		if((errWarMessagesList.contains("E70122") || errWarMessagesList.contains("W70122")) && !CommonUtils.isDateNull(account.getLastPurchaseDate()) && !CommonUtils.isDateNull(account.getOriginalAccountOpenDate())
				&& account.getOriginalAccountOpenDate().isAfter(account.getLastPurchaseDate())) {
			if(errWarMessagesList.contains("E70122")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70122");
				account.addErrWarJson(new ErrWarJson("e", "E70122"));
			} else if(errWarMessagesList.contains("W70122")) {
				account.addErrWarJson(new ErrWarJson("w", "E70122"));
			}
		}
		if((errWarMessagesList.contains("E70123") || errWarMessagesList.contains("W70123")) && !CommonUtils.isDateNull(account.getLastPurchaseDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
				&& account.getLastPurchaseDate().isAfter(account.getChargeOffDate())) {
			if(errWarMessagesList.contains("E70123")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70123");
				account.addErrWarJson(new ErrWarJson("e", "E70123"));
			} else if(errWarMessagesList.contains("W70123")) {
				account.addErrWarJson(new ErrWarJson("w", "E70123"));
			}
		}
		if((errWarMessagesList.contains("E70124") || errWarMessagesList.contains("W70124")) && (CommonUtils.isDateNull(account.getLastPurchaseDate()) && !CommonUtils.isDoubleNull(account.getAmtLastPurchase()) && account.getAmtLastPurchase() > 0)
				&& (!CommonUtils.isDateNull(account.getLastPurchaseDate()) && (CommonUtils.isDoubleNull(account.getAmtLastPurchase()) || account.getAmtLastPurchase() <= 0))) {
			if(errWarMessagesList.contains("E70124")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70124");
				account.addErrWarJson(new ErrWarJson("e", "E70124"));
			} else if(errWarMessagesList.contains("W70124")) {
				account.addErrWarJson(new ErrWarJson("w", "E70124"));
			}
		}
		if((errWarMessagesList.contains("E70125") || errWarMessagesList.contains("W70125")) && !CommonUtils.isDoubleNull(account.getAmtAssigned()) && account.getAmtAssigned() <= 0) {
			if(errWarMessagesList.contains("E70125")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70125");
				account.addErrWarJson(new ErrWarJson("e", "E70125"));
			} else if(errWarMessagesList.contains("W70125")) {
				account.addErrWarJson(new ErrWarJson("w", "E70125"));
			}
		}
		if((errWarMessagesList.contains("E70126") || errWarMessagesList.contains("W70126")) && (!CommonUtils.isDoubleNull(account.getAmtPostChargeOffInterest()) && account.getAmtPostChargeOffInterest() < 0)
				|| (!CommonUtils.isDoubleNull(account.getAmtPostChargeOffFee()) && account.getAmtPostChargeOffFee() < 0)) {
			if(errWarMessagesList.contains("E70126")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70126");
				account.addErrWarJson(new ErrWarJson("e", "E70126"));
			} else if(errWarMessagesList.contains("W70126")) {
				account.addErrWarJson(new ErrWarJson("w", "E70126"));
			}
		}
		if((errWarMessagesList.contains("E70127") || errWarMessagesList.contains("W70127")) && (account.getAmtPostChargeOffInterest() != null && account.getAmtPostChargeOffInterest() > 0 && (account.getPctPostChargeOffInterest() == null || account.getPctPostChargeOffInterest() == 0))
				|| (account.getAmtPostChargeOffFee() != null && account.getAmtPostChargeOffFee() > 0 && (account.getPctPostChargeOffFee() == null || account.getPctPostChargeOffFee() == 0))) {
			if(errWarMessagesList.contains("E70127")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70127");
				account.addErrWarJson(new ErrWarJson("e", "E70127"));
			} else if(errWarMessagesList.contains("W70127")) {
				account.addErrWarJson(new ErrWarJson("w", "E70127"));
			}
		}
		if((errWarMessagesList.contains("E70128") || errWarMessagesList.contains("W70128")) && !CommonUtils.isDateNull(account.getLastCashAdvanceDate()) && !CommonUtils.isDateNull(account.getOriginalAccountOpenDate())
				&& account.getOriginalAccountOpenDate().isAfter(account.getLastCashAdvanceDate())) {
			if(errWarMessagesList.contains("E70128")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70128");
				account.addErrWarJson(new ErrWarJson("e", "E70128"));
			} else if(errWarMessagesList.contains("W70128")) {
				account.addErrWarJson(new ErrWarJson("w", "E70128"));
			}
		}
		if((errWarMessagesList.contains("E70129") || errWarMessagesList.contains("W70129")) && !CommonUtils.isDateNull(account.getLastCashAdvanceDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
				&& account.getLastCashAdvanceDate().isAfter(account.getChargeOffDate())) {
			if(errWarMessagesList.contains("E70129")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70129");
				account.addErrWarJson(new ErrWarJson("e", "E70129"));
			} else if(errWarMessagesList.contains("W70129")) {
				account.addErrWarJson(new ErrWarJson("w", "E70129"));
			}
		}
		if((errWarMessagesList.contains("E70130") || errWarMessagesList.contains("W70130")) && (CommonUtils.isDateNull(account.getLastCashAdvanceDate()) && !CommonUtils.isDoubleNull(account.getAmtLastCashAdvance()) && account.getAmtLastCashAdvance() > 0)
				&& (!CommonUtils.isDateNull(account.getLastCashAdvanceDate()) && (CommonUtils.isDoubleNull(account.getAmtLastCashAdvance()) || account.getAmtLastCashAdvance() <= 0))) {
			if(errWarMessagesList.contains("E70130")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70130");
				account.addErrWarJson(new ErrWarJson("e", "E70130"));
			} else if(errWarMessagesList.contains("W70130")) {
				account.addErrWarJson(new ErrWarJson("w", "E70130"));
			}
		}
		if((errWarMessagesList.contains("E70131") || errWarMessagesList.contains("W70131")) && !CommonUtils.isDateNull(account.getLastBalanceTransferDate()) && !CommonUtils.isDateNull(account.getOriginalAccountOpenDate())
				&& account.getOriginalAccountOpenDate().isAfter(account.getLastBalanceTransferDate())) {
			if(errWarMessagesList.contains("E70131")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70131");
				account.addErrWarJson(new ErrWarJson("e", "E70131"));
			} else if(errWarMessagesList.contains("W70131")) {
				account.addErrWarJson(new ErrWarJson("w", "E70131"));
			}
		}
		if((errWarMessagesList.contains("E70132") || errWarMessagesList.contains("W70132")) && !CommonUtils.isDateNull(account.getLastBalanceTransferDate()) && !CommonUtils.isDateNull(account.getChargeOffDate())
				&& account.getLastBalanceTransferDate().isAfter(account.getChargeOffDate())) {
			if(errWarMessagesList.contains("E70132")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70132");
				account.addErrWarJson(new ErrWarJson("e", "E70132"));
			} else if(errWarMessagesList.contains("W70132")) {
				account.addErrWarJson(new ErrWarJson("w", "E70132"));
			}
		}
		if((errWarMessagesList.contains("E70133") || errWarMessagesList.contains("W70133")) && (CommonUtils.isDateNull(account.getLastBalanceTransferDate()) && !CommonUtils.isDoubleNull(account.getAmtLastBalanceTransfer()) && account.getAmtLastBalanceTransfer() > 0)
				&& (!CommonUtils.isDateNull(account.getLastBalanceTransferDate()) && (CommonUtils.isDoubleNull(account.getAmtLastBalanceTransfer()) || account.getAmtLastBalanceTransfer() <= 0))) {
			if(errWarMessagesList.contains("E70133")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70133");
				account.addErrWarJson(new ErrWarJson("e", "E70133"));
			} else if(errWarMessagesList.contains("W70133")) {
				account.addErrWarJson(new ErrWarJson("w", "E70133"));
			}
		}
		if((errWarMessagesList.contains("E70134") || errWarMessagesList.contains("W70134")) && !CommonUtils.isDoubleNull(account.getAmtAssigned())) {
			Double amtPrincipal = (!CommonUtils.isDoubleNull(account.getAmtPrincipalAssigned()) && account.getAmtPrincipalAssigned() > 0) ? account.getAmtPrincipalAssigned() :  0.00;
			Double amtInterest = (!CommonUtils.isDoubleNull(account.getAmtInterestAssigned()) && account.getAmtInterestAssigned() > 0) ? account.getAmtInterestAssigned() :  0.00;
			Double amtLatefee = (!CommonUtils.isDoubleNull(account.getAmtLatefeeAssigned()) && account.getAmtLatefeeAssigned() > 0) ? account.getAmtLatefeeAssigned() :  0.00;
			Double amtOtherfee = (!CommonUtils.isDoubleNull(account.getAmtOtherfeeAssigned()) && account.getAmtOtherfeeAssigned() > 0) ? account.getAmtOtherfeeAssigned() :  0.00;
			Double amtCourtcost = (!CommonUtils.isDoubleNull(account.getAmtCourtcostAssigned()) && account.getAmtCourtcostAssigned() > 0) ? account.getAmtCourtcostAssigned() :  0.00;
			Double amtAttorneyfee = (!CommonUtils.isDoubleNull(account.getAmtAttorneyfeeAssigned()) && account.getAmtAttorneyfeeAssigned() > 0) ? account.getAmtAttorneyfeeAssigned() :  0.00;

			String amtAssigned = CommonConstants.dfFormat.format(account.getAmtAssigned());
			String amtCal = CommonConstants.dfFormat.format(amtPrincipal + amtInterest + amtLatefee + amtOtherfee + amtCourtcost + amtAttorneyfee);

			if(!amtAssigned.equalsIgnoreCase(amtCal)) {
				if(errWarMessagesList.contains("E70134")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E70134");
					account.addErrWarJson(new ErrWarJson("e", "E70134"));
				} else if(errWarMessagesList.contains("W70134")) {
					account.addErrWarJson(new ErrWarJson("w", "E70134"));
				}
			}
		}
		if ((errWarMessagesList.contains("E70135") || errWarMessagesList.contains("W70135")) && CommonUtils.isDateNull(account.getPartnerAssignmentDate()) && !CommonUtils.isIntegerNull(account.getPartnerId())){
			if(errWarMessagesList.contains("E70135")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70135");
				account.addErrWarJson(new ErrWarJson("e", "E70135"));
			} else if(errWarMessagesList.contains("W70135")) {
				account.addErrWarJson(new ErrWarJson("w", "E70135"));
			}
		}
		if ((errWarMessagesList.contains("E70136") || errWarMessagesList.contains("W70136")) && !CommonUtils.isDateNull(account.getPartnerAssignmentDate()) && CommonUtils.isIntegerNull(account.getPartnerId())){
			if(errWarMessagesList.contains("E70136")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70136");
				account.addErrWarJson(new ErrWarJson("e", "E70136"));
			} else if(errWarMessagesList.contains("W70136")) {
				account.addErrWarJson(new ErrWarJson("w", "E70136"));
			}
		}
		if ((errWarMessagesList.contains("E70138") || errWarMessagesList.contains("W70138")) && account.getProduct() != null && account.getProduct().getShortName().equalsIgnoreCase("AL") && account.getAutoAccountInfoIds() == null){
			if(errWarMessagesList.contains("E70138")) {
				validationMap.put("isAccountValidated", false);
				account.setErrShortName("E70138");
				account.addErrWarJson(new ErrWarJson("e", "E70138"));
			} else if(errWarMessagesList.contains("W70138")) {
				account.addErrWarJson(new ErrWarJson("w", "E70138"));
			}
		}
	}
}