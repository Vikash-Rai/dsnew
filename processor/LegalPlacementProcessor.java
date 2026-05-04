package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.config.SqsMessageSender;
import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.LegalPlacement;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.validation.LegalPlacementValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;

public class LegalPlacementProcessor implements ItemProcessor<LegalPlacement, LegalPlacement> {

    private final Logger logger = LoggerFactory.getLogger(LegalPlacementProcessor.class);

	@Autowired
	private CacheableService cacheableService;

    @Autowired
    AdjustmentRepository adjustmentRepository;
    
    @Autowired
	SqsMessageSender sqsMessageSender;

	@Override
	public LegalPlacement process(LegalPlacement lp) throws Exception {
		try {
			if(!CommonUtils.isObjectNull(LegalPlacement.legalPlacementRecord) && LegalPlacement.legalPlacementRecord.containsKey(lp.getClientId() + "_" + lp.getClientAccountNumber())) {
				LegalPlacement lpl = LegalPlacement.legalPlacementRecord.get(lp.getClientId() + "_" + lp.getClientAccountNumber());
				if(lpl.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) 
						&& !lp.getLegalPlacementId().equals(lpl.getLegalPlacementId()) && ((lpl.getSuitStatus() == null || lpl.getSuitStatus()) 
						|| (lpl.getJudgmentStatus() == null || (!lpl.getJudgmentStatus().equalsIgnoreCase("DD") && !lpl.getJudgmentStatus().equalsIgnoreCase("DP"))))) {
					lp.setLegalPlacementIdCount(lp.getLegalPlacementIdCount() + 1);
				}
			}

			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("LegalPlacement Id", lp.getLegalPlacementId());
			validationMap.put("isLegalPlacementValidated", true);

			lp.setErrCodeJson(null);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(lp.getClientId(), cacheableService);

			LegalPlacementValidation.mandatoryValidation(lp, validationMap, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isLegalPlacementValidated").toString())) {
				LegalPlacementValidation.lookUpValidation(lp, validationMap, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isLegalPlacementValidated").toString())) {
					LegalPlacementValidation.standardize(lp);
					LegalPlacementValidation.deDupValidation(lp, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isLegalPlacementValidated").toString())) {
						LegalPlacementValidation.referenceUpdation(lp);
						LegalPlacementValidation.misingRefCheck(lp, validationMap, errWarMessagesList);

						if(Boolean.parseBoolean(validationMap.get("isLegalPlacementValidated").toString())) {
							LegalPlacementValidation.businessRule(lp, validationMap, errWarMessagesList);
						}
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isLegalPlacementValidated").toString())) {
				lp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				if(lp.getDtJudgment() != null && lp.getAmtCurrentbalance() != null) {
					String adjustmentType = null;
					Double amtDiff = 0.0;
					Double adPrincipal = 0.0;
					Double adInterest = 0.0;
					Double adFees = 0.0;
					Double adOther = 0.0;
					Double adAttorney = 0.0;

					Double currentBalance = (!CommonUtils.isDoubleNull(lp.getAmtCurrentbalance())) ? lp.getAmtCurrentbalance() :  0.00;
					Double principalCurrentBalance = (!CommonUtils.isDoubleNull(lp.getAmtPrincipalCurrentbalance())) ? lp.getAmtPrincipalCurrentbalance() :  0.00;
					Double interestCurrentBalance = (!CommonUtils.isDoubleNull(lp.getAmtInterestCurrentbalance())) ? lp.getAmtInterestCurrentbalance() :  0.00;
					Double lateFeeCurrentBalance = (!CommonUtils.isDoubleNull(lp.getAmtLateCurrentbalance())) ? lp.getAmtLateCurrentbalance() :  0.00;
					Double otherFeeCurrentBalance = (!CommonUtils.isDoubleNull(lp.getAmtOtherfeeCurrentbalance())) ? lp.getAmtOtherfeeCurrentbalance() :  0.00;
					Double attorneyFeeCurrentBalance = (!CommonUtils.isDoubleNull(lp.getAmtAttorneyfeeCurrentbalance())) ? lp.getAmtAttorneyfeeCurrentbalance() :  0.00;

					Double judgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgement())) ? lp.getAmtJudgement() :  0.00;
					Double principalJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentPrincipal())) ? lp.getAmtJudgmentPrincipal() :  0.00;
					Double interestJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentInterest())) ? lp.getAmtJudgmentInterest() :  0.00;
					Double feesJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentFees())) ? lp.getAmtJudgmentFees() :  0.00;
					Double otherJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentOther())) ? lp.getAmtJudgmentOther() :  0.00;
					Double attorneyJudgement = (!CommonUtils.isDoubleNull(lp.getAmtJudgmentAttorney())) ? lp.getAmtJudgmentAttorney() :  0.00;

					adjustmentType = CommonConstants.ADJUSTMENT_TYPE_BALANCE_ADJUSTMENT;
					amtDiff = judgement - currentBalance;
					adPrincipal = principalJudgement - principalCurrentBalance;
					adInterest = interestJudgement - interestCurrentBalance;
					adFees = feesJudgement - lateFeeCurrentBalance;
					adOther = otherJudgement - otherFeeCurrentBalance;
					adAttorney = attorneyJudgement - attorneyFeeCurrentBalance;

					Adjustment adj = new Adjustment("adjustment", lp.getClientId(), lp.getAccountId(), adjustmentType, lp.getDtJudgment(), 
							amtDiff, adPrincipal, adInterest, adFees, adOther, null, adAttorney, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
					adjustmentRepository.insertIntoAdjustment(adj.getRecordType(), adj.getClientId(), lp.getClientAccountNumber(), adj.getAccountId(), 
							adj.getAdjustmentType(), adj.getAdjustmentDate(), adj.getAmtAdjustment(), adj.getAmtPrincipal(), adj.getAmtInterest(), 
							adj.getAmtLatefee(), adj.getAmtOtherfee(), adj.getAmtAttorneyfee(), adj.getRecordStatusId());
				
					Long adjustmentId = adjustmentRepository.getAdjustmentId();
					String clientCode = adjustmentRepository.getClientCode(adj.getClientId());
					sqsMessageSender.sendMessage(clientCode,"AJ",adjustmentId);
				
				}
				lp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				LegalPlacement.legalPlacementRecord.clear();
				LegalPlacement.legalPlacementRecord.put(lp.getClientId() + "_" + lp.getClientAccountNumber(), lp);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return lp;
	}
}