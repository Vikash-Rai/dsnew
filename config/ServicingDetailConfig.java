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

import com.equabli.collectprism.entity.ServicingDetail;
import com.equabli.collectprism.processor.ServicingDetailProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ServicingDetailRepository;
import com.equabli.collectprism.writer.ServicingDetailWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ServicingDetailConfig {

   

    @Autowired
    private ServicingDetailRepository servicingDetailRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean
    @StepScope
    public CustomItemReader<ServicingDetail> servicingDetailReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<ServicingDetail> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("servicingDetailId", Direction.ASC);

        reader.setRepository(servicingDetailRepository);
        reader.setMethodName("getServicingDetailToProcess");
        reader.setJobName("ServicingDetail Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);

        return reader;
    }

    @Bean
    public ItemProcessor<ServicingDetail, ServicingDetail> servicingDetailProcessor() {
        return new ServicingDetailProcessor();
    }

    @Bean
    public ItemWriter<ServicingDetail> servicingDetailWriter() {
        return new ServicingDetailWriter();
    }

    @Bean
    protected Step processServicingDetail(ItemReader<ServicingDetail> itemReader, ItemProcessor<ServicingDetail, ServicingDetail> processor, ItemWriter<ServicingDetail> writer) {
        return new StepBuilder("processServicingDetail",jobRepository).<ServicingDetail, ServicingDetail> chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
          .reader(itemReader)
          .processor(processor)
          .writer(writer)
          .build();
    }

//    @Bean
//    public Job servicingDetailJob() {
//        return new JobBuilder("servicingDetailJob",jobRepository)
//          .start(processServicingDetail(servicingDetailReader(), servicingDetailProcessor(), servicingDetailWriter()))
//          .build();
//    }
    
    @Bean
    public Job servicingDetailJob(Step processServicingDetail) {
        return new JobBuilder("servicingDetailJob", jobRepository)
                .start(processServicingDetail)
                .build();
    }
}