package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Lien;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class LienValidation {

	public static void mandatoryValidation(Lien lien, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(lien.getRecordType())) {
            if(errWarMessagesList.contains("E12006")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12006"));
            } else if(errWarMessagesList.contains("W12006")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12006"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(lien.getClientAccountNumber())) {
            if(errWarMessagesList.contains("E12002")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12002"));
            } else if(errWarMessagesList.contains("W12002")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12002"));
            }
        }
        if (CommonUtils.isIntegerNullOrZero(lien.getClientId())) {
            if(errWarMessagesList.contains("E12001")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12001"));
            } else if(errWarMessagesList.contains("W12001")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12001"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(lien.getCaseNumber())) {
            if(errWarMessagesList.contains("E12003")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12003"));
            } else if(errWarMessagesList.contains("W12003")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12003"));
            }
        }
        if (CommonUtils.isDateNull(lien.getDtLienPlaced())) {
            if(errWarMessagesList.contains("E12004")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12004"));
            } else if(errWarMessagesList.contains("W12004")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12004"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(lien.getLienType())) {
            if(errWarMessagesList.contains("E12005")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12005"));
            } else if(errWarMessagesList.contains("W12005")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12005"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(lien.getLienAsset())) {
            if(errWarMessagesList.contains("E12007")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12007"));
            } else if(errWarMessagesList.contains("W12007")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12007"));
            }
        }
        if (CommonUtils.isDoubleNull(lien.getAmtLien())) {
            if(errWarMessagesList.contains("E12008")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12008"));
            } else if(errWarMessagesList.contains("W12008")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12008"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(lien.getLienStatus())) {
            if(errWarMessagesList.contains("E12009")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E12009"));
            } else if(errWarMessagesList.contains("W12009")) {
                lien.addErrWarJson(new ErrWarJson("w", "E12009"));
            }
        }
    }

    public static void lookUpValidation(Lien lien, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (!CommonUtils.isIntegerNullOrZero(lien.getClientId()) && lien.getClient() == null) {
            if(errWarMessagesList.contains("E22001")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E22001"));
            } else if(errWarMessagesList.contains("W22001")) {
                lien.addErrWarJson(new ErrWarJson("w", "E22001"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(lien.getLienType()) && lien.getLienTypeLookUp() == null) {
            if(errWarMessagesList.contains("E22002")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E22002"));
            } else if(errWarMessagesList.contains("W22002")) {
                lien.addErrWarJson(new ErrWarJson("w", "E22002"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(lien.getLienStatus()) && lien.getLienStatusLookUp() == null) {
            if(errWarMessagesList.contains("E22003")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E22003"));
            } else if(errWarMessagesList.contains("W22003")) {
                lien.addErrWarJson(new ErrWarJson("w", "E22003"));
            }
        }
    }

    public static void standardize(Lien lien) {
        if (!CommonUtils.isStringNullOrBlank(lien.getClientAccountNumber())) {
            lien.setClientAccountNumber(lien.getClientAccountNumber().toUpperCase());
        }
		if(!CommonUtils.isStringNullOrBlank(lien.getCaseNumber())) {
			lien.setCaseNumber(lien.getCaseNumber().toUpperCase());
		}
    }

    public static void referenceUpdation(Lien lien) {
        if (lien.getAccountIds() != null && !CommonUtils.isLongNull(lien.getAccountIds())) {
            lien.setAccountId(lien.getAccountIds());
        }
        if (lien.getConsumerIds() != null && !CommonUtils.isLongNull(lien.getConsumerIds())) {
            lien.setConsumerId(lien.getConsumerIds());
        }
        if (lien.getLegalPlacementIds() != null && !CommonUtils.isLongNull(lien.getLegalPlacementIds())) {
            lien.setLegalPlacementId(lien.getLegalPlacementIds());
        }
    }

    public static void misingRefCheck(Lien lien, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (CommonUtils.isLongNull(lien.getAccountId())) {
            if(errWarMessagesList.contains("E42001")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E42001"));
            } else if(errWarMessagesList.contains("W42001")) {
                lien.addErrWarJson(new ErrWarJson("w", "E42001"));
            }
        }
        if (CommonUtils.isLongNull(lien.getConsumerId())) {
            if(errWarMessagesList.contains("E42002")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E42002"));
            } else if(errWarMessagesList.contains("W42002")) {
                lien.addErrWarJson(new ErrWarJson("w", "E42002"));
            }
        }
        if (CommonUtils.isLongNull(lien.getLegalPlacementId())) {
            if(errWarMessagesList.contains("E42003")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E42003"));
            } else if(errWarMessagesList.contains("W42003")) {
                lien.addErrWarJson(new ErrWarJson("w", "E42003"));
            }
        }
    }

    public static void businessRule(Lien lien, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(lien.getRecordType()) && !lien.getRecordType().equalsIgnoreCase("lien")) {
            if(errWarMessagesList.contains("E72006")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E72006"));
            } else if(errWarMessagesList.contains("W72006")) {
                lien.addErrWarJson(new ErrWarJson("w", "E72006"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(lien.getCaseNumber()) && (lien.getCaseNumber().length() < 5 || lien.getCaseNumber().length() > 50)) {
            if(errWarMessagesList.contains("E72001")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E72001"));
            } else if(errWarMessagesList.contains("W72001")) {
                lien.addErrWarJson(new ErrWarJson("w", "E72001"));
            }
        }
        if (!CommonUtils.isDateNull(lien.getDtLienPlaced()) && lien.getDtLienPlaced().isAfter(LocalDate.now())) {
            if(errWarMessagesList.contains("E72002")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E72002"));
            } else if(errWarMessagesList.contains("W72002")) {
                lien.addErrWarJson(new ErrWarJson("w", "E72002"));
            }
        }
        if (!CommonUtils.isDoubleNull(lien.getAmtLien()) && lien.getAmtLien() < 0) {
            if(errWarMessagesList.contains("E72003")) {
                validationMap.put("isLienValidated", false);
                lien.addErrWarJson(new ErrWarJson("e", "E72003"));
            } else if(errWarMessagesList.contains("W72003")) {
                lien.addErrWarJson(new ErrWarJson("w", "E72003"));
            }
        }
    }
}
