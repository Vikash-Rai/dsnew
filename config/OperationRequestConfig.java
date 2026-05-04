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

import com.equabli.collectprism.entity.OperationRequest;
import com.equabli.collectprism.processor.OperationRequestProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.OperationRequestRepository;
import com.equabli.collectprism.writer.OperationRequestWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class OperationRequestConfig {

    @Autowired
    private OperationRequestRepository operationRequestRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<OperationRequest> operationRequestReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<OperationRequest> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("operationRequestId", Direction.ASC);

        reader.setRepository(operationRequestRepository);
        reader.setMethodName("getOperationRequestToProcess");
        reader.setJobName("Operation Request Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<OperationRequest, OperationRequest> operationRequestProcessor() {
        return new OperationRequestProcessor();
    }

    @Bean
    public ItemWriter<OperationRequest> operationRequestWriter() {
        return new OperationRequestWriter();
    }

    @Bean
    protected Step processOperationRequest(ItemReader<OperationRequest> itemReader, ItemProcessor<OperationRequest, OperationRequest> processor, ItemWriter<OperationRequest> writer) {
        return new StepBuilder("processOperationRequest",jobRepository).<OperationRequest, OperationRequest> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job operationRequestJob() {
//        return new JobBuilder("operationRequestJob",jobRepository)
//          .start(processOperationRequest(operationRequestReader(), operationRequestProcessor(), operationRequestWriter()))
//          .build();
//    }
    
    @Bean
    public Job operationRequestJob(Step processOperationRequest) {
        return new JobBuilder("operationRequestJob", jobRepository)
                .start(processOperationRequest)
                .build();
    }
}