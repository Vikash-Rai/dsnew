package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Cost;
import com.equabli.collectprism.repository.CostRepository;

public class CostWriter  implements ItemWriter<Cost>  {

    private final Logger logger = LoggerFactory.getLogger(CostWriter.class);

    @Autowired
    private CostRepository costRepository;

    @Override
    public void write(Chunk<? extends Cost> cost) throws Exception {
    	costRepository.saveAll(cost);
        for (Cost cst : cost) {
            logger.debug("Writer executed.....Cost Id={}.....Cost Status={}", cst.getCostId(), cst.getRecordStatusId());
        }
    }


}
