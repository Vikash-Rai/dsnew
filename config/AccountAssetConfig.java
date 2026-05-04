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

import com.equabli.collectprism.entity.AccountAsset;
import com.equabli.collectprism.processor.AccountAssetProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AccountAssetRepository;
import com.equabli.collectprism.writer.AccountAssetWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AccountAssetConfig {

    @Autowired
    private AccountAssetRepository accountAssetRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;
      

    @Bean
    @StepScope
    public CustomItemReader<AccountAsset> accountAssetReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<AccountAsset> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("accountUccId", Direction.ASC);

        reader.setRepository(accountAssetRepository);
        reader.setMethodName("getAccountAssetToProcess");
        reader.setJobName("Account Asset Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<AccountAsset, AccountAsset> accountAssetProcessor() {
        return new AccountAssetProcessor();
    }

    @Bean
    public ItemWriter<AccountAsset> accountAssetWriter() {
        return new AccountAssetWriter();
    }
  
    @Bean
    protected Step processAccountAsset(ItemReader<AccountAsset> itemReader, ItemProcessor<AccountAsset, AccountAsset> processor, ItemWriter<AccountAsset> writer) {
        return new StepBuilder("processAccountAsset",jobRepository).<AccountAsset, AccountAsset> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job accountAssetJob() {
//        return new JobBuilder("accountAssetJob",jobRepository)
//          .start(processAccountAsset(accountAssetReader(), accountAssetProcessor(), accountAssetWriter()))
//          .build();
//    }
    
    @Bean
    public Job accountAssetJob(Step processAccountAsset) {
        return new JobBuilder("accountAssetJob", jobRepository)
                .start(processAccountAsset)
                .build();
    }
}