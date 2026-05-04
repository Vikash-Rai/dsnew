package com.equabli.collectprism.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.equabli.collectprism.entity.ConfEntity;
import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.entity.ScrubRuleConfig;
import com.equabli.collectprism.repository.ConfEntityRepository;
import com.equabli.collectprism.repository.ScrubRuleConfigRepository;
import com.equabli.domain.Response;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.equabli.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class DataScrubbingConfigurationServiceImpl implements DataScrubbingConfigurationService {

	static Logger logger = LoggerFactory.getLogger(DataScrubbingServiceImpl.class);

	RestTemplateBuilder restTemplateBuilder;
	RestTemplate restTemplate;

	@Autowired
	ScrubRuleConfigRepository scrubRuleConfigRepository;
	
	@Autowired
	ConfEntityRepository confEntityRepository;
	
	NamedParameterJdbcTemplate namedTemplate;
	
	@Autowired
	HttpServletRequest request;

	public DataScrubbingConfigurationServiceImpl(DataSource dataSource) {
		namedTemplate = new NamedParameterJdbcTemplate(dataSource);

		restTemplateBuilder = new RestTemplateBuilder();
		restTemplate = restTemplateBuilder.build();
		restTemplate.setMessageConverters(Arrays.asList(new MappingJackson2HttpMessageConverter(), new FormHttpMessageConverter()));
	}
	
	final static String query = new StringBuffer()
			.append(" select  ewm.short_name shortName,ewm.entity_short_name entityShortName ,entity.full_name entityFullName, ewm.description description ,coalesce(ewm.is_applicable,'f')::boolean isReadOnly, ")
			.append(" coalesce(scrc.is_applicable,scre.is_applicable,'f')::boolean isApplicableForClient,  ")
			.append(" (case when coalesce(scrc.is_applicable,scre.is_applicable,'f') = false then false else coalesce(scrc.is_error,scre.is_error,'t')::boolean end) as isErrorCode ")
			.append(" from conf.errwarmessage ewm ")
			.append(" left outer join conf.scrub_rule_config scrc on ewm.short_name=scrc.errwar_short_name and scrc.configured_for='CL' and scrc.client_id=:clientId ")
			.append(" left outer join conf.scrub_rule_config scre on ewm.short_name=scre.errwar_short_name and scre.configured_for='EQ' ")
			.append(" left outer join conf.entity entity on ewm.entity_short_name = entity.short_name ")
			.toString();
	
	final static String where_clause = new StringBuffer().append("   where ewm.entity_short_name = :entityShortName  ").toString(); 
	
	@Override
	public Response<Map<String, Object>> getErrorWarnMessageDetailsForClient(Map<String, String> dataScrubbingSearch) {
		Response<Map<String, Object>> response = new Response<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();

		Integer clientId = (dataScrubbingSearch.containsKey("clientId") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("clientId"))) ? Integer.parseInt(dataScrubbingSearch.get("clientId")) : null;
		String entityShortName =  (dataScrubbingSearch.containsKey("entityShortName") && !CommonUtils.isStringNullOrBlank(dataScrubbingSearch.get("entityShortName"))) ?dataScrubbingSearch.get("entityShortName") : null;
		if(CommonUtils.isIntegerNullOrZero(clientId)) {
			response.setValidation(false);
			response.setMessage("Client ID cannot be empty.");
			return response;
		}
		if(CommonUtils.isStringNullOrBlank(entityShortName)) {
			response.setValidation(false);
			response.setMessage("entityShortName cannot be empty.");
			return response;
		}
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("clientId", clientId);
		String queryString = query;
		if (entityShortName != null && !entityShortName.equalsIgnoreCase("all")) {
			queryString = queryString + where_clause;
			paramMap.addValue("entityShortName", entityShortName);
		}

		List<ErrWarMessage> allApplicableScrubRulesForClientList = namedTemplate.query(queryString, paramMap, new BeanPropertyRowMapper(ErrWarMessage.class));

		Collections.sort(allApplicableScrubRulesForClientList, Comparator.comparing(ErrWarMessage::getShortName));
		map.put("clientConfigRules", allApplicableScrubRulesForClientList);

		response.setResponse(map);
		return response;
	}
	
	@Override
	public Response<String> updateErrorWarnMessageDetailsForClient(Map<String, Object> dataScrubbingSearch) {
		Response<String> response = new Response<String>();

		Integer clientId = (dataScrubbingSearch.containsKey("clientId") && !CommonUtils.isStringNullOrBlank(String.valueOf(dataScrubbingSearch.get("clientId"))))
				? Integer.parseInt(String.valueOf(dataScrubbingSearch.get("clientId"))) : null;

		if(CommonUtils.isIntegerNullOrZero(clientId)) {
			response.setValidation(false);
			response.setMessage("Client ID cannot be empty.");
			return response;
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try {			
			List<ScrubRuleConfig> scrubRuleConfigList = Arrays.asList(mapper.readValue(
					mapper.writeValueAsString(dataScrubbingSearch.get("scrubRule")), ScrubRuleConfig[].class));

			for (ScrubRuleConfig scrubRuleConfig : scrubRuleConfigList) {

				if(CommonUtils.isStringNullOrBlank(scrubRuleConfig.getErrwarShortName())) {
					response.setValidation(false);
					response.setMessage("Error Warning short name cannot be empty.");
					return response;
				}

				if(CommonUtils.isBooleanNull(scrubRuleConfig.getIsApplicable())) {
					scrubRuleConfig.setIsApplicable(false);
				}
				
				Map<String, Object> requestDetailsMap = CommonUtils.getDetailsFromRequestHeaders(request);
				
				int isUpdate = scrubRuleConfigRepository.updateErrorWarnMessageDetailsForClient(clientId, scrubRuleConfig.getErrwarShortName(), scrubRuleConfig.getIsApplicable(), requestDetailsMap.get(CommonConstants.USER_KEY).toString(),
						Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()), Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()), scrubRuleConfig.getIsErrorCode());
				if(isUpdate == 0) {
					scrubRuleConfigRepository.insertErrorWarnMessageDetailsForClient(CommonConstants.RECORD_SOURCE_CLIENT, clientId,  scrubRuleConfig.getErrwarShortName(),  CommonUtils.isBooleanNull(scrubRuleConfig.getIsApplicable())?false :scrubRuleConfig.getIsApplicable(), 
							 requestDetailsMap.get(CommonConstants.USER_KEY).toString(),requestDetailsMap.get(CommonConstants.USER_KEY).toString(),
								Integer.parseInt(requestDetailsMap.get(CommonConstants.RECORD_SOURCE_ID).toString()), Integer.parseInt(requestDetailsMap.get(CommonConstants.APP_ID).toString()), CommonUtils.isBooleanNull(scrubRuleConfig.getIsErrorCode())?true :scrubRuleConfig.getIsApplicable());
				}
			}
			response.setResponse("Record is inserted or updated successfully.");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			Utils.errorHandler(e, response, logger);
			response.setValidation(false);
		}
		return response;
	}
	
	@Override
	public Response<List<ConfEntity>> getAllConfEntities(){
		Response<List<ConfEntity>> response = new Response<List<ConfEntity>>();
		response.setValidation(true);
		List<ConfEntity> confEntities = confEntityRepository.getAllConfEntities();
		confEntities.sort(Comparator.comparing(ConfEntity::getFullName, String.CASE_INSENSITIVE_ORDER));
		response.setResponse(confEntities);
		return response;
	}
	
	
}
