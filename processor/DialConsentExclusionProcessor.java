package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.DialConsentExclusion;
import com.equabli.collectprism.validation.DialConsentExclusionValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class DialConsentExclusionProcessor implements ItemProcessor<DialConsentExclusion, DialConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(DialConsentExclusionProcessor.class);

    private JobParameters jobParameters;

	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }

	@Override
	public DialConsentExclusion process(DialConsentExclusion dce) throws Exception {
		try {
			Integer recordSourceId = Integer.parseInt(jobParameters.getString("recordSourceId"));

			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("DialConsentExclusion Id", dce.getDialConsentExclusionId());
			validationMap.put("isDialConsentExclusionValidated", true);

			DialConsentExclusionValidation.mandatoryValidation(dce, validationMap);

			if(Boolean.parseBoolean(validationMap.get("isDialConsentExclusionValidated").toString())) {
				DialConsentExclusionValidation.referenceUpdation(dce, recordSourceId);
				DialConsentExclusionValidation.misingRefCheck(dce, validationMap);
			}

			logger.debug("{}",validationMap);
			if(Boolean.parseBoolean(validationMap.get("isDialConsentExclusionValidated").toString())) {
				dce.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			} else {
				dce.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dce;
	}
}