package com.equabli.collectprism.service;

import java.util.List;
import java.util.Map;

import com.equabli.collectprism.entity.*;
import com.equabli.domain.DownloadCriteria;
import com.equabli.domain.Response;
import com.equabli.domain.SearchCriteria;

public interface DataScrubbingService {

	void accountsDataScrubbing(String updatedBy, String appId, String recordSourceId,String authHeader);

	void placementDataScrubbing(String updatedBy, String appId, String recordSourceId,String authHeader);

	void othersDataScrubbing(String updatedBy, String appId, String recordSourceId,String authHeader);

	Response<List<ErrWarMessage>> getEntitySuspectedInvDetails(Map<String, String> dataScrubbingSearch);

	Response<List<Map<String, Object>>> getEntityDetailsForSuspectedInv(Map<String, String> dataScrubbingSearch);

	Response<Map<String, Object>> accountScrubRejected();

	void downloadSuspectedAccounts(Account accountSearch);

	Response<Map<String,Object>> getDataScrubbingDownload(DownloadCriteria<Account> accountSearch);
	
	Response<Map<String, Object>> testDataScrubbingService();
	
	Response<Map<String,Object>> insertOrUpdatePaymentDetails(Payment payment);

	Response<Map<String,Object>> deletePaymentDetails(Long paymentId);

    Response<Double> getUpdatedAccountBalance(Payment payment);

	Response<Map<String,Object>> updateAccountDetails(Account account);

	Response<Account> getAccountInfo(Long accountId);

	Response<Map<String,Object>> insertBalanceAdjustment(Adjustment adjustment);

	Response<Map<String, Object>> insertOrUpdateBankruptcyDetails(Bankruptcy bankruptcy);

	Response<Map<String, Object>> getBankruptcyDetailById(Long bankruptcyId);

	Response<Map<String, Object>> getBankruptcyDetails(SearchCriteria<Bankruptcy> bankruptcySearch);
	
	Boolean getPartnerCommission(Integer clientId);
	
	Response<Map<String, Object>> updateAccountByAccountChange();
}