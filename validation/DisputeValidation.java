package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Dispute;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class DisputeValidation {

    public static void mandatoryValidation(Dispute dispute, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(dispute.getRecordType())) {
            if (errWarMessagesList.contains("E12506")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12506"));
            } else if (errWarMessagesList.contains("W12506")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12506"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(dispute.getClientAccountNumber())) {
            if (errWarMessagesList.contains("E12502")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12502"));
            } else if (errWarMessagesList.contains("W12502")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12502"));
            }
        }
        if (CommonUtils.isIntegerNullOrZero(dispute.getClientId())) {
            if (errWarMessagesList.contains("E12501")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12501"));
            } else if (errWarMessagesList.contains("W12501")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12501"));
            }
        }
        if (CommonUtils.isDateNull(dispute.getDtDisputeFilling())) {
            if (errWarMessagesList.contains("E12507")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12507"));
            } else if (errWarMessagesList.contains("W12507")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12507"));
            }
        }
        if (CommonUtils.isDateNull(dispute.getDtDisputeReport())) {
            if (errWarMessagesList.contains("E12508")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12508"));
            } else if (errWarMessagesList.contains("W12508")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12508"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(dispute.getDisputeType())) {
            if (errWarMessagesList.contains("E12503")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12503"));
            } else if (errWarMessagesList.contains("W12503")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12503"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(dispute.getDisputeChannel())) {
            if (errWarMessagesList.contains("E12504")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12504"));
            } else if (errWarMessagesList.contains("W12504")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12504"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(dispute.getChannelMode())) {
            if (errWarMessagesList.contains("E12505")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E12505"));
            } else if (errWarMessagesList.contains("W12505")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E12505"));
            }
        }
    }

    public static void lookUpValidation(Dispute dispute, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (!CommonUtils.isIntegerNullOrZero(dispute.getClientId()) && dispute.getClient() == null) {
            if (errWarMessagesList.contains("E22501")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E22501"));
            } else if (errWarMessagesList.contains("W22501")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E22501"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(dispute.getDisputeChannel()) && dispute.getChannelLookUp() == null) {
            if (errWarMessagesList.contains("E22502")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E22502"));
            } else if (errWarMessagesList.contains("W22502")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E22502"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(dispute.getDisputeType()) && dispute.getDisputeTypeLookUp() == null) {
            if (errWarMessagesList.contains("E22503")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E22503"));
            } else if (errWarMessagesList.contains("W22503")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E22503"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(dispute.getChannelMode()) && dispute.getChannelModeLookUp() == null) {
            if (errWarMessagesList.contains("E22504")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E22504"));
            } else if (errWarMessagesList.contains("W22504")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E22504"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(dispute.getDisputeStatus()) && dispute.getDisputeStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22505")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E22505"));
            } else if (errWarMessagesList.contains("W22505")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E22505"));
            }
        }
    }

    public static void standardize(Dispute dispute) {
        if (!CommonUtils.isStringNullOrBlank(dispute.getClientAccountNumber())) {
            dispute.setClientAccountNumber(dispute.getClientAccountNumber().toUpperCase());
        }
    }

    public static void referenceUpdation(Dispute dispute) {
        if (dispute.getAccountIds() != null && !CommonUtils.isLongNull(dispute.getAccountIds())) {
            dispute.setAccountId(dispute.getAccountIds());
        }
        if (dispute.getConsumerIds() != null && !CommonUtils.isLongNull(dispute.getConsumerIds())) {
            dispute.setConsumerId(dispute.getConsumerIds());
        }
    }

    public static void misingRefCheck(Dispute dispute, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (CommonUtils.isLongNull(dispute.getAccountId())) {
            if (errWarMessagesList.contains("E42501")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E42501"));
            } else if (errWarMessagesList.contains("W42501")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E42501"));
            }
        }
        if(!CommonUtils.isLongNull(dispute.getClientConsumerNumber()) && CommonUtils.isLongNull(dispute.getConsumerId())) {
            if(errWarMessagesList.contains("E42502")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E42502"));
            } else if(errWarMessagesList.contains("W42502")) {
            	dispute.addErrWarJson(new ErrWarJson("w", "E42502"));
            }
        }
    }

    public static void businessRule(Dispute dispute, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(dispute.getRecordType()) && !dispute.getRecordType().equalsIgnoreCase("dispute")) {
            if (errWarMessagesList.contains("E72506")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72506"));
            } else if (errWarMessagesList.contains("W72506")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E72506"));
            }
        }
        if (!CommonUtils.isDateNull(dispute.getDtDisputeReport()) && !CommonUtils.isDateNull(dispute.getDtDisputeFilling())
        		&& dispute.getDtDisputeReport().isBefore(dispute.getDtDisputeFilling())) {
            if (errWarMessagesList.contains("E72501")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72501"));
            } else if (errWarMessagesList.contains("W72501")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E72501"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(dispute.getChannelMode()) && CommonConstants.COMPLIANCE_MODE_VERBAL.equalsIgnoreCase(dispute.getChannelMode())
        		&& CommonUtils.isStringNullOrBlank(dispute.getDisputeDescription())) {
            if (errWarMessagesList.contains("E72502")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72502"));
            } else if (errWarMessagesList.contains("W72502")) {
            	dispute.addErrWarJson(new ErrWarJson("w", "E72502"));
            }
        }
        if (!CommonUtils.isDateNull(dispute.getDtDisputeReport()) && !CommonUtils.isDateNull(dispute.getDtFirstSLA())
                && dispute.getDtFirstSLA().compareTo(dispute.getDtDisputeReport()) > dispute.getDtFirstSLADeDup()) {
            if (errWarMessagesList.contains("E72503")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72503"));
            } else if (errWarMessagesList.contains("W72503")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E72503"));
            }
        }
        if (!CommonUtils.isDateNull(dispute.getDtDisputeReport()) && !CommonUtils.isDateNull(dispute.getDtLastSLA())
                && dispute.getDtLastSLA().compareTo(dispute.getDtDisputeReport()) > dispute.getDtLastSLADeDup()) {
            if (errWarMessagesList.contains("E72504")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72504"));
            } else if (errWarMessagesList.contains("W72504")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E72504"));
            }
        }
        if (!CommonUtils.isDateNull(dispute.getDtLastSLA()) && !CommonUtils.isDateNull(dispute.getDtFirstSLA())
                && dispute.getDtLastSLA().isBefore(dispute.getDtFirstSLA())) {
            if (errWarMessagesList.contains("E72505")) {
                validationMap.put("isDisputeValidated", false);
                dispute.addErrWarJson(new ErrWarJson("e", "E72505"));
            } else if (errWarMessagesList.contains("W72505")) {
                dispute.addErrWarJson(new ErrWarJson("w", "E72505"));
            }
        }
    }
}