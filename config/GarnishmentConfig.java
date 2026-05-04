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

import com.equabli.collectprism.entity.Garnishment;
import com.equabli.collectprism.processor.GarnishmentProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.GarnishmentRepository;
import com.equabli.collectprism.writer.GarnishmentWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class GarnishmentConfig {

   

    @Autowired
    private GarnishmentRepository garnishmentRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<Garnishment> garnishmentReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Garnishment> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("legalGarnishmentId", Direction.ASC);

        reader.setRepository(garnishmentRepository);
        reader.setMethodName("getGarnishmentToProcess");
        reader.setJobName("Garnishment Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Garnishment, Garnishment> garnishmentProcessor() {
        return new GarnishmentProcessor();
    }

    @Bean
    public ItemWriter<Garnishment> garnishmentWriter() {
        return new GarnishmentWriter();
    }

    @Bean
    protected Step processGarnishment(ItemReader<Garnishment> itemReader, ItemProcessor<Garnishment, Garnishment> processor, ItemWriter<Garnishment> writer) {
        return new StepBuilder("processGarnishment",jobRepository).<Garnishment, Garnishment> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job garnishmentJob() {
//        return new JobBuilder("garnishmentJob",jobRepository)
//          .start(processGarnishment(garnishmentReader(), garnishmentProcessor(), garnishmentWriter()))
//          .build();
//    }
    
    @Bean
    public Job garnishmentJob(Step processGarnishment) {
        return new JobBuilder("garnishmentJob", jobRepository)
                .start(processGarnishment)
                .build();
    }
}