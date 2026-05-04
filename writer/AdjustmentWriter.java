package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.repository.AdjustmentRepository;

public class AdjustmentWriter implements ItemWriter<Adjustment> {

    private final Logger logger = LoggerFactory.getLogger(AdjustmentWriter.class);

    @Autowired
    private AdjustmentRepository adjustmentRepository;

    @Override
    public void write(Chunk<? extends Adjustment> adjustment) throws Exception {
    	adjustmentRepository.saveAll(adjustment);
        for (Adjustment ad : adjustment) {
            logger.debug("Writer executed.....Adjustment Id={}.....Adjustment Status={}", ad.getAdjustmentId(), ad.getRecordStatusId());
        }
    }
}