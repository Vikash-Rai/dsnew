package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class PaymentWriter implements ItemWriter<Payment> {

    private final Logger logger = LoggerFactory.getLogger(PaymentWriter.class);

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Override
    public void write(Chunk<? extends Payment> payment) throws Exception {
    	paymentRepository.saveAll(payment);
        for (Payment pay : payment) {
            logger.debug("Writer executed.....Payment Id={}.....Payment Status={}", pay.getPaymentId(), pay.getRecordStatusId());
    		if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NSF) 
    				&& !CommonUtils.isLongNull(pay.getParentPaymentIds())) {
    			paymentRepository.updatePaymentReversalDate(pay.getParentPaymentIds());
    		}
        }
    }
}