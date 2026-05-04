package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.AccountTag;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.AccountTagValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class AccountTagProcessor implements ItemProcessor<AccountTag, AccountTag> {

    private final Logger logger = LoggerFactory.getLogger(AccountTagProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public AccountTag process(AccountTag accountTag) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("AccountTag Id", accountTag.getAccountTagId());
			validationMap.put("isAccountTagValidated", true);

			AccountTagValidation.mandatoryValidation(accountTag, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isAccountTagValidated").toString())) {
				AccountTagValidation.lookUpValidation(accountTag, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isAccountTagValidated").toString())) {
					AccountTagValidation.standardize(accountTag);
					AccountTagValidation.referenceUpdation(accountTag);
					AccountTagValidation.misingRefCheck(accountTag, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isAccountTagValidated").toString())) {
				accountTag.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				accountTag.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return accountTag;
	}

}