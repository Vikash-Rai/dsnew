package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.BankruptcyValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankruptcyProcessor implements ItemProcessor<Bankruptcy, Bankruptcy> {

    @Autowired
    private CacheableService cacheableService;

    private final Logger logger = LoggerFactory.getLogger(BankruptcyProcessor.class);

    @Override
    public Bankruptcy process(Bankruptcy bankruptcy) throws Exception {
        try {
            Map<String, Object> validationMap = new HashMap<String, Object>();
            validationMap.put("Bankruptcy Id", bankruptcy.getBankruptcyId());
            validationMap.put("isBankruptcyValidated", true);
            bankruptcy.setErrCodeJson(null);
            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(bankruptcy.getClientId(), cacheableService);

            BankruptcyValidation.mandatoryValidation(bankruptcy, validationMap, cacheableService, errWarMessagesList);
            if (Boolean.parseBoolean(validationMap.get("isBankruptcyValidated").toString())) {
                BankruptcyValidation.lookUpValidation(bankruptcy, validationMap, cacheableService, errWarMessagesList);

                if (Boolean.parseBoolean(validationMap.get("isBankruptcyValidated").toString())) {
                    BankruptcyValidation.standardize(bankruptcy);
                    BankruptcyValidation.referenceUpdation(bankruptcy);
                    BankruptcyValidation.misingRefCheck(bankruptcy, validationMap, errWarMessagesList);

                    if (Boolean.parseBoolean(validationMap.get("isBankruptcyValidated").toString())) {
                        BankruptcyValidation.businessRule(bankruptcy, validationMap, errWarMessagesList);
                    }
                }
            }

            logger.debug("{}", validationMap);
            if (!Boolean.parseBoolean(validationMap.get("isBankruptcyValidated").toString())) {
                bankruptcy.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
            } else {
                bankruptcy.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return bankruptcy;
    }
}
