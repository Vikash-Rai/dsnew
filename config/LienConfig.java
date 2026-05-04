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
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import com.equabli.collectprism.entity.Lien;
import com.equabli.collectprism.processor.LienProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.LienRepository;
import com.equabli.collectprism.writer.LienWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class LienConfig {
   
    @Autowired
    private LienRepository lienRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<Lien> lienReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Lien> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Sort.Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("lienId", Sort.Direction.ASC);

        reader.setRepository(lienRepository);
        reader.setMethodName("getLienToProcess");
        reader.setJobName("Lien Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<Lien, Lien> lienProcessor() {
        return new LienProcessor();
    }

    @Bean
    public ItemWriter<Lien> lienWriter() {
        return new LienWriter();
    }

    @Bean
    protected Step processLien(ItemReader<Lien> itemReader, ItemProcessor<Lien, Lien> processor, ItemWriter<Lien> writer) {
        return new StepBuilder("processLien",jobRepository).<Lien, Lien>chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
//    public Job lienJob() {
//        return new JobBuilder("lienJob",jobRepository)
//                .start(processLien(lienReader(), lienProcessor(), lienWriter()))
//                .build();
//    }
    
    @Bean
    public Job lienJob(Step processLien) {
        return new JobBuilder("lienJob", jobRepository)
                .start(processLien)
                .build();
    }
}
