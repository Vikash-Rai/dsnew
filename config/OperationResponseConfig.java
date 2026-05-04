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

import com.equabli.collectprism.entity.OperationResponse;
import com.equabli.collectprism.processor.OperationResponseProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.OperationResponseRepository;
import com.equabli.collectprism.writer.OperationResponseWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class OperationResponseConfig {


    @Autowired
    private OperationResponseRepository operationResponseRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<OperationResponse> operationResponseReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<OperationResponse> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("operationResponseId", Direction.ASC);

        reader.setRepository(operationResponseRepository);
        reader.setMethodName("getOperationResponseToProcess");
        reader.setJobName("Operation Response Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<OperationResponse, OperationResponse> operationResponseProcessor() {
        return new OperationResponseProcessor();
    }

    @Bean
    public ItemWriter<OperationResponse> operationResponseWriter() {
        return new OperationResponseWriter();
    }

    @Bean
    protected Step processOperationResponse(ItemReader<OperationResponse> itemReader, ItemProcessor<OperationResponse, OperationResponse> processor, ItemWriter<OperationResponse> writer) {
        return new StepBuilder("processOperationResponse",jobRepository).<OperationResponse, OperationResponse> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job operationResponseJob() {
//        return new JobBuilder("operationResponseJob",jobRepository)
//          .start(processOperationResponse(operationResponseReader(), operationResponseProcessor(), operationResponseWriter()))
//          .build();
//    }
    
    @Bean
    public Job operationResponseJob(Step processOperationResponse) {
        return new JobBuilder("operationResponseJob", jobRepository)
                .start(processOperationResponse)
                .build();
    }
}