package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.CoTOwner;
import com.equabli.collectprism.validation.CoTOwnerValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class CoTOwnerProcessor implements ItemProcessor<CoTOwner, CoTOwner> {

    private final Logger logger = LoggerFactory.getLogger(PartnerAssignmentProcessor.class);

    @Override
    public CoTOwner process(CoTOwner cotOwner) throws Exception {
        try {
            Map<String, Object> validationMap = new HashMap<String, Object>();
            validationMap.put("CoTOwner Id", cotOwner.getCotOwnerId());
            validationMap.put("isCoTOwnerValidated", true);

            CoTOwnerValidation.mandatoryValidation(cotOwner, validationMap);

            if (Boolean.parseBoolean(validationMap.get("isCoTOwnerValidated").toString())) {
                CoTOwnerValidation.lookUpValidation(cotOwner,validationMap);

                if (Boolean.parseBoolean(validationMap.get("isCoTOwnerValidated").toString())) {
                    CoTOwnerValidation.standardize(cotOwner);
                }
            }

            logger.debug("{}", validationMap);

			if(cotOwner.getRecordStatusId() != ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) {
	            if (!Boolean.parseBoolean(validationMap.get("isCoTOwnerValidated").toString())) {
	                cotOwner.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
	            } else {
	                cotOwner.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
	            }
			}
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return cotOwner;
    }
}