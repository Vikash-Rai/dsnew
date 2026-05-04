package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;

import com.equabli.collectprism.entity.EmailConsentExclusion;
import com.equabli.collectprism.validation.EmailConsentExclusionValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class EmailConsentExclusionProcessor implements ItemProcessor<EmailConsentExclusion, EmailConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(EmailConsentExclusionProcessor.class);

    private JobParameters jobParameters;

	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }

	@Override
	public EmailConsentExclusion process(EmailConsentExclusion ece) throws Exception {
		try {
			Integer recordSourceId = Integer.parseInt(jobParameters.getString("recordSourceId"));

			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("EmailConsentExclusion Id", ece.getEmailConsentExclusionId());
			validationMap.put("isEmailConsentExclusionValidated", true);

			EmailConsentExclusionValidation.mandatoryValidation(ece, validationMap);

			if(Boolean.parseBoolean(validationMap.get("isEmailConsentExclusionValidated").toString())) {
				EmailConsentExclusionValidation.referenceUpdation(ece, recordSourceId);
				EmailConsentExclusionValidation.standardize(ece);
				EmailConsentExclusionValidation.misingRefCheck(ece, validationMap);
			}

			logger.debug("{}",validationMap);
			if(Boolean.parseBoolean(validationMap.get("isEmailConsentExclusionValidated").toString())) {
				ece.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			} else {
				ece.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ece;
	}
}