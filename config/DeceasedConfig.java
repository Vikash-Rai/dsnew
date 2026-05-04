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

import com.equabli.collectprism.entity.Deceased;
import com.equabli.collectprism.processor.DeceasedProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.DeceasedRepository;
import com.equabli.collectprism.writer.DeceasedWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class DeceasedConfig {

	@Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private DeceasedRepository deceasedRepository;
    
    @Bean
    @StepScope
    public CustomItemReader<Deceased> deceasedReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Deceased> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("deceasedId", Direction.ASC);

        reader.setRepository(deceasedRepository);
        reader.setMethodName("getDeceasedToProcess");
        reader.setJobName("Deceased Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Deceased, Deceased> deceasedProcessor() {
        return new DeceasedProcessor();
    }

    @Bean
    public ItemWriter<Deceased> deceasedWriter() {
        return new DeceasedWriter();
    }

    @Bean
    protected Step processDeceased(ItemReader<Deceased> itemReader, ItemProcessor<Deceased, Deceased> processor, ItemWriter<Deceased> writer) {
        return new StepBuilder("processDeceased",jobRepository).<Deceased, Deceased> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job deceasedJob() {
//        return new JobBuilder("deceasedJob",jobRepository)
//          .start(processDeceased(deceasedReader(), deceasedProcessor(), deceasedWriter()))
//          .build();
//    }
    
    @Bean
    public Job deceasedJob(Step processDeceased) {
        return new JobBuilder("deceasedJob", jobRepository)
                .start(processDeceased)
                .build();
    }
}
