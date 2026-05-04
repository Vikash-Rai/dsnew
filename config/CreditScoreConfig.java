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

import com.equabli.collectprism.entity.CreditScore;
import com.equabli.collectprism.processor.CreditScoreProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.CreditScoreRepository;
import com.equabli.collectprism.writer.CreditScoreWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class CreditScoreConfig {


	    @Autowired
	    private CreditScoreRepository creditScoreRepository;
	    
	    @Autowired
	    private JobRepository jobRepository;
	    
	    @Autowired
	    PlatformTransactionManager platformTransactionManager;

	    @Bean
	    @StepScope
	    public CustomItemReader<CreditScore> creditScoreReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
	        CustomItemReader<CreditScore> reader = new CustomItemReader<>();
	        List<Object> queryMethodArguments = new ArrayList<>();
	        Map<String, Direction> sorts = new HashMap<>();

	        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
	        sorts.put("creditScoreId", Direction.ASC);

	        reader.setRepository(creditScoreRepository);
	        reader.setMethodName("getCreditScoreToProcess");
	        reader.setJobName("Credit Score Job");
	        reader.setArguments(queryMethodArguments);
	        reader.setPageSize(Constants.OTHERCHUNKSIZE);
	        reader.setSort(sorts);
	        reader.setAuthHeader(authHeader);
	        return reader;
	    }

	    @Bean
	    public ItemProcessor<CreditScore, CreditScore> creditScoreProcessor() {
	        return new CreditScoreProcessor();
	    }

	    @Bean
	    public ItemWriter<CreditScore> creditScoreWriter() {
	        return new CreditScoreWriter();
	    }

	    @Bean
	    protected Step processCreditScore(ItemReader<CreditScore> itemReader, ItemProcessor<CreditScore, CreditScore> processor, ItemWriter<CreditScore> writer) {
	        return new StepBuilder("processCreditScore",jobRepository).<CreditScore, CreditScore> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
	          .reader(itemReader)
	          .processor(processor)
	          .writer(writer)
	          .build();
	    }

//	    @Bean
//	    public Job creditScoreJob() {
//	        return new JobBuilder("creditScoreJob",jobRepository)
//	          .start(processCreditScore(creditScoreReader(), creditScoreProcessor(), creditScoreWriter()))
//	          .build();
//	    }
	    
	    @Bean
	    public Job creditScoreJob(Step processCreditScore) {
	        return new JobBuilder("creditScoreJob", jobRepository)
	                .start(processCreditScore)
	                .build();
	    }
}
