package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Garnishment;
import com.equabli.collectprism.repository.GarnishmentRepository;

public class GarnishmentWriter implements ItemWriter<Garnishment> {

    private final Logger logger = LoggerFactory.getLogger(GarnishmentWriter.class);

    @Autowired
    private GarnishmentRepository garnishmentRepository;

    @Override
    public void write(Chunk<? extends Garnishment> garnishments) throws Exception {
        garnishmentRepository.saveAll(garnishments);
        for (Garnishment g : garnishments) {
            logger.debug("Writer executed.....Garnishment Id={}.....Garnishment Status={}", g.getLegalGarnishmentId(), g.getRecordStatusId());
        }
    }
}