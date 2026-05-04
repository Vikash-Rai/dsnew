package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.OperationRequest;
import com.equabli.collectprism.validation.OperationRequestValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class OperationRequestProcessor implements ItemProcessor<OperationRequest, OperationRequest> {

    private final Logger logger = LoggerFactory.getLogger(OperationRequestProcessor.class);

	@Override
	public OperationRequest process(OperationRequest or) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("OperationRequest Id", or.getOperationRequestId());
			validationMap.put("isOperationRequestValidated", true);

			OperationRequestValidation.mandatoryValidation(or, validationMap);

			if(Boolean.parseBoolean(validationMap.get("isOperationRequestValidated").toString())) {
				OperationRequestValidation.lookUpValidation(or, validationMap);

				if(Boolean.parseBoolean(validationMap.get("isOperationRequestValidated").toString())) {
					OperationRequestValidation.standardize(or);
					OperationRequestValidation.referenceUpdation(or);
					OperationRequestValidation.misingRefCheck(or, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isOperationRequestValidated").toString())) {
				or.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				or.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				if(!CommonUtils.isBooleanNull(or.getIsAutoResponseOnOpRequest()) && or.getIsAutoResponseOnOpRequest() 
						&& or.getRequestSource().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER) 
						&& or.getOperationRequestType().equalsIgnoreCase(CommonConstants.OPERATION_REQUEST_TYPE_PARTNER_REQUEST_TO_RECALL)) {
					if(or.getComplianceIsOpen() > 0 || (!CommonUtils.isIntegerNull(or.getSifPifDays()) && !CommonUtils.isIntegerNull(or.getAutoRecallDays()) && or.getAutoRecallDays() > or.getSifPifDays())) {
						or.setRequestStatus(CommonConstants.OPERATION_REQUEST_STATUS_CANCEL);
					} else {
						or.setRequestStatus(CommonConstants.OPERATION_REQUEST_STATUS_SUCCESSFUL);
					}
					or.setFulfillmentDate(LocalDate.now());
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return or;
	}
}