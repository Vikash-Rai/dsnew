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

import com.equabli.collectprism.entity.CoTOwner;
import com.equabli.collectprism.processor.CoTOwnerProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.CoTOwnerRepository;
import com.equabli.collectprism.writer.CoTOwnerWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class CoTMasterListConfig {


    @Autowired
    private CoTOwnerRepository coTOwnerRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<CoTOwner> cotOwnerCustomItemReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<CoTOwner> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Sort.Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("cotOwnerId", Sort.Direction.ASC);

        reader.setRepository(coTOwnerRepository);
        reader.setMethodName("getCoTOwnerProcess");
        reader.setJobName("CoT Owner Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<CoTOwner, CoTOwner> cotOwnerProcessor() {
        return new CoTOwnerProcessor();
    }

    @Bean
    public ItemWriter<CoTOwner> cotOwnerWriter() {
        return new CoTOwnerWriter();
    }

    @Bean
    protected Step processCoTOwner(ItemReader<CoTOwner> itemReader, ItemProcessor<CoTOwner, CoTOwner> processor, ItemWriter<CoTOwner> writer) {
        return new StepBuilder("processCoTOwner",jobRepository).<CoTOwner, CoTOwner>chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
            .reader(itemReader)
            .processor(processor)
            .writer(writer)
            .build();
    }

//    @Bean
//    public Job cotMasterListJob() {
//        return new JobBuilder("cotMasterListJob",jobRepository)
//            .start(processCoTOwner(cotOwnerCustomItemReader(), cotOwnerProcessor(), cotOwnerWriter()))
//            .build();
//    }
    @Bean
    public Job cotMasterListJob(Step processCoTOwner) {
        return new JobBuilder("cotMasterListJob", jobRepository)
                .start(processCoTOwner)
                .build();
    }
}