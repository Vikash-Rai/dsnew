package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.dao.ChangeLogDao;
import com.equabli.collectprism.entity.ChangeLog;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.ChangeLogValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class ChangeLogProcessor implements ItemProcessor<ChangeLog, ChangeLog>  {

	  private final Logger logger = LoggerFactory.getLogger(ChangeLogProcessor.class);

	    @Autowired
		private CacheableService cacheableService;
	    
	    @Autowired
		private ChangeLogDao changeLogDao;

		@Override
		public ChangeLog process(ChangeLog changeLog) throws Exception {
			try {
				Map<String, Object> validationMap = new HashMap<String, Object>();
				validationMap.put("ChangeLog Id", changeLog.getChangeLogId());
				validationMap.put("isChangeLogValidated", true);

				changeLog.setErrCodeJson(null);
				
				List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(changeLog.getClientId(), cacheableService);
				
				ChangeLogValidation.mandatoryValidation(changeLog, validationMap, cacheableService,errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isChangeLogValidated").toString())) {
					ChangeLogValidation.lookUpValidation(changeLog, validationMap, cacheableService, errWarMessagesList);
					if(Boolean.parseBoolean(validationMap.get("isChangeLogValidated").toString())) {
						ChangeLogValidation.standardize(changeLog);
						ChangeLogValidation.referenceUpdation(changeLog);
						ChangeLogValidation.misingRefCheck(changeLog, validationMap, errWarMessagesList);
					}
					
					if(Boolean.parseBoolean(validationMap.get("isChangeLogValidated").toString())) {
						ChangeLogValidation.businessRule(changeLog, validationMap, errWarMessagesList,changeLogDao);
					}
				}

				logger.debug("{}",validationMap);
				if(!Boolean.parseBoolean(validationMap.get("isChangeLogValidated").toString())) {
					changeLog.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					changeLog.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return changeLog;
		}



}
