package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.AccountUcc;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.AccountUccValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class AccountUccProcessor implements ItemProcessor<AccountUcc, AccountUcc> {

    private final Logger logger = LoggerFactory.getLogger(AccountUccProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public AccountUcc process(AccountUcc accUcc) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("AccountUcc Id", accUcc.getAccountUccId());
			validationMap.put("isAccountUccValidated", true);

			AccountUccValidation.mandatoryValidation(accUcc, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isAccountUccValidated").toString())) {
				AccountUccValidation.lookUpValidation(accUcc, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isAccountUccValidated").toString())) {
					AccountUccValidation.standardize(accUcc);
					AccountUccValidation.referenceUpdation(accUcc);
					AccountUccValidation.misingRefCheck(accUcc, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isAccountUccValidated").toString())) {
				accUcc.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				accUcc.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return accUcc;
	}
}