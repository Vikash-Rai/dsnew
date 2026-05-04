package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.Dispute;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.DisputeValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisputeProcessor implements ItemProcessor<Dispute, Dispute> {

    @Autowired
    private CacheableService cacheableService;

    private final Logger logger = LoggerFactory.getLogger(DisputeProcessor.class);

    @Override
    public Dispute process(Dispute dispute) throws Exception {
        try {
            Map<String, Object> validationMap = new HashMap<String, Object>();
            validationMap.put("Dispute Id", dispute.getDisputeId());
            validationMap.put("isDisputeValidated", true);
            dispute.setErrCodeJson(null);
            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(dispute.getClientId(), cacheableService);

            DisputeValidation.mandatoryValidation(dispute, validationMap, cacheableService, errWarMessagesList);
            if (Boolean.parseBoolean(validationMap.get("isDisputeValidated").toString())) {
                DisputeValidation.lookUpValidation(dispute, validationMap, cacheableService, errWarMessagesList);

                if (Boolean.parseBoolean(validationMap.get("isDisputeValidated").toString())) {
                    DisputeValidation.standardize(dispute);
                    DisputeValidation.referenceUpdation(dispute);
                    DisputeValidation.misingRefCheck(dispute, validationMap, errWarMessagesList);

                    if (Boolean.parseBoolean(validationMap.get("isDisputeValidated").toString())) {
                        DisputeValidation.businessRule(dispute, validationMap, errWarMessagesList);
                    }
                }
            }

            logger.debug("{}", validationMap);
            if (!Boolean.parseBoolean(validationMap.get("isDisputeValidated").toString())) {
                dispute.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
            } else {
                dispute.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dispute;
    }
}
