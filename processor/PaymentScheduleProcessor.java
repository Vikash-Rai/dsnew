package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PaymentSchedule;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.validation.PaymentScheduleValidation;
import com.equabli.domain.entity.ConfRecordStatus;

public class PaymentScheduleProcessor implements ItemProcessor<PaymentSchedule, PaymentSchedule> {

    private final Logger logger = LoggerFactory.getLogger(PaymentScheduleProcessor.class);

    @Autowired
	private CacheableService cacheableService;

	@Override
	public PaymentSchedule process(PaymentSchedule ps) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("PaymentSchedule Id", ps.getPaymentScheduleId());
			validationMap.put("isPaymentScheduleValidated", true);

			PaymentScheduleValidation.mandatoryValidation(ps, validationMap, cacheableService);

			if(Boolean.parseBoolean(validationMap.get("isPaymentScheduleValidated").toString())) {
				PaymentScheduleValidation.lookUpValidation(ps, validationMap, cacheableService);

				if(Boolean.parseBoolean(validationMap.get("isPaymentScheduleValidated").toString())) {
					PaymentScheduleValidation.standardize(ps);
					PaymentScheduleValidation.referenceUpdation(ps);
					PaymentScheduleValidation.misingRefCheck(ps, validationMap);
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isPaymentScheduleValidated").toString())) {
				ps.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				ps.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return ps;
	}
}