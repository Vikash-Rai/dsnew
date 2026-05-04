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
import com.equabli.collectprism.entity.Account;
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
public class AccountConsumerDeDupTask implements Tasklet, StepExecutionListener {

	static Logger logger = LoggerFactory.getLogger(AccountConsumerDeDupTask.class);

	private CommonRestClient client;
	private AccountRepository accountRepository;
	private ConsumerRepository consumerRepository;
	private AddressRepository addressRepository;
	private EmailRepository emailRepository;
	private PhoneRepository phoneRepository;
	private String authHeader;

	public AccountConsumerDeDupTask(CommonRestClient client, AccountRepository accountRepository, ConsumerRepository consumerRepository, AddressRepository addressRepository, 
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
    	logger.info("Account Consumer DeDup Job Started!!! ");
		Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - Account Consumer DeDup Job started", "Data Scrubbing - Account Consumer DeDup Job started" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse.getValidation()) {
			logger.info("Email Sent");
		}
		int page1 = 0;
		int pageSize1 = Constants.OTHERCHUNKSIZE;
		int totalClientAccountConsumerDeDup = 0;
		LocalDateTime lastSuccessfulDateTime = consumerRepository.getMaxBatchStepExecutionStartTime("accountConsumerDeDupTaskStep")
		        .orElseGet(() -> LocalDateTime.now().minusYears(1));

		Page<Account> pageResult1;
		do {
		    pageResult1 = accountRepository.getClientAccountConsumerDeDup(lastSuccessfulDateTime, PageRequest.of(page1, pageSize1));
		    List<Account> batch = pageResult1.getContent();
		    totalClientAccountConsumerDeDup = totalClientAccountConsumerDeDup + batch.size();

		    for (Account acc : batch) {
		    	accountRepository.accountSuspectedByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), "E70104", new HashSet<>(Collections.singletonList(new ErrWarJson("e", "E70104"))), acc.getClientId(), acc.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				consumerRepository.suspectConsumerByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), acc.getClientId(), acc.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				addressRepository.suspectAddressByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), acc.getClientId(), acc.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				phoneRepository.suspectPhoneByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), acc.getClientId(), acc.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				emailRepository.suspectEmailByClientIdAndClientAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), acc.getClientId(), acc.getClientAccountNumber(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		    }

			Response<Boolean> mailResponse2 = client.sendMail("Data Scrubbing - Account Consumer DeDup Job In Progress", "Processed " + totalClientAccountConsumerDeDup + " de duped account for client account number" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
			if(mailResponse2.getValidation()) {
				logger.info("Email Sent");
			}
		    page1++;
		} while (pageResult1.hasNext());

		logger.debug("Total " + totalClientAccountConsumerDeDup + " de duped account processed for client account number");
		Response<Boolean> mailResponse2 = client.sendMail("Data Scrubbing - Account Consumer DeDup Job completed", "Total " + totalClientAccountConsumerDeDup + " de duped account processed for client account number" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse2.getValidation()) {
			logger.info("Email Sent");
		}

		int page2 = 0;
		int pageSize2 = Constants.OTHERCHUNKSIZE;
		int totalOriginalAccountConsumerDeDup = 0;

		Page<Account> pageResult2;
		do {
		    pageResult2 = accountRepository.getOriginalAccountConsumerDeDup(lastSuccessfulDateTime, PageRequest.of(page2, pageSize2));
		    List<Account> batch = pageResult2.getContent();
		    totalOriginalAccountConsumerDeDup = totalOriginalAccountConsumerDeDup + batch.size();

			for(Account acc: batch) {
				accountRepository.accountSuspectedByClientIdAndOriginalAccountNumber(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), "E70104", new HashSet<>(Collections.singletonList(new ErrWarJson("e", "E70104"))), acc.getClientId(), acc.getOriginalAccountNumber(), acc.getOriginalLenderCreditor(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			}

			Response<Boolean> mailResponse1 = client.sendMail("Data Scrubbing - Account Consumer DeDup Job In Progress", "Processed " + totalOriginalAccountConsumerDeDup + " de duped account for original account number " + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
			if(mailResponse1.getValidation()) {
				logger.info("Email Sent");
			}
		    page2++;
		} while (pageResult2.hasNext());

		logger.debug("Total " + totalOriginalAccountConsumerDeDup + " de duped account processed for original account number");
		logger.info("Account Consumer DeDup Job Completed!!! ");
		Response<Boolean> mailResponse4 = client.sendMail("Data Scrubbing - Account Consumer DeDup Job completed", "Total " + totalOriginalAccountConsumerDeDup + " de duped account processed for original account number" + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
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