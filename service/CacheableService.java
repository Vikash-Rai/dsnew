package com.equabli.collectprism.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;

import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.entity.LookUp;

public interface CacheableService {

	@Cacheable(cacheNames="lookUpByGroupKeyValue", key="#keyvalue")
	List<LookUp> lookUpByGroupKeyValue(final String keyvalue);
	
//	@Cacheable("getAllMandatoryApplicableScrubRules")
	List<ErrWarMessage> getAllMandatoryApplicableScrubRules();

//	@Cacheable(cacheNames="getAllApplicableScrubRulesForClient", key="#clientId")
	List<ErrWarMessage> getAllApplicableScrubRulesForClient(final Integer clientId, final Integer recordStatusId);

//	@Cacheable("getAllDefaultApplicableScrubRulesForEquabli")
	List<ErrWarMessage> getAllDefaultApplicableScrubRulesForEquabli();
}