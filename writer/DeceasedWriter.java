package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Deceased;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.DeceasedRepository;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class DeceasedWriter implements ItemWriter<Deceased> {

    private final Logger logger = LoggerFactory.getLogger(DeceasedWriter.class);

    @Autowired
    private DeceasedRepository deceasedRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void write(Chunk<? extends Deceased> deceased) throws Exception {
    	deceasedRepository.saveAll(deceased);
        for (Deceased dc : deceased) {
        	if(!CommonUtils.isIntegerNullOrZero(dc.getRecordStatusId()) && !CommonUtils.isStringNullOrBlank(dc.getClientAccountNumber())
        			&& dc.getClient() != null && dc.getClient().getShortName().equals(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)
        			&& dc.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())) {
    			accountRepository.updateAccountDelinquencyDate(dc.getClientId(), dc.getClientAccountNumber());
			}

        	logger.debug("Writer executed.....Deceased Id={}.....Deceased Status={}", dc.getDeceasedId(), dc.getRecordStatusId());
        }
    }
}