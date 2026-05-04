package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Cost;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.CostValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class CostProcessor  implements ItemProcessor<Cost, Cost> {

	  private final Logger logger = LoggerFactory.getLogger(CostProcessor.class);

	    @Autowired
		private CacheableService cacheableService;
	    
		@Override
		public Cost process(Cost cost) throws Exception {
			try {
				Map<String, Object> validationMap = new HashMap<String, Object>();
				validationMap.put("Cost Id", cost.getCostId());
				validationMap.put("isCostValidated", true);

				CostValidation.mandatoryValidation(cost, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isCostValidated").toString())) {
					CostValidation.lookUpValidation(cost, validationMap, cacheableService);

					if(Boolean.parseBoolean(validationMap.get("isCostValidated").toString())) {
						CostValidation.standardize(cost);
						CostValidation.referenceUpdation(cost);
						CostValidation.misingRefCheck(cost, validationMap);
					}
					
					if(Boolean.parseBoolean(validationMap.get("isCostValidated").toString())) {
						CostValidation.businessRule(cost, validationMap);
					}
				}

				logger.debug("{}",validationMap);
				if(!Boolean.parseBoolean(validationMap.get("isCostValidated").toString())) {
					cost.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					cost.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return cost;
		}


}
