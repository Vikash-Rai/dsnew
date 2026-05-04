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

import com.equabli.collectprism.entity.Employer;
import com.equabli.collectprism.processor.EmployerProcessor;
import com.equabli.collectprism.reader.CustomItemReader;
import com.equabli.collectprism.repository.EmployerRepository;
import com.equabli.collectprism.writer.EmployerWriter;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.utils.Constants;

@Configuration
@EnableBatchProcessing
public class EmployerConfig {

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	PlatformTransactionManager platformTransactionManager;

	@Autowired
	private JobRepository jobRepository;

	@Bean
	@StepScope
	public CustomItemReader<Employer> employerReader(@Value("#{jobParameters['authHeader']}") String authHeader) {
		CustomItemReader<Employer> reader = new CustomItemReader<>();
		List<Object> queryMethodArguments = new ArrayList<>();
		Map<String, Direction> sorts = new HashMap<>();

		queryMethodArguments.add(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
		sorts.put("employerId", Direction.ASC);

		reader.setRepository(employerRepository);
		reader.setMethodName("getEmployerToProcess");
		reader.setJobName("Employer Job");
		reader.setArguments(queryMethodArguments);
		reader.setPageSize(Constants.OTHERCHUNKSIZE);
		reader.setSort(sorts);
		reader.setAuthHeader(authHeader);

		return reader;
	}

	@Bean
	public ItemProcessor<Employer, Employer> employerProcessor() {
		return new EmployerProcessor();
	}

	@Bean
	public ItemWriter<Employer> employerWriter() {
		return new EmployerWriter();
	}

	@Bean
	protected Step processEmployer(ItemReader<Employer> itemReader, ItemProcessor<Employer, Employer> processor,
			ItemWriter<Employer> writer) {
		return new StepBuilder("processEmployer", jobRepository)
				.<Employer, Employer>chunk(Constants.OTHERCHUNKSIZE, platformTransactionManager).reader(itemReader)
				.processor(processor).writer(writer).build();
	}

//	@Bean
//	public Job employerJob() {
//		return new JobBuilder("employerJob", jobRepository)
//				.start(processEmployer(employerReader(), employerProcessor(), employerWriter())).build();
//	}

	@Bean
	public Job employerJob(Step processEmployer) {
		return new JobBuilder("employerJob", jobRepository).start(processEmployer).build();
	}
}