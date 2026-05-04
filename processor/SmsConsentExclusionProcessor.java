package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.SmsConsentExclusion;
import com.equabli.collectprism.validation.SmsConsentExclusionValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class SmsConsentExclusionProcessor implements ItemProcessor<SmsConsentExclusion, SmsConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(SmsConsentExclusionProcessor.class);

    private JobParameters jobParameters;

	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }

	@Override
	public SmsConsentExclusion process(SmsConsentExclusion sce) throws Exception {
		try {
			Integer recordSourceId = Integer.parseInt(jobParameters.getString("recordSourceId"));

			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("SmsConsentExclusion Id", sce.getSmsConsentExclusionId());
			validationMap.put("isSmsConsentExclusionValidated", true);

			SmsConsentExclusionValidation.mandatoryValidation(sce, validationMap);

			if(Boolean.parseBoolean(validationMap.get("isSmsConsentExclusionValidated").toString())) {
				SmsConsentExclusionValidation.referenceUpdation(sce, recordSourceId);
				SmsConsentExclusionValidation.misingRefCheck(sce, validationMap);
			}

			logger.debug("{}",validationMap);
			if(Boolean.parseBoolean(validationMap.get("isSmsConsentExclusionValidated").toString())) {
				sce.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			} else {
				sce.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sce;
	}
}