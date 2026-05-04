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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.processor.BankruptcyProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.BankruptcyRepository;
import com.equabli.collectprism.writer.BankruptcyWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class BankruptcyConfig {


    @Autowired
    private BankruptcyRepository bankruptcyRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope	
    public CustomItemReader<Bankruptcy> bankruptcyReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Bankruptcy> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Sort.Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("bankruptcyId", Sort.Direction.ASC);

        reader.setRepository(bankruptcyRepository);
        reader.setMethodName("getBankruptcyToProcess");
        reader.setJobName("Bankruptcy Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);
        return reader;
    }

    @Bean
    public ItemProcessor<Bankruptcy, Bankruptcy> bankruptcyProcessor() {
        return new BankruptcyProcessor();
    }

    @Bean
    public ItemWriter<Bankruptcy> bankruptcyWriter() {
        return new BankruptcyWriter();
    }

    @Bean
    protected Step processBankruptcy(ItemReader<Bankruptcy> itemReader, ItemProcessor<Bankruptcy, Bankruptcy> processor, ItemWriter<Bankruptcy> writer) {
        return new StepBuilder("processBankruptcy",jobRepository).<Bankruptcy, Bankruptcy>chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
//    public Job bankruptcyJob() {
//        return new JobBuilder("bankruptcyJob",jobRepository)
//                .start(processBankruptcy(bankruptcyReader(), bankruptcyProcessor(), bankruptcyWriter()))
//                .build();
//    }
    @Bean
    public Job bankruptcyJob(Step processBankruptcy) {
        return new JobBuilder("bankruptcyJob", jobRepository)
                .start(processBankruptcy)
                .build();
    }
}
