package com.equabli.collectprism.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.equabli.collectprism.entity.ConfEntity;
import com.equabli.collectprism.service.DataScrubbingConfigurationService;
import com.equabli.domain.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Data Scrubbing Configuration")
public class DataScrubbingConfigurationController {

	static Logger logger = LoggerFactory.getLogger(DataScrubbingConfigurationController.class);
	
	@Autowired
	private DataScrubbingConfigurationService configureService;
	
	@RequestMapping(value = "/getErrorWarnMessageDetailsForClient", method = RequestMethod.POST)
	@Operation(summary = "Get Error Warning Message Details for Client",  description = "Get Error Warning Message Details for Client")
	public Response<Map<String, Object>> getErrorWarnMessageDetailsForClient(@RequestBody Map<String, String> dataScrubbingSearch) {
		return configureService.getErrorWarnMessageDetailsForClient(dataScrubbingSearch);
	}
	
	@RequestMapping(value = "/updateErrorWarnMessageDetailsForClient", method = RequestMethod.POST)
	@Operation(summary = "Update Error Warning Message Details for Client",  description = "Update Error Warning Message Details for Client")
	public Response<String> updateErrorWarnMessageDetailsForClient(@RequestBody Map<String, Object> dataScrubbingSearch) {
		return configureService.updateErrorWarnMessageDetailsForClient(dataScrubbingSearch);
	}
	
	@RequestMapping(value = "/getAllConfEntities", method = RequestMethod.GET)
	@Operation(summary = "Get get all ConfEntities for Client",
			description = "API return list all ConfEntities for Client")
	public Response<List<ConfEntity>> getAllConfEntities(){
		return configureService.getAllConfEntities();
	}
	
}
