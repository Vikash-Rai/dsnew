package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.AccountAdditionalInfo;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.AccountAdditionalInfoValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class AccountAdditionalInfoProcessor implements ItemProcessor<AccountAdditionalInfo, AccountAdditionalInfo>  {

	private final Logger logger = LoggerFactory.getLogger(AccountAdditionalInfoProcessor.class);

	@Autowired
	private CacheableService cacheableService;

	@Override
	public AccountAdditionalInfo process(AccountAdditionalInfo accountAdditionalInfo) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("AccountAdditionalInfo Id", accountAdditionalInfo.getAccountAdditionalInfoId());
			validationMap.put("isAccountAdditionalInfoValidated", true);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(accountAdditionalInfo.getClientId(), cacheableService);

			AccountAdditionalInfoValidation.mandatoryValidation(accountAdditionalInfo, validationMap, cacheableService,errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isAccountAdditionalInfoValidated").toString())) {
				AccountAdditionalInfoValidation.lookUpValidation(accountAdditionalInfo, validationMap, cacheableService, errWarMessagesList);
				if(Boolean.parseBoolean(validationMap.get("isAccountAdditionalInfoValidated").toString())) {
					AccountAdditionalInfoValidation.standardize(accountAdditionalInfo);
					AccountAdditionalInfoValidation.referenceUpdation(accountAdditionalInfo);
					AccountAdditionalInfoValidation.misingRefCheck(accountAdditionalInfo, validationMap, errWarMessagesList);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isAccountAdditionalInfoValidated").toString())) {
				accountAdditionalInfo.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				accountAdditionalInfo.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return accountAdditionalInfo;
	}
}