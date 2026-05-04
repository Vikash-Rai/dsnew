package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ChainOfTitle;
import com.equabli.collectprism.repository.ChainOfTitleRepository;
import com.equabli.domain.helpers.CommonUtils;

public class ChainOfTitleWriter implements ItemWriter<ChainOfTitle> {

    private final Logger logger = LoggerFactory.getLogger(ChainOfTitleWriter.class);

    @Autowired
    private ChainOfTitleRepository chainOfTitleRepository;

    @Override
    public void write(Chunk<? extends ChainOfTitle> chainOfTitles) throws Exception {
        chainOfTitleRepository.saveAll(chainOfTitles);
        for (ChainOfTitle cot : chainOfTitles) {
        	if(!CommonUtils.isObjectNull(cot.getCotStatusId())) {
        		try {
        			chainOfTitleRepository.updateCotStatusIdForAllCot(cot.getCotStatusId(), cot.getClientId(), cot.getClientAccountNumber());
        		} catch (Exception e) {
        			logger.error(e.getMessage(), e);
				}
        	}
            logger.debug("Writer executed.....ChainOfTitle Id={}.....ChainOfTitle Status={}", cot.getChainOfTitleId(), cot.getRecordStatusId());
        }
    }
}