package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Deceased;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.DeceasedValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class DeceasedProcessor implements ItemProcessor<Deceased, Deceased> {

    private final Logger logger = LoggerFactory.getLogger(DeceasedProcessor.class);

    @Autowired
	private CacheableService cacheableService;
    
	@Override
	public Deceased process(Deceased deceased) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Deceased Id", deceased.getDeceasedId());
			validationMap.put("isDeceasedValidated", true);

			deceased.setErrCodeJson(null);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(deceased.getClientId(), cacheableService);

			DeceasedValidation.mandatoryValidation(deceased, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isDeceasedValidated").toString())) {
				DeceasedValidation.lookUpValidation(deceased, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isDeceasedValidated").toString())) {
					DeceasedValidation.standardize(deceased);
					DeceasedValidation.referenceUpdation(deceased);
					DeceasedValidation.misingRefCheck(deceased, validationMap, errWarMessagesList);
					DeceasedValidation.businessRule(deceased, validationMap,errWarMessagesList );
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isDeceasedValidated").toString())) {
				deceased.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				deceased.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return deceased;
	}
	}

