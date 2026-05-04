package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ServicingDetail;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.ServicingDetailValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class ServicingDetailProcessor implements ItemProcessor<ServicingDetail, ServicingDetail> {

    private final Logger logger = LoggerFactory.getLogger(ServicingDetailProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public ServicingDetail process(ServicingDetail sd) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Servicing Detail Id", sd.getServicingDetailId());
			validationMap.put("isServicingDetailValidated", true);

			ServicingDetailValidation.mandatoryValidation(sd, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isServicingDetailValidated").toString())) {
				ServicingDetailValidation.lookUpValidation(sd, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isServicingDetailValidated").toString())) {
					ServicingDetailValidation.referenceUpdation(sd);
					ServicingDetailValidation.misingRefCheck(sd, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isServicingDetailValidated").toString())) {
				sd.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				sd.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sd;
	}
}