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

import com.equabli.collectprism.entity.ChangeLog;
import com.equabli.collectprism.processor.ChangeLogProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ChangeLogRepository;
import com.equabli.collectprism.writer.ChangeLogWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ChangeLogConfig {



	    @Autowired
	    private ChangeLogRepository changeLogRepository;
	    
	    
	    @Autowired
	    private JobRepository jobRepository;
	    
	    @Autowired
	    PlatformTransactionManager platformTransactionManager;

	    @Bean
	    @StepScope
	    public CustomItemReader<ChangeLog> changeLogReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
	        CustomItemReader<ChangeLog> reader = new CustomItemReader<>();
	        List<Object> queryMethodArguments = new ArrayList<>();
	        Map<String, Direction> sorts = new HashMap<>();

	        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
	        sorts.put("changeLogId", Direction.ASC);

	        reader.setRepository(changeLogRepository);
	        reader.setMethodName("getChangeLogToProcess");
	        reader.setJobName("ChangeLog Job");
	        reader.setArguments(queryMethodArguments);
	        reader.setPageSize(Constants.OTHERCHUNKSIZE);
	        reader.setSort(sorts);
	        reader.setAuthHeader(authHeader);

	        return reader;
	    }

	    @Bean
	    public ItemProcessor<ChangeLog, ChangeLog> changeLogProcessor() {
	        return new ChangeLogProcessor();
	    }

	    @Bean
	    @StepScope
	    public ItemWriter<ChangeLog> changeLogWriter(@Value("#{jobParameters['authHeader']}") String authHeader) {
	    	ChangeLogWriter writer = new ChangeLogWriter();
	        writer.setAuthHeader(authHeader);
	        return writer;
	    }

	    @Bean
	    protected Step processChangeLog(ItemReader<ChangeLog> itemReader, ItemProcessor<ChangeLog, ChangeLog> processor, ItemWriter<ChangeLog> writer) {
	        return new StepBuilder("processChangeLog",jobRepository).<ChangeLog, ChangeLog> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
	          .reader(itemReader)
	          .processor(processor)
	          .writer(writer)
	          .build();
	    }

//	    @Bean
//	    public Job changeLogJob() {
//	        return new JobBuilder("changeLogJob",jobRepository)
//	          .start(processChangeLog(changeLogReader(), changeLogProcessor(), changeLogWriter()))
//	          .build();
//	    }

	    @Bean
	    public Job changeLogJob(Step processChangeLog) {
	        return new JobBuilder("changeLogJob", jobRepository)
	                .start(processChangeLog)
	                .build();
	    }

}
