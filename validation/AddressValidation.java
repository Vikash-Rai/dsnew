package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Address;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.LookUp;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class AddressValidation {

	public static void mandatoryValidation(Account account, Consumer consumer, Address address, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10401") || errWarMessagesList.contains("W10401")) && CommonUtils.isIntegerNullOrZero(address.getClientId())) {
			if(errWarMessagesList.contains("E10401")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E10401");
				address.addErrWarJson(new ErrWarJson("e", "E10401"));
				account.addErrWarJson(new ErrWarJson("e", "E10401"));
			} else if(errWarMessagesList.contains("W10401")) {
				address.addErrWarJson(new ErrWarJson("w", "E10401"));
				account.addErrWarJson(new ErrWarJson("w", "E10401"));
			}
		}
		if((errWarMessagesList.contains("E10402") || errWarMessagesList.contains("W10402")) && CommonUtils.isStringNullOrBlank(address.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10402")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E10402");
				address.addErrWarJson(new ErrWarJson("e", "E10402"));
				account.addErrWarJson(new ErrWarJson("e", "E10402"));
			} else if(errWarMessagesList.contains("W10402")) {
				address.addErrWarJson(new ErrWarJson("w", "E10402"));
				account.addErrWarJson(new ErrWarJson("w", "E10402"));
			}
		}
		if((errWarMessagesList.contains("E10418") || errWarMessagesList.contains("W10418")) && CommonUtils.isLongNull(address.getClientConsumerNumber())) {
			if(errWarMessagesList.contains("E10418")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E10418");
				address.addErrWarJson(new ErrWarJson("e", "E10418"));
				account.addErrWarJson(new ErrWarJson("e", "E10418"));
			} else if(errWarMessagesList.contains("W10418")) {
				address.addErrWarJson(new ErrWarJson("w", "E10418"));
				account.addErrWarJson(new ErrWarJson("w", "E10418"));
			}
		}
		if((errWarMessagesList.contains("E10433") || errWarMessagesList.contains("W10433")) && CommonUtils.isStringNullOrBlank(address.getAddress1())) {
			if(errWarMessagesList.contains("E10433")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E10433");
				address.addErrWarJson(new ErrWarJson("e", "E10433"));
				account.addErrWarJson(new ErrWarJson("e", "E10433"));
			} else if(errWarMessagesList.contains("W10433")) {
				address.addErrWarJson(new ErrWarJson("w", "E10433"));
				account.addErrWarJson(new ErrWarJson("w", "E10433"));
			}
		}
	}

	public static void lookUpValidation(Account account, Consumer consumer, Address address, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20401") || errWarMessagesList.contains("W20401")) && !CommonUtils.isIntegerNullOrZero(address.getClientId()) && address.getClient() == null) {
			if(errWarMessagesList.contains("E20401")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E20401");
				address.addErrWarJson(new ErrWarJson("e", "E20401"));
				account.addErrWarJson(new ErrWarJson("e", "E20401"));
			} else if(errWarMessagesList.contains("W20401")) {
				address.addErrWarJson(new ErrWarJson("w", "E20401"));
				account.addErrWarJson(new ErrWarJson("w", "E20401"));
			}
		}
		if((errWarMessagesList.contains("E20437") || errWarMessagesList.contains("W20437")) && !CommonUtils.isStringNullOrBlank(address.getStateCode()) && address.getCountryState() == null) {
			if(errWarMessagesList.contains("E20437")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E20437");
				address.addErrWarJson(new ErrWarJson("e", "E20437"));
				account.addErrWarJson(new ErrWarJson("e", "E20437"));
			} else if(errWarMessagesList.contains("W20437")) {
				address.addErrWarJson(new ErrWarJson("w", "E20437"));
				account.addErrWarJson(new ErrWarJson("w", "E20437"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(address.getZip()) && ((address.getZip().length() != 5 && address.getZip().length() != 9) || 
				(address.getZip().length() == 5 && address.getCountryZip() == null) ||
				(address.getZip().length() == 9 && address.getCountryZip() == null) )) {
			
			address.setZip(null);
		}
		if((errWarMessagesList.contains("E20440") || errWarMessagesList.contains("W20440")) && !CommonUtils.isStringNullOrBlank(address.getAddressStatus()) && address.getAddressStatusLookUp() == null) {
			if(errWarMessagesList.contains("E20440")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E20440");
				address.addErrWarJson(new ErrWarJson("e", "E20440"));
				account.addErrWarJson(new ErrWarJson("e", "E20440"));
			} else if(errWarMessagesList.contains("W20440")) {
				address.addErrWarJson(new ErrWarJson("w", "E20440"));
				account.addErrWarJson(new ErrWarJson("w", "E20440"));
			}
		}
		if((errWarMessagesList.contains("E20441") || errWarMessagesList.contains("W20441")) && !CommonUtils.isStringNullOrBlank(address.getAddressType()) && address.getAddressTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20441")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E20441");
				address.addErrWarJson(new ErrWarJson("e", "E20441"));
				account.addErrWarJson(new ErrWarJson("e", "E20441"));
			} else if(errWarMessagesList.contains("W20441")) {
				address.addErrWarJson(new ErrWarJson("w", "E20441"));
				account.addErrWarJson(new ErrWarJson("w", "E20441"));
			}
		}
	}

	public static void standardize(Account account, Address address, Consumer consumer, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isStringNullOrBlank(address.getClientAccountNumber())) {
			address.setClientAccountNumber(address.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(address.getStateCode())) {
			address.setStateCode(address.getStateCode().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(address.getCountry())) {
			address.setCountry(address.getCountry().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(address.getAddressStatus())) {
			address.setAddressStatus(address.getAddressStatus().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(address.getAddressType())) {
			address.setAddressType(address.getAddressType().toUpperCase());
		}
		if((errWarMessagesList.contains("E10433") || errWarMessagesList.contains("W10433")) && !CommonUtils.isStringNullOrBlank(address.getAddress1())) {
			String address1 = LookUp.replaceCharsByKeyCode(cacheableService.lookUpByGroupKeyValue("addess_exclude_char"), address.getAddress1());
			address.setAddress1(address1);

			if(CommonUtils.isStringNullOrBlank(address1.trim())) {
				if(errWarMessagesList.contains("E10433")) {
					validationMap.put("isAddressValidated", false);
					address.setErrShortName("E10433");
					address.addErrWarJson(new ErrWarJson("e", "E10433"));
					account.addErrWarJson(new ErrWarJson("e", "E10433"));
				} else if(errWarMessagesList.contains("W10433")) {
					address.addErrWarJson(new ErrWarJson("w", "E10433"));
					account.addErrWarJson(new ErrWarJson("w", "E10433"));
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(address.getAddress2())) {
			address.setAddress2(LookUp.replaceCharsByKeyCode(cacheableService.lookUpByGroupKeyValue("addess_exclude_char"), address.getAddress2()));
		}
		if(!CommonUtils.isStringNullOrBlank(address.getAddress3())) {
			address.setAddress3(LookUp.replaceCharsByKeyCode(cacheableService.lookUpByGroupKeyValue("addess_exclude_char"), address.getAddress3()));
		}
	}

	public static void deDupValidation(Account account, Address address, Consumer consumer, Map<String, Object> validationMap) {
		if(address.getIsPrimary() != null && address.getIsPrimary() && address.getIsPrimaryDeDup() > 1) {
			if(consumer.getAddressId() == null && address.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId())) {
				consumer.setAddressId(address.getAddressId());
			} else {
				address.setIsPrimary(false);
			}
		}
	}

	public static void referenceUpdation(Account account, Consumer consumer, Address address, Integer addressCount) {
		if(!CommonUtils.isLongNull(account.getAccountId())) {
			address.setAccountId(account.getAccountId());
		}
		if(!CommonUtils.isLongNull(consumer.getConsumerId())) {
			address.setConsumerId(consumer.getConsumerId());
		}

		if(CommonUtils.isBooleanNull(address.getIsPrimary())) {
			if(addressCount == 1) {
				address.setIsPrimary(true);
			} else {
				address.setIsPrimary(false);
			}
		}
	}

	public static void misingRefCheck(Account account, Consumer consumer, Address address, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E40416") || errWarMessagesList.contains("W40416")) && CommonUtils.isLongNull(address.getAccountId())) {
			if(errWarMessagesList.contains("E40416")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E40416");
				address.addErrWarJson(new ErrWarJson("e", "E40416"));
				account.addErrWarJson(new ErrWarJson("e", "E40416"));
			} else if(errWarMessagesList.contains("W40416")) {
				address.addErrWarJson(new ErrWarJson("w", "E40416"));
				account.addErrWarJson(new ErrWarJson("w", "E40416"));
			}
		}
		if((errWarMessagesList.contains("E40427") || errWarMessagesList.contains("W40427")) && CommonUtils.isLongNull(address.getConsumerId())) {
			if(errWarMessagesList.contains("E40427")) {
				validationMap.put("isAddressValidated", false);
				address.setErrShortName("E40427");
				address.addErrWarJson(new ErrWarJson("e", "E40427"));
				account.addErrWarJson(new ErrWarJson("e", "E40427"));
			} else if(errWarMessagesList.contains("W40427")) {
				address.addErrWarJson(new ErrWarJson("w", "E40427"));
				account.addErrWarJson(new ErrWarJson("w", "E40427"));
			}
		}
	}

	public static void businessRule(Address address, Map<String, Object> validationMap) {
		
		if((!CommonUtils.isStringNullOrBlank(address.getAddress1()) && !CommonUtils.isStringNullOrBlank(address.getCity()) && !CommonUtils.isStringNullOrBlank(address.getStateCode())) 
				|| (!CommonUtils.isStringNullOrBlank(address.getAddress1()) && !CommonUtils.isStringNullOrBlank(address.getZip()) && address.getCountryZip() != null)) {
			address.setAddressStatus(CommonConstants.ADDRESS_STATUS_ACTIVE_CURRENT);
		} else {
			address.setAddressStatus(CommonConstants.ADDRESS_STATUS_BAD_INVALID);
		}
		
		 
	}
}