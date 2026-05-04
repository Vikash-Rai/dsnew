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

import com.equabli.collectprism.entity.DialConsentExclusion;
import com.equabli.collectprism.processor.DialConsentExclusionProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.DialConsentExclusionRepository;
import com.equabli.collectprism.writer.DialConsentExclusionWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class DialConsentExclusionConfig {


    @Autowired
    private DialConsentExclusionRepository dialConsentExclusionRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<DialConsentExclusion> dialConsentExclusionReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<DialConsentExclusion> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("dialConsentExclusionId", Direction.ASC);

        reader.setRepository(dialConsentExclusionRepository);
        reader.setMethodName("getDialConsentExclusionToProcess");
        reader.setJobName("DialConsentExclusion Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<DialConsentExclusion, DialConsentExclusion> dialConsentExclusionProcessor() {
        return new DialConsentExclusionProcessor();
    }

    @Bean
    public ItemWriter<DialConsentExclusion> dialConsentExclusionWriter() {
        return new DialConsentExclusionWriter();
    }

    @Bean
    protected Step processDialConsentExclusion(ItemReader<DialConsentExclusion> itemReader, ItemProcessor<DialConsentExclusion, DialConsentExclusion> processor, ItemWriter<DialConsentExclusion> writer) {
        return new StepBuilder("processDialConsentExclusion",jobRepository).<DialConsentExclusion, DialConsentExclusion> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }
//
//    @Bean
//    public Job dialConsentExclusionJob() {
//        return new JobBuilder("dialConsentExclusionJob",jobRepository)
//          .start(processDialConsentExclusion(dialConsentExclusionReader(), dialConsentExclusionProcessor(), dialConsentExclusionWriter()))
//          .build();
//    }
    
    @Bean
    public Job dialConsentExclusionJob(Step processDialConsentExclusion) {
        return new JobBuilder("dialConsentExclusionJob", jobRepository)
                .start(processDialConsentExclusion)
                .build();
    }
}