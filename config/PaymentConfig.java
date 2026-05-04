package com.equabli.collectprism.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.processor.PaymentProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.writer.PaymentWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class PaymentConfig {

    

    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<Payment> normalPaymentReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Payment> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        HashMap<String, Direction> sorts = new LinkedHashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add(CommonConstants.PAYMENT_TYPE_NORMAL);
        sorts.put("clientId", Direction.DESC);
        sorts.put("clientAccountNumber", Direction.DESC);
        sorts.put("paymentDate", Direction.ASC);
        sorts.put("amtBalance", Direction.DESC);

        reader.setRepository(paymentRepository);
        reader.setMethodName("getPaymentToProcess");
        reader.setJobName("Normal Payment Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public CustomItemReader<Payment> nsfPaymentReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Payment> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        HashMap<String, Direction> sorts = new LinkedHashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add(CommonConstants.PAYMENT_TYPE_NSF);
        sorts.put("clientId", Direction.DESC);
        sorts.put("clientAccountNumber", Direction.DESC);
        sorts.put("paymentDate", Direction.ASC);
        sorts.put("amtBalance", Direction.DESC);

        reader.setRepository(paymentRepository);
        reader.setMethodName("getPaymentToProcess");
        reader.setJobName("Nsf Payment Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public ItemProcessor<Payment, Payment> paymentProcessor() {
        return new PaymentProcessor();
    }

    @Bean
    public ItemWriter<Payment> paymentWriter() {
        return new PaymentWriter();
    }

    @Bean
    protected Step normalProcessPayment(@Qualifier("normalPaymentReader") ItemReader<Payment> itemReader, ItemProcessor<Payment, Payment> processor, ItemWriter<Payment> writer) {
        return new StepBuilder("normalProcessPayment",jobRepository).<Payment, Payment> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    protected Step nsfProcessPayment(@Qualifier("nsfPaymentReader") ItemReader<Payment> itemReader, ItemProcessor<Payment, Payment> processor, ItemWriter<Payment> writer) {
        return new StepBuilder("nsfProcessPayment",jobRepository).<Payment, Payment> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job normalPaymentJob() {
//        return new JobBuilder("normalPaymentJob",jobRepository)
//          .start(normalProcessPayment(normalPaymentReader(), paymentProcessor(), paymentWriter()))
//          .build();
//    }
//
//    @Bean
//    public Job nsfPaymentJob() {
//        return new JobBuilder("nsfPaymentJob",jobRepository)
//          .start(nsfProcessPayment(nsfPaymentReader(), paymentProcessor(), paymentWriter()))
//          .build();
//    }
    
    
    @Bean
    public Job normalPaymentJob(Step normalProcessPayment) {
        return new JobBuilder("normalPaymentJob", jobRepository)
                .start(normalProcessPayment)
                .build();
    }
    
    
    @Bean
    public Job nsfPaymentJob(Step nsfProcessPayment) {
        return new JobBuilder("nsfPaymentJob", jobRepository)
                .start(nsfProcessPayment)
                .build();
    }
}