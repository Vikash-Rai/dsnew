package com.equabli.collectprism.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.processor.PaymentPlanProcessor;
import com.equabli.collectprism.reader.PaymentPlanReader;
import com.equabli.collectprism.writer.PaymentPlanWriter;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class PaymentPlanConfig {

	@Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;
   

    @Bean
    public ItemReader<PaymentPlan> paymentPlanReader() {
        return new PaymentPlanReader();
    }

    @Bean
    public ItemProcessor<PaymentPlan, PaymentPlan> paymentPlanProcessor() {
        return new PaymentPlanProcessor();
    }

    @Bean
    public ItemWriter<PaymentPlan> paymentPlanWriter() {
        return new PaymentPlanWriter();
    }

    @Bean
    protected Step processPaymentPlan(ItemReader<PaymentPlan> itemReader, ItemProcessor<PaymentPlan, PaymentPlan> processor, ItemWriter<PaymentPlan> writer) {
        return new StepBuilder("processPaymentPlan",jobRepository).<PaymentPlan, PaymentPlan> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    public Job paymentPlanJob() {
        return new JobBuilder("paymentPlanJob",jobRepository)
          .start(processPaymentPlan(paymentPlanReader(), paymentPlanProcessor(), paymentPlanWriter()))
          .build();
    }
}