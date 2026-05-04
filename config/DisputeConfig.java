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

import com.equabli.collectprism.entity.Dispute;
import com.equabli.collectprism.processor.DisputeProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.DisputeRepository;
import com.equabli.collectprism.writer.DisputeWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class DisputeConfig {

 

    @Autowired
    private DisputeRepository disputeRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<Dispute> disputeReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Dispute> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Sort.Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("disputeId", Sort.Direction.ASC);

        reader.setRepository(disputeRepository);
        reader.setMethodName("getDisputeToProcess");
        reader.setJobName("Dispute Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);
        return reader;
    }

    @Bean
    public ItemProcessor<Dispute, Dispute> disputeProcessor() {
        return new DisputeProcessor();
    }

    @Bean
    public ItemWriter<Dispute> disputeWriter() {
        return new DisputeWriter();
    }

    @Bean
    protected Step processDispute(ItemReader<Dispute> itemReader, ItemProcessor<Dispute, Dispute> processor, ItemWriter<Dispute> writer) {
        return new StepBuilder("processDispute",jobRepository).<Dispute, Dispute>chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
//    public Job disputeJob() {
//        return new JobBuilder("disputeJob",jobRepository)
//                .start(processDispute(disputeReader(), disputeProcessor(), disputeWriter()))
//                .build();
//    }
    
    @Bean
    public Job disputeJob(Step processDispute) {
        return new JobBuilder("disputeJob", jobRepository)
                .start(processDispute)
                .build();
    }
}
