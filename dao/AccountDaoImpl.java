package com.equabli.collectprism.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Address;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.Email;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Ledger;
import com.equabli.collectprism.entity.Phone;
import com.equabli.collectprism.entity.ScrubWarning;
import com.equabli.collectprism.processor.AccountProcessor;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AddressRepository;
import com.equabli.collectprism.repository.EmailRepository;
import com.equabli.collectprism.repository.LedgerRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.repository.PhoneRepository;
import com.equabli.collectprism.repository.ScrubWarningRepository;
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
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.SOLCalculation;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class AccountDaoImpl implements AccountDao {
    static Logger logger = LoggerFactory.getLogger(AccountDaoImpl.class);

    NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private CacheableService cacheableService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private ScrubWarningRepository scrubWarningRepository;

    @Autowired
    private LedgerRepository ledgerRepository;

    public AccountDaoImpl(DataSource dataSource) {
        namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Response<Map<String, Object>> updateAccountDetails(Account account) {
        Response<Map<String, Object>> response = new Response<>();
        try {
            Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
            account.setUpdatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
            account.setAppId(Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()));
            account.setRecordSourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()));
            List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(account.getClientId(), cacheableService);

            transferDataPayment(account);
            accountValid(account, errWarMessagesList);
            if(CommonUtils.isObjectNull(account.getErrCodeJson()) || account.getErrCodeJson().isEmpty()) {
                account.setDtmUtcUpdate(LocalDateTime.now());
				updateAccount(account);
                response.setMessage("Account Data Updated ");
                response.setValidation(true);

                Account acc = accountRepository.findById(account.getAccountId()).get();
                acc.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.RAW).getRecordStatusId());
                othersEntitiesValidation(acc, errWarMessagesList, requestDetailsMap);
                accountRepository.save(acc);
            	try {
            		for(Consumer con: acc.getConsumer()) {
                		if(!CommonUtils.isLongNullOrZero(con.getAddressId())) {
                			addressRepository.updateAddressIsPrimaryStatus(acc.getClientId(), acc.getClientAccountNumber(), con.getClientConsumerNumber(), con.getAddressId());
                		}
                		if(!CommonUtils.isLongNullOrZero(con.getEmailId())) {
                			emailRepository.updateEmailIsPrimaryStatus(acc.getClientId(), acc.getClientAccountNumber(), con.getClientConsumerNumber(), con.getEmailId());
                		}
                		if(!CommonUtils.isLongNullOrZero(con.getPhoneId())) {
                			phoneRepository.updatePhoneIsPrimaryStatus(acc.getClientId(), acc.getClientAccountNumber(), con.getClientConsumerNumber(), con.getPhoneId());
                		}
            		}
                	if(acc.getScrubWarnings() != null && acc.getScrubWarnings().size() > 0) {
                    	scrubWarningRepository.saveAll(acc.getScrubWarnings());
                	}
                	if(acc.getLedger() != null && !CommonUtils.isLongNullOrZero(acc.getLedger().getAccountId())) {
                		ledgerRepository.save(acc.getLedger());
                	}
            	} catch (Exception e) {
        			logger.error(e.getMessage(), e);
        		}
            } else {
                ErrWarJson errWarJson = account.getErrCodeJson().stream().findFirst().get();
                Map<String, Object> errWarMessagesMap = DataScrubbingUtils.getAllApplicableScrubRulesDesc(account.getClientId(), cacheableService);
                response.setMessage(errWarMessagesMap.get(errWarJson.getValue()) + "");
                response.setValidation(false);
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
            response.setMessage(e.toString());
            response.setValidation(false);
        }
        return response;
    }

	private void accountValid(Account account, List<String> errWarMessagesList) {
        Map<String, Object> validationMap = new HashMap<>();
        validationMap.put("Account Id", account.getAccountId());
        validationMap.put("isAccountValidated", true);
        account.setErrShortName(null);
        account.setErrCodeJson(null);

        AccountValidation.mandatoryValidation(account, validationMap, cacheableService, errWarMessagesList);
        if (Boolean.parseBoolean(validationMap.get("isAccountValidated").toString())) {
            AccountValidation.lookUpValidation(account, validationMap, cacheableService, errWarMessagesList);
            if (Boolean.parseBoolean(validationMap.get("isAccountValidated").toString())) {
                AccountValidation.standardize(account);
                AccountValidation.deDupValidation(account, validationMap, errWarMessagesList);
                AccountValidation.businessRule(account, validationMap, errWarMessagesList);
            }
        }
    }

    private void transferDataPayment(Account account) {
        account.setClient(paymentRepository.findClientById(account.getClientId()));
        if(!CommonUtils.isIntegerNullOrZero(account.getProductId())) {
            account.setProduct(accountRepository.getProduct(account.getProductId()));

            if(!CommonUtils.isIntegerNullOrZero(account.getProductSubTypeId())) {
            	account.setProductSubTypeCount(accountRepository.productSubTypeCount(account.getProductId(), account.getProductSubTypeId()));
            }
        }
        account.setCustomerTypeLookUp(accountRepository.getLookUpByKeyValue("customer_type",account.getCustomerType()));
        if(!CommonUtils.isStringNullOrBlank(account.getDebtType())) {
        	account.setDebtTypeLookUp(accountRepository.getLookUpByKeyValue("debt_type",account.getDebtType()));
        }
        if(!CommonUtils.isStringNullOrBlank(account.getPortfolioCode())) {
        	account.setPortfolioCodeLookup(accountRepository.getLookUpByKeyValue("portfolio_code",account.getPortfolioCode()));
        }
        if(!CommonUtils.isStringNullOrBlank(account.getOriginalAccountApplicationType())) {
        	account.setOriginalAccountApplicationTypeLookUp(accountRepository.getLookUpByKeyValue("account_application_type",account.getOriginalAccountApplicationType()));
        }
        if(!CommonUtils.isStringNullOrBlank(account.getSaleReviewStatus())) {
        	account.setSaleReviewStatusLookUp(accountRepository.getLookUpByKeyValue("sale_review_status",account.getSaleReviewStatus()));
        }
        if(!CommonUtils.isStringNullOrBlank(account.getOriginalLenderCreditor())) {
        	account.setOriginalAccountNoDeDup(accountRepository.originalAccountNoDeDup(account.getClientId(), account.getOriginalAccountNumber(), account.getOriginalLenderCreditor()));
        }
    }

    private void othersEntitiesValidation(Account account, List<String> errWarMessagesList, Map<String, Object> requestDetailsMap) {
		List<ScrubWarning> scrubWarningList = new ArrayList<ScrubWarning>();
        Map<String, Object> validationMap = new HashMap<>();
        String updatedBy = requestDetailsMap.get(CommonConstants.USER_KEY).toString();
        Integer appId = Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString());
        Integer recordSourceId = Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString());

        validationMap.put("Account Id", account.getAccountId());
        validationMap.put("isAccountValidated", true);
        account.setErrShortName(null);
        account.setErrCodeJson(null);

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
					} else if(errWarMessagesList.contains("E30220") || errWarMessagesList.contains("W30220")) {
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

				ConsumerValidation.mandatoryValidation(account, consumer, validationMap, cacheableService, errWarMessagesList);
				ConsumerValidation.lookUpValidation(account, consumer, validationMap, cacheableService,errWarMessagesList);
				ConsumerValidation.standardize(account, consumer, validationMap, cacheableService,errWarMessagesList);
				ConsumerValidation.deDupValidation(account, consumer, validationMap,errWarMessagesList);
				ConsumerValidation.referenceUpdation(account, consumer);
				ConsumerValidation.misingRefCheck(account, consumer, validationMap,errWarMessagesList);
				ConsumerValidation.businessRule(account, consumer, validationMap,errWarMessagesList);

				if(consumer.getAddress() != null && consumer.getAddress().size() > 0) {
					for(Address address: consumer.getAddress()) {
						validationMap.put("isAddressValidated", true);

						address.setErrShortName(null);
						address.setErrCodeJson(null);

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
				
				if(consumer.getAddressConsumers() != null && consumer.getAddressConsumers().size() > 0) {
					for(Address address: consumer.getAddressConsumers()) {
						validationMap.put("isAddressValidated", true);

						address.setErrShortName(null);
						address.setErrCodeJson(null);

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

				if(consumer.getPhone() != null && consumer.getPhone().size() > 0) {
					for(Phone phone: consumer.getPhone()) {
						validationMap.put("isPhoneValidated", true);

						phone.setErrShortName(null);
						phone.setErrCodeJson(null);

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
				if(consumer.getPhoneConsumer() != null && consumer.getPhoneConsumer().size() > 0) {
					for(Phone phone: consumer.getPhoneConsumer()) {
						validationMap.put("isPhoneValidated", true);

						phone.setErrShortName(null);
						phone.setErrCodeJson(null);

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

				if(consumer.getEmail() != null && consumer.getEmail().size() > 0) {
					for(Email email: consumer.getEmail()) {
						validationMap.put("isEmailValidated", true);

						email.setErrShortName(null);
						email.setErrCodeJson(null);

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
				
				if(consumer.getEmailConsumer() != null && consumer.getEmailConsumer().size() > 0) {
					for(Email email: consumer.getEmailConsumer()) {
						validationMap.put("isEmailValidated", true);

						email.setErrShortName(null);
						email.setErrCodeJson(null);

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

			if((errWarMessagesList.contains("E60102") || errWarMessagesList.contains("W60102")) && (CommonUtils.isObjectNull(account.getErrCodeJson()) || !DataScrubbingUtils.ifErrorWarningCodeExistsInErrJSON(account.getErrCodeJson()) ) && !Boolean.parseBoolean(validationMap.get("isPrimaryDebtor").toString())) {
				if(errWarMessagesList.contains("E60102")) {
					validationMap.put("isAccountValidated", false);
					account.setErrShortName("E60102");
					account.addErrWarJson(new ErrWarJson("e", "E60102"));
				} else if(errWarMessagesList.contains("W60102")) {
					account.addErrWarJson(new ErrWarJson("w", "E60102"));
				}
			}
		} else if(errWarMessagesList.contains("E60102")|| errWarMessagesList.contains("W60102")) {
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
				AccountProcessor.setSolCalulation(account, solCalulation);

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
								}else {
									solMonth = address.getStatutesOfLimitationMinMonthForProduct().getSolMonth();
									logger.debug("solMonth calculated for min#" + solMonth); 
								}

								if(address.getClientStatutesOfLimitation() != null) {
									clientConfiguredDays = address.getClientStatutesOfLimitation().getSolDay();
									logger.debug("clientConfiguredDays#" + clientConfiguredDays);
								}
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
						AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, account);
					} else if (CommonUtils.isObjectNull(sol.getCalculatedSOLDate())) {
						AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
					} else {
						Integer dateCompare = sol.getCalculatedSOLDate().compareTo(sol.getCurrentSOLDate());
						if(dateCompare > 0) {
							logger.info("Equabli SOL Date occurs after Client SOL Date");
							AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
						} else if(dateCompare < 0) {
							logger.info("Equabli SOL Date occurs before Client SOL Date");
							//SOL Conflict scenario
							AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, false, account);
						} else if(dateCompare == 0) {
							logger.info("Both dates are equal");
							AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), true, false, account);
						}
					}
				} else if(null != sol ) {
					AccountProcessor.updateSOLDateDetailsInAccount(sol.getCalculatedSOLDate(), sol.getCurrentSOLDate(), false, true, account);
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
	}

	private void updateAccount(Account account) {
		namedTemplate.update(UPDATE_ACCOUNT_SUSPECTED, Account.getParamsValue(account));
	}

	private static final String UPDATE_ACCOUNT_SUSPECTED =  " update data.account set current_lender_creditor = :currentLenderCreditor, original_lender_creditor = :originalLenderCreditor, "
			+ " product_id=:productId, productsubtype_id=:productSubTypeId, dt_original_account_open=:originalAccountOpenDate, dt_delinquency=:delinquencyDate, "
			+ " dt_charge_off=:chargeOffDate, dt_last_payment=:lastPaymentDate, dt_last_purchase=:lastPurchaseDate, dt_last_cash_advance=:lastCashAdvanceDate, dt_last_balance_transfer=:lastBalanceTransferDate, "
			+ " dt_statute=:solDate, customer_type=:customerType, debt_type=:debtType, portfolio_code=:portfolioCode, amt_last_payment=:amtLastPayment, amt_last_purchase=:amtLastPurchase, "
			+ " amt_last_cash_advance=:amtLastCashAdvance, amt_last_balance_transfer=:amtLastBalanceTransfer, amt_pre_charge_off_balance=:amtPreChargeOffBalance, amt_pre_charge_off_principle=:amtPreChargeOffPrinciple, "
			+ " amt_pre_charge_off_interest=:amtPreChargeOffInterest, amt_pre_charge_off_fee=:amtPreChargeOffFees, amt_post_charge_off_interest=:amtPostChargeOffInterest, "
			+ " pct_post_charge_off_interest=:pctPostChargeOffInterest, amt_post_charge_off_fees=:amtPostChargeOffFee, pct_post_charge_off_feerate=:pctPostChargeOffFee, "
			+ " amt_post_charge_off_payment=:amtPostChargeOffPayment, amt_post_charge_off_credit=:amtPostChargeOffCredit, amt_assigned=:amtAssigned, amt_principal_assigned=:amtPrincipalAssigned, "
			+ " amt_interest_assigned=:amtInterestAssigned, amt_latefee_assigned=:amtLatefeeAssigned, amt_otherfee_assigned=:amtOtherfeeAssigned, amt_courtcost_assigned=:amtCourtcostAssigned, "
			+ " amt_attorneyfee_assigned=:amtAttorneyfeeAssigned, product_affinity=:productAffinity "
			+ " where account_id = :accountId ";
}