package com.equabli.collectprism.validation;

import com.equabli.collectprism.entity.Complaint;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import java.util.List;
import java.util.Map;

public class ComplaintValidation {
    public static void mandatoryValidation(Complaint complaint, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(complaint.getRecordType())) {
            if (errWarMessagesList.contains("E12406")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12406"));
            } else if (errWarMessagesList.contains("W12406")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12406"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(complaint.getClientAccountNumber())) {
            if (errWarMessagesList.contains("E12402")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12402"));
            } else if (errWarMessagesList.contains("W12402")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12402"));
            }
        }
        if (CommonUtils.isIntegerNullOrZero(complaint.getClientId())) {
            if (errWarMessagesList.contains("E12401")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12401"));
            } else if (errWarMessagesList.contains("W12401")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12401"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(complaint.getComplaintType())) {
            if (errWarMessagesList.contains("E12403")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12403"));
            } else if (errWarMessagesList.contains("W12403")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12403"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(complaint.getComplaintReason())) {
            if (errWarMessagesList.contains("E12404")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12404"));
            } else if (errWarMessagesList.contains("W12404")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12404"));
            }
        }
        if (CommonUtils.isStringNullOrBlank(complaint.getComplaintChannel())) {
            if (errWarMessagesList.contains("E12405")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12405"));
            } else if (errWarMessagesList.contains("W12405")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12405"));
            }
        }
        if (CommonUtils.isDateNull(complaint.getDtFilling())) {
            if (errWarMessagesList.contains("E12407")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12407"));
            } else if (errWarMessagesList.contains("W12407")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12407"));
            }
        }
        if (CommonUtils.isDateNull(complaint.getDtReport())) {
            if (errWarMessagesList.contains("E12408")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E12408"));
            } else if (errWarMessagesList.contains("W12408")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E12408"));
            }
        }
    }

    public static void lookUpValidation(Complaint complaint, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (!CommonUtils.isIntegerNullOrZero(complaint.getClientId()) && complaint.getClient() == null) {
            if (errWarMessagesList.contains("E22401")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22401"));
            } else if (errWarMessagesList.contains("W22401")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22401"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplaintType()) && complaint.getComplaintTypeVal() == null) {
            if (errWarMessagesList.contains("E22402")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22402"));
            } else if (errWarMessagesList.contains("W22402")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22402"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplaintReason()) && complaint.getComplaintReasonVal() == null) {
            if (errWarMessagesList.contains("E22403")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22403"));
            } else if (errWarMessagesList.contains("W22403")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22403"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplaintType()) && !CommonUtils.isStringNullOrBlank(complaint.getComplaintReason()) && complaint.getMapComplaintTypeReasonId() == null) {
            if (errWarMessagesList.contains("E22404")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22404"));
            } else if (errWarMessagesList.contains("W22404")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22404"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplaintChannel()) && complaint.getComplaintChannelLookUp() == null) {
            if (errWarMessagesList.contains("E22405")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22405"));
            } else if (errWarMessagesList.contains("W22405")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22405"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getChannelMode()) && complaint.getChannelModeLookUp() == null) {
            if (errWarMessagesList.contains("E22406")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22406"));
            } else if (errWarMessagesList.contains("W22406")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22406"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplainant()) && complaint.getComplainantLookUp() == null) {
            if (errWarMessagesList.contains("E22407")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22407"));
            } else if (errWarMessagesList.contains("W22407")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22407"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getRegulatoryBody()) && complaint.getRegulatoryBodyLookUp() == null) {
            if (errWarMessagesList.contains("E22408")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22408"));
            } else if (errWarMessagesList.contains("W22408")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22408"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getComplaintStatus()) && complaint.getComplaintStatusLookUp() == null) {
            if (errWarMessagesList.contains("E22409")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E22409"));
            } else if (errWarMessagesList.contains("W22409")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E22409"));
            }
        }
    }

    public static void referenceUpdation(Complaint complaint) {
        if (complaint.getAccountIds() != null && !CommonUtils.isLongNull(complaint.getAccountIds())) {
            complaint.setAccountId(complaint.getAccountIds());
        }
        if(complaint.getConsumerIds() != null && !CommonUtils.isLongNull(complaint.getConsumerIds())) {
            complaint.setConsumerId(complaint.getConsumerIds());
        }
    }

    public static void misingRefCheck(Complaint complaint, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (CommonUtils.isLongNull(complaint.getAccountId())) {
            if (errWarMessagesList.contains("E42401")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E42401"));
            } else if (errWarMessagesList.contains("W42401")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E42401"));
            }
        }
        if(!CommonUtils.isLongNull(complaint.getClientConsumerNumber()) && CommonUtils.isLongNull(complaint.getConsumerId())) {
            if(errWarMessagesList.contains("E42402")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E42402"));
            } else if(errWarMessagesList.contains("W42402")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E42402"));
            }
        }
    }

    public static void businessRule(Complaint complaint, Map<String, Object> validationMap, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(complaint.getRecordType()) && !complaint.getRecordType().equalsIgnoreCase("complaint")) {
            if (errWarMessagesList.contains("E72406")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E72406"));
            } else if (errWarMessagesList.contains("W72406")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E72406"));
            }
        }
        if (!CommonUtils.isDateNull(complaint.getDtReport()) && !CommonUtils.isDateNull(complaint.getDtFilling())
                && complaint.getDtReport().isBefore(complaint.getDtFilling())) {
            if (errWarMessagesList.contains("E72401")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E72401"));
            } else if (errWarMessagesList.contains("W72401")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E72401"));
            }
        }
        if (!CommonUtils.isStringNullOrBlank(complaint.getChannelMode()) && CommonConstants.COMPLIANCE_MODE_VERBAL.equalsIgnoreCase(complaint.getChannelMode())
        		&& CommonUtils.isStringNullOrBlank(complaint.getDescription())) {
            if (errWarMessagesList.contains("E72402")) {
                validationMap.put("isComplaintValidated", false);
                complaint.addErrWarJson(new ErrWarJson("e", "E72402"));
            } else if (errWarMessagesList.contains("W72402")) {
                complaint.addErrWarJson(new ErrWarJson("w", "E72402"));
            }
        }
    }
}