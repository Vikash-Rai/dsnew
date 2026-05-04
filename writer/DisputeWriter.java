package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Dispute;
import com.equabli.collectprism.repository.DisputeRepository;

public class DisputeWriter implements ItemWriter<Dispute> {

    @Autowired
    private DisputeRepository disputeRepository;

    private final Logger logger = LoggerFactory.getLogger(DisputeWriter.class);

    @Override
    public void write(Chunk<? extends Dispute> disputes) throws Exception {
        disputeRepository.saveAll(disputes);
        for (Dispute dispute : disputes) {
            logger.debug("Writer executed.....Dispute Id={}.....Dispute Status={}", dispute.getDisputeId(), dispute.getRecordStatusId());
        }
    }
}
