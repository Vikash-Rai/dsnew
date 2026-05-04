package com.equabli.collectprism.reader;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.repository.PaymentPlanRepository;
import com.equabli.domain.entity.ConfRecordStatus;

public class PaymentPlanReader implements ItemReader<PaymentPlan> {

    private final Logger logger = LoggerFactory.getLogger(PaymentPlanReader.class);

    @Autowired
    private PaymentPlanRepository paymentPlanRepository;

    private Iterator<PaymentPlan> paymentPlanIterator;

    @BeforeStep
    public void before(StepExecution stepExecution) {
    	List<PaymentPlan> paymentplanList = paymentPlanRepository.getPaymentPlanToProcess(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId(), 
    			ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), "paymentPlanJob");
    	paymentplanList.addAll(paymentPlanRepository.getPaymentPlanToProcessAfterUpdate(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(), "paymentPlanJob"));
    	paymentplanList.addAll(paymentPlanRepository.getPaymentPlanToProcessafterPayScheduleUpdate(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(), "paymentPlanJob"));
    	paymentPlanIterator = paymentplanList.iterator();    	
    }

    @Override
    public PaymentPlan read() throws Exception {
        if (paymentPlanIterator != null && paymentPlanIterator.hasNext()) {
        	PaymentPlan pp = paymentPlanIterator.next();
            logger.debug("Reader executed.....PaymentPlan Id={}.....PaymentPlan Status={}", pp.getPaymentPlanId(), pp.getRecordStatusId());
            return pp;
        } else {
            logger.info("Reader executed completely");
            return null;
        }
    }
}