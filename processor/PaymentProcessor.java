package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.equabli.config.SqsMessageSender;
import com.equabli.collectprism.dao.PaymentDao;
import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.entity.PaymentBucketConfig;
import com.equabli.collectprism.repository.AdjustmentRepository;
import com.equabli.collectprism.repository.PaymentRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.service.DataScrubbingService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.PaymentValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.feign.DataScrubEnrichmentServiceCommunication;

public class PaymentProcessor implements ItemProcessor<Payment, Payment> {

    private final Logger logger = LoggerFactory.getLogger(PaymentProcessor.class);

    @Autowired
	private CacheableService cacheableService;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    PaymentDao paymentDao;
    
    @Autowired
	DataScrubEnrichmentServiceCommunication serviceCommunication;
    
    @Autowired
    AdjustmentRepository adjustmentRepository;
    
    @Autowired
	SqsMessageSender sqsMessageSender;
    
    @Autowired
    DataScrubbingService dataScrubbingService;
    
    @Value("#{jobParameters['authHeader']}")
    private String authHeader;

	@Override
	public Payment process(Payment pay) throws Exception {
		try {
			if(!CommonUtils.isObjectNull(Payment.paymentRecord) && Payment.paymentRecord.containsKey(pay.getClientId() + "_" + pay.getClientAccountNumber())) {
				pay.setAmtOtherfeeCurrentbalance(Payment.paymentRecord.get(pay.getClientId() + "_" + pay.getClientAccountNumber()).getAmtOtherfeeCurrentbalance());
				pay.setAmtInterestCurrentbalance(Payment.paymentRecord.get(pay.getClientId() + "_" + pay.getClientAccountNumber()).getAmtInterestCurrentbalance());
				pay.setAmtCurrentbalance(Payment.paymentRecord.get(pay.getClientId() + "_" + pay.getClientAccountNumber()).getAmtCurrentbalance());
			}
			if(!CommonUtils.isStringNullOrBlank(pay.getReversalParentId())) {
				Payment parentPayment = paymentRepository.getDataParentPaymentData(pay.getClientId(), pay.getReversalParentId(), pay.getPartnerId());
				if (parentPayment != null) {
					pay.setParentPaymentIds(parentPayment.getPaymentId());
					pay.setParentOtherFees(parentPayment.getAmtOtherfee());
					pay.setParentInterest(parentPayment.getAmtInterest());
					pay.setParentPrincipal(parentPayment.getAmtPrincipal());
					pay.setParentLateFees(parentPayment.getAmtLatefee());
					pay.setParentCourtCost(parentPayment.getAmtCourtcost());
					pay.setParentAttorneyFees(parentPayment.getAmtAttorneyfee());
					
					if(!CommonUtils.isIntegerNull(pay.getPartnerId()) && !(pay.getPartnerId().equals(pay.getPartnerIds()))){
						pay.setPctPartnerCommission(parentPayment.getPctPartnerCommission());
					}
				}
			}

			Map<String, Object> validationMap = new HashMap<String, Object>();
			validationMap.put("Payment Id", pay.getPaymentId());
			validationMap.put("isPaymentValidated", true);

			pay.setErrCodeJson(null);

    		List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(pay.getClientId(), cacheableService);
    		Boolean isPartnerCommission = dataScrubbingService.getPartnerCommission(pay.getClientId());

    		List<Map<String,Object>> listOfDateJudement = paymentDao.isLegalPlacementExists(pay.getClientId(), pay.getClientAccountNumber());
    		List<PaymentBucketConfig> paymentBucketConfig = paymentDao.getPaymentBucketDistributionConfig(pay.getClientId()).getResponse();
//			Boolean isPastPayment = false;
//			if( (!CommonUtils.isDateNull(pay.getPaymentDate()) && !CommonUtils.isObjectNull(pay.getAccount()) && !CommonUtils.isDateNull(pay.getAccount().getDtmUtcCreate() ))
//					&&  pay.getPaymentDate().isBefore(pay.getAccount().getDtmUtcCreate().toLocalDate())) {
//				isPastPayment = true;
//			}
			
//			if(!isPastPayment) {
				PaymentValidation.mandatoryValidation(pay, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
					PaymentValidation.lookUpValidation(pay, validationMap, cacheableService, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
						PaymentValidation.standardize(pay);
						PaymentValidation.referenceUpdation(pay,serviceCommunication,authHeader,isPartnerCommission);
						PaymentValidation.misingRefCheck(pay, validationMap, errWarMessagesList);

						if(Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
							PaymentValidation.businessRule(pay, validationMap, errWarMessagesList, adjustmentRepository,listOfDateJudement,sqsMessageSender, paymentBucketConfig);
						}
					}
				}
//			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isPaymentValidated").toString())) {
				pay.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				pay.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				Payment.paymentRecord.clear();
				Payment.paymentRecord.put(pay.getClientId() + "_" + pay.getClientAccountNumber(), pay);

				if(!CommonUtils.isStringNullOrBlank(pay.getPaymentType()) && pay.getPaymentType().equalsIgnoreCase(CommonConstants.PAYMENT_TYPE_NORMAL) 
						 && !CommonUtils.isLongNull(pay.getParentPaymentIds())) {
					 Payment paymentCancel =  paymentRepository.findById(pay.getParentPaymentIds()).get();
					 Payment cPayment = Payment.getCancelPayment(paymentCancel);
					 paymentDao.insertIntoPaymentCancelPayment(cPayment);
					 paymentRepository.updatePaymentReversalDate(pay.getParentPaymentIds());

				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return pay;
	}
}