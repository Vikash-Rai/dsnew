package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.OperationResponse;
import com.equabli.collectprism.validation.OperationResponseValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class OperationResponseProcessor implements ItemProcessor<OperationResponse, OperationResponse> {

    private final Logger logger = LoggerFactory.getLogger(OperationResponseProcessor.class);

	@Override
	public OperationResponse process(OperationResponse or) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("OperationResponse Id", or.getOperationResponseId());
			validationMap.put("isOperationResponseValidated", true);

			OperationResponseValidation.mandatoryValidation(or, validationMap);

			if(Boolean.parseBoolean(validationMap.get("isOperationResponseValidated").toString())) {
				OperationResponseValidation.lookUpValidation(or, validationMap);

				if(Boolean.parseBoolean(validationMap.get("isOperationResponseValidated").toString())) {
					OperationResponseValidation.standardize(or);
					OperationResponseValidation.referenceUpdation(or);
					OperationResponseValidation.misingRefCheck(or, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isOperationResponseValidated").toString())) {
				or.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				or.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return or;
	}
}