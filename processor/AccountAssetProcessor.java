package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.AccountAsset;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.AccountAssetValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class AccountAssetProcessor implements ItemProcessor<AccountAsset, AccountAsset> {

    private final Logger logger = LoggerFactory.getLogger(AccountAssetProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public AccountAsset process(AccountAsset accAsset) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("AccountAsset Id", accAsset.getAccountUccId());
			validationMap.put("isAccountAssetValidated", true);

			AccountAssetValidation.mandatoryValidation(accAsset, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isAccountAssetValidated").toString())) {
				AccountAssetValidation.lookUpValidation(accAsset, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isAccountAssetValidated").toString())) {
					AccountAssetValidation.standardize(accAsset);
					AccountAssetValidation.referenceUpdation(accAsset);
					AccountAssetValidation.misingRefCheck(accAsset, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isAccountAssetValidated").toString())) {
				accAsset.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				accAsset.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return accAsset;
	}
}