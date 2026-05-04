package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Employer;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.EmployerValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class EmployerProcessor implements ItemProcessor<Employer, Employer> {

    private final Logger logger = LoggerFactory.getLogger(EmployerProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public Employer process(Employer emp) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Employer Id", emp.getEmployerId());
			validationMap.put("isEmployerValidated", true);

			emp.setErrCodeJson(null);

    		List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(emp.getClientId(), cacheableService);

			EmployerValidation.mandatoryValidation(emp, validationMap, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isEmployerValidated").toString())) {
				EmployerValidation.lookUpValidation(emp, validationMap, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isEmployerValidated").toString())) {
					EmployerValidation.standardize(emp);
					EmployerValidation.deDupValidation(emp, validationMap, errWarMessagesList);
					EmployerValidation.referenceUpdation(emp);
					EmployerValidation.misingRefCheck(emp, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isEmployerValidated").toString())) {
						EmployerValidation.businessRule(emp, validationMap, errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isEmployerValidated").toString())) {
				emp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				emp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return emp;
	}
}