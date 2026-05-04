package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.Complaint;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.ComplaintValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintProcessor implements ItemProcessor<Complaint, Complaint> {

    @Autowired
    private CacheableService cacheableService;

    private final Logger logger = LoggerFactory.getLogger(ComplaintProcessor.class);

    @Override
    public Complaint process(Complaint complaint) throws Exception {
        try {
            Map<String, Object> validationMap = new HashMap<String, Object>();
            validationMap.put("Complaint Id", complaint.getComplaintId());
            validationMap.put("isComplaintValidated", true);
            complaint.setErrCodeJson(null);
            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(complaint.getClientId(), cacheableService);

            ComplaintValidation.mandatoryValidation(complaint, validationMap, cacheableService, errWarMessagesList);
            if (Boolean.parseBoolean(validationMap.get("isComplaintValidated").toString())) {
                ComplaintValidation.lookUpValidation(complaint, validationMap, cacheableService, errWarMessagesList);

                if (Boolean.parseBoolean(validationMap.get("isComplaintValidated").toString())) {
                    ComplaintValidation.referenceUpdation(complaint);
                    ComplaintValidation.misingRefCheck(complaint, validationMap, errWarMessagesList);

                    if (Boolean.parseBoolean(validationMap.get("isComplaintValidated").toString())) {
                        ComplaintValidation.businessRule(complaint, validationMap, errWarMessagesList);
                    }
                }
            }

            logger.debug("{}", validationMap);
            if (!Boolean.parseBoolean(validationMap.get("isComplaintValidated").toString())) {
                complaint.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
            } else {
                complaint.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return complaint;
    }
}
