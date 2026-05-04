package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.BankruptcyRepository;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

public class BankruptcyWriter implements ItemWriter<Bankruptcy> {

    @Autowired
    private BankruptcyRepository bankruptcyRepository;

    @Autowired
    private AccountRepository accountRepository;

    private final Logger logger = LoggerFactory.getLogger(BankruptcyWriter.class);

    @Override
    public void write(Chunk<? extends Bankruptcy> bankruptcies) throws Exception {
        bankruptcyRepository.saveAll(bankruptcies);
        for (Bankruptcy bk : bankruptcies) {
        	if(!CommonUtils.isIntegerNullOrZero(bk.getRecordStatusId()) && !CommonUtils.isStringNullOrBlank(bk.getClientAccountNumber())
        			&& bk.getClient() != null && bk.getClient().getShortName().equals(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)
        			&& bk.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())) {
    			accountRepository.updateAccountDelinquencyDate(bk.getClientId(), bk.getClientAccountNumber());
			}

            logger.debug("Writer executed.....Bankruptcy Id={}.....Bankruptcy Status={}", bk.getBankruptcyId(), bk.getRecordStatusId());
        }
    }
}