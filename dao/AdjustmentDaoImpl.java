package com.equabli.collectprism.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.equabli.client.CommonRestClient;
import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.AdjustmentValidation;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class AdjustmentDaoImpl implements AdjustmentDao {

    static Logger logger = LoggerFactory.getLogger(AdjustmentDaoImpl.class);

    @Autowired
    HttpServletRequest request;

    @Autowired
    private CacheableService cacheableService;

    @Autowired
    private CommonRestClient client;

    @Autowired
    private AdjustmentRepository adjustmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Response<Map<String, Object>> insertBalanceAdjustment(Adjustment adjustment) {
        {
            Response<Map<String, Object>> response = new Response<Map<String, Object>>();
            Map<String, Object> validationMap = new HashMap<String, Object>();
            try {
                Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
                adjustment.setUpdatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());
                adjustment.setAppId(Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()));
                adjustment.setRecordSourceId(Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()));
                adjustment.setCreatedBy(requestDetailsMap.get(CommonConstants.USER_KEY).toString());

                transferDataPayment(adjustment);
                paymentValid(adjustment, validationMap);
                if (!CommonUtils.isObjectNull(adjustment.getRecordStatusId()) && adjustment.getRecordStatusId() == ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId()) {
                    adjustment.setRecordType("adjustment");
                    adjustment.setDtmUtcUpdate(LocalDateTime.now());
                    adjustmentRepository.save(adjustment);
                    client.callingLedgerTransactionApi(request.getHeader("Authorization"));
                    response.setMessage("Adjustment got ingested");
                    response.setValidation(true);
                } else {
                    response.setMessage(validationMap.get("error").toString());
                    response.setValidation(false);
                }
            } catch (Exception e) {
                logger.error(e.toString(), e);
                response.setMessage(e.toString());
                response.setValidation(false);
            }
            return response;
        }
    }

    private void transferDataPayment(Adjustment adjustment) {
        adjustment.setClient(paymentRepository.findClientById(adjustment.getClientId()));
        adjustment.setPartner(adjustmentRepository.findPartner(adjustment.getPartnerId()));
        adjustment.setAdjustmentTypeLookUp(accountRepository.getLookUpByKeyValue("adjustment_type", adjustment.getAdjustmentType()));
		Account accountAmtData = paymentRepository.getDataAmtAccount(adjustment.getClientId(), adjustment.getClientAccountNumber());
		if (accountAmtData != null) {
			adjustment.setAccountIds(accountAmtData.getAccountId());
	        adjustment.setAmtOtherfeeCurrentbalance(accountAmtData.getAmtOtherfeeCurrentbalance());
	        adjustment.setAmtInterestCurrentbalance(accountAmtData.getAmtInterestCurrentbalance());
	        adjustment.setAmtPrincipalCurrentbalance(accountAmtData.getAmtPrincipalCurrentbalance());
	        adjustment.setAmtCurrentbalance(accountAmtData.getAmtCurrentbalance());
	        adjustment.setAmtPreChargeOffPrinciple(accountAmtData.getAmtPreChargeOffPrinciple());
	        adjustment.setAmtPreChargeOffInterest(accountAmtData.getAmtPreChargeOffInterest());
	        adjustment.setAmtPreChargeOffFee(accountAmtData.getAmtPreChargeOffFees());
	        adjustment.setAmtLatefeeCurrentbalance(accountAmtData.getAmtLatefeeCurrentbalance());
	        adjustment.setAmtCourtCostCurrentbalance(accountAmtData.getAmtCourtcostCurrentbalance());
	        adjustment.setAmtAttorneyFeeCurrentBalance(accountAmtData.getAmtAttorneyfeeCurrentbalance());
	        adjustment.setAmtPreChargeOffBalance(accountAmtData.getAmtPreChargeOffBalance());
	        adjustment.setAmtPostChargeOffInterest(accountAmtData.getAmtPostChargeOffInterest());
	        adjustment.setAmtPostChargeOffFees(accountAmtData.getAmtPostChargeOffFee());
	        adjustment.setAmtPostChargeOffPayment(accountAmtData.getAmtPostChargeOffPayment());
	        adjustment.setAmtPostChargeOffCredit(accountAmtData.getAmtPostChargeOffCredit());
	        adjustment.setDtChargeOff(accountAmtData.getChargeOffDate());
	    }
    }

    private void paymentValid(Adjustment adjustment, Map<String, Object> validationMap) {
        validationMap.put("Adjustment Id", adjustment.getAdjustmentId());
        validationMap.put("isAdjustmentValidated", true);

        adjustment.setErrCodeJson(null);

        List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(adjustment.getClientId(), cacheableService);
        AdjustmentValidation.mandatoryValidation(adjustment, validationMap, cacheableService, errWarMessagesList);

        if (Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
            AdjustmentValidation.lookUpValidation(adjustment, validationMap, cacheableService, errWarMessagesList);

            if (Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
                AdjustmentValidation.standardize(adjustment);
                AdjustmentValidation.referenceUpdation(adjustment);
                AdjustmentValidation.misingRefCheck(adjustment, validationMap, errWarMessagesList);

                if (Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
                    AdjustmentValidation.businessRule(adjustment, validationMap, errWarMessagesList);
                }
            }
        }

        if (!Boolean.parseBoolean(validationMap.get("isAdjustmentValidated").toString())) {
            adjustment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
        } else {
            adjustment.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
        }
    }
}
