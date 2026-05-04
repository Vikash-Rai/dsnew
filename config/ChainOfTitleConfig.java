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

import com.equabli.collectprism.entity.ChainOfTitle;
import com.equabli.collectprism.processor.ChainOfTitleProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ChainOfTitleRepository;
import com.equabli.collectprism.writer.ChainOfTitleWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ChainOfTitleConfig {

 
    @Autowired
    private ChainOfTitleRepository chainOfTitleRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<ChainOfTitle> chainOfTitleReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<ChainOfTitle> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.BROKEN).getRecordStatusId());
        sorts.put("clientAccountNumber", Direction.ASC);
        sorts.put("dtStart", Direction.ASC);
        sorts.put("dtEnd", Direction.ASC);

        reader.setRepository(chainOfTitleRepository);
        reader.setMethodName("getChainOfTitleToProcess");
        reader.setJobName("Chain of Title Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<ChainOfTitle, ChainOfTitle> chainOfTitleProcessor() {
        return new ChainOfTitleProcessor();
    }

    @Bean
    public ItemWriter<ChainOfTitle> chainOfTitleWriter() {
        return new ChainOfTitleWriter();
    }

    @Bean
    protected Step processChainOfTitle(ItemReader<ChainOfTitle> itemReader, ItemProcessor<ChainOfTitle, ChainOfTitle> processor, ItemWriter<ChainOfTitle> writer) {
        return new StepBuilder("processChainOfTitle",jobRepository).<ChainOfTitle, ChainOfTitle> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job chainOfTitleJob() {
//        return new JobBuilder("chainOfTitleJob",jobRepository)
//          .start(processChainOfTitle(chainOfTitleReader(), chainOfTitleProcessor(), chainOfTitleWriter()))
//          .build();
//    }
    
    @Bean
    public Job chainOfTitleJob(Step processChainOfTitle) {
        return new JobBuilder("chainOfTitleJob", jobRepository)
                .start(processChainOfTitle)
                .build();
    }
}