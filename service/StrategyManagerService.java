package com.equabli.collectprism.service;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.equabli.domain.Response;

public interface StrategyManagerService {
	
	@GetMapping(value = "/account/commission")
	public Response<Map<String, Object>> getCommissionByClientAccNum(@RequestHeader Map<String, Object> headers,@RequestBody Map<String, Object> request );
	

}
