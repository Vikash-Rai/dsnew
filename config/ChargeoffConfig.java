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

import com.equabli.collectprism.entity.ChargeOffAccount;
import com.equabli.collectprism.processor.ChargeOffAccountProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ChargeOffAccountRepository;
import com.equabli.collectprism.writer.ChargeOffAccountWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ChargeoffConfig {
	
	    @Autowired
	    private ChargeOffAccountRepository chargeOffAccountRepository;
	    
	    @Autowired
	    private JobRepository jobRepository;
	    
	    @Autowired
	    PlatformTransactionManager platformTransactionManager;

	    @Bean
	    @StepScope
	    public CustomItemReader<ChargeOffAccount> chargeOffAccountReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
	        CustomItemReader<ChargeOffAccount> reader = new CustomItemReader<>();
	        List<Object> queryMethodArguments = new ArrayList<>();
	        Map<String, Direction> sorts = new HashMap<>();

	        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
	        sorts.put("chargeOffAccountId", Direction.ASC);

	        reader.setRepository(chargeOffAccountRepository);
	        reader.setMethodName("getChargeOffAccountToProcess");
	        reader.setJobName("ChargeOffAccount Job");
	        reader.setArguments(queryMethodArguments);
	        reader.setPageSize(Constants.OTHERCHUNKSIZE);
	        reader.setSort(sorts);
	        reader.setAuthHeader(authHeader);

	        return reader;
	    }

	    @Bean
	    public ItemProcessor<ChargeOffAccount, ChargeOffAccount> chargeOffAccountProcessor() {
	        return new ChargeOffAccountProcessor();
	    }

	    @Bean
	    public ItemWriter<ChargeOffAccount> chargeOffAccountWriter() {
	        return new ChargeOffAccountWriter();
	    }

	    @Bean
	    protected Step processChargeOff(ItemReader<ChargeOffAccount> itemReader, ItemProcessor<ChargeOffAccount, ChargeOffAccount> processor, ItemWriter<ChargeOffAccount> writer) {
	        return new StepBuilder("processChargeOffAccount",jobRepository).<ChargeOffAccount, ChargeOffAccount> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
	          .reader(itemReader)
	          .processor(processor)
	          .writer(writer)
	          .build();
	    }

//	    @Bean
//	    public Job chargeOffAccountJob() {
//	        return new JobBuilder("chargeOffAccountJob",jobRepository)
//	          .start(processChargeOff(chargeOffAccountReader(), chargeOffAccountProcessor(), chargeOffAccountWriter()))
//	          .build();
//	    }

	    @Bean
	    public Job chargeOffAccountJob(Step processChargeOff) {
	        return new JobBuilder("chargeOffAccountJob", jobRepository)
	                .start(processChargeOff)
	                .build();
	    }


}
