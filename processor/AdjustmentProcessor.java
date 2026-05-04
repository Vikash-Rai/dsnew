package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.AdjustmentValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class AdjustmentProcessor implements ItemProcessor<Adjustment, Adjustment> {

    private final Logger logger = LoggerFactory.getLogger(AdjustmentProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public Adjustment process(Adjustment ad) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Adjustment Id", ad.getAdjustmentId());
			validationMap.put("isAdjustmentValidated", true);
			ad.setErrCodeJson(null);
            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(ad.getClientId(), cacheableService);

			AdjustmentValidation.mandatoryValidation(ad, validationMap, cacheableService, errWarMessagesList);
			if(Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
				AdjustmentValidation.lookUpValidation(ad, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
					AdjustmentValidation.standardize(ad);
					AdjustmentValidation.referenceUpdation(ad);
					AdjustmentValidation.misingRefCheck(ad, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
						AdjustmentValidation.businessRule(ad, validationMap, errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
				ad.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				ad.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ad;
	}
}