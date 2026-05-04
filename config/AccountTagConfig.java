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

import com.equabli.collectprism.entity.AccountTag;
import com.equabli.collectprism.processor.AccountTagProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AccountTagRepository;
import com.equabli.collectprism.writer.AccountTagWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AccountTagConfig {

    @Autowired
    private AccountTagRepository accountTagRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<AccountTag> accountTagReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<AccountTag> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("accountTagId", Direction.ASC);

        reader.setRepository(accountTagRepository);
        reader.setMethodName("getAccountTagToProcess");
        reader.setJobName("Account Tag Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<AccountTag, AccountTag> accountTagProcessor() {
        return new AccountTagProcessor();
    }

    @Bean
    public ItemWriter<AccountTag> accountTagWriter() {
        return new AccountTagWriter();
    }

    @Bean
    protected Step processAccountTag(ItemReader<AccountTag> itemReader, ItemProcessor<AccountTag, AccountTag> processor, ItemWriter<AccountTag> writer) {
        return new StepBuilder("processAccountTag",jobRepository).<AccountTag, AccountTag> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job accountTagJob() {
//        return new JobBuilder("accountTagJob",jobRepository)
//          .start(processAccountTag(accountTagReader(), accountTagProcessor(), accountTagWriter()))
//          .build();
//    }
    
    @Bean
    public Job accountTagJob(Step processAccountTag) {
        return new JobBuilder("accountTagJob", jobRepository)
                .start(processAccountTag)
                .build();
    }
}