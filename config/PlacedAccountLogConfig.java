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

import com.equabli.collectprism.entity.PlacedAccountLog;
import com.equabli.collectprism.processor.PlacedAccountLogProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.PlacedAccountLogRepository;
import com.equabli.collectprism.writer.PlacedAccountLogWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class PlacedAccountLogConfig {

    
    @Autowired
    private PlacedAccountLogRepository placedAccountLogRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<PlacedAccountLog> placedAccountLogReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<PlacedAccountLog> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("placedAccountLogId", Direction.ASC);

        reader.setRepository(placedAccountLogRepository);
        reader.setMethodName("getPlacedAccountLogToProcess");
        reader.setJobName("Placed Account Log Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<PlacedAccountLog, PlacedAccountLog> placedAccountLogProcessor() {
        return new PlacedAccountLogProcessor();
    }

    @Bean
    public ItemWriter<PlacedAccountLog> placedAccountLogWriter() {
        return new PlacedAccountLogWriter();
    }

    @Bean
    protected Step processPlacedAccountLog(ItemReader<PlacedAccountLog> itemReader, ItemProcessor<PlacedAccountLog, PlacedAccountLog> processor, ItemWriter<PlacedAccountLog> writer) {
        return new StepBuilder("processPlacedAccountLog",jobRepository).<PlacedAccountLog, PlacedAccountLog> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job placedAccountJob() {
//        return new JobBuilder("placedAccountJob",jobRepository)
//          .start(processPlacedAccountLog(placedAccountLogReader(), placedAccountLogProcessor(), placedAccountLogWriter()))
//          .build();
//    }
//    
    @Bean
    public Job placedAccountJob(Step processPlacedAccountLog) {
        return new JobBuilder("placedAccountJob", jobRepository)
                .start(processPlacedAccountLog)
                .build();
    }
}