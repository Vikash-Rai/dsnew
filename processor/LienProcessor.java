package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.Lien;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.LienValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LienProcessor implements ItemProcessor<Lien, Lien> {
    private final Logger logger = LoggerFactory.getLogger(LienProcessor.class);

    @Autowired
    private CacheableService cacheableService;

    @Override
    public Lien process(Lien lien) throws Exception {
        try {
            Map<String, Object> validationMap = new HashMap<String, Object>();
            validationMap.put("Lien Id", lien.getLienId());
            validationMap.put("isLienValidated", true);

            lien.setErrCodeJson(null);

            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(lien.getClientId(), cacheableService);

            LienValidation.mandatoryValidation(lien, validationMap, cacheableService, errWarMessagesList);

            if (Boolean.parseBoolean(validationMap.get("isLienValidated").toString())) {
                LienValidation.lookUpValidation(lien, validationMap, cacheableService, errWarMessagesList);

                if (Boolean.parseBoolean(validationMap.get("isLienValidated").toString())) {
                    LienValidation.standardize(lien);
                    LienValidation.referenceUpdation(lien);
                    LienValidation.misingRefCheck(lien, validationMap, errWarMessagesList);

                    if (Boolean.parseBoolean(validationMap.get("isLienValidated").toString())) {
                        LienValidation.businessRule(lien, validationMap, errWarMessagesList);
                    }
                }
            }

            logger.debug("{}", validationMap);
            if (!Boolean.parseBoolean(validationMap.get("isLienValidated").toString())) {
                lien.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
            } else {
                lien.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return lien;
    }

}
