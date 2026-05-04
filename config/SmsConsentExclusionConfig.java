package com.equabli.collectprism.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.collectprism.entity.SmsConsentExclusion;
import com.equabli.collectprism.processor.SmsConsentExclusionProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.SmsConsentExclusionRepository;
import com.equabli.collectprism.writer.SmsConsentExclusionWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class SmsConsentExclusionConfig {

    

    @Autowired
    private SmsConsentExclusionRepository smsConsentExclusionRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<SmsConsentExclusion> smsConsentExclusionReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<SmsConsentExclusion> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("smsConsentExclusionId", Direction.ASC);

        reader.setRepository(smsConsentExclusionRepository);
        reader.setMethodName("getSmsConsentExclusionToProcess");
        reader.setJobName("SmsConsentExclusion Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<SmsConsentExclusion, SmsConsentExclusion> smsConsentExclusionProcessor() {
        return new SmsConsentExclusionProcessor();
    }

    @Bean
    public ItemWriter<SmsConsentExclusion> smsConsentExclusionWriter() {
        return new SmsConsentExclusionWriter();
    }

    @Bean
    protected Step processSmsConsentExclusion(ItemReader<SmsConsentExclusion> itemReader, ItemProcessor<SmsConsentExclusion, SmsConsentExclusion> processor, ItemWriter<SmsConsentExclusion> writer) {
        return new StepBuilder("processSmsConsentExclusion",jobRepository).<SmsConsentExclusion, SmsConsentExclusion> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job smsConsentExclusionJob() {
//        return new JobBuilder("smsConsentExclusionJob",jobRepository)
//          .start(processSmsConsentExclusion(smsConsentExclusionReader(), smsConsentExclusionProcessor(), smsConsentExclusionWriter()))
//          .build();
//    }
    
    @Bean
    public Job smsConsentExclusionJob(Step processSmsConsentExclusion) {
        return new JobBuilder("smsConsentExclusionJob", jobRepository)
                .start(processSmsConsentExclusion)
                .build();
    }
}