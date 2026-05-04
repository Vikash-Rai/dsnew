package com.equabli.collectprism.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.equabli.client.CommonRestClient;
import com.equabli.config.SqsMessageSender;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.entity.PaymentBucketConfig;
import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.PaymentValidation;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.ErrorUtils;
import com.equabli.feign.DataScrubEnrichmentServiceCommunication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.json.JSONArray;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class PaymentDaoImpl implements PaymentDao {
	static Logger logger = LoggerFactory.getLogger(PaymentDaoImpl.class);

	NamedParameterJdbcTemplate namedTemplate;

	@Autowired
	HttpServletRequest request;

	@Autowired
	private CacheableService cacheableService;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private CommonRestClient client;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AdjustmentRepository adjustmentRepository;
	
	@Autowired
	SqsMessageSender sqsMessageSender;
	
	@Autowired
	DataScrubEnrichmentServiceCommunication serviceCommunication;
	

	public PaymentDaoImpl(DataSource dataSource) {
		namedTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	  private static final String INSERT_INTO_PAYMENT_CANCEL_TYPE =  " insert into data.payment "
				+ "(record_type, client_id, partner_id, partner_plan_number, account_id, client_account_number, payment_serial, dt_payment, amt_payment, dt_payment_posting,"
				+ " amt_balance, payment_method, payment_type, payment_status, payment_broken_reason, approval_code, partner_batch_number, posting_number, gateway_vendor, "
				+ " count_balance_payment, record_status_id, record_source_id, amt_principal, amt_interest, amt_latefee, amt_courtcost, amt_attorneyfee, payment_source ) "
				+ " values( "
				+ " :recordType, :clientId, :partnerId, :partnerPlanNumber, :accountId, :clientAccountNumber, :paymentSerial, :paymentDate, :amtPayment, :dtPaymentPosting,"
				+ "  :amtBalance, :paymentMethod, :paymentType, :paymentStatus, :paymentBrokenReason, :approvalCode, :partnerBatchNumber, :postingNumber, :gatewayVendor, "
				+ " :countBalancePayment, :recordStatusId, :recordSourceId, :amtPrincipal, :amtInterest, :amtLatefee, :amtCourtcost, :amtAttorneyfee, :paymentSource ) ";
	
	  public static final String GET_LEGAL_PLACEMENT_OF_CLIENT_ACCOUNT_NUMBER = new StringBuffer()
				.append("  ")
				.append("   select dt_judgement dtjudgement ,amt_judgement amtjudgement,amt_judgment_principal amtjudgmentprincipal,amt_judgment_interest amtjudgmentinterest,amt_judgment_fees amtjudgmentfees,amt_judgment_attorney amtjudgmentattorney,amt_judgment_other amtjudgmentother from data.legal_placement  where client_id = :clientId and client_account_number = :clientAccountNumber ")
				.append("  ")
				.toString();
	  
		public static final String GET_CLIENT_CODE_BY_CLIENT_ID = "select short_name clientcode from conf.client where client_id =:clientId and record_status_id = " + ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId();

		public static final String GET_PAYMENT_ID = "select max(payment_id) paymentid from data.payment ";
	  
	 @Override
	public void insertIntoPaymentCancelPayment(Payment cPayment) {
		namedTemplate.update(INSERT_INTO_PAYMENT_CANCEL_TYPE, Payment.getParamsValue(cPayment));
		String clientCode = getClientCode(cPayment.getClientId());
		Long paymentId = getPaymentId();
		sqsMessageSender.sendMessage(clientCode,"PM",paymentId);
		
	}
	
	 private static final String UPDATE_ERRORCODEJSON_PAYMENT_PLAN = "update data.payment_plan set err_code_json = :errCodeJson::JSONB where payment_plan_id = :paymentPlanId";
	 
	 @Override
	 public void updatePaymentPlanErrCodeJson(PaymentPlan pp) {
		 Map<String, Object> paramMap= new HashMap<String, Object>();
		 ObjectMapper mapper = new ObjectMapper();
		 JSONArray errCodeJSon= mapper.convertValue(pp.getErrCodeJson(), JSONArray.class);
		 paramMap.put("errCodeJson",  errCodeJSon.toString());
		 paramMap.put("paymentPlanId", pp.getPaymentPlanId());
		 namedTemplate.update(UPDATE_ERRORCODEJSON_PAYMENT_PLAN, paramMap);
	 }
	 
	 @Override
		public Timestamp testDataScrubbingService(){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			return	namedTemplate.queryForObject(" select current_timestamp ", paramMap, Timestamp.class);
		}

	@Override
	public Double getUpdatedAccountBalance(Payment payment){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("clientAccountNumber", payment.getClientAccountNumber());
		paramMap.put("clientId", payment.getClientId());

		return	namedTemplate.queryForObject(" select amt_currentbalance from data.account where client_id = :clientId "
				+ " and  client_account_number = :clientAccountNumber  ", paramMap, Double.class);
	}

	@Override
	public Response<Map<String, Object>> insertOrUpdatePaymentDetails(Payment payment,Boolean isPartnerCommission) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
			payment.setUpdatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
			payment.setAppId(Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()));
			payment.setRecordSourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()));
			payment.setCreatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
			
			if(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()) == ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_EA).getRecordSourceId()) {
				payment.setPaymentSource(CommonConstants.RECORD_SOURCE_PARTNER);
			}else if(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()) == ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_CL).getRecordSourceId()) {
				payment.setPaymentSource(CommonConstants.RECORD_SOURCE_CLIENT);
			}else {
				payment.setPaymentSource(CommonConstants.RECORD_SOURCE_EQUABLI);
			}
			
			if(CommonUtils.isStringNullOrBlank(payment.getPaymentSettlementType())) {
				payment.setPaymentSettlementType("NN");
			}
			transferDataPayment(payment);
			paymentValid(payment,isPartnerCommission);
			if(CommonUtils.isObjectNull(payment.getErrCodeJson()) || payment.getErrCodeJson().isEmpty()) {
				payment.setRecordType("payment");
				payment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				payment.setDtmUtcUpdate(LocalDateTime.now());
				paymentRepository.save(payment);
	    		if(!CommonUtils.isStringNullOrBlank(payment.getPaymentType()) && payment.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NSF) 
	    				&& !CommonUtils.isLongNull(payment.getParentPaymentIds())) {
	    			paymentRepository.updatePaymentReversalDate(payment.getParentPaymentIds());
	    		}
	        	client.callingLedgerTransactionApi(request.getHeader("Authorization"));
				response.setMessage("payment Data got Ingested/Updated ");
				response.setValidation(true);
			} else {
				ErrWarJson errWarJson = payment.getErrCodeJson().stream().findFirst().get();
				Map<String, Object> errWarMessagesMap = DataScrubbingUtils.getAllApplicableScrubRulesDesc(payment.getClientId(), cacheableService);
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

	@Override
	public Response<Map<String, Object>> deletePaymentDetails(Long paymentId) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		try {
			paymentRepository.deletePaymentByPaymentId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.DELETED).getRecordStatusId(), paymentId);
			response.setMessage("Delete Payment successfully!");
			response.setValidation(true);
		} catch (Exception e) {
			logger.error(e.toString(), e);
			response.setMessage("Delete Payment fail!");
			response.setValidation(false);
		}
		return response;
	}

	private void paymentValid(Payment pay,Boolean isPartnerCommission) {
		Map<String, Object> validationMap = new HashMap<String, Object>();
		validationMap.put("Payment Id", pay.getPaymentId());
		validationMap.put("isPaymentValidated", true);

		pay.setErrCodeJson(null);

		List<Map<String,Object>> listOfDateJudement = isLegalPlacementExists(pay.getClientId(),pay.getClientAccountNumber());
		List<PaymentBucketConfig> paymentBucketConfig = getPaymentBucketDistributionConfig(pay.getClientId()).getResponse();
		
		List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(pay.getClientId(), cacheableService);
		PaymentValidation.mandatoryValidation(pay, validationMap, cacheableService, errWarMessagesList);

		if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
			PaymentValidation.lookUpValidation(pay, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
				PaymentValidation.standardize(pay);
				PaymentValidation.referenceUpdation(pay,serviceCommunication,request.getHeader(HttpHeaders.AUTHORIZATION),isPartnerCommission);
				PaymentValidation.misingRefCheck(pay, validationMap, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
					PaymentValidation.businessRule(pay, validationMap, errWarMessagesList, adjustmentRepository,listOfDateJudement,sqsMessageSender, paymentBucketConfig);
				}
			}
		}
	}

	private void transferDataPayment(Payment payment) {
		payment.setPaymentPlan(paymentRepository.findPaymentPlanByPartnerIdAndPartnerPlan(payment.getPartnerId(), payment.getPartnerPlanNumber()));
		payment.setClient(paymentRepository.findClientById(payment.getClientId()));
		payment.setPaymentMethodLookUp(accountRepository.getLookUpByKeyValue("payment_method", payment.getPaymentMethod()));
		payment.setPaymentTypeLookUp(accountRepository.getLookUpByKeyValue("payment_type", payment.getPaymentType()));
		payment.setPaymentStatusLookUp(accountRepository.getLookUpByKeyValue("payment_status", payment.getPaymentStatus()));
		payment.setPaymentBrokenReasonLookUp(accountRepository.getLookUpByKeyValue("payment_plan_broken_reason", payment.getPaymentBrokenReason()));
		payment.setIsNSFDeDup(paymentRepository.isNSFDeDup(payment.getClientId(), payment.getReversalParentId(), payment.getPartnerId()));
		payment.setPaymentSettlementTypeLookUp(accountRepository.getLookUpByKeyValue("payment_settlement_type", payment.getPaymentSettlementType()));
		payment.setCotDtFrom(!paymentRepository.getCotDtFrom(payment.getClientId(), payment.getClientAccountNumber(), payment.getPartnerId()).isEmpty()
							?paymentRepository.getCotDtFrom(payment.getClientId(), payment.getClientAccountNumber(), payment.getPartnerId()).get(0):null);
		payment.setCotDtTill(!paymentRepository.getCotDtTill(payment.getClientId(), payment.getClientAccountNumber(), payment.getPartnerId()).isEmpty()
				?paymentRepository.getCotDtTill(payment.getClientId(), payment.getClientAccountNumber(), payment.getPartnerId()).get(0):null);
		if(!CommonUtils.isStringNullOrBlank(payment.getReversalParentId())) {
			Payment parentPayment = paymentRepository.getDataParentPaymentData(payment.getClientId(), payment.getReversalParentId(), payment.getPartnerId());
			if (parentPayment != null) {
				payment.setParentPaymentIds(parentPayment.getPaymentId());
				payment.setParentOtherFees(parentPayment.getAmtOtherfee());
				payment.setParentInterest(parentPayment.getAmtInterest());
				payment.setParentPrincipal(parentPayment.getAmtPrincipal());
				payment.setParentLateFees(parentPayment.getAmtLatefee());
			}
		}
		Account accountAmtData = paymentRepository.getDataAmtAccount(payment.getClientId(), payment.getClientAccountNumber());
		if (accountAmtData != null) {
			payment.setAccountIds(accountAmtData.getAccountId());
			payment.setDtChargeOff(accountAmtData.getChargeOffDate());
			payment.setAmtOtherfeeCurrentbalance(accountAmtData.getAmtOtherfeeCurrentbalance());
			payment.setAmtInterestCurrentbalance(accountAmtData.getAmtInterestCurrentbalance());
			payment.setAmtPrincipalCurrentbalance(accountAmtData.getAmtPrincipalCurrentbalance());
			payment.setAmtCurrentbalance(accountAmtData.getAmtCurrentbalance());
			payment.setAmtPreChargeOffPrinciple(accountAmtData.getAmtPreChargeOffPrinciple());
			payment.setAmtPreChargeOffInterest(accountAmtData.getAmtPreChargeOffInterest());
			payment.setAmtPreChargeOffFee(accountAmtData.getAmtPreChargeOffFees());
			payment.setAmtLatefeeCurrentbalance(accountAmtData.getAmtLatefeeCurrentbalance());
			payment.setAmtCourtCostCurrentbalance(accountAmtData.getAmtCourtcostCurrentbalance());
			payment.setAmtAttorneyFeeCurrentBalance(accountAmtData.getAmtAttorneyfeeCurrentbalance());
			payment.setAmtPreChargeOffBalance(accountAmtData.getAmtPreChargeOffBalance());
			payment.setAmtPostChargeOffInterest(accountAmtData.getAmtPostChargeOffInterest());
			payment.setAmtPostChargeOffFees(accountAmtData.getAmtPostChargeOffFee());
			payment.setAmtPostChargeOffPayment(accountAmtData.getAmtPostChargeOffPayment());
			payment.setAmtPostChargeOffCredit(accountAmtData.getAmtPostChargeOffCredit());
		}
	}

	@Override
	public List<Map<String, Object>> isLegalPlacementExists(Integer clientId,String clientAccountNumber) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		paramMap.put("clientId", clientId);
		paramMap.put("clientAccountNumber", clientAccountNumber);
		
		  list = namedTemplate.queryForList(GET_LEGAL_PLACEMENT_OF_CLIENT_ACCOUNT_NUMBER, paramMap);
		return list;
	}
	
	public String getClientCode(Integer clientId){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
		paramMap.put("clientId", clientId);
		return	namedTemplate.queryForObject(GET_CLIENT_CODE_BY_CLIENT_ID, paramMap, String.class);
		}catch(Exception ex) {
			logger.error(paramMap.toString(), ex);
			return null;
		}
	}
	
	public Long getPaymentId(){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
		return	namedTemplate.queryForObject(GET_PAYMENT_ID, paramMap, Long.class);
		}catch(Exception ex) {
			logger.error(paramMap.toString(), ex);
			return null;
		}
	}
	
	public Response<List<PaymentBucketConfig>>  getPaymentBucketDistributionConfig(Integer clientId) {
		Response<List<PaymentBucketConfig>> response = new Response<List<PaymentBucketConfig>>();
		try {
			String query = new StringBuffer()
//					.append(" ")
//					.append(" select   ")
//					.append(" value.configured_value configuredValue,  name.short_name paymentDistributionType   ")
//					.append("  from  ")
//					.append(" 	conf.appconfig_value value   ")
//					.append("  inner join   ")
//					.append(" 	 conf.appconfig_name name on value.appconfig_name_id = name.appconfig_name_id  ")
//					.append("   where   ")
//					.append("  	value.configured_for = 'EQ' and name.short_name in ('BUCKET_DIS_NOPPLAN', 'BUCKET_DIS_PPLAN', 'BUCKET_DIS_BUCKET' ) ")
//					.toString();
					.append(" SELECT DISTINCT ON (name.short_name) ")
					.append("  value.configured_value configuredValue, ")
					.append(" name.short_name AS paymentDistributionType ,  value.client_id clientId ")
					.append(" FROM conf.appconfig_value value ")
					.append(" INNER JOIN conf.appconfig_name name  ")
					.append("   ON value.appconfig_name_id = name.appconfig_name_id ")
					.append(" WHERE   ")
					.append("  ( ")
					.append("      (value.configured_for = 'CL' AND value.client_id = :clientId) ")
					.append("       OR value.configured_for = 'EQ' ")
					.append("   ) ")
					.append(" AND name.short_name IN ('BUCKET_DIS_NOPPLAN', 'BUCKET_DIS_PPLAN', 'BUCKET_DIS_BUCKET')  ")
					.append(" ORDER BY  ")
					.append("     name.short_name, ")
					.append("    CASE  ")
					.append("      WHEN value.configured_for = 'CL' AND value.client_id = :clientId THEN 1 ")
					.append("       ELSE 2 ")
					.append("     END ").toString();
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("clientId", clientId);
			List<PaymentBucketConfig> configuration = namedTemplate.query(query, paramMap, new BeanPropertyRowMapper<>(PaymentBucketConfig.class));
			response.setResponse(configuration);
		}catch (Exception e) {
			e.printStackTrace();
			response = ErrorUtils.buildErrorResponse(response, e, logger);
		}
		return response;
	}
}
