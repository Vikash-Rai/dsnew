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

import com.equabli.collectprism.entity.LegalPlacement;
import com.equabli.collectprism.processor.LegalPlacementProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.LegalPlacementRepository;
import com.equabli.collectprism.writer.LegalPlacementWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class LegalPlacementConfig {

   

    @Autowired
    private LegalPlacementRepository legalPlacementRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<LegalPlacement> legalPlacementReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<LegalPlacement> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("clientId", Direction.DESC);
        sorts.put("clientAccountNumber", Direction.DESC);
        sorts.put("suitStatus", Direction.DESC);
        sorts.put("judgmentStatus", Direction.DESC);

        reader.setRepository(legalPlacementRepository);
        reader.setMethodName("getLegalPlacementToProcess");
        reader.setJobName("Legal Placement Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<LegalPlacement, LegalPlacement> legalPlacementProcessor() {
        return new LegalPlacementProcessor();
    }

    @Bean
    public ItemWriter<LegalPlacement> legalPlacementWriter() {
        return new LegalPlacementWriter();
    }

    @Bean
    protected Step processLegalPlacement(ItemReader<LegalPlacement> itemReader, ItemProcessor<LegalPlacement, LegalPlacement> processor, ItemWriter<LegalPlacement> writer) {
        return new StepBuilder("processLegalPlacement",jobRepository).<LegalPlacement, LegalPlacement> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job legalPlacementJob() {
//        return new JobBuilder("legalPlacementJob",jobRepository)
//          .start(processLegalPlacement(legalPlacementReader(), legalPlacementProcessor(), legalPlacementWriter()))
//          .build();
//    }
    
    @Bean
    public Job legalPlacementJob(Step processLegalPlacement) {
        return new JobBuilder("legalPlacementJob", jobRepository)
                .start(processLegalPlacement)
                .build();
    }
}