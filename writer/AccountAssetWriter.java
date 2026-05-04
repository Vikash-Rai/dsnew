package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.AccountAsset;
import com.equabli.collectprism.repository.AccountAssetRepository;

public class AccountAssetWriter implements ItemWriter<AccountAsset> {

    private final Logger logger = LoggerFactory.getLogger(AccountAssetWriter.class);

    @Autowired
    private AccountAssetRepository accountAssetRepository;

    @Override
    public void write(Chunk<? extends AccountAsset> accountAsset) throws Exception {
    	accountAssetRepository.saveAll(accountAsset);
        for (AccountAsset accAsset : accountAsset) {
            logger.debug("Writer executed.....AccountAsset Id={}.....AccountAsset Status={}", accAsset.getAccountUccId(), accAsset.getRecordStatusId());
        }
    }

}