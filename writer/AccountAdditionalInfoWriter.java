package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.AccountAdditionalInfo;
import com.equabli.collectprism.repository.AccountAdditionalInfoRepository;
import com.equabli.collectprism.repository.ErrWarMessageRepository;

public class AccountAdditionalInfoWriter  implements ItemWriter<AccountAdditionalInfo>{

    private final Logger logger = LoggerFactory.getLogger(AccountAdditionalInfoWriter.class);

    @Autowired
    private AccountAdditionalInfoRepository accountAdditionalInfoRepository;
    
    @Autowired
    ErrWarMessageRepository errWarMessageRepository;

    @Override
    public void write(Chunk<? extends AccountAdditionalInfo> accountAdditionalInfos) throws Exception {
    	accountAdditionalInfoRepository.saveAll(accountAdditionalInfos);
    	
        for (AccountAdditionalInfo accountAdditionalInfo : accountAdditionalInfos) {
            logger.debug("Writer executed.....AccountAdditionalInfo Id={}.....AccountAdditionalInfo Status={}", accountAdditionalInfo.getAccountAdditionalInfoId(), accountAdditionalInfo.getRecordStatusId());
        }
    }
}