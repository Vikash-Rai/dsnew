package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.util.DataScrubbingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.CommunicationDetail;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.CommunicationDetailValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class CommunicationDetailProcessor implements ItemProcessor<CommunicationDetail, CommunicationDetail> {

    private final Logger logger = LoggerFactory.getLogger(CommunicationDetailProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public CommunicationDetail process(CommunicationDetail detail) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("CommunicationDetail Id", detail.getCommunicationDetailId());
			validationMap.put("isCommunicationDetailValidated", true);

			detail.setErrCodeJson(null);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(detail.getClientId(), cacheableService);

			CommunicationDetailValidation.mandatoryValidation(detail, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isCommunicationDetailValidated").toString())) {
				CommunicationDetailValidation.lookUpValidation(detail, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isCommunicationDetailValidated").toString())) {
					CommunicationDetailValidation.standardize(detail);
					CommunicationDetailValidation.referenceUpdation(detail);
					CommunicationDetailValidation.misingRefCheck(detail, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isCommunicationDetailValidated").toString())) {
						CommunicationDetailValidation.businessRule(detail, validationMap, errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isCommunicationDetailValidated").toString())) {
				detail.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				detail.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return detail;
	}
}