package com.equabli.collectprism.dao;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.BankruptcyRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DBQueryUtils;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.BankruptcyValidation;
import com.equabli.domain.Metadata;
import com.equabli.domain.Response;
import com.equabli.domain.SearchCriteria;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.ErrorUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class BankruptcyDaoImpl implements BankruptcyDao {

    static Logger logger = LoggerFactory.getLogger(PaymentDaoImpl.class);

    NamedParameterJdbcTemplate namedTemplate;

    @Autowired
    HttpServletRequest request;

    @Autowired
    private CacheableService cacheableService;

    @Autowired
    private BankruptcyRepository bankruptcyRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    public BankruptcyDaoImpl(DataSource dataSource) {
        namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Response<Map<String, Object>> insertOrUpdateBankruptcyDetails(Bankruptcy bankruptcy) {
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();
        try {
            Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
            bankruptcy.setUpdatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
            bankruptcy.setAppId(Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()));
            bankruptcy.setRecordSourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()));
            bankruptcy.setCreatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
            bankruptcy.setRecordType("bankruptcy");
            bankruptcy.setBankruptcySource(requestDetailsMap.get(CommonConstants.ORG_TYPE).toString());

            if(bankruptcy.getBankruptcySource().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_CLIENT)) {
            	bankruptcy.setBankruptcySourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString()));
            } else if (bankruptcy.getBankruptcySource().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER)) {
            	bankruptcy.setBankruptcySourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.PARTNER_ID).toString()));
            }

            if(bankruptcy.getBankruptcyId() == null) {
                Bankruptcy bankruptcyData = bankruptcyRepository.getDataBankruptcy(bankruptcy.getClientId(), bankruptcy.getClientAccountNumber(), bankruptcy.getClientBankruptcyId());
                if(bankruptcyData != null) {
                    return getBankruptcyDetailById(bankruptcyData.getBankruptcyId());
                }
            }

            transferDataBankruptcy(bankruptcy);
            bankruptcyValid(bankruptcy);
            if(CommonUtils.isObjectNull(bankruptcy.getErrCodeJson()) || bankruptcy.getErrCodeJson().isEmpty()) {
                bankruptcy.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
                bankruptcy.setDtmUtcUpdate(LocalDateTime.now());
                bankruptcyRepository.save(bankruptcy);
                response.setMessage("Bankruptcy Data got Ingested/Updated ");
                response.setValidation(true);
            } else {
                ErrWarJson errWarJson = bankruptcy.getErrCodeJson().stream().findFirst().get();
                Map<String, Object> errWarMessagesMap = DataScrubbingUtils.getAllApplicableScrubRulesDesc(bankruptcy.getClientId(), cacheableService);
                response.setMessage(errWarMessagesMap.get(errWarJson.getValue()) + "");
                response.setValidation(false);
            }
        }
        catch (Exception e) {
            logger.error(e.toString(), e);
            response.setMessage(e.toString());
            response.setValidation(false);
        }
        return response;
    }

    @Override
    public Response<Map<String, Object>> getBankruptcyDetailById(Long bankruptcyId) {
    	Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
        Response<Map<String, Object>> response = new Response<Map<String, Object>>();

        try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("bankruptcyId", bankruptcyId);

			String query = DBQueryUtils.GET_BANKRUPTCY_DETAIL + DBQueryUtils.BANKRUPTCY_JOIN + DBQueryUtils.BANKRUPTCY_ID;
        	List<Bankruptcy> bankruptcyDetails = namedTemplate.query(query, paramMap, new BeanPropertyRowMapper(Bankruptcy.class));

        	if(bankruptcyDetails.size() > 0) {
            	Bankruptcy bankruptcy = bankruptcyDetails.get(0);

            	if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID)) {
            		Integer clientId = Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString());

            		if(!clientId.equals(bankruptcy.getClientId())) {
                        logger.error("Invalid Client User", "Invalid Client User");
                        response.setValidation(false);
            			return response;
            		}
            	}

            	if(requestDetailsMap.containsKey(CommonConstants.PARTNER_ID)) {
            		Integer partnerId = Integer.parseInt(requestDetailsMap.get(CommonConstants.PARTNER_ID).toString());

            		if(!partnerId.equals(bankruptcy.getPartnerId())) {
                        logger.error("Invalid Partner User", "Invalid Partner User");
                        response.setValidation(false);
            			return response;
            		}
            	}

            	if(bankruptcy != null && bankruptcy.getEquabliAttorneyId() != null) {
                	bankruptcy.setAttorneyConsumer(consumerRepository.getConsumerDataByConsumerId(bankruptcy.getEquabliAttorneyId(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()));
                }

                if(bankruptcy != null && bankruptcy.getEquabliTrusteeId() != null) {
                	bankruptcy.setTrusteeConsumer(consumerRepository.getConsumerDataByConsumerId(bankruptcy.getEquabliTrusteeId(), ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()));
                }

                ObjectMapper oMapper = new ObjectMapper();
                oMapper.registerModule(new JavaTimeModule());
                Map<String, Object> map = oMapper.convertValue(bankruptcy, Map.class);

                response.setResponse(map);
        	}
            response.setValidation(true);
        } catch (Exception e) {
            logger.error(e.toString(), e);
            response.setValidation(false);
        }
        return response;
    }

    @Override
    public Response<Map<String, Object>> getBankruptcyDetails(SearchCriteria<Bankruptcy> bankruptcySearch) {
    	Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
		Response<Map<String,Object>> response = new Response<Map<String,Object>>();

		Map<String,Object> resultMap = new HashMap<String,Object>();
		Metadata metadata = bankruptcySearch.getMetadata();
		Bankruptcy bankruptcy = bankruptcySearch.getSearchCriteria();

		try {
			if(requestDetailsMap.containsKey(CommonConstants.CLIENT_ID)) 
				bankruptcy.setClientId(Integer.parseInt(requestDetailsMap.get(CommonConstants.CLIENT_ID).toString()));

			if(requestDetailsMap.containsKey(CommonConstants.PARTNER_ID)) 
				bankruptcy.setPartnerId(Integer.parseInt(requestDetailsMap.get(CommonConstants.PARTNER_ID).toString()));

			Date dtClaimProofFiled = (bankruptcy.getDtClaimProofFiled() != null) ? CommonConstants.SDF_YYYY_MM_DD.parse(bankruptcy.getDtClaimProofFiled().toString()) : null;
			Date dtBankruptcyReport  = (bankruptcy.getDtBankruptcyReport() != null) ? CommonConstants.SDF_YYYY_MM_DD.parse(bankruptcy.getDtBankruptcyReport().toString()) : null;

			MapSqlParameterSource paramMap = new MapSqlParameterSource();
			paramMap.addValue("clientId", bankruptcy.getClientId());
			paramMap.addValue("partnerId", bankruptcy.getPartnerId());
            paramMap.addValue("consumerId",bankruptcy.getConsumerId());
			paramMap.addValue("clientAccountNumber", bankruptcy.getClientAccountNumber());
            paramMap.addValue("clientConsumerNumber", bankruptcy.getClientConsumerNumber());
			paramMap.addValue("originalAccountNumber", bankruptcy.getOriginalAccountNumber());
			paramMap.addValue("bankruptcyChapter", bankruptcy.getBankruptcyChapter());
			paramMap.addValue("bankruptcyType", bankruptcy.getBankruptcyType());
			paramMap.addValue("bankruptcyCaseNumber", bankruptcy.getBankruptcyCaseNumber());
			paramMap.addValue("dtClaimProofFiled", dtClaimProofFiled);
			paramMap.addValue("dtBankruptcyReport", dtBankruptcyReport);
			paramMap.addValue("pageSize", metadata.getPageSize());
			paramMap.addValue("pageNumber", metadata.getPageNumber());

			String BANKRUPTCY_SEARCH = " where b.record_status_id = " + ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId();
			BANKRUPTCY_SEARCH = (bankruptcy.getClientId() != null) ? BANKRUPTCY_SEARCH + " and b.client_id = :clientId " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getPartnerId() != null) ? BANKRUPTCY_SEARCH + " and acc.partner_id = :partnerId " : BANKRUPTCY_SEARCH;
            BANKRUPTCY_SEARCH = (bankruptcy.getConsumerId() != null) ? BANKRUPTCY_SEARCH + " and b.consumer_id = :consumerId " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getClientAccountNumber() != null) ? BANKRUPTCY_SEARCH + " and b.client_account_number = :clientAccountNumber " : BANKRUPTCY_SEARCH;
            BANKRUPTCY_SEARCH = (bankruptcy.getClientConsumerNumber() != null) ? BANKRUPTCY_SEARCH + " and b.client_consumer_number = :clientConsumerNumber " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getOriginalAccountNumber() != null) ? BANKRUPTCY_SEARCH + " and acc.original_account_number = :originalAccountNumber " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getBankruptcyChapter() != null) ? BANKRUPTCY_SEARCH + " and b.bankruptcy_chapter = :bankruptcyChapter " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getBankruptcyType() != null) ? BANKRUPTCY_SEARCH + " and b.bankruptcy_type = :bankruptcyType " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (bankruptcy.getBankruptcyCaseNumber() != null) ? BANKRUPTCY_SEARCH + " and b.bankruptcy_case_number = :bankruptcyCaseNumber " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (dtClaimProofFiled != null) ? BANKRUPTCY_SEARCH + " and date(b.dt_bankruptcy_filling) = :dtClaimProofFiled " : BANKRUPTCY_SEARCH;
			BANKRUPTCY_SEARCH = (dtBankruptcyReport != null) ? BANKRUPTCY_SEARCH + " and date(b.dt_bankruptcy_report) = :dtBankruptcyReport " : BANKRUPTCY_SEARCH;

			String ORDER_BY_QUERY = " order by " + metadata.getSortColumn() + " " + metadata.getSortDirection();

			String query = DBQueryUtils.GET_BANKRUPTCY_DETAIL + DBQueryUtils.BANKRUPTCY_JOIN + BANKRUPTCY_SEARCH + ORDER_BY_QUERY + DBQueryUtils.LIMIT_QUERY;
        	List<Bankruptcy> bankruptcies = namedTemplate.query(query, paramMap, new BeanPropertyRowMapper(Bankruptcy.class));

			String queryCount = DBQueryUtils.GET_BANKRUPTCY_COUNT + DBQueryUtils.BANKRUPTCY_JOIN + BANKRUPTCY_SEARCH;
			List<Metadata> metaData = namedTemplate.query(queryCount, paramMap, new BeanPropertyRowMapper(Metadata.class));

			if(metaData.size() > 0) {
				resultMap.put("metaData", metaData.get(0));
			}
			resultMap.put("getAllBankruptcies", bankruptcies);
			response.setResponse(resultMap);
			response.setValidation(true);
		} catch (Exception e) {
			ErrorUtils.buildErrorResponse(response, e, logger);
		}

		return response;
    }

    private void transferDataBankruptcy(Bankruptcy bankruptcy) {
        bankruptcy.setClient(bankruptcyRepository.findClientById(bankruptcy.getClientId()));
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyCourtState())) {
    		bankruptcy.setCountryState(bankruptcyRepository.findCountryStateByStateCode(bankruptcy.getBankruptcyCourtState()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyChapter())) {
        	bankruptcy.setBankruptcyChapterLookUp(accountRepository.getLookUpByKeyValue("bankruptcy_chapter", bankruptcy.getBankruptcyChapter()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyType())) {
        	bankruptcy.setBankruptcyTypeLookUp(accountRepository.getLookUpByKeyValue("bankruptcy_type", bankruptcy.getBankruptcyType()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getChannel())) {
        	bankruptcy.setChannelLookUp(accountRepository.getLookUpByKeyValue("compliance_channel", bankruptcy.getChannel()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getModeOfReceipt())) {
        	bankruptcy.setModeOfReceiptLookUp(accountRepository.getLookUpByKeyValue("compliance_mode", bankruptcy.getModeOfReceipt()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyPetitionStatus())) {
        	bankruptcy.setBankruptcyPetitionStatusLookUp(accountRepository.getLookUpByKeyValue("bankruptcy_petition_status", bankruptcy.getBankruptcyPetitionStatus()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getBankruptcyProcessStatus())) {
        	bankruptcy.setBankruptcyProcessStatusLookUp(accountRepository.getLookUpByKeyValue("bankruptcy_process_status", bankruptcy.getBankruptcyProcessStatus()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getObjectionStatus())) {
        	bankruptcy.setObjectionStatusLookUp(accountRepository.getLookUpByKeyValue("objection_status", bankruptcy.getObjectionStatus()));
        }
        if(!CommonUtils.isStringNullOrBlank(bankruptcy.getAutomaticStayStatus())) {
        	bankruptcy.setAutomaticStayStatusLookUp(accountRepository.getLookUpByKeyValue("automatic_stay_status", bankruptcy.getAutomaticStayStatus()));
        }

        Account accountAmtData = paymentRepository.getDataAmtAccount(bankruptcy.getClientId(), bankruptcy.getClientAccountNumber());
		if (accountAmtData != null) {
			bankruptcy.setAccountIds(accountAmtData.getAccountId());
	    }

        if(!CommonUtils.isLongNullOrZero(bankruptcy.getClientConsumerNumber())) {
            Consumer getConsumerData = consumerRepository.getConsumerData(bankruptcy.getClientId(), bankruptcy.getClientAccountNumber(), bankruptcy.getClientConsumerNumber());
    		if (getConsumerData != null) {
    			bankruptcy.setConsumerIds(getConsumerData.getConsumerId());
    	    }
        }

        if(!CommonUtils.isLongNullOrZero(bankruptcy.getClientAttorneyId())) {
            Consumer getAttorneyConsumerData = consumerRepository.getConsumerDataByContactType(bankruptcy.getClientId(), bankruptcy.getClientAccountNumber(), bankruptcy.getClientAttorneyId(), "BA");
    		if (getAttorneyConsumerData != null) {
    			bankruptcy.setConsumerAttorneyIds(getAttorneyConsumerData.getConsumerId());
    	    }
        }

        if(!CommonUtils.isLongNullOrZero(bankruptcy.getClientTrusteeId())) {
			Consumer getTrusteeConsumerData = consumerRepository.getConsumerDataByContactType(bankruptcy.getClientId(), bankruptcy.getClientAccountNumber(), bankruptcy.getClientTrusteeId(), "TT");
			if (getTrusteeConsumerData != null) {
				bankruptcy.setConsumerTrusteeIds(getTrusteeConsumerData.getConsumerId());
		    }
        }
    }

    private void bankruptcyValid(Bankruptcy bankruptcy) {
        Map<String, Object> validationMap = new HashMap<String, Object>();
        validationMap.put("Payment Id", bankruptcy.getBankruptcyId());
        validationMap.put("isPaymentValidated", true);

        bankruptcy.setErrCodeJson(null);

        List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(bankruptcy.getClientId(), cacheableService);
        BankruptcyValidation.mandatoryValidation(bankruptcy, validationMap, cacheableService, errWarMessagesList);

        if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
            BankruptcyValidation.lookUpValidation(bankruptcy, validationMap, cacheableService, errWarMessagesList);

            if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
                BankruptcyValidation.standardize(bankruptcy);
                BankruptcyValidation.referenceUpdation(bankruptcy);
                BankruptcyValidation.misingRefCheck(bankruptcy, validationMap, errWarMessagesList);

                if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
                    BankruptcyValidation.businessRule(bankruptcy, validationMap, errWarMessagesList);
                }
            }
        }
    }
}
