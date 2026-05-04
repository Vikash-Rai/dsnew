package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ChainOfTitle;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class ChainOfTitleValidation {

	public static void mandatoryValidation(ChainOfTitle chainOfTitle, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E11806") || errWarMessagesList.contains("W11806")) && CommonUtils.isStringNullOrBlank(chainOfTitle.getRecordType())) {
			if(errWarMessagesList.contains("E11806")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E11806"));
			} else if(errWarMessagesList.contains("W11806")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E11806"));
			}
		}
		if((errWarMessagesList.contains("E11802") || errWarMessagesList.contains("W11802")) && CommonUtils.isStringNullOrBlank(chainOfTitle.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E11802")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E11802"));
			} else if(errWarMessagesList.contains("W11802")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E11802"));
			}
		}
		if((errWarMessagesList.contains("E11801") || errWarMessagesList.contains("W11801")) && CommonUtils.isIntegerNullOrZero(chainOfTitle.getClientId())) {
			if(errWarMessagesList.contains("E11801")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E11801"));
			} else if(errWarMessagesList.contains("W11801")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E11801"));
			}
		}
		if((errWarMessagesList.contains("E11811") || errWarMessagesList.contains("W11811"))&& CommonUtils.isDateNull(chainOfTitle.getDtStart())) {
			if(errWarMessagesList.contains("E11811")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E11811"));
			} else if(errWarMessagesList.contains("W11811")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E11811"));
			}
		}
		if((errWarMessagesList.contains("E11809") || errWarMessagesList.contains("W11809")) && CommonUtils.isStringNullOrBlank(chainOfTitle.getCotType())) {
			if(errWarMessagesList.contains("E11809")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E11809"));
			} else if(errWarMessagesList.contains("W11809")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E11809"));
			}
		}
	}

	public static void lookUpValidation(ChainOfTitle chainOfTitle, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E21801") || errWarMessagesList.contains("W21801")) && !CommonUtils.isIntegerNullOrZero(chainOfTitle.getClientId()) && chainOfTitle.getClient() == null) {
			if(errWarMessagesList.contains("E21801")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E21801"));
			} else if(errWarMessagesList.contains("W21801")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E21801"));
			}
		}
		if((errWarMessagesList.contains("E21809") || errWarMessagesList.contains("W21809")) && !CommonUtils.isStringNullOrBlank(chainOfTitle.getCotType()) && chainOfTitle.getOwnerTypes() == null) {
			if(errWarMessagesList.contains("E21809")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E21809"));
			} else if(errWarMessagesList.contains("W21809")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E21809"));
			}
		}
	}

	public static void standardize(ChainOfTitle chainOfTitle) {
		if(!CommonUtils.isStringNullOrBlank(chainOfTitle.getRecordType())) {
			chainOfTitle.setRecordType(chainOfTitle.getRecordType().toLowerCase());
		}
		if(!CommonUtils.isStringNullOrBlank(chainOfTitle.getClientAccountNumber())) {
			chainOfTitle.setClientAccountNumber(chainOfTitle.getClientAccountNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(ChainOfTitle chainOfTitle) {
		if(chainOfTitle.getAccounts() != null && !CommonUtils.isLongNull(chainOfTitle.getAccounts().getAccountId())) {
			chainOfTitle.setAccountId(chainOfTitle.getAccounts().getAccountId());
		}
		if(chainOfTitle.getClient() != null && chainOfTitle.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
			if(chainOfTitle.getDtEnd()==null && chainOfTitle.getDtStartAfter()!=null) {
				chainOfTitle.setDtEnd(chainOfTitle.getDtStartAfter().minusDays(1));
			}
			if(chainOfTitle.getSubpoolCotOwner() != null && !CommonUtils.isLongNull(chainOfTitle.getSubpoolCotOwner().getCotOwnerId())) {
				chainOfTitle.setOwnerId(chainOfTitle.getSubpoolCotOwner().getCotOwnerId());
			}
		} else {
			if(chainOfTitle.getCotOwner() != null && !CommonUtils.isLongNull(chainOfTitle.getCotOwner().getCotOwnerId())) {
				chainOfTitle.setOwnerId(Long.valueOf(chainOfTitle.getCotOwner().getCotOwnerId()));
			}
		}
	}

	public static void misingRefCheck(ChainOfTitle chainOfTitle, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E41816") || errWarMessagesList.contains("W41816")) && !CommonUtils.isStringNullOrBlank(chainOfTitle.getClientAccountNumber()) && CommonUtils.isLongNull(chainOfTitle.getAccountId())) {
			if(errWarMessagesList.contains("E41816")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E41816"));
			} else if(errWarMessagesList.contains("W41816")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E41816"));
			}
		}
		if(chainOfTitle.getClient() != null && chainOfTitle.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
			if((errWarMessagesList.contains("E41812") || errWarMessagesList.contains("W41812")) && chainOfTitle.getSubpoolCotOwner() != null
					&& !CommonUtils.isLongNull(chainOfTitle.getSubpoolCotOwner().getCotOwnerId()) && CommonUtils.isLongNull(chainOfTitle.getOwnerId())) {
				if(errWarMessagesList.contains("E41812")) {
					validationMap.put("isChainOfTitleValidated", false);
					chainOfTitle.addErrWarJson(new ErrWarJson("e", "E41812"));
				} else if(errWarMessagesList.contains("W41812")) {
					chainOfTitle.addErrWarJson(new ErrWarJson("w", "E41812"));
				}
			}
		} else {
			if((errWarMessagesList.contains("E41812") || errWarMessagesList.contains("W41812")) && !CommonUtils.isLongNull(chainOfTitle.getOwnerId())
					&& (chainOfTitle.getCotOwner() == null || chainOfTitle.getCotOwner().getCotOwnerId() == null || chainOfTitle.getCotOwner().getCotOwnerId() == 0)) {
				if(errWarMessagesList.contains("E41812")) {
					validationMap.put("isChainOfTitleValidated", false);
					chainOfTitle.addErrWarJson(new ErrWarJson("e", "E41812"));
				} else if(errWarMessagesList.contains("W41812")) {
					chainOfTitle.addErrWarJson(new ErrWarJson("w", "E41812"));
				}
			}
		}
		if((errWarMessagesList.contains("E41812") || errWarMessagesList.contains("W41812")) && CommonUtils.isLongNull(chainOfTitle.getOwnerId())) {
			if(errWarMessagesList.contains("E41812")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E41812"));
			} else if(errWarMessagesList.contains("W41812")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E41812"));
			}
		}
	}

	public static void businessRule(ChainOfTitle chainOfTitle, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E71806") || errWarMessagesList.contains("W71806")) && chainOfTitle.getDtStartAfter() == null && !CommonUtils.isDateNull(chainOfTitle.getDtEnd())) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71806")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71806"));
			} else if(errWarMessagesList.contains("W71806")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71806"));
			}
		}
		if((errWarMessagesList.contains("E71807") || errWarMessagesList.contains("W71807")) && chainOfTitle.getDtStartAfter() != null && CommonUtils.isDateNull(chainOfTitle.getDtEnd())) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71807")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71807"));
			} else if(errWarMessagesList.contains("W71807")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71807"));
			}
		}
		if((errWarMessagesList.contains("E71801") || errWarMessagesList.contains("W71801")) && !CommonUtils.isIntegerNullOrZero(chainOfTitle.getDtEndPreviousCount()) && chainOfTitle.getDtEndPreviousCount() > 0 && chainOfTitle.getDtEndPrevious() == null) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71801")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71801"));
			} else if(errWarMessagesList.contains("W71801")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71801"));
			}
		}
		if((errWarMessagesList.contains("E71808") || errWarMessagesList.contains("W71808")) && chainOfTitle.getDtEndPrevious() != null && !chainOfTitle.getDtEndPrevious().plusDays(1).isEqual(chainOfTitle.getDtStart())) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71808")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71808"));
			} else if(errWarMessagesList.contains("W71808")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71808"));
			}
		}
		if((errWarMessagesList.contains("E71802") || errWarMessagesList.contains("W71802")) && !CommonUtils.isDateNull(chainOfTitle.getDtStart()) && !CommonUtils.isDateNull(chainOfTitle.getDtEnd())
				&& ((chainOfTitle.getDtStart().isEqual(chainOfTitle.getDtEnd())) || (chainOfTitle.getDtStart().isAfter(chainOfTitle.getDtEnd())))) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71802")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71802"));
			} else if(errWarMessagesList.contains("W71802")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71802"));
			}
		}
		if((errWarMessagesList.contains("E71803") || errWarMessagesList.contains("W71803")) && !CommonUtils.isDateNull(chainOfTitle.getDtStartAfter()) && !CommonUtils.isDateNull(chainOfTitle.getDtEnd())
				&& ((chainOfTitle.getDtEnd().isEqual(chainOfTitle.getDtStartAfter())) || (chainOfTitle.getDtEnd().isAfter(chainOfTitle.getDtStartAfter())))) {
			chainOfTitle.setCotStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
			if(errWarMessagesList.contains("E71803")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71803"));
			} else if(errWarMessagesList.contains("W71803")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71803"));
			}
		}
		if((errWarMessagesList.contains("E71804") || errWarMessagesList.contains("W71804")) && !CommonUtils.isIntegerNullOrZero(chainOfTitle.getDtFromCount()) && chainOfTitle.getDtFromCount() > 0) {
			if(errWarMessagesList.contains("E71804")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71804"));
			} else if(errWarMessagesList.contains("W71804")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71804"));
			}
		}
		if((errWarMessagesList.contains("E71805") || errWarMessagesList.contains("W71805")) && !CommonUtils.isIntegerNullOrZero(chainOfTitle.getDtTillCount()) && chainOfTitle.getDtTillCount() > 0) {
			if(errWarMessagesList.contains("E71805")) {
				validationMap.put("isChainOfTitleValidated", false);
				chainOfTitle.addErrWarJson(new ErrWarJson("e", "E71805"));
			} else if(errWarMessagesList.contains("W71805")) {
				chainOfTitle.addErrWarJson(new ErrWarJson("w", "E71805"));
			}
		}
	}
}