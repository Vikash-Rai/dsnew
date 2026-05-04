package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ChargeOffAccount;
import com.equabli.collectprism.repository.ChargeOffAccountRepository;

public class ChargeOffAccountWriter implements ItemWriter<ChargeOffAccount>{
	
	 private final Logger logger = LoggerFactory.getLogger(ChargeOffAccountWriter.class);

	    @Autowired
	    private ChargeOffAccountRepository chargeOffAccountRepository;

	    @Override
	    public void write(Chunk<? extends ChargeOffAccount> chargeOff) throws Exception {
	    	chargeOffAccountRepository.saveAll(chargeOff);
	        for (ChargeOffAccount chargeOffAccount : chargeOff) {
	            logger.debug("Writer executed.....ChargeOff Id={}.....ChargeOff Status={}", chargeOffAccount.getChargeOffAccountId(), chargeOffAccount.getRecordStatusId());
	        }
	    }

}
