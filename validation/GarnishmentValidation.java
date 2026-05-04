package com.equabli.collectprism.validation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Garnishment;
import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.helpers.CommonUtils;

public class GarnishmentValidation {

	public static void mandatoryValidation(Garnishment g, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
        if (CommonUtils.isStringNullOrBlank(g.getRecordType())) {
			if(errWarMessagesList.contains("E12106")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12106"));
			} else if(errWarMessagesList.contains("W12106")) {
				g.addErrWarJson(new ErrWarJson("w", "E12106"));
			}
        }
		if(CommonUtils.isIntegerNullOrZero(g.getClientId())) {
			if(errWarMessagesList.contains("E12101")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12101"));
			} else if(errWarMessagesList.contains("W12101")) {
				g.addErrWarJson(new ErrWarJson("w", "E12101"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getClientAccountNumber())) {
			if(errWarMessagesList.contains("E12102")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12102"));
			} else if(errWarMessagesList.contains("W12102")) {
				g.addErrWarJson(new ErrWarJson("w", "E12102"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getCaseNumber())) {
			if(errWarMessagesList.contains("E12103")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12103"));
			} else if(errWarMessagesList.contains("W12103")) {
				g.addErrWarJson(new ErrWarJson("w", "E12103"));
			}
		}
		if(CommonUtils.isDateNull(g.getDtGarnishment())) {
			if(errWarMessagesList.contains("E12104")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12104"));
			} else if(errWarMessagesList.contains("W12104")) {
				g.addErrWarJson(new ErrWarJson("w", "E12104"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getGarnishmentType())) {
			if(errWarMessagesList.contains("E12105")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12105"));
			} else if(errWarMessagesList.contains("W12105")) {
				g.addErrWarJson(new ErrWarJson("w", "E12105"));
			}
		}
		if(CommonUtils.isDoubleNull(g.getAmtGarnishment())) {
			if(errWarMessagesList.contains("E12107")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12107"));
			} else if(errWarMessagesList.contains("W12107")) {
				g.addErrWarJson(new ErrWarJson("w", "E12107"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getGarnishmentApprover())) {
			if(errWarMessagesList.contains("E12108")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12108"));
			} else if(errWarMessagesList.contains("W12108")) {
				g.addErrWarJson(new ErrWarJson("w", "E12108"));
			}
		}
		if(CommonUtils.isStringNullOrBlank(g.getGarnishmentFrequency())) {
			if(errWarMessagesList.contains("E12109")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E12109"));
			} else if(errWarMessagesList.contains("W12109")) {
				g.addErrWarJson(new ErrWarJson("w", "E12109"));
			}
		}
	}

	public static void lookUpValidation(Garnishment g, Map<String, Object> validationMap, CacheableService cacheableService, List<String> errWarMessagesList) {
		if(!CommonUtils.isIntegerNullOrZero(g.getClientId()) && g.getClient() == null) {
			if(errWarMessagesList.contains("E22101")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E22101"));
			} else if(errWarMessagesList.contains("W22101")) {
				g.addErrWarJson(new ErrWarJson("w", "E22101"));
			}
		}
        if (!CommonUtils.isStringNullOrBlank(g.getGarnishmentStatus()) && g.getGarnishmentStatusLookUp() == null) {
			if(errWarMessagesList.contains("E22102")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E22102"));
			} else if(errWarMessagesList.contains("W22102")) {
				g.addErrWarJson(new ErrWarJson("w", "E22102"));
			}
        }
        if (!CommonUtils.isStringNullOrBlank(g.getGarnishmentFrequency()) && g.getGarnishmentFrequencyLookUp() == null) {
			if(errWarMessagesList.contains("E22103")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E22103"));
			} else if(errWarMessagesList.contains("W22103")) {
				g.addErrWarJson(new ErrWarJson("w", "E22103"));
			}
        }
	}

	public static void standardize(Garnishment g) {
		if(!CommonUtils.isStringNullOrBlank(g.getClientAccountNumber())) {
			g.setClientAccountNumber(g.getClientAccountNumber().toUpperCase());
		}
		if(!CommonUtils.isStringNullOrBlank(g.getCaseNumber())) {
			g.setCaseNumber(g.getCaseNumber().toUpperCase());
		}
	}

	public static void referenceUpdation(Garnishment g  ) {
		if(g.getAccountIds() != null && !CommonUtils.isLongNull(g.getAccountIds())) {
			g.setAccountId(g.getAccountIds());
		}
        if (g.getConsumerIds() != null && !CommonUtils.isLongNull(g.getConsumerIds())) {
            g.setConsumerId(g.getConsumerIds());
        }
        if (g.getLegalPlacementIds() != null && !CommonUtils.isLongNull(g.getLegalPlacementIds())) {
            g.setLegalPlacementId(g.getLegalPlacementIds());
        }
        if (g.getEmployerIds() != null && !CommonUtils.isLongNull(g.getEmployerIds())) {
            g.setEmployerId(g.getEmployerIds());
        }
        if (g.getAccountAssetIds() != null && !CommonUtils.isLongNull(g.getAccountAssetIds())) {
            g.setAccountAssetId(g.getAccountAssetIds());
        }
	}

	public static void misingRefCheck(Garnishment g, Map<String, Object> validationMap, List<String> errWarMessagesList) {
		if(CommonUtils.isLongNull(g.getAccountId())) {
			if(errWarMessagesList.contains("E42101")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E42101"));
			} else if(errWarMessagesList.contains("W42101")) {
				g.addErrWarJson(new ErrWarJson("w", "E42101"));
			}
		}
        if (CommonUtils.isLongNull(g.getConsumerId())) {
			if(errWarMessagesList.contains("E42102")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E42102"));
			} else if(errWarMessagesList.contains("W42102")) {
				g.addErrWarJson(new ErrWarJson("w", "E42102"));
			}
        }
        if (CommonUtils.isLongNull(g.getLegalPlacementId())) {
			if(errWarMessagesList.contains("E42103")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E42103"));
			} else if(errWarMessagesList.contains("W42103")) {
				g.addErrWarJson(new ErrWarJson("w", "E42103"));
			}
        }
        if (CommonUtils.isLongNull(g.getEmployerId())) {
			if(errWarMessagesList.contains("E42104")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E42104"));
			} else if(errWarMessagesList.contains("W42104")) {
				g.addErrWarJson(new ErrWarJson("w", "E42104"));
			}
        }
        if (CommonUtils.isLongNull(g.getAccountAssetId())) {
			if(errWarMessagesList.contains("E42105")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E42105"));
			} else if(errWarMessagesList.contains("W42105")) {
				g.addErrWarJson(new ErrWarJson("w", "E42105"));
			}
        }
	}

	public static void businessRule(Garnishment g, Map<String, Object> validationMap , PaymentPlan pp, List<String> errWarMessagesList) {
        if (!CommonUtils.isStringNullOrBlank(g.getRecordType()) && !g.getRecordType().equalsIgnoreCase("garnishment")) {
			if(errWarMessagesList.contains("E72106")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E72106"));
			} else if(errWarMessagesList.contains("W72106")) {
				g.addErrWarJson(new ErrWarJson("w", "E72106"));
			}
        }
		if(!CommonUtils.isStringNullOrBlank(g.getCaseNumber()) && (g.getCaseNumber().length() < 5 || g.getCaseNumber().length() > 50)) {
			if(errWarMessagesList.contains("E72101")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E72101"));
			} else if(errWarMessagesList.contains("W72101")) {
				g.addErrWarJson(new ErrWarJson("w", "E72101"));
			}
		}
		if(g.getAmtGarnishment() != null && g.getAmtGarnishment() < 0) {
			if(errWarMessagesList.contains("E72102")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E72102"));
			} else if(errWarMessagesList.contains("W72102")) {
				g.addErrWarJson(new ErrWarJson("w", "E72102"));
			}
		}
		if (!CommonUtils.isDateNull(g.getDtGarnishment()) && g.getDtGarnishment().isAfter(LocalDate.now())) {
			if(errWarMessagesList.contains("E72103")) {
				validationMap.put("isGarnishmentValidated", false);
				g.addErrWarJson(new ErrWarJson("e", "E72103"));
			} else if(errWarMessagesList.contains("W72103")) {
				g.addErrWarJson(new ErrWarJson("w", "E72103"));
			}
		}
		if(!CommonUtils.isLongNullOrZero(g.getPartnerPlanNumber())) {
			
			if(!CommonUtils.isObjectNull(pp) && !CommonUtils.isLongNullOrZero(pp.getPaymentPlanId())) {
				g.setPaymentPlanId(pp.getPaymentPlanId());
				
				if( !g.getAmtGarnishment().equals(pp.getPaymentSettlementAmt())) {
					if(errWarMessagesList.contains("E72105")) {
						validationMap.put("isGarnishmentValidated", false);
						g.addErrWarJson(new ErrWarJson("e", "E72105"));
					} else if(errWarMessagesList.contains("W72105")) {
						g.addErrWarJson(new ErrWarJson("w", "E72105"));
					}
				}
				
				if(!g.getGarnishmentFrequency().equals(pp.getPaymentPlanInterval())) {
					if(errWarMessagesList.contains("E72107")) {
						validationMap.put("isGarnishmentValidated", false);
						g.addErrWarJson(new ErrWarJson("e", "E72107"));
					} else if(errWarMessagesList.contains("W72107")) {
						g.addErrWarJson(new ErrWarJson("w", "E72107"));
					}
				}
				
			}else {
				if(errWarMessagesList.contains("E72104")) {
					validationMap.put("isGarnishmentValidated", false);
					g.addErrWarJson(new ErrWarJson("e", "E72104"));
				} else if(errWarMessagesList.contains("W72104")) {
					g.addErrWarJson(new ErrWarJson("w", "E72104"));
				}
			}
		}
	}
}