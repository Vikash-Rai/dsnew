package com.equabli.collectprism.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.entity.LookUp;
import com.equabli.collectprism.repository.ErrWarMessageRepository;
import com.equabli.collectprism.repository.LookUpRepository;

@Service
public class CacheableServiceImpl implements CacheableService {

    @Autowired
    private LookUpRepository lookUpRepository;
    
    @Autowired
    private ErrWarMessageRepository errWarMessageRepository;

	@Override
	public List<LookUp> lookUpByGroupKeyValue(String keyvalue) {
		return lookUpRepository.lookUpByGroupKeyValue(keyvalue);
	}
	
	@Override
	public List<ErrWarMessage> getAllMandatoryApplicableScrubRules() {
		return errWarMessageRepository.getAllMandatoryApplicableScrubRules();
	}

	@Override
	public List<ErrWarMessage> getAllApplicableScrubRulesForClient(Integer clientId, Integer recordStatusId) {
		return errWarMessageRepository.getAllApplicableScrubRulesForClient(clientId, recordStatusId);
	}

	@Override
	public List<ErrWarMessage> getAllDefaultApplicableScrubRulesForEquabli() {
		return errWarMessageRepository.getAllDefaultApplicableScrubRulesForEquabli();
	}
}