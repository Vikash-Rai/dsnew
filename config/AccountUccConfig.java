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

import com.equabli.collectprism.entity.AccountUcc;
import com.equabli.collectprism.processor.AccountUccProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AccountUccRepository;
import com.equabli.collectprism.writer.AccountUccWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AccountUccConfig {

    @Autowired
    private AccountUccRepository accountUccRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<AccountUcc> accountUccReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<AccountUcc> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("accountUccId", Direction.ASC);

        reader.setRepository(accountUccRepository);
        reader.setMethodName("getAccountUccToProcess");
        reader.setJobName("Account Ucc Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<AccountUcc, AccountUcc> accountUccProcessor() {
        return new AccountUccProcessor();
    }

    @Bean
    public ItemWriter<AccountUcc> accountUccWriter() {
        return new AccountUccWriter();
    }

    @Bean
    protected Step processAccountUcc(ItemReader<AccountUcc> itemReader, ItemProcessor<AccountUcc, AccountUcc> processor, ItemWriter<AccountUcc> writer) {
        return new StepBuilder("processAccountUcc",jobRepository).<AccountUcc, AccountUcc> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job accountUccJob() {
//        return new JobBuilder("accountUccJob",jobRepository)
//          .start(processAccountUcc(accountUccReader(), accountUccProcessor(), accountUccWriter()))
//          .build();
//    }
    
    @Bean
    public Job accountUccJob(Step processAccountUcc) {
        return new JobBuilder("accountUccJob", jobRepository)
                .start(processAccountUcc)
                .build();
    }
}