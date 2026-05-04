package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ChargeOffAccount;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.ChargeOffAccountValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class ChargeOffAccountProcessor  implements ItemProcessor<ChargeOffAccount, ChargeOffAccount>  {
	
	  private final Logger logger = LoggerFactory.getLogger(ChargeOffAccountProcessor.class);

	    @Autowired
		private CacheableService cacheableService;

		@Override
		public ChargeOffAccount process(ChargeOffAccount chargeOff) throws Exception {
			try {
				Map<String, Object> validationMap = new HashMap<String, Object>();
				validationMap.put("ChargeOff Id", chargeOff.getChargeOffAccountId());
				validationMap.put("isChargeOffAccountValidated", true);

				chargeOff.setErrCodeJson(null);
				
				List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(chargeOff.getClientId(), cacheableService);
				
				ChargeOffAccountValidation.mandatoryValidation(chargeOff, validationMap, cacheableService,errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isChargeOffAccountValidated").toString())) {
					ChargeOffAccountValidation.lookUpValidation(chargeOff, validationMap, cacheableService, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isChargeOffAccountValidated").toString())) {
						ChargeOffAccountValidation.standardize(chargeOff);
						ChargeOffAccountValidation.referenceUpdation(chargeOff);
						ChargeOffAccountValidation.misingRefCheck(chargeOff, validationMap, errWarMessagesList);
					}
					
					if(Boolean.parseBoolean(validationMap.get("isChargeOffAccountValidated").toString())) {
						ChargeOffAccountValidation.businessRule(chargeOff, validationMap, errWarMessagesList);
					}
				}

				logger.debug("{}",validationMap);
				if(!Boolean.parseBoolean(validationMap.get("isChargeOffAccountValidated").toString())) {
					chargeOff.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
				} else {
					chargeOff.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			return chargeOff;
		}




}
