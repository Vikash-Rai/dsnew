package com.equabli.collectprism.processor;

import java.time.LocalDate;
import java.util.*;

import com.equabli.domain.helpers.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Address;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.Email;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Ledger;
import com.equabli.collectprism.entity.Phone;
import com.equabli.collectprism.entity.ScrubWarning;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.AccountValidation;
import com.equabli.collectprism.validation.AddressValidation;
import com.equabli.collectprism.validation.ConsumerValidation;
import com.equabli.collectprism.validation.EmailValidation;
import com.equabli.collectprism.validation.PhoneValidation;
import com.equabli.domain.Queue;
import com.equabli.domain.QueueReason;
import com.equabli.domain.QueueStatus;
import com.equabli.domain.Response;
import com.equabli.domain.SOLCalulation;
import com.equabli.domain.StatuteOfLimitation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.SOLCalculation;

public class AccountProcessor implements ItemProcessor<Account, Account> {

    private final Logger logger = LoggerFactory.getLogger(AccountProcessor.class);

    @Autowired
	private CacheableService cacheableService;

    private long jobExecutionId;

    private JobParameters jobParameters;

	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobExecutionId = stepExecution.getJobExecutionId();
        jobParameters = stepExecution.getJobParameters();
    }

    @Override
    public Account process(Account account) throws Exception {
    	try {
    		List<ScrubWarning> scrubWarningList = new ArrayList<ScrubWarning>();

    		String updatedBy = jobParameters.getString("updatedBy");
    		Integer appId = Integer.parseInt(jobParameters.getString("appId"));
    		Integer recordSourceId = Integer.parseInt(jobParameters.getString("recordSourceId"));

    		Map<String, Object> validationMap = new HashMap<String, Object>();
    		validationMap.put("Account Id", account.getAccountId());
    		validationMap.put("isAccountValidated", true);
    		validationMap.put("isConsumerValidated", false);
    		validationMap.put("isPhoneValidated", false);
    		validationMap.put("isAddressValidated", false);
    		validationMap.put("jobExecutionId", jobExecutionId);

    		account.setErrShortName(null);
    		account.setErrCodeJson(null);

    		List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(account.getClientId(), cacheableService);

    		if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    			AccountValidation.mandatoryValidation(account, validationMap, cacheableService,errWarMessagesList);

    		if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    			AccountValidation.lookUpValidation(account, validationMap, cacheableService,errWarMessagesList);

    		if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    			AccountValidation.standardize(account);
    			AccountValidation.deDupValidation(account, validationMap,errWarMessagesList);
    		}

    		if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
    				&& !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    			AccountValidation.businessRule(account, validationMap,errWarMessagesList);

    		if(account.getConsumer() != null && account.getConsumer().size() > 0) {
    			validationMap.put("isPrimaryDebtor", false);
    			consumerValidation: for(Consumer consumer: account.getConsumer()) {
    				validationMap.put("isConsumerValidated", true);

    				consumer.setErrShortName(null);
    				consumer.setErrCodeJson(null);

    				if(consumer.getContactTypeLookUp() != null && consumer.getContactTypeLookUp().getKeycode().equalsIgnoreCase("PD")) {
    					consumer.setEntityPrimary(true);

    					if(!Boolean.parseBoolean(validationMap.get("isPrimaryDebtor").toString())) {
    						validationMap.put("isPrimaryDebtor", true);
    					} else if((errWarMessagesList.contains("E30220") || errWarMessagesList.contains("W30220"))) {
							if(errWarMessagesList.contains("E30220")) {
								validationMap.put("isConsumerValidated", false);
								consumer.setErrShortName("E30220");
								consumer.addErrWarJson(new ErrWarJson("e", "E30220"));
							} else if(errWarMessagesList.contains("W30220")) {
								consumer.addErrWarJson(new ErrWarJson("w", "E30220"));
							}

    						if(consumer.isEntityPrimary()) {
    							if(errWarMessagesList.contains("E30220")) {
    								account.setErrShortName("E30220");
    								account.addErrWarJson(new ErrWarJson("e", "E30220"));
    							} else if(errWarMessagesList.contains("W30220")) {
    								account.addErrWarJson(new ErrWarJson("w", "E30220"));
    							}
    						}
    					}
    				}

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    					ConsumerValidation.mandatoryValidation(account, consumer, validationMap, cacheableService, errWarMessagesList);

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    					ConsumerValidation.lookUpValidation(account, consumer, validationMap, cacheableService,errWarMessagesList);

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    					ConsumerValidation.standardize(account, consumer, validationMap, cacheableService,errWarMessagesList);

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    					ConsumerValidation.deDupValidation(account, consumer, validationMap,errWarMessagesList);

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    					ConsumerValidation.referenceUpdation(account, consumer);
    					ConsumerValidation.misingRefCheck(account, consumer, validationMap,errWarMessagesList);
    				}

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId()))
    					ConsumerValidation.businessRule(account, consumer, validationMap,errWarMessagesList);

    				if(consumer.getAddress() != null && consumer.getAddress().size() > 0) {
    					for(Address address: consumer.getAddress()) {
    						validationMap.put("isAddressValidated", true);

    						address.setErrShortName(null);
    						address.setErrCodeJson(null);

    						if(!address.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !address.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							AddressValidation.mandatoryValidation(account, consumer, address, validationMap, cacheableService, errWarMessagesList);

    							AddressValidation.lookUpValidation(account, consumer, address, validationMap, cacheableService, errWarMessagesList);

    							AddressValidation.standardize(account, address, consumer, validationMap, cacheableService, errWarMessagesList);
    							AddressValidation.deDupValidation(account, address, consumer, validationMap);

    							AddressValidation.referenceUpdation(account, consumer, address, consumer.getAddress().size());
    							AddressValidation.misingRefCheck(account, consumer, address, validationMap, errWarMessagesList);

    							AddressValidation.businessRule(address, validationMap);

    							if(CommonUtils.isObjectNull(address.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(address.getErrCodeJson())) {					
    								address.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								address.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}
    				
    				if(consumer.getAddressConsumers() != null && consumer.getAddressConsumers().size() > 0) {
    					for(Address address: consumer.getAddressConsumers()) {
    						validationMap.put("isAddressValidated", true);

    						address.setErrShortName(null);
    						address.setErrCodeJson(null);

    						if(!address.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !address.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							AddressValidation.mandatoryValidation(account, consumer, address, validationMap, cacheableService, errWarMessagesList);

    							AddressValidation.lookUpValidation(account, consumer, address, validationMap, cacheableService, errWarMessagesList);

    							AddressValidation.standardize(account, address, consumer, validationMap, cacheableService, errWarMessagesList);
    							AddressValidation.deDupValidation(account, address, consumer, validationMap);

    							AddressValidation.referenceUpdation(account, consumer, address, consumer.getAddressConsumers().size());
    							AddressValidation.misingRefCheck(account, consumer, address, validationMap, errWarMessagesList);

    							AddressValidation.businessRule(address, validationMap);

    							if(CommonUtils.isObjectNull(address.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(address.getErrCodeJson())) {					
    								address.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								address.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}

    				if(consumer.getPhone() != null && consumer.getPhone().size() > 0) {
    					for(Phone phone: consumer.getPhone()) {
    						validationMap.put("isPhoneValidated", true);

    						phone.setErrShortName(null);
    						phone.setErrCodeJson(null);

    						if(!phone.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !phone.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							PhoneValidation.mandatoryValidation(account, consumer, phone, validationMap, cacheableService, errWarMessagesList);

    							PhoneValidation.lookUpValidation(account, consumer, phone, validationMap, cacheableService, errWarMessagesList);

    							PhoneValidation.standardize(phone);
    							PhoneValidation.deDupValidation(account, consumer, phone, validationMap, errWarMessagesList);

    							PhoneValidation.referenceUpdation(account, consumer, phone, consumer.getPhone().size());
    							PhoneValidation.misingRefCheck(account, consumer, phone, validationMap, errWarMessagesList);

    							PhoneValidation.businessRule(phone, validationMap, scrubWarningList, appId, recordSourceId, errWarMessagesList);

    							if(CommonUtils.isObjectNull(phone.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(phone.getErrCodeJson())) {				
    								phone.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								phone.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}
    				if(consumer.getPhoneConsumer() != null && consumer.getPhoneConsumer().size() > 0) {
    					for(Phone phone: consumer.getPhoneConsumer()) {
    						validationMap.put("isPhoneValidated", true);

    						phone.setErrShortName(null);
    						phone.setErrCodeJson(null);

    						if(!phone.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !phone.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							PhoneValidation.mandatoryValidation(account, consumer, phone, validationMap, cacheableService, errWarMessagesList);

    							PhoneValidation.lookUpValidation(account, consumer, phone, validationMap, cacheableService, errWarMessagesList);

    							PhoneValidation.standardize(phone);
    							PhoneValidation.deDupValidation(account, consumer, phone, validationMap, errWarMessagesList);

    							PhoneValidation.referenceUpdation(account, consumer, phone, consumer.getPhoneConsumer().size());
    							PhoneValidation.misingRefCheck(account, consumer, phone, validationMap, errWarMessagesList);

    							PhoneValidation.businessRule(phone, validationMap, scrubWarningList, appId, recordSourceId, errWarMessagesList);

    							if(CommonUtils.isObjectNull(phone.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(phone.getErrCodeJson())) {				
    								phone.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								phone.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}

    				if(consumer.getEmail() != null && consumer.getEmail().size() > 0) {
    					for(Email email: consumer.getEmail()) {
    						validationMap.put("isEmailValidated", true);

    						email.setErrShortName(null);
    						email.setErrCodeJson(null);

    						if(!email.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !email.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							EmailValidation.mandatoryValidation(account, email, validationMap, errWarMessagesList);

    							EmailValidation.lookUpValidation(account, email, validationMap, errWarMessagesList);

    							EmailValidation.standardize(email);
    							EmailValidation.deDupValidation(account, consumer, email, validationMap, scrubWarningList, errWarMessagesList);

    							EmailValidation.referenceUpdation(account, consumer, email, consumer.getEmail().size());
    							EmailValidation.misingRefCheck(account, email, validationMap, errWarMessagesList);

    							EmailValidation.businessRule(email, validationMap, errWarMessagesList);

    							if(CommonUtils.isObjectNull(email.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(email.getErrCodeJson())) {
    								email.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								email.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}
    				
    				if(consumer.getEmailConsumer() != null && consumer.getEmailConsumer().size() > 0) {
    					for(Email email: consumer.getEmailConsumer()) {
    						validationMap.put("isEmailValidated", true);

    						email.setErrShortName(null);
    						email.setErrCodeJson(null);

    						if(!email.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    								&& !email.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    							EmailValidation.mandatoryValidation(account, email, validationMap, errWarMessagesList);

    							EmailValidation.lookUpValidation(account, email, validationMap, errWarMessagesList);

    							EmailValidation.standardize(email);
    							EmailValidation.deDupValidation(account, consumer, email, validationMap, scrubWarningList, errWarMessagesList);

    							EmailValidation.referenceUpdation(account, consumer, email, consumer.getEmailConsumer().size());
    							EmailValidation.misingRefCheck(account, email, validationMap, errWarMessagesList);

    							EmailValidation.businessRule(email, validationMap, errWarMessagesList);

    							if(CommonUtils.isObjectNull(email.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(email.getErrCodeJson())) {
    								email.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    							} else {
    								email.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}

    				if(!consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
    						&& !consumer.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId())) {
    					if(CommonUtils.isObjectNull(consumer.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(consumer.getErrCodeJson())) {
    						consumer.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
    					} else {
    						consumer.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    						if(consumer.isEntityPrimary()) {
    							validationMap.put("isAccountValidated", false);
    							break consumerValidation;
    						}
    					}
    				}
    			}

    			if((errWarMessagesList.contains("E60102") || errWarMessagesList.contains("W60102")) && (CommonUtils.isObjectNull(account.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(account.getErrCodeJson()) ) && !Boolean.parseBoolean(validationMap.get("isPrimaryDebtor").toString())) {
					if(errWarMessagesList.contains("E60102")) {
						validationMap.put("isAccountValidated", false);
						account.setErrShortName("E60102");
						account.addErrWarJson(new ErrWarJson("e", "E60102"));
					} else if(errWarMessagesList.contains("W60102")) {
						account.addErrWarJson(new ErrWarJson("w", "E60102"));
					}
    			}
    		} else if((errWarMessagesList.contains("E60102") || errWarMessagesList.contains("W60102"))) {
				if(errWarMessagesList.contains("E60102")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E60102");
					account.addErrWarJson(new ErrWarJson("e", "E60102"));
				} else if(errWarMessagesList.contains("W60102")) {
					account.addErrWarJson(new ErrWarJson("w", "E60102"));
				}
    		}

    		account.setScrubWarnings(scrubWarningList);

    		logger.debug("{}",validationMap);
    		if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) && !account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SOLWAIT).getRecordStatusId())) {
    			if(!account.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId())
    					&& (CommonUtils.isObjectNull(account.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(account.getErrCodeJson()))) {
    				account.setLedger(Ledger.setLedger(account, updatedBy, recordSourceId, appId));
    				account.setCurrentBalanceData(account);
    				try {// Updating Pre Charge Off Buckets
    					if(CommonUtils.isObjectNull(account.getChargeOffDate()))
    						account.setPreChargeOffBuckets(account);
    				}catch (Exception e) {
						e.printStackTrace();
					}
    				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SOLWAIT).getRecordStatusId());

    				Integer solMonth = 0;
    				Integer clientConfiguredDays = 0;
    				SOLCalulation solCalulation = new SOLCalulation();
    				Response<StatuteOfLimitation> response = new  Response<StatuteOfLimitation>();
    				setSolCalulation(account, solCalulation);

        			for(Consumer consumer: account.getConsumer()) {
        				if(consumer.getContactTypeLookUp() != null && consumer.getContactTypeLookUp().getKeycode().equalsIgnoreCase("PD")) {
							for(Address address: consumer.getAddress()) {
								if(address.getIsPrimary() != null && address.getIsPrimary()) {
									solCalulation.setStateCode(address.getStateCode());
									solCalulation.setCountryStateId(!CommonUtils.isObjectNull(address.getCountryState()) ? address.getCountryState().getCountryStateId() : null);

									logger.debug("client_id#" + address.getClientId() + "#client_account_number#" + address.getClientAccountNumber() + "#state_code#" + address.getStateCode());

									if(address.getStatutesOfLimitation() != null) {
										solMonth = address.getStatutesOfLimitation().getSolMonth();
										logger.debug("solMonth#" + solMonth);
									}

									if(address.getClientStatutesOfLimitation() != null) {
										clientConfiguredDays = address.getClientStatutesOfLimitation().getSolDay();
										logger.debug("clientConfiguredDays#" + clientConfiguredDays);
									}
								}else {
									solMonth = address.getStatutesOfLimitationMinMonthForProduct().getSolMonth();
									logger.debug("solMonth calculated for min#" + solMonth);
								}
                			}
        				}
    				}

    				response = SOLCalculation.solCalculation(account.getAccountId(), solCalulation, solMonth, clientConfiguredDays, response);

					StatuteOfLimitation sol = response.getResponse();


					if(null != sol && sol.getIsPrimaryStateExists()) {
						logger.debug(sol.getCalculatedSOLDate() + "");

						if(CommonUtils.isDateNull(sol.getCurrentSOLDate())) {
							logger.info("Current SOL Date is null ");
							updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, account);
						} else if (CommonUtils.isObjectNull(sol.getCalculatedSOLDate())) {
							updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
						} else {
							Integer dateCompare = sol.getCalculatedSOLDate().compareTo(sol.getCurrentSOLDate());
							if(dateCompare > 0) {
								logger.info("Equabli SOL Date occurs after Client SOL Date");
								updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
							} else if(dateCompare < 0) {
								logger.info("Equabli SOL Date occurs before Client SOL Date");
								//SOL Conflict scenario
								updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, false, account);
							} else if(dateCompare == 0) {
								logger.info("Both dates are equal");
								updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, account);
							}
						}
					} else if(null != sol ) {
						updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
					} else {
						account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
						logger.info("SOL is not calculated");
					}
					if(!CommonUtils.isStringNullOrBlank(account.getPartnerType()) && CommonConstants.PARTNER_TYPE_HOLDING_UNIT.equals(account.getPartnerType())) {
						account.setQueueId(Queue.confQueue.get(Queue.QUEUE_DNP).getQueueId());
						account.setQueueStatusId(QueueStatus.confQueueStatus.get(QueueStatus.QUEUESTATUS_DNP).getQueueStatusId());
						account.setQueueReasonId(QueueReason.confQueueReason.get(QueueReason.QUEUEREASON_NA).getQueueReasonId());
					} else {
						account.setQueueId(Queue.confQueue.get(Queue.QUEUE_PRP).getQueueId());
						account.setQueueStatusId(QueueStatus.confQueueStatus.get(QueueStatus.QUEUESTATUS_OPN).getQueueStatusId());
						account.setQueueReasonId(QueueReason.confQueueReason.get(QueueReason.QUEUEREASON_NA).getQueueReasonId());
					}
    			} else {
    				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    				if(account.getConsumer() != null && account.getConsumer().size() > 0) {
    					for(Consumer consumer: account.getConsumer()) {
    						if(CommonUtils.isStringNullOrBlank(consumer.getErrShortName())) {
    							consumer.setErrShortName(account.getErrShortName());
    						}
    						consumer.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    						if(consumer.getAddress() != null && consumer.getAddress().size() > 0) {
    							for(Address address: consumer.getAddress()) {
    								if(CommonUtils.isStringNullOrBlank(address.getErrShortName())) {
    									address.setErrShortName(account.getErrShortName());
    								}
    								address.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    						if(consumer.getPhone() != null && consumer.getPhone().size() > 0) {
    							for(Phone phone: consumer.getPhone()) {
    								if(CommonUtils.isStringNullOrBlank(phone.getErrShortName())) {
    									phone.setErrShortName(account.getErrShortName());
    								}
    								phone.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    						if(consumer.getEmail() != null && consumer.getEmail().size() > 0) {
    							for(Email email: consumer.getEmail()) {
    								if(CommonUtils.isStringNullOrBlank(email.getErrShortName())) {
    									email.setErrShortName(account.getErrShortName());
    								}
    								email.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
    							}
    						}
    					}
    				}
    			}
    		} else {
    			account.setErrShortName(null);
    			account.setErrCodeJson(null);
    		}
    	} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return account;
    }

	public static void setSolCalulation(Account account, SOLCalulation solCalulation) {
		solCalulation.setEquabliAccountId(account.getAccountId());
		solCalulation.setProductId(account.getProductId());
		solCalulation.setLastPaymentDate(account.getLastPaymentDate());
		solCalulation.setChargeOffDate(account.getChargeOffDate());
		solCalulation.setSOLDate(account.getSolDate());
		solCalulation.setClientId(account.getClientId());
		solCalulation.setPartnerId(account.getPartnerId());
		solCalulation.setDtDelinquency(account.getDelinquencyDate());
		solCalulation.setDtClientStatute(account.getClientSolDate());
		solCalulation.setDtEquabliStatute(account.getEquabliSolDate());
		if(account.getClient() != null)
			solCalulation.setClientCode(account.getClient().getShortName());
		if(account.getProduct() != null)
			solCalulation.setDebtCategoryId(account.getProduct().getDebtCategoryId());
		if(account.getCustomAppConfigValue() != null)
			solCalulation.setCycleDay(account.getCustomAppConfigValue().getCustomAppConfigValueId());
	}

	public static void updateSOLDateDetailsInAccount(LocalDate equabliCalculatedDate, LocalDate clientSOLDate, Boolean updateEquabliDateInAccount, Boolean updateClientDateInAccount, Account account) {
		if(updateEquabliDateInAccount) {
			account.setSolDate(equabliCalculatedDate);
			account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		} else if(updateClientDateInAccount) {
			account.setSolDate(clientSOLDate);
			account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		}

		if(!CommonUtils.isDateNull(equabliCalculatedDate)) {
			account.setEquabliSolDate(equabliCalculatedDate);
		}
	}
}