package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.dao.PaymentDao;
import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.repository.PaymentPlanRepository;
import com.equabli.domain.helpers.CommonUtils;

public class PaymentPlanWriter implements ItemWriter<PaymentPlan> {

    private final Logger logger = LoggerFactory.getLogger(PaymentPlanWriter.class);

    @Autowired
    private PaymentPlanRepository paymentPlanRepository;
    
    @Autowired
    PaymentDao paymentDao;
    
    @Override
    public void write(Chunk<? extends PaymentPlan> paymentPlan) throws Exception {
    	paymentPlanRepository.saveAll(paymentPlan);
        for (PaymentPlan pp : paymentPlan) {
        	if(!CommonUtils.isObjectNull( pp.getErrCodeJson())) {
        		try {
        			paymentDao.updatePaymentPlanErrCodeJson(pp);
        		}catch (Exception e) {
        			logger.error(e.getMessage(), e);
				}
        	}
            logger.debug("Writer executed.....PaymentPlan Id={}.....PaymentPlan Status={}", pp.getPaymentPlanId(), pp.getRecordStatusId());
        }
    }
}