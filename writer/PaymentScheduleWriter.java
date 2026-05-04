package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PaymentSchedule;
import com.equabli.collectprism.repository.PaymentScheduleRepository;

public class PaymentScheduleWriter implements ItemWriter<PaymentSchedule> {

    private final Logger logger = LoggerFactory.getLogger(PaymentScheduleWriter.class);

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Override
    public void write(Chunk<? extends PaymentSchedule> paymentSchedule) throws Exception {
    	paymentScheduleRepository.saveAll(paymentSchedule);
        for (PaymentSchedule ps : paymentSchedule) {
            logger.debug("Writer executed.....PaymentSchedule Id={}.....PaymentSchedule Status={}", ps.getPaymentScheduleId(), ps.getRecordStatusId());
        }
    }
}