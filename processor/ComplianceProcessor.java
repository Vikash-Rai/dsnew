package com.equabli.collectprism.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Compliance;
import com.equabli.collectprism.entity.Consumer;
import com.equabli.collectprism.repository.ComplianceRepository;
import com.equabli.collectprism.repository.ConsumerRepository;
import com.equabli.collectprism.service.CacheableService;
import com.equabli.collectprism.util.DataScrubbingUtils;
import com.equabli.collectprism.validation.ComplianceValidation;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonUtils;

public class ComplianceProcessor implements ItemProcessor<Compliance, Compliance> {

    private final Logger logger = LoggerFactory.getLogger(ComplianceProcessor.class);

    @Autowired
	private CacheableService cacheableService;
    
    @Autowired
    ConsumerRepository consumerRepository;
    
    @Autowired
    ComplianceRepository complianceRepository;
    
    private static final Set<String> VALID_COMPLICANE_TYPES_FOR_REPLICATION = 
	        Set.of("BK", "DC", "SC"); 

	@Override
	public Compliance process(Compliance comp) throws Exception {
		try {
			Map<String, Object> validationMap = new HashMap<String, Object>();
			if(comp.getAccountIds() != null && !CommonUtils.isLongNull(comp.getAccountIds())) {
			validationMap.put("Compliance Id", comp.getComplianceId());
			validationMap.put("isComplianceValidated", true);

			comp.setErrCodeJson(null);

    		List<String> errWarMessagesList = DataScrubbingUtils.getAllApplicableScrubRules(comp.getClientId(), cacheableService);

			ComplianceValidation.mandatoryValidation(comp, validationMap, cacheableService, errWarMessagesList);

			if(Boolean.parseBoolean(validationMap.get("isComplianceValidated").toString())) {
				ComplianceValidation.lookUpValidation(comp, validationMap, cacheableService, errWarMessagesList);

				if(Boolean.parseBoolean(validationMap.get("isComplianceValidated").toString())) {
					ComplianceValidation.standardize(comp);
					ComplianceValidation.referenceUpdation(comp);
					ComplianceValidation.misingRefCheck(comp, validationMap, errWarMessagesList);

					if(Boolean.parseBoolean(validationMap.get("isComplianceValidated").toString())) {
						ComplianceValidation.businessRule(comp, validationMap, errWarMessagesList);
					}
				}
			}

			logger.debug("{}",validationMap);
			if(!Boolean.parseBoolean(validationMap.get("isComplianceValidated").toString())) {
				comp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
			} else {
				comp.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
				if(VALID_COMPLICANE_TYPES_FOR_REPLICATION.contains(comp.getComplianceType()) &&
						!CommonUtils.isStringNullOrBlank(comp.getComplianceReplicationValue()) && Boolean.parseBoolean(comp.getComplianceReplicationValue()) ) {
					List<Consumer> consumerWithSameSSN = consumerRepository.getConsumerDataByIdentificationNumberAndAccountId(comp.getConsumerIdentificationNumber(), comp.getAccountId());
					for(Consumer con : consumerWithSameSSN) {
						Compliance compNew = new Compliance();
						
						BeanUtils.copyProperties(comp, compNew, "complianceId");
						
						compNew.setComplianceId(null);
						compNew.setAccountId(con.getAccountId());
						compNew.setClientConsumerNumber(con.getClientConsumerNumber());
						compNew.setConsumerId(con.getConsumerId());
						compNew.setClientAccountNumber(con.getClientAccountNumber());
						complianceRepository.save(compNew);
					}
				}
				
			}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return comp;
	}
}