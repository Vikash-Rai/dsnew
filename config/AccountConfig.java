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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.client.CommonRestClient;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.processor.AccountProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AddressRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.repository.EmailRepository;
import com.equabli.collectprism.repository.PhoneRepository;
import com.equabli.collectprism.task.AccountConsumerDeDupTask;
import com.equabli.collectprism.task.ConsumerNumberDeDupTask;
import com.equabli.collectprism.writer.AccountWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class AccountConfig {

    @Autowired
	private CommonRestClient client;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<Account> reader(@Value("#{jobParameters['authHeader']}") String authHeader) {
    	CustomItemReader<Account> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("accountId", Direction.ASC);

        reader.setRepository(accountRepository);
        reader.setMethodName("findByRecordStatusId");
        reader.setJobName("Accounts Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.CHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public CustomItemReader<Account> consumerReprocessReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
    	CustomItemReader<Account> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add("consumerReprocessJob");
        sorts.put("accountId", Direction.ASC);

        reader.setRepository(accountRepository);
        reader.setMethodName("getConsumerToReprocess");
        reader.setJobName("Consumer Reprocess Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.CHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public CustomItemReader<Account> addressReprocessReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
    	CustomItemReader<Account> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add("addressReprocessJob");
        sorts.put("accountId", Direction.ASC);

        reader.setRepository(accountRepository);
        reader.setMethodName("getAddressToReprocess");
        reader.setJobName("Address Reprocess Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.CHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public CustomItemReader<Account> phoneReprocessReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
    	CustomItemReader<Account> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add("phoneReprocessJob");
        sorts.put("accountId", Direction.ASC);

        reader.setRepository(accountRepository);
        reader.setMethodName("getPhoneToReprocess");
        reader.setJobName("Phone Reprocess Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.CHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    @StepScope
    public CustomItemReader<Account> emailReprocessReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
    	CustomItemReader<Account> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        queryMethodArguments.add("emailReprocessJob");
        sorts.put("accountId", Direction.ASC);

        reader.setRepository(accountRepository);
        reader.setMethodName("getEmailToReprocess");
        reader.setJobName("Email Reprocess Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.CHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Account, Account> itemProcessor() {
        return new AccountProcessor();
    }

    @Bean
    public ItemWriter<Account> itemWriter() {
        return new AccountWriter();
    }

    @Bean
    public Step consumerNumberDeDupTaskStep() {
    	return new StepBuilder("consumerNumberDeDupTaskStep",jobRepository)
    		.tasklet(new ConsumerNumberDeDupTask(client, accountRepository, consumerRepository, addressRepository, emailRepository, phoneRepository), platformTransactionManager)
            .build();
    }

    @Bean
    public Step accountConsumerDeDupTaskStep() {
    	return new StepBuilder("accountConsumerDeDupTaskStep",jobRepository)
    		.tasklet(new AccountConsumerDeDupTask(client, accountRepository, consumerRepository, addressRepository, emailRepository, phoneRepository), platformTransactionManager)
            .build();
    }

    @Bean
    protected Step processAccounts(@Qualifier("reader") ItemReader<Account> itemReader, ItemProcessor<Account, Account> processor, ItemWriter<Account> writer) {
        return new StepBuilder("processAccounts",jobRepository).<Account, Account> chunk(Constants.CHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    protected Step consumerReprocess( @Qualifier("consumerReprocessReader")  ItemReader<Account> itemReader, ItemProcessor<Account, Account> processor, ItemWriter<Account> writer) {
        return new StepBuilder("consumerReprocess",jobRepository).<Account, Account> chunk(Constants.CHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    protected Step addressReprocess(@Qualifier("addressReprocessReader") ItemReader<Account> itemReader, ItemProcessor<Account, Account> processor, ItemWriter<Account> writer) {
        return new StepBuilder("addressReprocess",jobRepository).<Account, Account> chunk(Constants.CHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    protected Step phoneReprocess(@Qualifier("phoneReprocessReader") ItemReader<Account> itemReader, ItemProcessor<Account, Account> processor, ItemWriter<Account> writer) {
        return new StepBuilder("phoneReprocess",jobRepository).<Account, Account> chunk(Constants.CHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    protected Step emailReprocess(@Qualifier("emailReprocessReader") ItemReader<Account> itemReader, ItemProcessor<Account, Account> processor, ItemWriter<Account> writer) {
        return new StepBuilder("emailReprocess",jobRepository).<Account, Account> chunk(Constants.CHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

    @Bean
    public Job consumerNumberDeDupTaskJob(){
        return new JobBuilder("consumerNumberDeDupTaskJob",jobRepository)
            .start(consumerNumberDeDupTaskStep())
            .build();
    }

    @Bean
    public Job accountConsumerDeDupTaskJob(){
        return new JobBuilder("accountConsumerDeDupTaskJob",jobRepository)
            .start(accountConsumerDeDupTaskStep())
            .build();
    }

//    @Bean
//    public Job accountsDataScrubbingJob() {
//        return new JobBuilder("accountsDataScrubbingJob",jobRepository)
//          .start(processAccounts(reader(), itemProcessor(), itemWriter()))
//          .build();
//    }
    
    @Bean
    public Job accountsDataScrubbingJob(Step processAccounts) {
        return new JobBuilder("accountsDataScrubbingJob", jobRepository)
                .start(processAccounts)
                .build();
    }

//    @Bean
//    public Job consumerReprocessJob() {
//        return new JobBuilder("consumerReprocessJob",jobRepository)
//          .start(consumerReprocess(consumerReprocessReader(), itemProcessor(), itemWriter()))
//          .build();
//    }
    
    @Bean
    public Job consumerReprocessJob(Step consumerReprocess) {
        return new JobBuilder("consumerReprocessJob", jobRepository)
                .start(consumerReprocess)
                .build();
    }

//    @Bean
//    public Job addressReprocessJob() {
//        return new JobBuilder("addressReprocessJob",jobRepository)
//          .start(addressReprocess(addressReprocessReader(), itemProcessor(), itemWriter()))
//          .build();
//    }
    
    @Bean
    public Job addressReprocessJob(Step addressReprocess) {
        return new JobBuilder("addressReprocessJob", jobRepository)
                .start(addressReprocess)
                .build();
    }

//    @Bean
//    public Job phoneReprocessJob() {
//        return new JobBuilder("phoneReprocessJob",jobRepository)
//          .start(phoneReprocess(phoneReprocessReader(), itemProcessor(), itemWriter()))
//          .build();
//    }
    
    @Bean
    public Job phoneReprocessJob(Step phoneReprocess) {
        return new JobBuilder("phoneReprocessJob", jobRepository)
                .start(phoneReprocess)
                .build();
    }

//    @Bean
//    public Job emailReprocessJob() {
//        return new JobBuilder("emailReprocessJob",jobRepository)
//          .start(emailReprocess(emailReprocessReader(), itemProcessor(), itemWriter()))
//          .build();
//    }
    
    @Bean
    public Job emailReprocessJob(Step emailReprocess) {
        return new JobBuilder("emailReprocessJob", jobRepository)
                .start(emailReprocess)
                .build();
    }
}