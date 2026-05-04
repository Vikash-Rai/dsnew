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

import com.equabli.collectprism.entity.CommunicationDetail;
import com.equabli.collectprism.processor.CommunicationDetailProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.CommunicationDetailRepository;
import com.equabli.collectprism.writer.CommunicationDetailWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class CommunicationDetailConfig {

 
    @Autowired
    private CommunicationDetailRepository communicationDetailRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<CommunicationDetail> detailReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<CommunicationDetail> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("communicationDetailId", Direction.ASC);

        reader.setRepository(communicationDetailRepository);
        reader.setMethodName("getCommunicationDetailToProcess");
        reader.setJobName("Communication Detail Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
       reader.setSort(sorts);
       reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<CommunicationDetail, CommunicationDetail> detailProcessor() {
        return new CommunicationDetailProcessor();
    }

    @Bean
    public ItemWriter<CommunicationDetail> detailWriter() {
        return new CommunicationDetailWriter();
    }

    @Bean
    protected Step processCommunicationDetail(ItemReader<CommunicationDetail> itemReader, ItemProcessor<CommunicationDetail, CommunicationDetail> processor, ItemWriter<CommunicationDetail> writer) {
        return new StepBuilder("processCommunicationDetail",jobRepository).<CommunicationDetail, CommunicationDetail> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job communicationDetailJob() {
//        return new JobBuilder("communicationDetailJob",jobRepository)
//          .start(processCommunicationDetail(detailReader(), detailProcessor(), detailWriter()))
//          .build();
//    }
    
    @Bean
    public Job communicationDetailJob(Step processCommunicationDetail) {
        return new JobBuilder("communicationDetailJob", jobRepository)
                .start(processCommunicationDetail)
                .build();
    }
}