package com.equabli.collectprism.controller;

import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.service.DataScrubbingService;
import com.equabli.domain.Response;
import com.equabli.domain.entity.ConfApp;
import com.equabli.domain.entity.ConfRecordSource;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.domain.helpers.ErrorUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@Tag(name = "Data Scrubbing Account")
public class AccountController {

	static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private DataScrubbingService scrubbingService;

	CommonUtils commonUtils;

	public AccountController(DataSource dataSource) {
		commonUtils = new CommonUtils(dataSource);
	}

	@RequestMapping(value = "/accountsDataScrubbing", method = RequestMethod.GET)
	@Operation(summary = "Accounts data scrubbing", 
					description = "API for accounts data scrubbing")
    public Response<Map<String, Object>> accountsDataScrubbing(HttpServletRequest request) {
		Response<Map<String,Object>> response = new Response<Map<String,Object>>();
        try {
    		Boolean runningStatus =	commonUtils.getRunningStatusForProcess();
    		if(runningStatus) {
    			response.setValidation(false);
    			response.setMessage(CommonConstants.PROCESS_STILL_RUNNING);
    			return response;
    		}

    		Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
    		String authHeader=request.getHeader("Authorization");
			String updatedBy = requestDetailsMap.get(CommonConstants.USER_KEY).toString();
			if(!requestDetailsMap.containsKey(CommonConstants.APP_ID)) {
				requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(ConfRecordSource.confRecordSource.get(ConfRecordSource.ECP_AP).getRecordSourceId(), ConfApp.confApp.get(ConfApp.ECP_BAT).getAppId());
			}
			String appId = requestDetailsMap.get(CommonConstants.APP_ID).toString();
			String recordSourceId = requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString();

    		scrubbingService.accountsDataScrubbing(updatedBy, appId, recordSourceId,authHeader);
        } catch (Exception ex) {
        	ErrorUtils.buildErrorResponse(response, ex, logger);
    		return response;
        }
		response.setValidation(true);
		response.setMessage("Success");
		return response;
    }

	@RequestMapping(value = "/accountScrubRejected", method = RequestMethod.GET)
	@Operation(summary = "Accounts scrub rejected", 
					description = "API for moving accounts to rejected state which are in suspected state after certain interval of period")
	public Response<Map<String, Object>> accountScrubRejected() {
		return scrubbingService.accountScrubRejected();
	}
	
	@RequestMapping(value = "/testDataScrubbingService", method = RequestMethod.GET)
	public Response<Map<String, Object>> testEmailService(){
		return scrubbingService.testDataScrubbingService();
	}

	@RequestMapping(value = "/updateAccountDetails", method = RequestMethod.POST)
	@Operation(summary = "Update account detail",
			description = "API for update account detail")
	public Response<Map<String,Object>> updateAccountDetails(@RequestBody Account account) {
		return scrubbingService.updateAccountDetails(account);
	}

	@RequestMapping(value = "/getAccountInfo/{accountId}", method = RequestMethod.GET)
	@Operation(summary = "Get account info",
			description = "API get account info")
	public Response<Account> getAccountInfo(@PathVariable("accountId") Long accountId) {
		return scrubbingService.getAccountInfo(accountId);
	}
	
	
	@RequestMapping(value = "/updateAccountByAccountChange", method = RequestMethod.POST)
	@Operation(summary = "Get account info",
			description = "API get account info")
	public Response<Map<String, Object>> updateAccountByAccountChange() {
		return scrubbingService.updateAccountByAccountChange();
	}
}