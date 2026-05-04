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

import com.equabli.collectprism.entity.Complaint;
import com.equabli.collectprism.processor.ComplaintProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.ComplaintRepository;
import com.equabli.collectprism.writer.ComplaintWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class ComplaintConfig {


    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Bean
    @StepScope
    public CustomItemReader<Complaint> complaintReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
        CustomItemReader<Complaint> reader = new CustomItemReader<>();
        List<Object> queryMethodArguments = new ArrayList<>();
        Map<String, Sort.Direction> sorts = new HashMap<>();

        queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
        sorts.put("complaintId", Sort.Direction.ASC);

        reader.setRepository(complaintRepository);
        reader.setMethodName("getComplaintToProcess");
        reader.setJobName("Complaint Job");
        reader.setArguments(queryMethodArguments);
        reader.setPageSize(Constants.OTHERCHUNKSIZE);
        reader.setSort(sorts);
        reader.setAuthHeader(authHeader);
        
        return reader;
    }

    @Bean
    public ItemProcessor<Complaint, Complaint> complaintProcessor() {
        return new ComplaintProcessor();
    }

    @Bean
    public ItemWriter<Complaint> complaintWriter() {
        return new ComplaintWriter();
    }

    @Bean
    protected Step processComplaint(ItemReader<Complaint> itemReader, ItemProcessor<Complaint, Complaint> processor, ItemWriter<Complaint> writer) {
        return new StepBuilder("processComplaint",jobRepository).<Complaint, Complaint>chunk(Constants.OTHERCHUNKSIZE,platformTransactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//    @Bean
//    public Job complaintJob() {
//        return new JobBuilder("complaintJob",jobRepository)
//                .start(processComplaint(complaintReader(), complaintProcessor(), complaintWriter()))
//                .build();
//    }
    
    @Bean
    public Job complaintJob(Step processComplaint) {
        return new JobBuilder("complaintJob", jobRepository)
                .start(processComplaint)
                .build();
    }
}
