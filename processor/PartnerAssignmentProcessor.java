package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PartnerAssignment;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.PartnerAssignmentValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class PartnerAssignmentProcessor implements ItemProcessor<PartnerAssignment, PartnerAssignment> {

    private final Logger logger = LoggerFactory.getLogger(PartnerAssignmentProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public PartnerAssignment process(PartnerAssignment partnerAssignment) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("PartnerAssignment Id", partnerAssignment.getPartnerAssignmentId());
			validationMap.put("isPartnerAssignmentValidated", true);

			PartnerAssignmentValidation.mandatoryValidation(partnerAssignment, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isPartnerAssignmentValidated").toString())) {
				PartnerAssignmentValidation.lookUpValidation(partnerAssignment, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isPartnerAssignmentValidated").toString())) {
					PartnerAssignmentValidation.standardize(partnerAssignment);
					PartnerAssignmentValidation.referenceUpdation(partnerAssignment);
					PartnerAssignmentValidation.misingRefCheck(partnerAssignment, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isPartnerAssignmentValidated").toString())) {
				partnerAssignment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				partnerAssignment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return partnerAssignment;
	}

}