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

import com.equabli.collectprism.entity.EmailConsentExclusion;
import com.equabli.collectprism.processor.EmailConsentExclusionProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.EmailConsentExclusionRepository;
import com.equabli.collectprism.writer.EmailConsentExclusionWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class EmailConsentExclusionConfig {

 

    @Autowired
    private EmailConsentExclusionRepository emailConsentExclusionRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<EmailConsentExclusion> emailConsentExclusionReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<EmailConsentExclusion> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("emailConsentExclusionId", Direction.ASC);

        reader.setRepository(emailConsentExclusionRepository);
        reader.setMethodName("getEmailConsentExclusionToProcess");
        reader.setJobName("EmailConsentExclusion Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<EmailConsentExclusion, EmailConsentExclusion> emailConsentExclusionProcessor() {
        return new EmailConsentExclusionProcessor();
    }

    @Bean
    public ItemWriter<EmailConsentExclusion> emailConsentExclusionWriter() {
        return new EmailConsentExclusionWriter();
    }

    @Bean
    protected Step processEmailConsentExclusion(ItemReader<EmailConsentExclusion> itemReader, ItemProcessor<EmailConsentExclusion, EmailConsentExclusion> processor, ItemWriter<EmailConsentExclusion> writer) {
        return new StepBuilder("processEmailConsentExclusion",jobRepository).<EmailConsentExclusion, EmailConsentExclusion> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job emailConsentExclusionJob() {
//        return new JobBuilder("emailConsentExclusionJob",jobRepository)
//          .start(processEmailConsentExclusion(emailConsentExclusionReader(), emailConsentExclusionProcessor(), emailConsentExclusionWriter()))
//          .build();
//    }
    
    @Bean
    public Job emailConsentExclusionJob(Step processEmailConsentExclusion) {
        return new JobBuilder("emailConsentExclusionJob", jobRepository)
                .start(processEmailConsentExclusion)
                .build();
    }
}