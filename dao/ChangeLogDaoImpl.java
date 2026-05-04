package com.equabli.collectprism.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.servlet.http.HttpServletRequest;

@Repository
public class ChangeLogDaoImpl implements ChangeLogDao{

	
	static Logger logger = LoggerFactory.getLogger(ChangeLogDaoImpl.class);

	NamedParameterJdbcTemplate namedTemplate;

	@Autowired
	HttpServletRequest request;


	public ChangeLogDaoImpl(DataSource dataSource) {
		namedTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	
	public static final String SUBPOOL_ID_DETAILS = new StringBuffer()
			.append("   ")
			.append("  select  ")
			.append("  	subPool.client_subpool_id subpoolid ")
			.append("  from  ")
			.append("  	conf.client_subpool subPool  ")
			.append("    where  ")
			.append("  	subPool.client_subpool_id = :subPoolId and subPool.client_id = :clientId ")
			.append("   ")
			.toString();
	
	@Override
	public Boolean validSubPoolId(Long subPoolId,Integer clientId) {
		Boolean subPoolIdValid = false;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("subPoolId", subPoolId);
		paramMap.put("clientId", clientId);
		try {
			Long clientSubPoolId = namedTemplate.queryForObject(SUBPOOL_ID_DETAILS, paramMap, Long.class);
			subPoolIdValid= true;
			logger.debug("subPoolIdValid  :: "+ subPoolIdValid);
		}catch (Exception e) {
			logger.error("  validation failed ::  " + subPoolId);
		}
		return subPoolIdValid;
	}
}
