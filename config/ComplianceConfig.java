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

import com.equabli.collectprism.entity.Compliance;
import com.equabli.collectprism.processor.ComplianceProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ComplianceRepository;
import com.equabli.collectprism.writer.ComplianceWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ComplianceConfig {


    @Autowired
    private ComplianceRepository complianceRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<Compliance> complianceReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Compliance> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("complianceId", Direction.ASC);

        reader.setRepository(complianceRepository);
        reader.setMethodName("getComplianceToProcess");
        reader.setJobName("Compliance Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Compliance, Compliance> complianceProcessor() {
        return new ComplianceProcessor();
    }

    @Bean
    public ItemWriter<Compliance> complianceWriter() {
        return new ComplianceWriter();
    }

    @Bean
    protected Step processCompliance(ItemReader<Compliance> itemReader, ItemProcessor<Compliance, Compliance> processor, ItemWriter<Compliance> writer) {
        return new StepBuilder("processCompliance",jobRepository).<Compliance, Compliance> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job complianceJob() {
//        return new JobBuilder("complianceJob",jobRepository)
//          .start(processCompliance(complianceReader(), complianceProcessor(), complianceWriter()))
//          .build();
//    }
    
    @Bean
    public Job complianceJob(Step processCompliance) {
        return new JobBuilder("complianceJob", jobRepository)
                .start(processCompliance)
                .build();
    }
}