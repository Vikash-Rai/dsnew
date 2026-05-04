package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ChainOfTitle;
import com.equabli.collectprism.entity.ChainOfTitles;
import com.equabli.collectprism.entity.CoTOwner;
import com.equabli.collectprism.repository.ChainOfTitleRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.ChainOfTitleValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class ChainOfTitleProcessor implements ItemProcessor<ChainOfTitle, ChainOfTitle> {

    private final Logger logger = LoggerFactory.getLogger(ChainOfTitleProcessor.class);

    @Autowired
	private CacheableService cacheableService;

    @Autowired
    private ChainOfTitleRepository chainOfTitleRepository;

	@Override
	public ChainOfTitle process(ChainOfTitle chainOfTitle) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("ChainOfTitle Id", chainOfTitle.getChainOfTitleId());
			validationMap.put("isChainOfTitleValidated", true);
			chainOfTitle.setChainOfTitles(chainOfTitleRepository.getChainOfTitlesToProcess(chainOfTitle.getClientId(), chainOfTitle.getCotType(), chainOfTitle.getClientAccountNumber(), chainOfTitle.getChainOfTitleId()));
			Integer dtEndPreviousCount = 0;
			Integer dtFromCount = 0;
			Integer dtTillCount = 0;

			for(ChainOfTitles cots: chainOfTitle.getChainOfTitles() ) {
				if(CommonUtils.isDateNull(chainOfTitle.getDtStartAfter()) && cots.getDtStart().isAfter(chainOfTitle.getDtStart())) {
					chainOfTitle.setDtStartAfter(cots.getDtStart());
				}
				if(!CommonUtils.isDateNull(cots.getDtStart()) && !CommonUtils.isDateNull(chainOfTitle.getDtStart()) && cots.getDtStart().isBefore(chainOfTitle.getDtStart())) {
					chainOfTitle.setDtEndPrevious(cots.getDtEnd());
					dtEndPreviousCount++;
				}
				if(!CommonUtils.isDateNull(cots.getDtStart()) && !CommonUtils.isDateNull(cots.getDtEnd()) && !CommonUtils.isDateNull(chainOfTitle.getDtStart()) 
						&& (cots.getDtStart().isBefore(chainOfTitle.getDtStart()) || cots.getDtStart().isEqual(chainOfTitle.getDtStart())) 
						&& (cots.getDtEnd().isEqual(chainOfTitle.getDtStart()) || cots.getDtEnd().isAfter(chainOfTitle.getDtStart()))) {
					dtFromCount++;
				}
				if(!CommonUtils.isDateNull(cots.getDtStart()) && !CommonUtils.isDateNull(cots.getDtEnd()) && !CommonUtils.isDateNull(chainOfTitle.getDtEnd()) 
						&& (cots.getDtStart().isBefore(chainOfTitle.getDtEnd()) || cots.getDtStart().isEqual(chainOfTitle.getDtEnd())) 
						&& (cots.getDtEnd().isEqual(chainOfTitle.getDtEnd()) || cots.getDtEnd().isAfter(chainOfTitle.getDtEnd()))) {
					dtTillCount++;
				}
			}

			chainOfTitle.setDtEndPreviousCount(dtEndPreviousCount);
			chainOfTitle.setDtFromCount(dtFromCount);
			chainOfTitle.setDtTillCount(dtTillCount);
			chainOfTitle.setCotStatusId(null);
			chainOfTitle.setErrCodeJson(null);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(chainOfTitle.getClientId(), cacheableService);
			ChainOfTitleValidation.mandatoryValidation(chainOfTitle, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isChainOfTitleValidated").toString())) {
				ChainOfTitleValidation.lookUpValidation(chainOfTitle, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isChainOfTitleValidated").toString())) {
					ChainOfTitleValidation.standardize(chainOfTitle);
					ChainOfTitleValidation.referenceUpdation(chainOfTitle);
					ChainOfTitleValidation.misingRefCheck(chainOfTitle, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isChainOfTitleValidated").toString())) {
						ChainOfTitleValidation.businessRule(chainOfTitle, validationMap, errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
    		if(chainOfTitle.getRecordStatusId() != ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) {
    			if(!Boolean.parseBoolean(validationMap.get("isChainOfTitleValidated").toString())) {
    				chainOfTitle.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    			} else {
    				chainOfTitle.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    			}
    		}

    		if(chainOfTitle.getRecordStatusId() == ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) {
    			ChainOfTitle cot = (ChainOfTitle.chainOfTitleRecord.containsKey(chainOfTitle.getClientId() + "_" + chainOfTitle.getClientAccountNumber())) ?
    				ChainOfTitle.chainOfTitleRecord.get(chainOfTitle.getClientId() + "_" + chainOfTitle.getClientAccountNumber()) : new ChainOfTitle();
				if(chainOfTitle.getClient() != null && chainOfTitle.getClient().getShortName().equalsIgnoreCase(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME) 
						&& (chainOfTitle.getSubpoolCotOwner() != null || chainOfTitle.getCotOwners() != null)) {
					CoTOwner cotOwner = (chainOfTitle.getSubpoolCotOwner() != null) ? chainOfTitle.getSubpoolCotOwner() : chainOfTitle.getCotOwners();
        			String creditor = (cotOwner.getOwnerName().length() > 50) ? cotOwner.getOwnerName().substring(0, 49) 
        								: cotOwner.getOwnerName();
        			if(CommonUtils.isStringNullOrBlank(chainOfTitle.getAccounts().getInitialLenderCreditor())) {
            			if(cot.getAccounts() == null || CommonUtils.isStringNullOrBlank(cot.getAccounts().getInitialLenderCreditor())) {
            				chainOfTitle.getAccounts().setInitialLenderCreditor(creditor);
            			} else {
            				chainOfTitle.getAccounts().setInitialLenderCreditor(cot.getAccounts().getInitialLenderCreditor());
            			}
        			}
        			if(!CommonUtils.isDateNull(chainOfTitle.getAccounts().getChargeOffDate()) && (chainOfTitle.getAccounts().getChargeOffDate().isEqual(chainOfTitle.getDtStart()) || chainOfTitle.getAccounts().getChargeOffDate().isAfter(chainOfTitle.getDtStart())) 
        					&& (CommonUtils.isDateNull(chainOfTitle.getDtEnd()) || chainOfTitle.getAccounts().getChargeOffDate().isEqual(chainOfTitle.getDtEnd()) || chainOfTitle.getAccounts().getChargeOffDate().isBefore(chainOfTitle.getDtEnd()))){
        				chainOfTitle.getAccounts().setOriginalLenderCreditor(creditor);
        			}
        			if(CommonUtils.isDateNull(chainOfTitle.getDtEnd())){
        				chainOfTitle.getAccounts().setCurrentLenderCreditor(creditor);
        			}
        		} else if(chainOfTitle.getCotOwner() != null) {
        			String creditor = (chainOfTitle.getCotOwner().getOwnerName().length() > 50) ? chainOfTitle.getCotOwner().getOwnerName().substring(0, 49) 
        								: chainOfTitle.getCotOwner().getOwnerName();
        			if(CommonUtils.isStringNullOrBlank(chainOfTitle.getAccounts().getInitialLenderCreditor())) {
            			if(cot.getAccounts() == null || CommonUtils.isStringNullOrBlank(cot.getAccounts().getInitialLenderCreditor())) {
            				chainOfTitle.getAccounts().setInitialLenderCreditor(creditor);
            			} else {
            				chainOfTitle.getAccounts().setInitialLenderCreditor(cot.getAccounts().getInitialLenderCreditor());
            			}
        			}
        			if(!CommonUtils.isDateNull(chainOfTitle.getAccounts().getChargeOffDate()) && (chainOfTitle.getAccounts().getChargeOffDate().isEqual(chainOfTitle.getDtStart()) || chainOfTitle.getAccounts().getChargeOffDate().isAfter(chainOfTitle.getDtStart())) 
        					&& (CommonUtils.isDateNull(chainOfTitle.getDtEnd()) || chainOfTitle.getAccounts().getChargeOffDate().isEqual(chainOfTitle.getDtEnd()) || chainOfTitle.getAccounts().getChargeOffDate().isBefore(chainOfTitle.getDtEnd()))){
        				chainOfTitle.getAccounts().setOriginalLenderCreditor(creditor);
        			}
        			if(CommonUtils.isDateNull(chainOfTitle.getDtEnd())){
        				chainOfTitle.getAccounts().setCurrentLenderCreditor(creditor);
        			}
        		}

				ChainOfTitle.chainOfTitleRecord.clear();
    			ChainOfTitle.chainOfTitleRecord.put(chainOfTitle.getClientId() + "_" + chainOfTitle.getClientAccountNumber(), chainOfTitle);
    		}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return chainOfTitle;
	}

}
