package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.CreditScore;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.CreditScoreValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class CreditScoreProcessor  implements ItemProcessor<CreditScore, CreditScore>{

	  private final Logger logger = LoggerFactory.getLogger(CreditScoreProcessor.class);

	    @Autowired
		private CacheableService cacheableService;

		@Override
		public CreditScore process(CreditScore creditScore) throws Exception {
			try {
				Map<String, Object> validationMap = new HashMap<String, Object>();
				validationMap.put("CreditScore Id", creditScore.getCreditScoreId());
				validationMap.put("isCreditScoreValidated", true);

				CreditScoreValidation.mandatoryValidation(creditScore, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isCreditScoreValidated").toString())) {
					CreditScoreValidation.lookUpValidation(creditScore, validationMap, cacheableService);

					if(Boolean.parseBoolean(validationMap.get("isCreditScoreValidated").toString())) {
						CreditScoreValidation.standardize(creditScore);
						CreditScoreValidation.referenceUpdation(creditScore);
						CreditScoreValidation.misingRefCheck(creditScore, validationMap);
					}
				}

				logger.debug("{}",validationMap);
				if(!Boolean.parseBoolean(validationMap.get("isCreditScoreValidated").toString())) {
					creditScore.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					creditScore.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return creditScore;
		}
}
