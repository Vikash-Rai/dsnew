package com.equabli.collectprism.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;

public class DataScrubbingUtils {

	public static List<String> getAllApplicableScrubRules(Integer clientId, CacheableService cacheableService){
		List<ErrWarMessage> mandatoryApplicableScrubRules = cacheableService.getAllMandatoryApplicableScrubRules();
		List<ErrWarMessage> applicableScrubRulesForClient = cacheableService.getAllApplicableScrubRulesForClient(clientId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		List<ErrWarMessage> defaultApplicableScrubRulesForEquabli = new ArrayList<ErrWarMessage>();
		List<String> allApplicableScrubRules = new ArrayList<String>();
		for(ErrWarMessage err : mandatoryApplicableScrubRules) {
			allApplicableScrubRules.add(err.getShortName());
		}
		if(applicableScrubRulesForClient.size() > 0) {
			for(ErrWarMessage err : applicableScrubRulesForClient) {
				if(!CommonUtils.isBooleanNull(err.getIsApplicable()) && err.getIsApplicable()) {
					allApplicableScrubRules.add(err.getShortName());
				}
			}
		}else {
			defaultApplicableScrubRulesForEquabli = cacheableService.getAllDefaultApplicableScrubRulesForEquabli();
			for(ErrWarMessage err : defaultApplicableScrubRulesForEquabli) {
				if(!CommonUtils.isBooleanNull(err.getIsApplicable()) && err.getIsApplicable()) {
					allApplicableScrubRules.add(err.getShortName());
				}
			}
		}
		return allApplicableScrubRules;
	}

	public static Map<String, Object> getAllApplicableScrubRulesDesc(Integer clientId, CacheableService cacheableService){
		List<ErrWarMessage> mandatoryApplicableScrubRules = cacheableService.getAllMandatoryApplicableScrubRules();
		List<ErrWarMessage> applicableScrubRulesForClient = cacheableService.getAllApplicableScrubRulesForClient(clientId, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		List<ErrWarMessage> defaultApplicableScrubRulesForEquabli = new ArrayList<ErrWarMessage>();
		Map<String, Object> allApplicableScrubRules = new HashMap<String, Object>();
		for(ErrWarMessage err : mandatoryApplicableScrubRules) {
			allApplicableScrubRules.put(err.getShortName(), err.getDescription());
		}
		if(applicableScrubRulesForClient.size() > 0) {
			for(ErrWarMessage err : applicableScrubRulesForClient) {
				if(!CommonUtils.isBooleanNull(err.getIsApplicable()) && err.getIsApplicable()) {
					allApplicableScrubRules.put(err.getShortName(), err.getDescription());
				}
			}
		}else {
			defaultApplicableScrubRulesForEquabli = cacheableService.getAllDefaultApplicableScrubRulesForEquabli();
			for(ErrWarMessage err : defaultApplicableScrubRulesForEquabli) {
				if(!CommonUtils.isBooleanNull(err.getIsApplicable()) && err.getIsApplicable()) {
					allApplicableScrubRules.put(err.getShortName(), err.getDescription());
				}
			}
		}
		return allApplicableScrubRules;
	}

	public static Boolean ifErrorWarningCodeExistsInErrJSON(Set<ErrWarJson> errWarJson) {
		Boolean isErrExists = false;
		for(ErrWarJson e : errWarJson) {
			if(e.getKey().equals("e")) {
				isErrExists = true;
				break;
			}
		}
		return isErrExists;
	}
}