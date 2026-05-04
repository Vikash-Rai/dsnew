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

import com.equabli.collectprism.entity.PartnerAssignment;
import com.equabli.collectprism.processor.PartnerAssignmentProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.PartnerAssignmentRepository;
import com.equabli.collectprism.writer.PartnerAssignmentWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class PartnerAssignmentConfig {

  

    @Autowired
    private PartnerAssignmentRepository partnerAssignmentRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<PartnerAssignment> partnerAssignmentReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<PartnerAssignment> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("partnerAssignmentId", Direction.ASC);

        reader.setRepository(partnerAssignmentRepository);
        reader.setMethodName("getPartnerAssignmentToProcess");
        reader.setJobName("Partner Assignment Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<PartnerAssignment, PartnerAssignment> partnerAssignmentProcessor() {
        return new PartnerAssignmentProcessor();
    }

    @Bean
    public ItemWriter<PartnerAssignment> partnerAssignmentWriter() {
        return new PartnerAssignmentWriter();
    }

    @Bean
    protected Step processPartnerAssignment(ItemReader<PartnerAssignment> itemReader, ItemProcessor<PartnerAssignment, PartnerAssignment> processor, ItemWriter<PartnerAssignment> writer) {
        return new StepBuilder("processPartnerAssignment",jobRepository).<PartnerAssignment, PartnerAssignment> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job partnerAssignmentJob() {
//        return new JobBuilder("partnerAssignmentJob",jobRepository)
//          .start(processPartnerAssignment(partnerAssignmentReader(), partnerAssignmentProcessor(), partnerAssignmentWriter()))
//          .build();
//    }
    
    @Bean
    public Job partnerAssignmentJob(Step processPartnerAssignment) {
        return new JobBuilder("partnerAssignmentJob", jobRepository)
                .start(processPartnerAssignment)
                .build();
    }
}