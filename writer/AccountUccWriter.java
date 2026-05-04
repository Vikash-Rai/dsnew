package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.batch.item.Chunk;
import com.equabli.collectprism.entity.AccountUcc;
import com.equabli.collectprism.repository.AccountUccRepository;

public class AccountUccWriter implements ItemWriter<AccountUcc> {

    private final Logger logger = LoggerFactory.getLogger(AccountUccWriter.class);

    @Autowired
    private AccountUccRepository accountUccRepository;

    @Override
    public void write(Chunk<? extends AccountUcc> accountUcc) throws Exception {
    	accountUccRepository.saveAll(accountUcc);
        for (AccountUcc accUcc : accountUcc) {
            logger.debug("Writer executed.....AccountUcc Id={}.....AccountUcc Status={}", accUcc.getAccountUccId(), accUcc.getRecordStatusId());
        }
    }
}