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

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.processor.AdjustmentProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.writer.AdjustmentWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AdjustmentConfig {

    @Autowired
    private AdjustmentRepository adjustmentRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<Adjustment> adjustmentReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Adjustment> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("adjustmentId", Direction.ASC);

        reader.setRepository(adjustmentRepository);
        reader.setMethodName("getAdjustmentToProcess");
        reader.setJobName("Adjustment Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Adjustment, Adjustment> adjustmentProcessor() {
        return new AdjustmentProcessor();
    }

    @Bean
    public ItemWriter<Adjustment> adjustmentWriter() {
        return new AdjustmentWriter();
    }

    @Bean
    protected Step processAdjustment(ItemReader<Adjustment> itemReader, ItemProcessor<Adjustment, Adjustment> processor, ItemWriter<Adjustment> writer) {
        return new StepBuilder("processAdjustment",jobRepository).<Adjustment, Adjustment> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job adjustmentJob() {
//        return new JobBuilder("adjustmentJob",jobRepository)
//          .start(processAdjustment(adjustmentReader(), adjustmentProcessor(), adjustmentWriter()))
//          .build();
//    }
    

    @Bean
    public Job adjustmentJob(Step processAdjustment) {
        return new JobBuilder("adjustmentJob", jobRepository)
                .start(processAdjustment)
                .build();
    }
}