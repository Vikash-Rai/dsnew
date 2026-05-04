package com.equabli.collectprism.dao;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.entity.PaymentBucketConfig;
import com.equabli.collectprism.entity.PaymentPlan;
import com.equabli.domain.Response;

public interface PaymentDao {

	void insertIntoPaymentCancelPayment(Payment cPayment);

	void updatePaymentPlanErrCodeJson(PaymentPlan pp);
	
	Timestamp testDataScrubbingService();

	Response<Map<String,Object>> insertOrUpdatePaymentDetails(Payment payment, Boolean isPartnerCommission);
	
	Double getUpdatedAccountBalance(Payment payment);

	Response<Map<String, Object>> deletePaymentDetails(Long paymentId);
	
	List<Map<String, Object>> isLegalPlacementExists(Integer clientId,String clientAccountNumber);
	
	Response<List<PaymentBucketConfig>>  getPaymentBucketDistributionConfig(Integer clientId);

}
