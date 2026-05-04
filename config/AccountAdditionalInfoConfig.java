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

import com.equabli.collectprism.entity.AccountAdditionalInfo;
import com.equabli.collectprism.processor.AccountAdditionalInfoProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AccountAdditionalInfoRepository;
import com.equabli.collectprism.writer.AccountAdditionalInfoWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AccountAdditionalInfoConfig {

	@Autowired
	private AccountAdditionalInfoRepository accountAdditionalInfoRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	PlatformTransactionManager platformTransactionManager;

	@Bean
	@StepScope
	public CustomItemReader<AccountAdditionalInfo> accountAdditionalInfoReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
		CustomItemReader<AccountAdditionalInfo> reader = new CustomItemReader<>();
	    List<Object> queryMethodArguments = new ArrayList<>();
	    Map<String, Direction> sorts = new HashMap<>();

	    queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
	    sorts.put("accountAdditionalInfoId", Direction.ASC);

	    reader.setRepository(accountAdditionalInfoRepository);
	    reader.setMethodName("getAccountAdditionalInfoToProcess");
	    reader.setJobName("AccountAdditionalInfo Job");
	    reader.setArguments(queryMethodArguments);
	    reader.setPageSize(Constants.OTHERCHUNKSIZE);
	    reader.setSort(sorts);
	    reader.setAuthHeader(authHeader);

	    return reader;
	}

	@Bean
	public ItemProcessor<AccountAdditionalInfo, AccountAdditionalInfo> accountAdditionalInfoProcessor() {
		return new AccountAdditionalInfoProcessor();
	}

	@Bean
	public ItemWriter<AccountAdditionalInfo> accountAdditionalInfoWriter() {
		return new AccountAdditionalInfoWriter();
	}

	@Bean
	protected Step processAccountAdditionalInfo(ItemReader<AccountAdditionalInfo> itemReader, ItemProcessor<AccountAdditionalInfo, AccountAdditionalInfo> processor, ItemWriter<AccountAdditionalInfo> writer) {
		return new StepBuilder("processAccountAdditionalInfo",jobRepository).<AccountAdditionalInfo, AccountAdditionalInfo> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
		.reader(itemReader)
	    .processor(processor)
	    .writer(writer)
	    .build();
	}

//	@Bean
//	public Job accountAdditionalInfoJob() {
//		return new JobBuilder("accountAdditionalInfoJob",jobRepository)
//		.start(processAccountAdditionalInfo(accountAdditionalInfoReader(), accountAdditionalInfoProcessor(), accountAdditionalInfoWriter()))
//	    .build();
//	}
	
	@Bean
    public Job accountAdditionalInfoJob(Step processAccountAdditionalInfo) {
        return new JobBuilder("accountAdditionalInfoJob", jobRepository)
                .start(processAccountAdditionalInfo)
                .build();
    }
}