package com.equabli.collectprism.approach.builders;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.equabli.datascrubbing.rule.PrimaryConsumerValidation;
import com.equabli.datascrubbing.rule.ValidationRule;
import com.equabli.collectprism.approach.validationhandler.ValidationResult;

@Component
@Order(1)
public class PlacementValidation implements ValidationRule{

	@Autowired
	@Qualifier("primaryConsumerValidation")
	PrimaryConsumerValidation primaryConsumerValidation;
	
	public List<ValidationResult> execute() {
			List<ValidationResult> isPrimaryConsumer = primaryConsumerValidation.execute();
//			if(!isPrimaryConsumer) {
//				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
//			}else {
//				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
//
//			}
		return isPrimaryConsumer;
	}
	
	
//	public void doPlacement() {
//		
//		// List<ValidationResult>  = primaryConsumerValidation.execute(null);
//		//List<ValidationResult>  = rule2.execute(null);
//		//primaryConsumerValidation.execute(null);
//		
//		//rule2.execute(null);
//		
//		for (Account account : accounts) {
//			boolean isPrimaryConsumer = primaryConsumerValidation.execute(account);
//			if(!isPrimaryConsumer) {
//				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.SUSPECTED).getRecordStatusId());
//			}else {
//				account.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
//
//			}
//		}
//		return accounts;
//	}
}
