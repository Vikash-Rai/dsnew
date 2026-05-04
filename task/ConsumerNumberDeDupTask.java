package com.equabli.collectprism.task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.equabli.client.CommonRestClient;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AddressRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.repository.EmailRepository;
import com.equabli.collectprism.repository.PhoneRepository;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.utils.Constants;

@Component
public class ConsumerNumberDeDupTask implements Tasklet, StepExecutionListener {

	static Logger logger = LoggerFactory.getLogger(ConsumerNumberDeDupTask.class);

	private CommonRestClient client;
	private AccountRepository accountRepository;
	private ConsumerRepository consumerRepository;
	private AddressRepository addressRepository;
	private EmailRepository emailRepository;
	private PhoneRepository phoneRepository;
	private String authHeader;

	public ConsumerNumberDeDupTask(CommonRestClient client, AccountRepository accountRepository, ConsumerRepository consumerRepository, AddressRepository addressRepository, 
				EmailRepository emailRepository, PhoneRepository phoneRepository) {
		this.client = client;
		this.accountRepository = accountRepository;
		this.consumerRepository = consumerRepository;
		this.addressRepository = addressRepository;
		this.emailRepository = emailRepository;
		this.phoneRepository = phoneRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("Consumer Number DeDup Job Started!!!");

		Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - Consumer Number DeDup Job started", "Data Scrubbing - Consumer Number DeDup Job started" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse.getValidation()) {
			logger.info("Email Sent");
		}
		int page = 0;
		int pageSize = Constants.OTHERCHUNKSIZE;
		int totalClientConsumerNoDeDup = 0;
		LocalDateTime lastSuccessfulDateTime = consumerRepository.getMaxBatchStepExecutionStartTime("consumerNumberDeDupTaskStep")
		        .orElseGet(() -> LocalDateTime.now().minusYears(1));

		Page<Consumer> pageResult;
		do {
		    pageResult = consumerRepository.getClientConsumerNoDeDup(lastSuccessfulDateTime, PageRequest.of(page, pageSize));
		    List<Consumer> batch = pageResult.getContent();
		    totalClientConsumerNoDeDup = totalClientConsumerNoDeDup + batch.size();

		    for (Consumer cns : batch) {
				accountRepository.accountSuspectedByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), "E30218", new HashSet<>(Collections.singletonList(new ErrWarJson("e", "E30218"))), cns.getClientId(), cns.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				consumerRepository.suspectConsumerByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), cns.getClientId(), cns.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				addressRepository.suspectAddressByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), cns.getClientId(), cns.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				phoneRepository.suspectPhoneByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), cns.getClientId(), cns.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				emailRepository.suspectEmailByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), cns.getClientId(), cns.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}

			Response<Boolean> mailResponse2 = client.sendMail("Data Scrubbing - Consumer Number DeDup Job In Progress", "Processed " + totalClientConsumerNoDeDup + " de duped consumer" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
			if(mailResponse2.getValidation()) {
				logger.info("Email Sent");
			}
		    page++;
		} while (pageResult.hasNext());

		logger.debug("Total " + totalClientConsumerNoDeDup + " de duped consumer processed");
		logger.info("Consumer Number DeDup Job Completed!!! ");
		Response<Boolean> mailResponse4 = client.sendMail("Data Scrubbing - Consumer Number DeDup Job completed", "Total " + totalClientConsumerNoDeDup + " de duped consumer processed" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse4.getValidation()) {
			logger.info("Email Sent");
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		this.authHeader = stepExecution
	            .getJobParameters()
	            .getString("authHeader");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
	}
}