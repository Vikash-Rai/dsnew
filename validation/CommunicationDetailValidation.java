package com.equabli.collectprism.validation;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.CommunicationDetail;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.ConfRegulatoryBody;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.EntityUtil;

public class CommunicationDetailValidation {

	public static void mandatoryValidation(CommunicationDetail detail, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E10606") || errWarMessagesList.contains("W10606")) && CommonUtils.isStringNullOrBlank(detail.getRecordType())) {
			if(errWarMessagesList.contains("E10606")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10606"));
			} else if(errWarMessagesList.contains("W10606")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10606"));
			}
		}
		if((errWarMessagesList.contains("E10602") || errWarMessagesList.contains("W10602")) && CommonUtils.isStringNullOrBlank(detail.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E10602")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10602"));
			} else if(errWarMessagesList.contains("W10602")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10602"));
			}
		}
		if((errWarMessagesList.contains("E10601") || errWarMessagesList.contains("W10601")) && CommonUtils.isIntegerNullOrZero(detail.getClientId())) {
			if(errWarMessagesList.contains("E10601")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10601"));
			} else if(errWarMessagesList.contains("W10601")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10601"));
			}
		}
		if((errWarMessagesList.contains("E70610") || errWarMessagesList.contains("W70610")) && detail.getComplianceSubtypeHierarcyCount() > 0 && CommonUtils.isStringNullOrBlank(detail.getComplianceSubtype())) {
			if(errWarMessagesList.contains("E70610")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E70610"));
			} else if(errWarMessagesList.contains("W70610")) {
				detail.addErrWarJson(new ErrWarJson("w", "E70610"));
			}
		}
		if((errWarMessagesList.contains("E10643") || errWarMessagesList.contains("W10643")) && CommonUtils.isStringNullOrBlank(detail.getCommunicationChannel())) {
			if(errWarMessagesList.contains("E10643")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10643"));
			} else if(errWarMessagesList.contains("W10643")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10643"));
			}
		}
		if((errWarMessagesList.contains("E10613") || errWarMessagesList.contains("W10613")) && CommonUtils.isStringNullOrBlank(detail.getCommunicationReason())) {
			if(errWarMessagesList.contains("E10613")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10613"));
			} else if(errWarMessagesList.contains("W10613")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10613"));
			}
		}
		if((errWarMessagesList.contains("E10614") || errWarMessagesList.contains("W10614")) && CommonUtils.isStringNullOrBlank(detail.getCommunicationSubreason())) {
			if(errWarMessagesList.contains("E10614")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10614"));
			} else if(errWarMessagesList.contains("W10614")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10614"));
			}
		}
		if((errWarMessagesList.contains("E10615") || errWarMessagesList.contains("W10615")) && CommonUtils.isStringNullOrBlank(detail.getCommunicationOutcome())) {
			if(errWarMessagesList.contains("E10615")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10615"));
			} else if(errWarMessagesList.contains("W10615")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10615"));
			}
		}
		if((errWarMessagesList.contains("E10648") || errWarMessagesList.contains("W10648")) && CommonUtils.isStringNullOrBlank(detail.getDirection())) {
			if(errWarMessagesList.contains("E10648")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10648"));
			} else if(errWarMessagesList.contains("W10648")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10648"));
			}
		}
		if((errWarMessagesList.contains("E10646") || errWarMessagesList.contains("W10646")) && CommonUtils.isDateNull(detail.getDtCommunication())) {
			if(errWarMessagesList.contains("E10646")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E10646"));
			} else if(errWarMessagesList.contains("W10646")) {
				detail.addErrWarJson(new ErrWarJson("w", "E10646"));
			}
		}
	}

	public static void lookUpValidation(CommunicationDetail detail, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E20601") || errWarMessagesList.contains("W20601")) && !CommonUtils.isIntegerNullOrZero(detail.getClientId()) && detail.getClient() == null) {
			if(errWarMessagesList.contains("E20601")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20601"));
			} else if(errWarMessagesList.contains("W20601")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20601"));
			}
		}
		if((errWarMessagesList.contains("E20663") || errWarMessagesList.contains("W20663")) && !CommonUtils.isStringNullOrBlank(detail.getComplianceType()) && detail.getComplianceTypeLookUp() == null) {
			if(errWarMessagesList.contains("E20663")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20663"));
			} else if(errWarMessagesList.contains("W20663")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20663"));
			}
		}
		if((errWarMessagesList.contains("E20679") || errWarMessagesList.contains("W20679")) && detail.getComplianceSubtypeHierarcyCount() > 0 && !CommonUtils.isStringNullOrBlank(detail.getComplianceSubtype()) && detail.getComplianceSubtypeCount() == 0) {
			if(errWarMessagesList.contains("E20679")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20679"));
			} else if(errWarMessagesList.contains("W20679")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20679"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getRegulatoryBodyShortName()) && detail.getRegulatoryBody() == null) {
			detail.setRegulatoryBodyShortName(ConfRegulatoryBody.REGULATORYBODY_OT);
			detail.setRegulatoryBodyId(ConfRegulatoryBody.confRegulatoryBody.get(ConfRegulatoryBody.REGULATORYBODY_OT).getRegulatoryBodyId());
		}
		if((errWarMessagesList.contains("E20643") || errWarMessagesList.contains("W20643")) && !CommonUtils.isStringNullOrBlank(detail.getCommunicationChannel()) && detail.getCommunicationChannelLookUp() == null) {
			if(errWarMessagesList.contains("E20643")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20643"));
			} else if(errWarMessagesList.contains("W20643")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20643"));
			}
		}
		if((errWarMessagesList.contains("E20613") || errWarMessagesList.contains("W20613")) && !CommonUtils.isStringNullOrBlank(detail.getCommunicationReason()) && detail.getCommunicationReasonLookUp() == null) {
			if(errWarMessagesList.contains("E20613")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20613"));
			} else if(errWarMessagesList.contains("W20613")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20613"));
			}
		}
		if((errWarMessagesList.contains("E20614") || errWarMessagesList.contains("W20614")) && detail.getCommunicationSubreasonHierarcyCount() > 0 && !CommonUtils.isStringNullOrBlank(detail.getCommunicationSubreason()) && detail.getCommunicationSubreasonCount() == 0) {
			if(errWarMessagesList.contains("E20614")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20614"));
			} else if(errWarMessagesList.contains("W20614")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20614"));
			}
		}
		if((errWarMessagesList.contains("E20615") || errWarMessagesList.contains("W20615")) && detail.getCommunicationOutcomeHierarcyCount() > 0 && !CommonUtils.isStringNullOrBlank(detail.getCommunicationOutcome()) && detail.getCommunicationOutcomeCount() == 0) {
			if(errWarMessagesList.contains("E20615")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E20615"));
			} else if(errWarMessagesList.contains("W20615")) {
				detail.addErrWarJson(new ErrWarJson("w", "E20615"));
			}
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getDirection()) && !detail.getDirection().equalsIgnoreCase("Inbound") && !detail.getDirection().equalsIgnoreCase("Outbound")) {
			detail.setDirection("Other");
		}
	}

	public static void standardize(CommunicationDetail detail) {
		if(!CommonUtils.isStringNullOrBlank(detail.getClientAccountNumber())) {
			detail.setClientAccountNumber(detail.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationChannel())) {
			detail.setCommunicationChannel(detail.getCommunicationChannel().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationOutcome())) {
			detail.setCommunicationOutcome(detail.getCommunicationOutcome().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationReason())) {
			detail.setCommunicationReason(detail.getCommunicationReason().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationSubreason())) {
			detail.setCommunicationSubreason(detail.getCommunicationSubreason().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getComplianceType())) {
			detail.setComplianceType(detail.getComplianceType().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getComplianceSubtype())) {
			detail.setComplianceSubtype(detail.getComplianceSubtype().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getDirection())) {
			detail.setDirection(EntityUtil.capitailizeWord(detail.getDirection()));
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getDisposition())) {
			detail.setDisposition(EntityUtil.capitailizeWord(detail.getDisposition()));
		}
	}

	public static void referenceUpdation(CommunicationDetail detail) {
		if(detail.getAccountIds() != null && !CommonUtils.isLongNull(detail.getAccountIds())) {
			detail.setAccountId(detail.getAccountIds());
		}
		if(detail.getConsumerIds() != null && !CommonUtils.isLongNull(detail.getConsumerIds())) {
			detail.setConsumerId(detail.getConsumerIds());
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getRegulatoryBodyShortName()) && detail.getRegulatoryBody() != null) {
			detail.setRegulatoryBodyId(detail.getRegulatoryBody().getRegulatoryBodyId());
		}
		if(detail.getComplianceIds() != null && !CommonUtils.isLongNull(detail.getComplianceIds())) {
			detail.setComplianceId(detail.getComplianceIds());
		}
	}

	public static void misingRefCheck(CommunicationDetail detail, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if((errWarMessagesList.contains("E70601") || errWarMessagesList.contains("W70601")) && CommonUtils.isLongNull(detail.getAccountId()) && CommonUtils.isLongNull(detail.getConsumerId())) {
			if(errWarMessagesList.contains("E70601")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E70601"));
			} else if(errWarMessagesList.contains("W70601")) {
				detail.addErrWarJson(new ErrWarJson("w", "E70601"));
			}
		}
		if((errWarMessagesList.contains("E40616") || errWarMessagesList.contains("W40616")) && !CommonUtils.isStringNullOrBlank(detail.getClientAccountNumber()) && CommonUtils.isLongNull(detail.getAccountId())) {
			if(errWarMessagesList.contains("E40616")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E40616"));
			} else if(errWarMessagesList.contains("W40616")) {
				detail.addErrWarJson(new ErrWarJson("w", "E40616"));
			}
		}
		if((errWarMessagesList.contains("E40627") || errWarMessagesList.contains("W40627")) && !CommonUtils.isLongNull(detail.getClientConsumerNumber()) && CommonUtils.isLongNull(detail.getConsumerId())) {
			if(errWarMessagesList.contains("E40627")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E40627"));
			} else if(errWarMessagesList.contains("W40627")) {
				detail.addErrWarJson(new ErrWarJson("w", "E40627"));
			}
		}
		if((errWarMessagesList.contains("E40610") || errWarMessagesList.contains("W40610")) && (!CommonUtils.isStringNullOrBlank(detail.getComplianceType()) || !CommonUtils.isStringNullOrBlank(detail.getComplianceSubtype())
			|| !CommonUtils.isDateNull(detail.getDtCompliance()) || !CommonUtils.isIntegerNullOrZero(detail.getRegulatoryBodyId())) 
				&& CommonUtils.isLongNull(detail.getComplianceId())) {
			if(errWarMessagesList.contains("E40610")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E40610"));
			} else if(errWarMessagesList.contains("W40610")) {
				detail.addErrWarJson(new ErrWarJson("w", "E40610"));
			}
		}
	}

	public static void businessRule(CommunicationDetail detail, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(!CommonUtils.isStringNullOrBlank(detail.getComplianceType())
				&& (detail.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_COMPLAINTS) 
					|| detail.getComplianceType().equalsIgnoreCase(CommonConstants.COMPLIANCE_TYPE_DISPUTE))) {
			if((errWarMessagesList.contains("E70602") || errWarMessagesList.contains("W70602")) && CommonUtils.isStringNullOrBlank(detail.getRegulatoryBodyShortName())) {
				if(errWarMessagesList.contains("E70602")) {
					validationMap.put("isCommunicationDetailValidated", false);
					detail.addErrWarJson(new ErrWarJson("e", "E70602"));
				} else if(errWarMessagesList.contains("W70602")) {
					detail.addErrWarJson(new ErrWarJson("w", "E70602"));
				}
			}
		} else {
			if((errWarMessagesList.contains("E70603") || errWarMessagesList.contains("W70603")) && !CommonUtils.isStringNullOrBlank(detail.getRegulatoryBodyShortName())) {
				if(errWarMessagesList.contains("E70603")) {
					validationMap.put("isCommunicationDetailValidated", false);
					detail.addErrWarJson(new ErrWarJson("e", "E70603"));
				} else if(errWarMessagesList.contains("W70603")) {
					detail.addErrWarJson(new ErrWarJson("w", "E70603"));
				}
			}
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationReason()) && detail.getCommunicationReason().equalsIgnoreCase("M")) {
			if((errWarMessagesList.contains("E70604") || errWarMessagesList.contains("W70604")) && (!CommonUtils.isDoubleNull(detail.getPctDiscount()) && (detail.getPctDiscount() < 0 || detail.getPctDiscount() > 100))) {
				if(errWarMessagesList.contains("E70604")) {
					validationMap.put("isCommunicationDetailValidated", false);
					detail.addErrWarJson(new ErrWarJson("e", "E70604"));
				} else if(errWarMessagesList.contains("W70604")) {
					detail.addErrWarJson(new ErrWarJson("w", "E70604"));
				}
			}
		} else if((errWarMessagesList.contains("E70605") || errWarMessagesList.contains("W70605")) && !CommonUtils.isDoubleNull(detail.getPctDiscount())) {
			if(errWarMessagesList.contains("E70605")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E70605"));
			} else if(errWarMessagesList.contains("W70605")) {
				detail.addErrWarJson(new ErrWarJson("w", "E70605"));
			}
		}
		if((errWarMessagesList.contains("E70606") || errWarMessagesList.contains("W70606")) && !CommonUtils.isDateNull(detail.getDtCommunication()) && !CommonUtils.isDateNull(detail.getDtOutcome()) && detail.getDtCommunication().isAfter(detail.getDtOutcome())) {
			if(errWarMessagesList.contains("E70606")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E70606"));
			} else if(errWarMessagesList.contains("W70606")) {
				detail.addErrWarJson(new ErrWarJson("w", "E70606"));
			}
		}
		if((errWarMessagesList.contains("E70607") || errWarMessagesList.contains("W70607")) && !CommonUtils.isDateNull(detail.getDtCommunication()) && !CommonUtils.isDateNull(detail.getDtOutcome())
				&& !CommonUtils.isTimeNull(detail.getTmUtcCommunication()) && !CommonUtils.isTimeNull(detail.getTmUtcOutcome()) 
				&& detail.getDtCommunication().isEqual(detail.getDtOutcome()) && detail.getTmUtcCommunication().isAfter(detail.getTmUtcOutcome())) {
			if(errWarMessagesList.contains("E70607")) {
				validationMap.put("isCommunicationDetailValidated", false);
				detail.addErrWarJson(new ErrWarJson("e", "E70607"));
			} else if(errWarMessagesList.contains("W70607")) {
				detail.addErrWarJson(new ErrWarJson("w", "E70607"));
			}
		}
		if(detail.getIsComplianceSubtypeHierarcyNA() > 0 && CommonUtils.isStringNullOrBlank(detail.getComplianceSubtype())) {
			detail.setComplianceSubtype("NA");
		}
		if(!CommonUtils.isStringNullOrBlank(detail.getCommunicationSource()) && detail.getCommunicationSource().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER)) {
			if(!detail.getCommunicationSourceId().equals(detail.getPartnerIds())) {
				if((errWarMessagesList.contains("E70608") || errWarMessagesList.contains("W70608")) && !CommonUtils.isDateNull(detail.getCotDtFrom()) && detail.getCotDtFrom().isAfter(detail.getDtCommunication())) {
					if(errWarMessagesList.contains("E70608")) {
						validationMap.put("isCommunicationDetailValidated", false);
						detail.addErrWarJson(new ErrWarJson("e", "E70608"));
					} else if(errWarMessagesList.contains("W70608")) {
						detail.addErrWarJson(new ErrWarJson("w", "E70608"));
					}
				}
				if((errWarMessagesList.contains("E70609") || errWarMessagesList.contains("W70609")) && !CommonUtils.isDateNull(detail.getCotDtTill()) && detail.getCotDtTill().isBefore(detail.getDtCommunication())) {
					if(errWarMessagesList.contains("E70609")) {
						validationMap.put("isCommunicationDetailValidated", false);
						detail.addErrWarJson(new ErrWarJson("e", "E70609"));
					} else if(errWarMessagesList.contains("W70609")) {
						detail.addErrWarJson(new ErrWarJson("w", "E70609"));
					}
				}
				if((errWarMessagesList.contains("E70611") || errWarMessagesList.contains("W70611")) && CommonUtils.isDateNull(detail.getCotDtFrom()) && CommonUtils.isDateNull(detail.getCotDtTill())) {
					if(errWarMessagesList.contains("E70611")) {
						validationMap.put("isCommunicationDetailValidated", false);
						detail.addErrWarJson(new ErrWarJson("e", "E70611"));
					} else if(errWarMessagesList.contains("W70611")) {
						detail.addErrWarJson(new ErrWarJson("w", "E70611"));
					}
				}
			}
		}
	}
}