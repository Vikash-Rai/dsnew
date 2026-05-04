package com.equabli.collectprism.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.entity.Payment;
import com.equabli.collectprism.service.DataScrubbingService;
import com.equabli.domain.DownloadCriteria;
import com.equabli.domain.Response;
import com.equabli.domain.SearchCriteria;
import com.equabli.domain.entity.ConfApp;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.ErrorUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@Tag(name = "Data Scrubbing")
public class DataScrubbingController {

	static Logger logger = LoggerFactory.getLogger(DataScrubbingController.class);

    @Autowired
    private DataScrubbingService scrubbingService;

	CommonUtils commonUtils;

	public DataScrubbingController(DataSource dataSource) {
		commonUtils = new CommonUtils(dataSource);
	}

	@RequestMapping(value = "/dataScrubbing", method = RequestMethod.GET)
	@Operation(summary = "Data scrubbing", 
					description = "API for data scrubbing")
    public Response<Map<String, Object>> invokeDataScrubbingBatch(HttpServletRequest request) {
		Response<Map<String,Object>> response = new Response<Map<String,Object>>();
        try {
    		Boolean runningStatus =	commonUtils.getRunningStatusForProcess();
    		if(runningStatus) {
    			response.setValidation(false);
    			response.setMessage(CommonConstants.PROCESS_STILL_RUNNING);
    			return response;
    		}
    		String authHeader=request.getHeader("Authorization");
    		Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);

			String updatedBy = requestDetailsMap.get(CommonConstants.USER_KEY).toString();
			if(!requestDetailsMap.containsKey(CommonConstants.RECORD_SOURCE_ID) || !requestDetailsMap.containsKey(CommonConstants.APP_ID)) {
				requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
			}
			String appId = requestDetailsMap.get(CommonConstants.APP_ID).toString();
			String recordSourceId = requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString();

    		scrubbingService.othersDataScrubbing(updatedBy, appId, recordSourceId,authHeader);
        } catch (Exception ex) {
        	ErrorUtils.buildErrorResponse(response, ex, logger);
    		return response;
        }
		response.setValidation(true);
		response.setMessage("Success");
		return response;
    }

	@RequestMapping(value = "/getEntitySuspectedInvDetails", method = RequestMethod.POST)
	@Operation(summary = "Get entity suspected inventory details", 
					description = "API for getting inventory details for suspected entity")
	public Response<List<ErrWarMessage>> getEntitySuspectedInvDetails(@RequestBody Map<String, String> dataScrubbingSearch) {
		return scrubbingService.getEntitySuspectedInvDetails(dataScrubbingSearch);
	}

	@RequestMapping(value = "/getEntityDetailsForSuspectedInventory", method = RequestMethod.POST)
	@Operation(summary = "Get entity details for suspected inventory", 
					description = "API for getting entity details for suspected inventory")
	public Response<List<Map<String, Object>>> getEntityDetailsForSuspectedInventory(@RequestBody Map<String, String> dataScrubbingSearch) {
		return scrubbingService.getEntityDetailsForSuspectedInv(dataScrubbingSearch);
	}

	@RequestMapping(value = "/downloadSuspectedAccounts", method = RequestMethod.POST)
	@Operation(summary = "Download suspected accounts", 
					description = "API for downloading suspected accounts")
	public void downloadSuspectedAccounts(@RequestBody Account accountSearch) {
		scrubbingService.downloadSuspectedAccounts(accountSearch);
	}
	
	@RequestMapping(value = "/downloadDataScrubbingSuspectedAccount", method = RequestMethod.POST)
	@Operation(summary = "Get Data Scrubbing Download",
			description = "API return Data Scrubbing Download")
	public Response<Map<String,Object>> getDataScrubbingDownload(@RequestBody DownloadCriteria<Account> downloadCriteria) {
		return scrubbingService.getDataScrubbingDownload(downloadCriteria);
	}

	@RequestMapping(value = "/insertOrUpdatePaymentDetails", method = RequestMethod.POST)
	@Operation(summary = "Get Data Scrubbing Download",
			description = "API return Data Scrubbing Download")
	public Response<Map<String,Object>> insertOrUpdatePaymentDetails(@RequestBody Payment payment) {
		return scrubbingService.insertOrUpdatePaymentDetails(payment);
	}

	@RequestMapping(value = "/deletePaymentDetail/{paymentId}", method = RequestMethod.GET)
	@Operation(summary = "Delete Payment Detail",
			description = "API delete Payment Detail")
	public Response<Map<String,Object>> deletePaymentDetails(@PathVariable Long paymentId) {
		return scrubbingService.deletePaymentDetails(paymentId);
	}

	@RequestMapping(value = "/getUpdatedAccountBalance", method = RequestMethod.GET)
	@Operation(summary = "Get getUpdatedAccountBalance",
			description = "API getUpdatedAccountBalance")
	public Response<Double> getUpdatedAccountBalance(@RequestParam Integer clientId, @RequestParam String clientAccountNumber, @RequestParam Double amtPayment , 
			@RequestParam String paymentType , @RequestParam String paymentDate) {
		Payment payment = new Payment();
		payment.setClientId(clientId);
		payment.setClientAccountNumber(clientAccountNumber);
		payment.setAmtPayment(amtPayment);
		payment.setPaymentType(paymentType);
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy"); 
	     DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	     LocalDate dtPayment = LocalDate.parse(LocalDate.parse(paymentDate, formatter).format(formatter2),formatter2);
	        
		payment.setPaymentDate(dtPayment);
		return scrubbingService.getUpdatedAccountBalance(payment);
	}

	@RequestMapping(value = "/insertBalanceAdjustment", method = RequestMethod.POST)
	@Operation(summary = "Insert Balance Adjustment",
			description = "API for Insert Balance Adjustment")
	public Response<Map<String,Object>> insertBalanceAdjustment(@RequestBody Adjustment adjustment) {
		return scrubbingService.insertBalanceAdjustment(adjustment);
	}

	@RequestMapping(value = "/insertOrUpdateBankruptcyDetails", method = RequestMethod.POST)
	@Operation(summary = "Insert Or Update BankruptcyDetails",
			description = "API for Insert Or Update BankruptcyDetails")
	public Response<Map<String, Object>> insertOrUpdateBankruptcyDetails(@RequestBody Bankruptcy bankruptcy) {
		return scrubbingService.insertOrUpdateBankruptcyDetails(bankruptcy);
	}

	@RequestMapping(value = "/getBankruptcyDetails/{bankruptcyId}", method = RequestMethod.GET)
	@Operation(summary = "get Bankruptcy Detail",
			description = "API get Bankruptcy Detail")
	public Response<Map<String, Object>> getBankruptcyDetailsByBankruptcyId(@PathVariable Long bankruptcyId) {
		return scrubbingService.getBankruptcyDetailById(bankruptcyId);
	}

	@RequestMapping(value = "/getBankruptcyDetails", method = RequestMethod.POST)
	@Operation(summary = "get Bankruptcy Detail",
			description = "API get Bankruptcy Detail")
	public Response<Map<String, Object>> getBankruptcyDetails(@RequestBody SearchCriteria<Bankruptcy> bankruptcySearch) {
		return scrubbingService.getBankruptcyDetails(bankruptcySearch);
	}

	@RequestMapping(value = "/placement/{placementId}", method = RequestMethod.POST)
	@Operation(summary = "get Placement Detail",
			description = "API get Placement Detail")
	public Response<Map<String, Object>> placement(@PathVariable Long placementId) {
		logger.info("Placement Detail API called");
		Response<Map<String, Object>> response = new Response<>();
		Map<String, Object> data = new HashMap<>();
		data.put("placementId", placementId);
		data.put("status", "SUCCESS");
		response.setValidation(true);
		response.setMessage("Success");
		response.setResponse(data);
		return response;
	}
}