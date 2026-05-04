package com.equabli.collectprism.service;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.equabli.client.CommonRestClient;
import com.equabli.common.auth.FetchTokenData;
import com.equabli.common.auth.TokenData;
import com.equabli.collectprism.dao.AccountDao;
import com.equabli.collectprism.dao.AdjustmentDao;
import com.equabli.collectprism.dao.BankruptcyDao;
import com.equabli.collectprism.dao.PaymentDao;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Address;
import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.Email;
import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.entity.Phone;
import com.equabli.collectprism.processor.AccountChangeProcessor;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AddressRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.repository.EmailRepository;
import com.equabli.collectprism.repository.ErrWarMessageRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.repository.PhoneRepository;
import com.equabli.collectprism.util.DBQueryUtils;
import com.equabli.domain.AppConfigValue;
import com.equabli.domain.AppDataDownloadRequest;
import com.equabli.domain.DownloadCriteria;
import com.equabli.domain.Response;
import com.equabli.domain.SearchCriteria;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.EntityUtil;
import com.equabli.domain.helpers.ErrorUtils;
import com.equabli.domain.reporting.ReportingUtils;
import com.equabli.feign.DataScrubEnrichmentServiceCommunication;
import com.equabli.helper.CsvHelper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class DataScrubbingServiceImpl implements DataScrubbingService {

	static Logger logger = LoggerFactory.getLogger(DataScrubbingServiceImpl.class);

	RestTemplateBuilder restTemplateBuilder;
	RestTemplate restTemplate;
	
	@Autowired
	DataScrubEnrichmentServiceCommunication serviceCommunication;
	
	@Autowired
	FetchTokenData fetchTokenData;
	
	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	@Qualifier("consumerNumberDeDupTaskJob")
	private Job consumerNumberDeDupTaskJob;

	@Autowired
	@Qualifier("accountConsumerDeDupTaskJob")
	private Job accountConsumerDeDupTaskJob;

	@Autowired
	@Qualifier("accountsDataScrubbingJob")
	private Job accountsDataScrubbingJob;

	@Autowired
	@Qualifier("consumerReprocessJob")
	private Job consumerReprocessJob;

	@Autowired
	@Qualifier("addressReprocessJob")
	private Job addressReprocessJob;

	@Autowired
	@Qualifier("phoneReprocessJob")
	private Job phoneReprocessJob;

	@Autowired
	@Qualifier("emailReprocessJob")
	private Job emailReprocessJob;

	@Autowired
	@Qualifier("complianceJob")
    private Job complianceJob;

	@Autowired
	@Qualifier("communicationDetailJob")
    private Job communicationDetailJob;

	@Autowired
	@Qualifier("normalPaymentJob")
	private Job normalPaymentJob;

	@Autowired
	@Qualifier("nsfPaymentJob")
	private Job nsfPaymentJob;

	@Autowired
	@Qualifier("paymentPlanJob")
    private Job paymentPlanJob;

	@Autowired
	@Qualifier("paymentScheduleJob")
    private Job paymentScheduleJob;

	@Autowired
	@Qualifier("legalPlacementJob")
    private Job legalPlacementJob;

	@Autowired
	@Qualifier("operationRequestJob")
    private Job operationRequestJob;

	@Autowired
	@Qualifier("operationResponseJob")
    private Job operationResponseJob;

	@Autowired
	@Qualifier("employerJob")
    private Job employerJob;

	@Autowired
	@Qualifier("servicingDetailJob")
    private Job servicingDetailJob;

	@Autowired
	@Qualifier("dialConsentExclusionJob")
    private Job dialConsentExclusionJob;

	@Autowired
	@Qualifier("smsConsentExclusionJob")
    private Job smsConsentExclusionJob;

	@Autowired
	@Qualifier("emailConsentExclusionJob")
    private Job emailConsentExclusionJob;

	@Autowired
	@Qualifier("adjustmentJob")
    private Job adjustmentJob;

	@Autowired
	@Qualifier("placedAccountJob")
    private Job placedAccountJob;

	@Autowired
	@Qualifier("accountUccJob")
    private Job accountUccJob;
	
	@Autowired
	@Qualifier("accountAssetJob")
    private Job accountAssetJob;
	
	@Autowired
	@Qualifier("creditScoreJob")
    private Job creditScoreJob;
	
	@Autowired
	@Qualifier("costJob")
    private Job costJob;
	
	@Autowired
	@Qualifier("changeLogJob")
    private Job changeLogJob;

	@Autowired
	@Qualifier("chargeOffAccountJob")
	private Job chargeOffAccountJob;

	@Autowired
	@Qualifier("partnerAssignmentJob")
	private Job partnerAssignmentJob;

	@Autowired
	@Qualifier("chainOfTitleJob")
	private Job chainOfTitleJob;

	@Autowired
	@Qualifier("cotMasterListJob")
	private Job cotMasterListJob;

	@Autowired
	@Qualifier("accountTagJob")
	private Job accountTagJob;

	@Autowired
	@Qualifier("lienJob")
	private Job lienJob;

	@Autowired
	@Qualifier("garnishmentJob")
	private Job garnishmentJob;

	@Autowired
	@Qualifier("bankruptcyJob")
	private Job bankruptcyJob;

	@Autowired
	@Qualifier("complaintJob")
	private Job complaintJob;

	@Autowired
	@Qualifier("disputeJob")
	private Job disputeJob;
	
	@Autowired
	@Qualifier("autoAccountInfoJob")
	private Job autoAccountInfoJob;

	@Autowired
	@Qualifier("accountAdditionalInfoJob")
    private Job accountAdditionalInfoJob;

	@Autowired
	private ErrWarMessageRepository messageRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private ConsumerRepository consumerRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PhoneRepository phoneRepository;

	@Autowired
	private EmailRepository emailRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CommonRestClient client;

	@Autowired
	private CsvHelper csvhelper;
	
	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	private BankruptcyDao bankruptcyDao;

	@Autowired
	private AdjustmentDao adjustmentDao;

	NamedParameterJdbcTemplate namedTemplate;
	
	@Autowired
	@Qualifier("deceasedJob")
	private Job deceasedJob;
	
	@Autowired
	AccountChangeProcessor accountChangeProcessor;


	public DataScrubbingServiceImpl(DataSource dataSource) {
		namedTemplate = new NamedParameterJdbcTemplate(dataSource);
		restTemplateBuilder = new RestTemplateBuilder();
		restTemplate = restTemplateBuilder.build();
		restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter(), new FormHttpMessageConverter()));
	}

	@Async("asyncAccountsDataScrubbingExecutor")
	public void accountsDataScrubbing(String updatedBy, String appId, String recordSourceId,String authHeader) {
        try {
   		 	logger.info("****** start asyncAccountsDataScrubbingExecutor ********");
			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).addString("updatedBy", updatedBy).addString("appId", appId).addString("recordSourceId", recordSourceId).addString("authHeader", authHeader).toJobParameters();

			jobLauncher.run(consumerNumberDeDupTaskJob, jobParameters);
            jobLauncher.run(accountConsumerDeDupTaskJob, jobParameters);
            jobLauncher.run(accountsDataScrubbingJob, jobParameters);
            jobLauncher.run(consumerReprocessJob, jobParameters);
            jobLauncher.run(addressReprocessJob, jobParameters);
            jobLauncher.run(phoneReprocessJob, jobParameters);
            jobLauncher.run(emailReprocessJob, jobParameters);
   		 	logger.info("****** end asyncAccountsDataScrubbingExecutor ********");
        } catch (Exception ex) {
        	ErrorUtils.buildErrorResponse(null, ex, logger);
   		 	logger.error("****** end error asyncAccountsDataScrubbingExecutor ********");
        }
	}

	@Async("asyncOtherDataScrubbingExecutor")
	public void othersDataScrubbing(String updatedBy, String appId, String recordSourceId,String authHeader) {
        try {
        	logger.info("****** start asyncOtherDataScrubbingExecutor ********");
			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis()).addString("updatedBy", updatedBy).addString("appId", appId).addString("recordSourceId", recordSourceId).addString("authHeader", authHeader).toJobParameters();

        	jobLauncher.run(complianceJob, jobParameters);
        	jobLauncher.run(communicationDetailJob, jobParameters);
        	jobLauncher.run(paymentScheduleJob, jobParameters);
        	jobLauncher.run(paymentPlanJob, jobParameters);
        	jobLauncher.run(normalPaymentJob, jobParameters);
        	jobLauncher.run(nsfPaymentJob, jobParameters);
        	jobLauncher.run(legalPlacementJob, jobParameters);
        	jobLauncher.run(operationRequestJob, jobParameters);
        	jobLauncher.run(operationResponseJob, jobParameters);
        	jobLauncher.run(employerJob, jobParameters);
        	jobLauncher.run(servicingDetailJob, jobParameters);
        	jobLauncher.run(dialConsentExclusionJob, jobParameters);
        	jobLauncher.run(smsConsentExclusionJob, jobParameters);
        	jobLauncher.run(emailConsentExclusionJob, jobParameters);
        	jobLauncher.run(adjustmentJob, jobParameters);
        	jobLauncher.run(placedAccountJob, jobParameters);
        	jobLauncher.run(accountUccJob, jobParameters);
        	jobLauncher.run(accountAssetJob, jobParameters);
        	jobLauncher.run(creditScoreJob, jobParameters);
        	jobLauncher.run(costJob, jobParameters);
        	jobLauncher.run(changeLogJob, jobParameters);
        	jobLauncher.run(chargeOffAccountJob, jobParameters);
			jobLauncher.run(partnerAssignmentJob, jobParameters);
			jobLauncher.run(cotMasterListJob, jobParameters);
			jobLauncher.run(chainOfTitleJob, jobParameters);
			jobLauncher.run(accountTagJob, jobParameters);
			jobLauncher.run(lienJob, jobParameters);
			jobLauncher.run(garnishmentJob, jobParameters);
			jobLauncher.run(bankruptcyJob, jobParameters);
			jobLauncher.run(deceasedJob, jobParameters);
			jobLauncher.run(complaintJob, jobParameters);
			jobLauncher.run(disputeJob, jobParameters);
			jobLauncher.run(autoAccountInfoJob, jobParameters);
			jobLauncher.run(accountAdditionalInfoJob, jobParameters);
			
			try {
				client.callingChargedOffAccountsApi(authHeader);
	        } catch (Exception ex) {
	        	ErrorUtils.buildErrorResponse(null, ex, logger);
	        }
			try {
	        	client.callingLedgerTransactionApi(authHeader);
	        } catch (Exception ex) {
	        	ErrorUtils.buildErrorResponse(null, ex, logger);
	        }
			try {
	        	client.callingChangeLogProcess(authHeader);
	        } catch (Exception ex) {
	        	ErrorUtils.buildErrorResponse(null, ex, logger);
	        }
			try {
	        	client.callingSOLRecalculationProcess(authHeader);
	        } catch (Exception ex) {
	        	ErrorUtils.buildErrorResponse(null, ex, logger);
	        }
//			try {
//	        	client.callingNsfPaymentProcessApi(authHeader);
//	        } catch (Exception ex) {
//	        	ErrorUtils.buildErrorResponse(null, ex, logger);
//	        }
        	logger.info("****** end asyncOtherDataScrubbingExecutor ********");
        } catch (Exception ex) {
        	ErrorUtils.buildErrorResponse(null, ex, logger);
        	logger.error("****** end error asyncOtherDataScrubbingExecutor ********");
        }
	}

	@Override
	public Response<List<ErrWarMessage>> getEntitySuspectedInvDetails(Map<String, String> dataScrubbingSearch) {
		Response<List<ErrWarMessage>> response = new Response<List<ErrWarMessage>>();

		try {
			Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);

			Integer clientId = (dataScrubbingSearch.containsKey("clientId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("clientId"))) ? Integer.parseInt(dataScrubbingSearch.get("clientId")) : null;
			Long clientJobScheduleId = (dataScrubbingSearch.containsKey("clientJobScheduleId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("clientJobScheduleId"))) ? Long.parseLong(dataScrubbingSearch.get("clientJobScheduleId")) : null;
			Date placementDateFrom = (dataScrubbingSearch.containsKey("placementDateFrom") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("placementDateFrom"))) ? new SimpleDateFormat(CommonConstants.DT_FORMAT_MM_DD_YYYY).parse(dataScrubbingSearch.get("placementDateFrom")) : null;
			Date placementDateTo = (dataScrubbingSearch.containsKey("placementDateTo") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("placementDateTo"))) ? new SimpleDateFormat(CommonConstants.DT_FORMAT_MM_DD_YYYY).parse(dataScrubbingSearch.get("placementDateTo")) : null;
			Integer partnerId = (dataScrubbingSearch.containsKey("partnerId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("partnerId"))) ? Integer.parseInt(dataScrubbingSearch.get("partnerId")) : null;
			String entity = dataScrubbingSearch.get("entity");
			if(CommonUtils.isStringNullOrBlank(entity)) {
				response.setValidation(false);
				response.setMessage("Entity cannot be empty.");
				return response;
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("clientId", clientId);
			paramMap.put("suspectedRecordStatusId",ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			paramMap.put("clientJobScheduleId",clientJobScheduleId);
			paramMap.put("placementDateFrom",placementDateFrom);
			paramMap.put("placementDateTo",placementDateTo);

			if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID))
				clientId = Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString());

			if(requestDetailsMap.containsKey(CommonConstants.PARTNER_ID))
				partnerId = Integer.parseInt(requestDetailsMap.get(CommonConstants.PARTNER_ID).toString());

			try {
				if(entity.equalsIgnoreCase("AC")) {
					logger.info("fetching info for accounts!!");
//				response.setResponse(messageRepository.getAccSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getAccSuspectedInvDetailsNew(clientId, clientJobScheduleId,  placementDateFrom, placementDateTo));
				} else if(entity.equalsIgnoreCase("CM")) {
					logger.info("fetching info for consumer!!");
//				response.setResponse(messageRepository.getConsumerSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getConsumerSuspectedInvDetailsNew(clientId, clientJobScheduleId,  placementDateFrom, placementDateTo));
				} else if(entity.equalsIgnoreCase("AD")) {
					logger.info("fetching info for address!!");
//				response.setResponse(messageRepository.getAddressSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getAddressSuspectedInvDetailsNew(clientId, clientJobScheduleId,  placementDateFrom, placementDateTo));
				} else if(entity.equalsIgnoreCase("PH")) {
					logger.info("fetching info for phone!!");
//				response.setResponse(messageRepository.getPhoneSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getPhoneSuspectedInvDetailsNew(clientId, clientJobScheduleId,  placementDateFrom, placementDateTo));
				} else if(entity.equalsIgnoreCase("EM")) {
					logger.info("fetching info for email!!");
//				response.setResponse(messageRepository.getEmailSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getEmailSuspectedInvDetailsNew(clientId, clientJobScheduleId, placementDateFrom, placementDateTo));
				} else if(entity.equalsIgnoreCase("PM")) {
					logger.info("fetching info for payment!!");
//				response.setResponse(messageRepository.getEmailSuspectedInvDetails(clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo));
					response.setResponse(messageRepository.getPaymentSuspectedInvDetailsNew(clientId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), clientJobScheduleId, placementDateFrom, placementDateTo, partnerId));
				} else if(entity.equalsIgnoreCase("CL")) {
					logger.info("fetching info for changelog!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_CHANGE_LOG_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("PP")) {
					logger.info("fetching info for payment plan!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_PAYMENT_PLAN_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("EP")) {
					logger.info("fetching info for employer!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_EMPLOYER_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("CP")) {
					logger.info("fetching info for compliance!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_COMPLIANCE_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("CO")) {
					logger.info("fetching info for chargeoff_account!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_CHARGE_OFF_ACCOUNT_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("TG")) {
					logger.info("fetching info for account_tag!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_ACCOUNT_TAG_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("CT")) {
					logger.info("fetching info for chainoftitle!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_CHAIN_OF_TITLE_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("CD")) {
					logger.info("fetching info for communication_detail!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_COMMUNICATION_SUSPECTED_DETAIL, paramMap)));
				} else if (entity.equalsIgnoreCase("LP")) {
					logger.info("fetching info for legal_placement!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_LEGAL_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("LL")) {
					logger.info("fetching info for legal_lien!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_LIEN_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("LG")) {
					logger.info("fetching info for legal_garnishment!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_GARNISHMENT_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("BK")) {
					logger.info("fetching info for bankruptcy!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_BANKRUPTCY_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("CI")) {
					logger.info("fetching info for complaint!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_COMPLAINT_SUSPECTED_DETAIL, paramMap)));
				} else if(entity.equalsIgnoreCase("DI")) {
					logger.info("fetching info for dispute!!");
					response.setResponse(setErrWarMessageDetailsResult(namedTemplate.queryForList(DBQueryUtils.GET_DISPUTE_SUSPECTED_DETAIL, paramMap)));
				} else {
					response.setValidation(false);
					response.setMessage("Invalid Entity.");
					return response;
				}
			}catch (NullPointerException e) {
				logger.error("NullPointerException Caught");
			}
			response.setValidation(true);
		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
			response.setValidation(false);
		}

		return response;
	}

	@Override
	public Response<List<Map<String, Object>>> getEntityDetailsForSuspectedInv(Map<String, String> dataScrubbingSearch) {
		Response<List<Map<String, Object>>> response = new Response<List<Map<String, Object>>>();

		try {
			Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);

			String shortName = dataScrubbingSearch.get("shortName");
			Integer clientId = (dataScrubbingSearch.containsKey("clientId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("clientId"))) ? Integer.parseInt(dataScrubbingSearch.get("clientId")) : null;
			Integer clientJobScheduleId = (dataScrubbingSearch.containsKey("clientJobScheduleId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("clientJobScheduleId"))) ? Integer.parseInt(dataScrubbingSearch.get("clientJobScheduleId")) : null;
			Date placementDateFrom = (dataScrubbingSearch.containsKey("placementDateFrom") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("placementDateFrom"))) ? new SimpleDateFormat(CommonConstants.DT_FORMAT_MM_DD_YYYY).parse(dataScrubbingSearch.get("placementDateFrom")) : null;
			Date placementDateTo = (dataScrubbingSearch.containsKey("placementDateTo") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("placementDateTo"))) ? new SimpleDateFormat(CommonConstants.DT_FORMAT_MM_DD_YYYY).parse(dataScrubbingSearch.get("placementDateTo")) : null;
			String entity = dataScrubbingSearch.get("entity");
			Integer partnerId = (dataScrubbingSearch.containsKey("partnerId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("partnerId"))) ? Integer.parseInt(dataScrubbingSearch.get("partnerId")) : null;

			if(CommonUtils.isStringNullOrBlank(entity)) {
				response.setValidation(false);
				response.setMessage("Entity cannot be empty.");
				return response;
			}

			if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID)) 
				clientId = Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString());

			if(requestDetailsMap.containsKey(CommonConstants.PARTNER_ID))
				partnerId = Integer.parseInt(requestDetailsMap.get(CommonConstants.PARTNER_ID).toString());

			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("code", shortName.substring(0,1).toLowerCase());
			paramMap.put("codeType", "E" + shortName.substring(1, shortName.length()));
			paramMap.put("clientId",clientId);
			paramMap.put("suspectedRecordStatusId",ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			paramMap.put("clientJobScheduleId",clientJobScheduleId);
			paramMap.put("placementDateFrom",placementDateFrom);
			paramMap.put("placementDateTo",placementDateTo);

			if(entity.equalsIgnoreCase("AC")) {
//				List<Account> accDetailsForSuspectedInv = accountRepository.getAccDetailsForSuspectedInv(shortName, clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo);
				List<Account> accDetailsForSuspectedInv = accountRepository.getAccDetailsForSuspectedInvNew(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId,  placementDateFrom, placementDateTo);
				
				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Account account = new Account();
				for(Account acc: accDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : account.getClass().getDeclaredFields()) {
						responseValue.put(field.getName(), EntityUtil.invokeGetter(acc, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("CM")) {
//				List<Consumer> conDetailsForSuspectedInv = consumerRepository.getConsumerDetailsForSuspectedInv(shortName, clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo);
				List<Consumer> conDetailsForSuspectedInv = consumerRepository.getConsumerDetailsForSuspectedInvNew(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId, placementDateFrom, placementDateTo);
				
				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Consumer consumer = new Consumer();
				for(Consumer con: conDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : consumer.getClass().getDeclaredFields()) {
						if(field.getName().equalsIgnoreCase("isEntityPrimary"))
							continue;
						responseValue.put(field.getName(), EntityUtil.invokeGetter(con, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("AD")) {
//				List<Address> addDetailsForSuspectedInv = addressRepository.getAddressDetailsForSuspectedInv(shortName, clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo);
				List<Address> addDetailsForSuspectedInv = addressRepository.getAddressDetailsForSuspectedInvNew(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId,  placementDateFrom, placementDateTo);
				
				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Address address = new Address();
				for(Address add: addDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : address.getClass().getDeclaredFields()) {
						if(field.getName().equalsIgnoreCase("isEntityPrimary"))
							continue;
						responseValue.put(field.getName(), EntityUtil.invokeGetter(add, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("PH")) {
//				List<Phone> phDetailsForSuspectedInv = phoneRepository.getPhoneDetailsForSuspectedInv(shortName, clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo);
				List<Phone> phDetailsForSuspectedInv = phoneRepository.getPhoneDetailsForSuspectedInvNew(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId, placementDateFrom, placementDateTo);
				
				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Phone phone = new Phone();
				for(Phone ph: phDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : phone.getClass().getDeclaredFields()) {
						responseValue.put(field.getName(), EntityUtil.invokeGetter(ph, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("EM")) {
//				List<Email> emDetailsForSuspectedInv = emailRepository.getEmailDetailsForSuspectedInv(shortName, clientId, clientJobScheduleId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), placementDateFrom, placementDateTo);
				List<Email> emDetailsForSuspectedInv = emailRepository.getEmailDetailsForSuspectedInvNew(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId,  placementDateFrom, placementDateTo);
				
				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Email email = new Email();
				for(Email em: emDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : email.getClass().getDeclaredFields()) {
						responseValue.put(field.getName(), EntityUtil.invokeGetter(em, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("PM")) {
				List<Payment> paymentDetailsForSuspectedInv = paymentRepository.getPaymentDetailsForSuspectedInv(shortName.substring(0,1).toLowerCase(), shortName.substring(1, shortName.length()), clientId, clientJobScheduleId,  placementDateFrom, placementDateTo, partnerId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());

				List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();

				Payment payment = new Payment();
				for(Payment pay: paymentDetailsForSuspectedInv) {
					Map<String, Object> responseValue = new LinkedHashMap<String, Object>();
					for (Field field : payment.getClass().getDeclaredFields()) {
						responseValue.put(field.getName(), EntityUtil.invokeGetter(pay, field.getName()));
					}
					res.add(responseValue);
				}

				response.setResponse(res);
			} else if(entity.equalsIgnoreCase("CL")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_CHANGE_LOG_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("PP")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_PAYMENT_PLAN_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("EP")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_EMPLOYER_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("CP")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_COMPLIANCE_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("CO")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_CHARGE_OFF_ACCOUNT_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("TG")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_ACCOUNT_TAG_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("CT")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_CHAIN_OF_TITLE_DETAIL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("CD")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_COMMUNICATION_DETAIL_FOR_SUSPECTED, paramMap));
			} else if (entity.equalsIgnoreCase("LP")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_LEGAL_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("LL")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_LIEN_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("LG")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_GARNISHMENT_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("BK")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_BANKRUPTCY_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("CI")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_COMPLAINT_FOR_SUSPECTED, paramMap));
			} else if(entity.equalsIgnoreCase("DI")) {
				response.setResponse(namedTemplate.queryForList(DBQueryUtils.GET_DISPUTE_FOR_SUSPECTED, paramMap));
			} else {
				response.setValidation(false);
				response.setMessage("Invalid Entity.");
				return response;
			}
			response.setValidation(true);
		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
			response.setValidation(false);
		}

		return response;
	}

	@Override
	public Response<Map<String, Object>> accountScrubRejected() {
		Response<Map<String,Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			ParameterizedTypeReference<Response<List<AppConfigValue>>> responseType = new ParameterizedTypeReference<Response<List<AppConfigValue>>>() { };

	        HttpHeaders requestHeaders = new HttpHeaders();
	        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	        requestHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			requestHeaders = CommonUtils.setHeadersToInternalRequest(requestHeaders, request);

	        AppConfigValue appConfigValue = new AppConfigValue();
	        appConfigValue.setAppConfigNameShortName("ACC_SCRUB_TLV");
	        //HttpEntity<AppConfigValue> requestEntity = new HttpEntity<>(appConfigValue, requestHeaders);
	       
	        List<AppConfigValue> appConfigValueList= serviceCommunication.getAppConfigValue(fetchTokenData.getTokenData(), appConfigValue).get();
	        
//	        ResponseEntity<Response<List<AppConfigValue>>> returnData = restTemplate.exchange(ServicesHostUrls.configurationManagementHost+"getAppConfigValue", HttpMethod.POST, requestEntity, responseType);
//			List<AppConfigValue> appConfigValueList = returnData.getBody().getResponse();

			Integer equabliLevelVal = 0;
			int updateCount = 0;
			List<Integer> clientIds = new ArrayList<Integer>();

			for(AppConfigValue configValue: appConfigValueList) {
				if(!CommonUtils.isIntegerNullOrZero(configValue.getClientId())) {
					clientIds.add(configValue.getClientId());
					updateCount += accountRepository.accountScrubRejectedForClient(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SCRUBREJECTED).getRecordStatusId(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), Integer.parseInt(configValue.getConfiguredValue()), configValue.getClientId());
				} else {
					equabliLevelVal = Integer.parseInt(configValue.getConfiguredValue());
				}
			}

			if(clientIds.size() > 0) {
				updateCount += accountRepository.accountScrubRejectedAtEquabliLevelForRemainingClients(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SCRUBREJECTED).getRecordStatusId(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), equabliLevelVal, clientIds);
			} else {
				updateCount += accountRepository.accountScrubRejectedAtEquabliLevelForAllClients(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SCRUBREJECTED).getRecordStatusId(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), equabliLevelVal);
			}

			map.put("updateCount", updateCount);
			response.setResponse(map);
			response.setValidation(true);
			response.setMessage("Success");
		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}

		return response;
	}

	@Override
	public void downloadSuspectedAccounts(Account accountSearch) {
        String[] headers = {"Account Id", "Client Name", "Client Account Number", "Original Account Number", "Error Code", "Error Description"};
		Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);

		if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID)) 
			accountSearch.setClientId(Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString()));

		List<Account> suspectedAccounts = accountRepository.downloadSuspectedAccounts(accountSearch.getClientId(), 
				ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId(), accountSearch.getPlacementDateFrom(), accountSearch.getPlacementDateTo());
        List<List<Object>> objectList = new ArrayList<List<Object>>();
        for (Account acc : suspectedAccounts) {
        	List<Object> obj = new ArrayList<Object>();
        	obj = Arrays.asList(acc.getAccountId(), acc.getClient().getFullName(), acc.getClientAccountNumber(), acc.getOriginalAccountNumber(), 
        			acc.getShortName(), acc.getDescription());
        	objectList.add(obj);
        }
        csvhelper.writeToCsv(headers, "SuspectedAccounts", objectList, response);
    }

	private static List<ErrWarMessage> setErrWarMessageDetailsResult(List<Map<String, Object>> errWarMessageDetails) {
		List<ErrWarMessage> errWarMessages = new ArrayList<>();
		for (Map<String, Object> value : errWarMessageDetails) {
			ErrWarMessage errWarMessage = new ErrWarMessage(String.valueOf(value.get("shortName")),
					String.valueOf(value.get("description")),
					Long.valueOf(value.get("count").toString()));
			errWarMessages.add(errWarMessage);
		}
		return errWarMessages;
	}

	@Override
	public Response<Map<String,Object>> getDataScrubbingDownload(DownloadCriteria<Account> accountSearch) {
		HttpHeaders headers = CommonUtils.setHeadersToInternalRequest(new HttpHeaders(), request);
		Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);

		Response<Map<String,Object>> response = new Response<Map<String,Object>>();
		Map<String, Object> paramMap = new HashMap<String,Object>();
		Account acc = accountSearch.getSearchCriteria().getSearchCriteria();
		TokenData tokenData = fetchTokenData.getTokenData();
		Map<String,Object> headerMap = CommonUtils.setHeadersToInternalRequest(tokenData, request);

		if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID)) 
			acc.setClientId(Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString()));

		String entity = (!CommonUtils.isStringNullOrBlank(acc.getEntity())) ? acc.getEntity() : null;
		Long clientJobScheduleId = acc.getClientJobScheduleId() != null ?  (long) acc.getClientJobScheduleId() : null;
		paramMap.put("clientId", acc.getClientId());
		paramMap.put("clientJobScheduleId",clientJobScheduleId);
		paramMap.put("placementDateFrom", acc.getPlacementDateFrom() != null ?  acc.getPlacementDateFrom() : null);
		paramMap.put("placementDateTo", acc.getPlacementDateTo()!= null ?  acc.getPlacementDateTo() : null);

		String query = null;
		if(entity.equalsIgnoreCase("AC")) {
			query = DBQueryUtils.GET_ACCOUNT_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CM")) {
			query = DBQueryUtils.GET_CONSUMER_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("AD")) {
			query = DBQueryUtils.GET_ADDRESS_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("PH")) {
			query = DBQueryUtils.GET_PHONE_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("EM")) {
			query = DBQueryUtils.GET_EMAIL_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("PM")) {
			query = DBQueryUtils.GET_PAYMENT_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("PP")) {
			query = DBQueryUtils.GET_PAYMENT_PLAN_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CL")) {
			query = DBQueryUtils.GET_CHANGELOG_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("EP")) {
			query = DBQueryUtils.GET_EMPLOYER_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CP")) {
			query = DBQueryUtils.GET_COMPLIANCE_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CO")) {
			query = DBQueryUtils.GET_CHARGEOFF_ACCOUNT_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("TG")) {
			query = DBQueryUtils.GET_ACCOUNT_TAG_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CT")) {
			query = DBQueryUtils.GET_CHAIN_OF_TITLE_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CD")) {
			query = DBQueryUtils.GET_COMMUNICATION_DETAIL_DATA_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("LP")) {
			query = DBQueryUtils.GET_LEGAL_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("LL")) {
			query = DBQueryUtils.GET_LIEN_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("LG")) {
			query = DBQueryUtils.GET_GARNISHMENT_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("BK")) {
			query = DBQueryUtils.GET_BANKRUPTCY_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("CI")) {
			query = DBQueryUtils.GET_COMPLAINT_SCRUBBING_DOWNLOAD;
		} else if(entity.equalsIgnoreCase("DI")) {
			query = DBQueryUtils.GET_DISPUTE_SCRUBBING_DOWNLOAD;
		} else {
			response.setValidation(false);
			response.setMessage("Invalid Entity.");
			return response;
		}

//		if(!CommonUtils.isIntegerNullOrZero(acc.getClientId())) {
//			query = query + CLIENT_ID_WHERE_CLAUSE;
//		}
//		if(!CommonUtils.isIntegerNullOrZero(acc.getClientJobScheduleId())) {
//			query = query + CLIENT_JOB_SCHEDULE_ID_WHERE_CLAUSE;
//		}

		final String finalQuery = query;

		AppDataDownloadRequest appDataDownloadRequest = new AppDataDownloadRequest(accountSearch.getDownloadName(), "SB", accountSearch.getExportFormat(), query);
		appDataDownloadRequest = ReportingUtils.insertIntoAppDataDownloadRequest(appDataDownloadRequest, request,fetchTokenData.getTokenData());
		final int appDataDownloadRequestId = appDataDownloadRequest.getAppDataDownloadRequestId();
		final String reportName = CommonUtils.getRequestAttributeValue(request, CommonConstants.PRINCIPLE_LOGIN_KEY)+"_"+"DataScrubbing";

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					logger.info("downloading report");
					namedTemplate.query(finalQuery, paramMap, new ResultSetExtractor<Object>() {
						@Override
						public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
							ReportingUtils.generateReportFromResultSet(rs, accountSearch.getRequiredColumns(), reportName, headers, restTemplate, accountSearch.getExportFormat(), "SB", accountSearch.getDownloadName(),appDataDownloadRequestId,headerMap);
							return null;
						}
					});
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		});

		response.setMessage("Your request has been submitted to generate a file which will be available on the 'File Download' screen");
		return response;
	}
	
	@Override
	public Response<Map<String, Object>> testDataScrubbingService(){
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			Timestamp timestamp = paymentDao.testDataScrubbingService();
			Map<String, Object> responseMap = new HashMap<String, Object>();
			responseMap.put("timestamp", timestamp);
			response.setResponse(responseMap);
			response.setMessage("Service Verified!!");
			response.setValidation(true);
		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
	}

	
	@Override
	public Response<Map<String,Object>> insertOrUpdatePaymentDetails(Payment payment) {
		
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Boolean isPartnerCommission = getPartnerCommission(payment.getClientId());
		try {
		return 	paymentDao.insertOrUpdatePaymentDetails(payment,isPartnerCommission);
			
		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
		
	}

	@Override
	public Response<Map<String,Object>> insertOrUpdateBankruptcyDetails(Bankruptcy bankruptcy) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			return bankruptcyDao.insertOrUpdateBankruptcyDetails(bankruptcy);

		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;

	}

	@Override
	public Response<Map<String, Object>> getBankruptcyDetailById(Long bankruptcyId) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			return bankruptcyDao.getBankruptcyDetailById(bankruptcyId);

		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;

	}

	@Override
	public Response<Map<String, Object>> getBankruptcyDetails(SearchCriteria<Bankruptcy> bankruptcySearch) {
		return bankruptcyDao.getBankruptcyDetails(bankruptcySearch);
	}

	@Override
	public Response<Map<String,Object>> deletePaymentDetails(Long paymentId) {

		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
		return 	paymentDao.deletePaymentDetails(paymentId);

		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;

	}

	@Override
	public Response<Double> getUpdatedAccountBalance(Payment payment) {
		
		Response<Double> response = new Response<Double>();
		Double updatedAccountBalance = 0.00;
		try {
				
	   List<Map<String,Object>> listOfDateJudement =paymentDao.isLegalPlacementExists(payment.getClientId(),payment.getClientAccountNumber());	
		Double amtCurrentBalance =  paymentDao.getUpdatedAccountBalance(payment);
		if(listOfDateJudement.size() == 0  || (listOfDateJudement.size() > 0 && 
				(payment.getPaymentDate().isAfter(LocalDate.parse(listOfDateJudement.get(0).get("dtjudgement").toString())))) ) { 
			if(payment.getPaymentType().equalsIgnoreCase("F") || payment.getPaymentType().equalsIgnoreCase("C")) {
				 updatedAccountBalance =  amtCurrentBalance + payment.getAmtPayment();
			}else {
				 updatedAccountBalance =  amtCurrentBalance - payment.getAmtPayment();
			}   
		   }else {
			   updatedAccountBalance= amtCurrentBalance;
		   }
		
		response.setValidation(true);
        response.setResponse(updatedAccountBalance);
		
		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
		
	}

	@Override
	public Response<Map<String, Object>> insertBalanceAdjustment(Adjustment adjustment) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			return adjustmentDao.insertBalanceAdjustment(adjustment);

		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
	}

	@Override
	public Response<Map<String, Object>> updateAccountDetails(Account account) {
		Response<Map<String, Object>> response = new Response<>();
		try {
			return accountDao.updateAccountDetails(account);
		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
	}

	@Override
	public Response<Account> getAccountInfo(Long accountId) {
		Response<Account> response = new Response<Account>();
		try {
            response.setValidation(true);
			response.setResponse(accountRepository.getAccountByAccountId(accountId));
		}catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
	}
	
	@Override
	public Boolean getPartnerCommission(Integer clientId) {
		Boolean isPartnerCommission = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clientId", clientId);
		try {
			isPartnerCommission = namedTemplate.queryForObject(DBQueryUtils.GET_PARTNER_COMMISSION, paramMap, Boolean.class);
			logger.debug("getPartnerCommission  :: "+ isPartnerCommission);
		}catch (Exception e) {
			logger.error("  No configuration for clientId ::  " + clientId);
		}
		
		return isPartnerCommission;
	}
	

	@Override
	public void placementDataScrubbing(String updatedBy, String appId, String recordSourceId, String authHeader) {
		accountsDataScrubbing(updatedBy, appId, recordSourceId, authHeader);
	}

	public Response<Map<String, Object>> updateAccountByAccountChange() {
		Response<Map<String, Object>> response =accountChangeProcessor.processChanges();
		return response;
	}
}
