package com.equabli.collectprism.processor;

import com.equabli.collectprism.entity.Garnishment;
import com.equabli.collectprism.repository.PaymentPlanRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.GarnishmentValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GarnishmentProcessor implements ItemProcessor<Garnishment, Garnishment> {

    private final Logger logger = LoggerFactory.getLogger(GarnishmentProcessor.class);

    @Autowired
	private CacheableService cacheableService;
    
    @Autowired
    private PaymentPlanRepository paymentPlanRepository;

	@Override
	public Garnishment process(Garnishment garnishment) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Garnishment Id", garnishment.getLegalGarnishmentId());
			validationMap.put("isGarnishmentValidated", true);

			garnishment.setErrCodeJson(null);

			List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(garnishment.getClientId(), cacheableService);

			GarnishmentValidation.mandatoryValidation(garnishment, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isGarnishmentValidated").toString())) {
				GarnishmentValidation.lookUpValidation(garnishment, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isGarnishmentValidated").toString())) {
					GarnishmentValidation.standardize(garnishment);
					GarnishmentValidation.referenceUpdation(garnishment);
					GarnishmentValidation.misingRefCheck(garnishment, validationMap, errWarMessagesList);
					GarnishmentValidation.businessRule(garnishment, validationMap ,paymentPlanRepository.getPaymentPlanForGarnishment(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(), garnishment.getClientId(), garnishment.getClientAccountNumber() , garnishment.getPartnerPlanNumber()),errWarMessagesList );
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isGarnishmentValidated").toString())) {
				garnishment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				garnishment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return garnishment;
	}
}