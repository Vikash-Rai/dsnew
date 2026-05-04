package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.LookUp;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.EntityUtil;

public class ConsumerValidation {

	public static void mandatoryValidation(Account account, Consumer consumer, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10201") || errWarMessagesList.contains("W10201")) && CommonUtils.isIntegerNullOrZero(consumer.getClientId())) {
			if(errWarMessagesList.contains("E10201")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10201");
				consumer.addErrWarJson(new ErrWarJson("e", "E10201"));
			} else if(errWarMessagesList.contains("W10201")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10201"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10201")) {
					account.setErrShortName("E10201");
					account.addErrWarJson(new ErrWarJson("e", "E10201"));
				} else if(errWarMessagesList.contains("W10201")) {
					account.addErrWarJson(new ErrWarJson("w", "E10201"));
				}
			}
		}
		if((errWarMessagesList.contains("E10202") || errWarMessagesList.contains("W10202")) && CommonUtils.isStringNullOrBlank(consumer.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10202")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10202");
				consumer.addErrWarJson(new ErrWarJson("e", "E10202"));
			} else if(errWarMessagesList.contains("W10202")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10202"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10202")) {
					account.setErrShortName("E10202");
					account.addErrWarJson(new ErrWarJson("e", "E10202"));
				} else if(errWarMessagesList.contains("W10202")) {
					account.addErrWarJson(new ErrWarJson("w", "E10202"));
				}
			}
		}
		if((errWarMessagesList.contains("E10218") || errWarMessagesList.contains("W10218")) && CommonUtils.isLongNull(consumer.getClientConsumerNumber())) {
			if(errWarMessagesList.contains("E10218")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10218");
				consumer.addErrWarJson(new ErrWarJson("e", "E10218"));
			} else if(errWarMessagesList.contains("W10218")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10218"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10218")) {
					account.setErrShortName("E10218");
					account.addErrWarJson(new ErrWarJson("e", "E10218"));
				} else if(errWarMessagesList.contains("W10218")) {
					account.addErrWarJson(new ErrWarJson("w", "E10218"));
				}
			}
		}
		if((errWarMessagesList.contains("E10220") || errWarMessagesList.contains("W10220")) && CommonUtils.isIntegerNullOrZero(consumer.getContactType())) {
			if(errWarMessagesList.contains("E10220")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10220");
				consumer.addErrWarJson(new ErrWarJson("e", "E10220"));
			} else if(errWarMessagesList.contains("W10220")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10220"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10220")) {
					account.setErrShortName("E10220");
					account.addErrWarJson(new ErrWarJson("e", "E10220"));
				} else if(errWarMessagesList.contains("W10220")) {
					account.addErrWarJson(new ErrWarJson("w", "E10220"));
				}
			}
		}
		if((errWarMessagesList.contains("E10221") || errWarMessagesList.contains("W10221")) && !CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerType().equalsIgnoreCase(CommonConstants.CUSTOMER_TYPE_INDIVIDUAL)
				&& CommonUtils.isStringNullOrBlank(consumer.getFirstName())) {
			if(errWarMessagesList.contains("E10221")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10221");
				consumer.addErrWarJson(new ErrWarJson("e", "E10221"));
			} else if(errWarMessagesList.contains("W10221")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10221"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10221")) {
					account.setErrShortName("E10221");
					account.addErrWarJson(new ErrWarJson("e", "E10221"));
				} else if(errWarMessagesList.contains("W10221")) {
					account.addErrWarJson(new ErrWarJson("w", "E10221"));
				}
			}
		}
		if((errWarMessagesList.contains("E10223") || errWarMessagesList.contains("W10223")) && !CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerType().equalsIgnoreCase(CommonConstants.CUSTOMER_TYPE_INDIVIDUAL)
				&& CommonUtils.isStringNullOrBlank(consumer.getLastName())) {
			if(errWarMessagesList.contains("E10223")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10223");
				consumer.addErrWarJson(new ErrWarJson("e", "E10223"));
			} else if(errWarMessagesList.contains("W10223")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10223"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10223")) {
					account.setErrShortName("E10223");
					account.addErrWarJson(new ErrWarJson("e", "E10223"));
				} else if(errWarMessagesList.contains("W10223")) {
					account.addErrWarJson(new ErrWarJson("w", "E10223"));
				}
			}
		}
		if((errWarMessagesList.contains("E10224") || errWarMessagesList.contains("W10224")) && !CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerType().equalsIgnoreCase(CommonConstants.CUSTOMER_TYPE_COMMERCIAL)
				&& CommonUtils.isStringNullOrBlank(consumer.getBusinessName())) {
			if(errWarMessagesList.contains("E10224")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E10224");
				consumer.addErrWarJson(new ErrWarJson("e", "E10224"));
			} else if(errWarMessagesList.contains("W10224")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E10224"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E10224")) {
					account.setErrShortName("E10224");
					account.addErrWarJson(new ErrWarJson("e", "E10224"));
				} else if(errWarMessagesList.contains("W10224")) {
					account.addErrWarJson(new ErrWarJson("w", "E10224"));
				}
			}
		}
	}

	public static void lookUpValidation(Account account, Consumer consumer, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20201") || errWarMessagesList.contains("W20201")) && !CommonUtils.isIntegerNullOrZero(consumer.getClientId()) && consumer.getClient() == null) {
			if(errWarMessagesList.contains("E20201")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E20201");
				consumer.addErrWarJson(new ErrWarJson("e", "E20201"));
			} else if(errWarMessagesList.contains("W20201")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E20201"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E20201")) {
					account.setErrShortName("E20201");
					account.addErrWarJson(new ErrWarJson("e", "E20201"));
				} else if(errWarMessagesList.contains("W20201")) {
					account.addErrWarJson(new ErrWarJson("w", "E20201"));
				}
			}
		}
		if((errWarMessagesList.contains("E20220") || errWarMessagesList.contains("W20220")) && !CommonUtils.isIntegerNullOrZero(consumer.getContactType()) && consumer.getContactTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20220")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E20220");
				consumer.addErrWarJson(new ErrWarJson("e", "E20220"));
			} else if(errWarMessagesList.contains("W20220")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E20220"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E20220")) {
					account.setErrShortName("E20220");
					account.addErrWarJson(new ErrWarJson("e", "E20220"));
				} else if(errWarMessagesList.contains("W20220")) {
					account.addErrWarJson(new ErrWarJson("w", "E20220"));
				}
			}
		}
		if((errWarMessagesList.contains("E20291") || errWarMessagesList.contains("W20291")) && !CommonUtils.isStringNullOrBlank(consumer.getPreferredLanguage()) && consumer.getPreferredLanguageLookUp() == null) {
			if(errWarMessagesList.contains("E20291")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E20291");
				consumer.addErrWarJson(new ErrWarJson("e", "E20291"));
			} else if(errWarMessagesList.contains("W20291")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E20291"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E20291")) {
					account.setErrShortName("E20291");
					account.addErrWarJson(new ErrWarJson("e", "E20291"));
				} else if(errWarMessagesList.contains("W20291")) {
					account.addErrWarJson(new ErrWarJson("w", "E20291"));
				}
			}
		}
	}

	public static void standardize(Account account, Consumer consumer, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isStringNullOrBlank(consumer.getClientAccountNumber())) {
			consumer.setClientAccountNumber(consumer.getClientAccountNumber().toUpperCase());
		}
		if((errWarMessagesList.contains("E10221") || errWarMessagesList.contains("W10221")) && !CommonUtils.isStringNullOrBlank(consumer.getFirstName())) {
			String firstName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_prefix_exclude_char"), consumer.getFirstName());
			firstName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_suffix_exclude_char"), firstName);
			firstName = EntityUtil.capitailizeWord(firstName);
			consumer.setFirstName(firstName.trim());

			if(!CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerType().equalsIgnoreCase(CommonConstants.CUSTOMER_TYPE_INDIVIDUAL) 
					&& CommonUtils.isStringNullOrBlank(firstName.trim())) {
				if(errWarMessagesList.contains("E10221")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E10221");
					consumer.addErrWarJson(new ErrWarJson("e", "E10221"));
				} else if(errWarMessagesList.contains("W10221")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E10221"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E10221")) {
						account.setErrShortName("E10221");
						account.addErrWarJson(new ErrWarJson("e", "E10221"));
					} else if(errWarMessagesList.contains("W10221")) {
						account.addErrWarJson(new ErrWarJson("w", "E10221"));
					}
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(consumer.getMiddleName())) {
			String middleName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_prefix_exclude_char"), consumer.getMiddleName());
			middleName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_suffix_exclude_char"), middleName);
			middleName = EntityUtil.capitailizeWord(middleName);
			consumer.setMiddleName(middleName.trim());
		}
		if((errWarMessagesList.contains("E10223") || errWarMessagesList.contains("W10223")) && !CommonUtils.isStringNullOrBlank(consumer.getLastName())) {
			String lastName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_prefix_exclude_char"), consumer.getLastName());
			lastName = LookUp.replaceCharsByKeyValue(cacheableService.lookUpByGroupKeyValue("name_suffix_exclude_char"), lastName);
			lastName = EntityUtil.capitailizeWord(lastName);
			consumer.setLastName(lastName.trim());

			if(!CommonUtils.isStringNullOrBlank(account.getCustomerType()) && account.getCustomerType().equalsIgnoreCase(CommonConstants.CUSTOMER_TYPE_INDIVIDUAL) 
					&& CommonUtils.isStringNullOrBlank(lastName.trim())) {
				if(errWarMessagesList.contains("E10223")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E10223");
					consumer.addErrWarJson(new ErrWarJson("e", "E10223"));
				} else if(errWarMessagesList.contains("W10223")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E10223"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E10223")) {
						account.setErrShortName("E10223");
						account.addErrWarJson(new ErrWarJson("e", "E10223"));
					} else if(errWarMessagesList.contains("W10223")) {
						account.addErrWarJson(new ErrWarJson("w", "E10223"));
					}
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(consumer.getBusinessName())) {
			consumer.setBusinessName(EntityUtil.capitailizeWord(consumer.getBusinessName()));
		}
		if(!CommonUtils.isStringNullOrBlank(consumer.getIdentificationNumber())) {
			consumer.setIdentificationNumber(consumer.getIdentificationNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(consumer.getServiceBranch())) {
			consumer.setServiceBranch(EntityUtil.capitailizeWord(consumer.getServiceBranch()));
		}
	}

	public static void deDupValidation(Account account, Consumer consumer, Map<String, Object> validationMap, List<String> errWarMessagesList ) {
		if((errWarMessagesList.contains("E30218") || errWarMessagesList.contains("W30218")) && consumer.getClientConsumerNoDeDup() > 1) {
			if(errWarMessagesList.contains("E30218")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E30218");
				consumer.addErrWarJson(new ErrWarJson("e", "E30218"));
			} else if(errWarMessagesList.contains("W30218")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E30218"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E30218")) {
					account.setErrShortName("E30218");
					account.addErrWarJson(new ErrWarJson("e", "E30218"));
				} else if(errWarMessagesList.contains("W30218")) {
					account.addErrWarJson(new ErrWarJson("w", "E30218"));
				}
			}
		}
	}

	public static void referenceUpdation(Account account, Consumer consumer) {
		if(!CommonUtils.isLongNull(account.getAccountId())) {
			consumer.setAccountId(account.getAccountId());
		}
		if(consumer.getEmployer() != null && !CommonUtils.isIntegerNullOrZero(consumer.getEmployer().getEmployerId())) {
			consumer.setEmployerId(consumer.getEmployer().getEmployerId());
		}
	}

	public static void misingRefCheck(Account account, Consumer consumer, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E40216") || errWarMessagesList.contains("W40216")) && CommonUtils.isLongNull(consumer.getAccountId())) {
			if(errWarMessagesList.contains("E40216")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E40216");
				consumer.addErrWarJson(new ErrWarJson("e", "E40216"));
			} else if(errWarMessagesList.contains("W40216")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E40216"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E40216")) {
					account.setErrShortName("E40216");
					account.addErrWarJson(new ErrWarJson("e", "E40216"));
				} else if(errWarMessagesList.contains("W40216")) {
					account.addErrWarJson(new ErrWarJson("w", "E40216"));
				}
			}
		}
	}

	public static void businessRule(Account account, Consumer consumer, Map<String, Object> validationMap, List<String > errWarMessagesList) {
		if((errWarMessagesList.contains("E70201") || errWarMessagesList.contains("W70201"))) {
			if(!CommonUtils.isStringNullOrBlank(consumer.getFirstName()) && consumer.getFirstName().length() < 2) {
				if(errWarMessagesList.contains("E70201")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70201");
					consumer.addErrWarJson(new ErrWarJson("e", "E70201"));
				} else if(errWarMessagesList.contains("W70201")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70201"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70201")) {
						account.setErrShortName("E70201");
						account.addErrWarJson(new ErrWarJson("e", "E70201"));
					} else if(errWarMessagesList.contains("W70201")) {
						account.addErrWarJson(new ErrWarJson("w", "E70201"));
					}
				}
			}
			if(!CommonUtils.isStringNullOrBlank(consumer.getLastName()) && consumer.getLastName().length() < 2) {
				if(errWarMessagesList.contains("E70201")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70201");
					consumer.addErrWarJson(new ErrWarJson("e", "E70201"));
				} else if(errWarMessagesList.contains("W70201")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70201"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70201")) {
						account.setErrShortName("E70201");
						account.addErrWarJson(new ErrWarJson("e", "E70201"));
					} else if(errWarMessagesList.contains("W70201")) {
						account.addErrWarJson(new ErrWarJson("w", "E70201"));
					}
				}
			}
		}
		if((errWarMessagesList.contains("E70210") || errWarMessagesList.contains("W70210")) && !CommonUtils.isStringNullOrBlank(consumer.getFirstName()) && !CommonUtils.containsChars(consumer.getFirstName(), "^[ a-zA-Z-.]*$")) {
			if(errWarMessagesList.contains("E70210")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70210");
				consumer.addErrWarJson(new ErrWarJson("e", "E70210"));
			} else if(errWarMessagesList.contains("W70210")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70210"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70210")) {
					account.setErrShortName("E70210");
					account.addErrWarJson(new ErrWarJson("e", "E70210"));
				} else if(errWarMessagesList.contains("W70210")) {
					account.addErrWarJson(new ErrWarJson("w", "E70210"));
				}
			}
		}
		if((errWarMessagesList.contains("E70211") || errWarMessagesList.contains("W70211")) && !CommonUtils.isStringNullOrBlank(consumer.getMiddleName()) && !CommonUtils.containsChars(consumer.getMiddleName(), "^[ a-zA-Z-.]*$")) {
			if(errWarMessagesList.contains("E70211")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70211");
				consumer.addErrWarJson(new ErrWarJson("e", "E70211"));
			} else if(errWarMessagesList.contains("W70211")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70211"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70211")) {
					account.setErrShortName("E70211");
					account.addErrWarJson(new ErrWarJson("e", "E70211"));
				} else if(errWarMessagesList.contains("W70211")) {
					account.addErrWarJson(new ErrWarJson("w", "E70211"));
				}
			}
		}
		if((errWarMessagesList.contains("E70212") || errWarMessagesList.contains("W70212")) && !CommonUtils.isStringNullOrBlank(consumer.getLastName()) && !CommonUtils.containsChars(consumer.getLastName(), "^[ a-zA-Z-.'’]*$")) {
			if(errWarMessagesList.contains("E70212")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70212");
				consumer.addErrWarJson(new ErrWarJson("e", "E70212"));
			} else if(errWarMessagesList.contains("W70212")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70212"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70212")) {
					account.setErrShortName("E70212");
					account.addErrWarJson(new ErrWarJson("e", "E70212"));
				} else if(errWarMessagesList.contains("W70212")) {
					account.addErrWarJson(new ErrWarJson("w", "E70212"));
				}
			}
		}
		if((errWarMessagesList.contains("E70210") || errWarMessagesList.contains("W70210")) && !CommonUtils.isStringNullOrBlank(consumer.getFirstName())
				&& CommonUtils.isStringNullOrBlank(CommonUtils.replaceCharsWithEmptyChar(Arrays.asList("'", "’", "."), consumer.getFirstName()).trim())) {
			if(errWarMessagesList.contains("E70210")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70210");
				consumer.addErrWarJson(new ErrWarJson("e", "E70210"));
			} else if(errWarMessagesList.contains("W70210")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70210"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70210")) {
					account.setErrShortName("E70210");
					account.addErrWarJson(new ErrWarJson("e", "E70210"));
				} else if(errWarMessagesList.contains("W70210")) {
					account.addErrWarJson(new ErrWarJson("w", "E70210"));
				}
			}
		}
		if((errWarMessagesList.contains("E70211") || errWarMessagesList.contains("W70211")) && !CommonUtils.isStringNullOrBlank(consumer.getMiddleName())
				&& CommonUtils.isStringNullOrBlank(CommonUtils.replaceCharsWithEmptyChar(Arrays.asList("'", "’", "."), consumer.getMiddleName()).trim())) {
			if(errWarMessagesList.contains("E70211")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70211");
				consumer.addErrWarJson(new ErrWarJson("e", "E70211"));
			} else if(errWarMessagesList.contains("W70211")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70211"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70211")) {
					account.setErrShortName("E70211");
					account.addErrWarJson(new ErrWarJson("e", "E70211"));
				} else if(errWarMessagesList.contains("W70211")) {
					account.addErrWarJson(new ErrWarJson("w", "E70211"));
				}
			}
		}
		if((errWarMessagesList.contains("E70212") || errWarMessagesList.contains("W70212")) && !CommonUtils.isStringNullOrBlank(consumer.getLastName())
				&& CommonUtils.isStringNullOrBlank(CommonUtils.replaceCharsWithEmptyChar(Arrays.asList("'", "’", "."), consumer.getLastName()).trim())) {
			if(errWarMessagesList.contains("E70212")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70212");
				consumer.addErrWarJson(new ErrWarJson("e", "E70212"));
			} else if(errWarMessagesList.contains("W70212")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70212"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70212")) {
					account.setErrShortName("E70212");
					account.addErrWarJson(new ErrWarJson("e", "E70212"));
				} else if(errWarMessagesList.contains("W70212")) {
					account.addErrWarJson(new ErrWarJson("w", "E70212"));
				}
			}
		}
		if(errWarMessagesList.contains("E70204") || errWarMessagesList.contains("W70204")) {
			if(!CommonUtils.isStringNullOrBlank(consumer.getFirstName()) && !CommonUtils.isStringNullOrBlank(consumer.getMiddleName()) 
					&& (consumer.getFirstName().equalsIgnoreCase(consumer.getMiddleName()))) {
				if(errWarMessagesList.contains("E70204")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70204");
					consumer.addErrWarJson(new ErrWarJson("e", "E70204"));
				} else if(errWarMessagesList.contains("W70204")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70204"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70204")) {
						account.setErrShortName("E70204");
						account.addErrWarJson(new ErrWarJson("e", "E70204"));
					} else if(errWarMessagesList.contains("W70204")) {
						account.addErrWarJson(new ErrWarJson("w", "E70204"));
					}
				}
			}
			if(!CommonUtils.isStringNullOrBlank(consumer.getFirstName()) && !CommonUtils.isStringNullOrBlank(consumer.getLastName()) 
					&& (consumer.getFirstName().equalsIgnoreCase(consumer.getLastName()))) {
				if(errWarMessagesList.contains("E70204")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70204");
					consumer.addErrWarJson(new ErrWarJson("e", "E70204"));
				} else if(errWarMessagesList.contains("W70204")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70204"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70204")) {
						account.setErrShortName("E70204");
						account.addErrWarJson(new ErrWarJson("e", "E70204"));
					} else if(errWarMessagesList.contains("W70204")) {
						account.addErrWarJson(new ErrWarJson("w", "E70204"));
					}
				}
			}
			if(!CommonUtils.isStringNullOrBlank(consumer.getMiddleName()) && !CommonUtils.isStringNullOrBlank(consumer.getLastName()) 
					&& (consumer.getMiddleName().equalsIgnoreCase(consumer.getLastName()))) {
				if(errWarMessagesList.contains("E70204")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70204");
					consumer.addErrWarJson(new ErrWarJson("e", "E70204"));
				} else if(errWarMessagesList.contains("W70204")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70204"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70204")) {
						account.setErrShortName("E70204");
						account.addErrWarJson(new ErrWarJson("e", "E70204"));
					} else if(errWarMessagesList.contains("W70204")) {
						account.addErrWarJson(new ErrWarJson("w", "E70204"));
					}
				}
			}
		}
		if((errWarMessagesList.contains("E70205") || errWarMessagesList.contains("W70205")) && !CommonUtils.isDateNull(consumer.getDob()) && ChronoUnit.YEARS.between(consumer.getDob(), LocalDate.now()) > 100) {
			if(errWarMessagesList.contains("E70205")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70205");
				consumer.addErrWarJson(new ErrWarJson("e", "E70205"));
			} else if(errWarMessagesList.contains("W70205")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70205"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70205")) {
					account.setErrShortName("E70205");
					account.addErrWarJson(new ErrWarJson("e", "E70205"));
				} else if(errWarMessagesList.contains("W70205")) {
					account.addErrWarJson(new ErrWarJson("w", "E70205"));
				}
			}
		}
		if((errWarMessagesList.contains("E70206") || errWarMessagesList.contains("W70206")) && !CommonUtils.isDateNull(consumer.getDob()) && !CommonUtils.isDateNull(account.getOriginalAccountOpenDate())
				&& ChronoUnit.YEARS.between(consumer.getDob(), account.getOriginalAccountOpenDate()) < 18) {
			if(errWarMessagesList.contains("E70206")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70206");
				consumer.addErrWarJson(new ErrWarJson("e", "E70206"));
			} else if(errWarMessagesList.contains("W70206")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70206"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70206")) {
					account.setErrShortName("E70206");
					account.addErrWarJson(new ErrWarJson("e", "E70206"));
				} else if(errWarMessagesList.contains("W70206")) {
					account.addErrWarJson(new ErrWarJson("w", "E70206"));
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(consumer.getIdentificationNumber())) {
			if((errWarMessagesList.contains("E70207") || errWarMessagesList.contains("W70207")) && consumer.getIdentificationNumber().trim().length() != 9) {
				if(errWarMessagesList.contains("E70207")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70207");
					consumer.addErrWarJson(new ErrWarJson("e", "E70207"));
				} else if(errWarMessagesList.contains("W70207")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70207"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70207")) {
						account.setErrShortName("E70207");
						account.addErrWarJson(new ErrWarJson("e", "E70207"));
					} else if(errWarMessagesList.contains("W70207")) {
						account.addErrWarJson(new ErrWarJson("w", "E70207"));
					}
				}
			}
			if((errWarMessagesList.contains("E70208") || errWarMessagesList.contains("W70208")) && !CommonUtils.onlyDigits(consumer.getIdentificationNumber())) {
				if(errWarMessagesList.contains("E70208")) {
					validationMap.put("isConsumerValidated", false);
					consumer.setErrShortName("E70208");
					consumer.addErrWarJson(new ErrWarJson("e", "E70208"));
				} else if(errWarMessagesList.contains("W70208")) {
					consumer.addErrWarJson(new ErrWarJson("w", "E70208"));
				}
				if(consumer.isEntityPrimary()) {
					if(errWarMessagesList.contains("E70208")) {
						account.setErrShortName("E70208");
						account.addErrWarJson(new ErrWarJson("e", "E70208"));
					} else if(errWarMessagesList.contains("W70208")) {
						account.addErrWarJson(new ErrWarJson("w", "E70208"));
					}
				}
			}
			if((errWarMessagesList.contains("E70209") || errWarMessagesList.contains("W70209")) && consumer.getIdentificationNumber().trim().length() >= 6) {
				String ssn0 = consumer.getIdentificationNumber().trim().substring(0,1);
				String ssn1 = consumer.getIdentificationNumber().trim().substring(0,3);
				String ssn2 = consumer.getIdentificationNumber().trim().substring(3,5);
				String ssn3 = consumer.getIdentificationNumber().trim().substring(5);
				if((!CommonUtils.isStringNullOrBlank(ssn1) && !CommonUtils.isStringNullOrBlank(ssn2) && !CommonUtils.isStringNullOrBlank(ssn3) 
						&& (ssn0.equalsIgnoreCase("9") || ssn1.equalsIgnoreCase("000") || ssn1.equalsIgnoreCase("666") || ssn2.equalsIgnoreCase("00") || ssn3.equalsIgnoreCase("0000"))) 
						|| CommonUtils.isSingleInteger(consumer.getIdentificationNumber()) || CommonUtils.isConsecutiveNumber(consumer.getIdentificationNumber())) {
					if(errWarMessagesList.contains("E70209")) {
						validationMap.put("isConsumerValidated", false);
						consumer.setErrShortName("E70209");
						consumer.addErrWarJson(new ErrWarJson("e", "E70209"));
					} else if(errWarMessagesList.contains("W70209")) {
						consumer.addErrWarJson(new ErrWarJson("w", "E70209"));
					}
					if(consumer.isEntityPrimary()) {
						if(errWarMessagesList.contains("E70209")) {
							account.setErrShortName("E70209");
							account.addErrWarJson(new ErrWarJson("e", "E70209"));
						} else if(errWarMessagesList.contains("W70209")) {
							account.addErrWarJson(new ErrWarJson("w", "E70209"));
						}
					}
				} 
			}
		}
		if((errWarMessagesList.contains("E70481") || errWarMessagesList.contains("W70481")) && !CommonUtils.isStringNullOrBlank(consumer.getDriverLicenseNumber()) && !CommonUtils.containsChars(consumer.getDriverLicenseNumber(), "^[ a-zA-Z0-9-]*$")) {
			if(errWarMessagesList.contains("E70481")) {
				validationMap.put("isConsumerValidated", false);
				consumer.setErrShortName("E70481");
				consumer.addErrWarJson(new ErrWarJson("e", "E70481"));
			} else if(errWarMessagesList.contains("W70481")) {
				consumer.addErrWarJson(new ErrWarJson("w", "E70481"));
			}
			if(consumer.isEntityPrimary()) {
				if(errWarMessagesList.contains("E70481")) {
					account.setErrShortName("E70481");
					account.addErrWarJson(new ErrWarJson("e", "E70481"));
				} else if(errWarMessagesList.contains("W70481")) {
					account.addErrWarJson(new ErrWarJson("w", "E70481"));
				}
			}
		}
	}
}