package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.CreditScore;
import com.equabli.collectprism.repository.CreditScoreRepository;

public class CreditScoreWriter  implements ItemWriter<CreditScore> {

    private final Logger logger = LoggerFactory.getLogger(CreditScoreWriter.class);

    @Autowired
    private CreditScoreRepository creditScoreRepository;

    @Override
    public void write(Chunk<? extends CreditScore> creditScore) throws Exception {
    	creditScoreRepository.saveAll(creditScore);
        for (CreditScore crdtScore : creditScore) {
            logger.debug("Writer executed.....CreditScore Id={}.....CreditScore Status={}", crdtScore.getCreditScoreId(), crdtScore.getRecordStatusId());
        }
    }
}
