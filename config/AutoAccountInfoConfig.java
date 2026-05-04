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

import com.equabli.collectprism.entity.AutoAccountInfo;
import com.equabli.collectprism.processor.AutoAccountInfoProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AutoAccountInfoRepository;
import com.equabli.collectprism.writer.AutoAccountInfoWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AutoAccountInfoConfig {



	    @Autowired
	    private AutoAccountInfoRepository autoAccountInfoRepository;
	    
	    
	    @Autowired
	    private JobRepository jobRepository;
	    
	    @Autowired
	    PlatformTransactionManager platformTransactionManager;

	    @Bean
	    @StepScope
	    public CustomItemReader<AutoAccountInfo> autoAccountInfoReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
	        CustomItemReader<AutoAccountInfo> reader = new CustomItemReader<>();
	        List<Object> queryMethodArguments = new ArrayList<>();
	        Map<String, Direction> sorts = new HashMap<>();

	        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
	        sorts.put("autoAccountInfoId", Direction.ASC);

	        reader.setRepository(autoAccountInfoRepository);
	        reader.setMethodName("getAutoAccountInfoToProcess");
	        reader.setJobName("AutoAccountInfo Job");
	        reader.setArguments(queryMethodArguments);
	        reader.setPageSize(Constants.OTHERCHUNKSIZE);
	        reader.setSort(sorts);
	        reader.setAuthHeader(authHeader);

	        return reader;
	    }

	    @Bean
	    public ItemProcessor<AutoAccountInfo, AutoAccountInfo> autoAccountInfoProcessor() {
	        return new AutoAccountInfoProcessor();
	    }

	    @Bean
	    public ItemWriter<AutoAccountInfo> autoAccountInfoWriter() {
	        return new AutoAccountInfoWriter();
	    }

	    @Bean
	    protected Step processAutoAccountInfo(ItemReader<AutoAccountInfo> itemReader, ItemProcessor<AutoAccountInfo, AutoAccountInfo> processor, ItemWriter<AutoAccountInfo> writer) {
	        return new StepBuilder("processAutoAccountInfo",jobRepository).<AutoAccountInfo, AutoAccountInfo> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
	          .reader(itemReader)
	          .processor(processor)
	          .writer(writer)
	          .build();
	    }

//	    @Bean
//	    public Job autoAccountInfoJob() {
//	        return new JobBuilder("autoAccountInfoJob",jobRepository)
//	          .start(processAutoAccountInfo(autoAccountInfoReader(), autoAccountInfoProcessor(), autoAccountInfoWriter()))
//	          .build();
//	    }
	    @Bean
	    public Job autoAccountInfoJob(Step processAutoAccountInfo) {
	        return new JobBuilder("autoAccountInfoJob", jobRepository)
	                .start(processAutoAccountInfo)
	                .build();
	    }


}
