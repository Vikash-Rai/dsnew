package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PlacedAccountLog;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.PlacedAccountLogValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class PlacedAccountLogProcessor implements ItemProcessor<PlacedAccountLog, PlacedAccountLog> {

    private final Logger logger = LoggerFactory.getLogger(PlacedAccountLogProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public PlacedAccountLog process(PlacedAccountLog pal) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Placed Account Log Id", pal.getPlacedAccountLogId());
			validationMap.put("isPlacedAccountLogValidated", true);

			PlacedAccountLogValidation.mandatoryValidation(pal, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isPlacedAccountLogValidated").toString())) {
				PlacedAccountLogValidation.lookUpValidation(pal, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isPlacedAccountLogValidated").toString())) {
					PlacedAccountLogValidation.standardize(pal);
					PlacedAccountLogValidation.referenceUpdation(pal);
					PlacedAccountLogValidation.misingRefCheck(pal, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isPlacedAccountLogValidated").toString())) {
				pal.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				pal.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pal;
	}
}