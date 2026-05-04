package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PartnerAssignment;
import com.equabli.collectprism.repository.PartnerAssignmentRepository;

public class PartnerAssignmentWriter implements ItemWriter<PartnerAssignment> {

    private final Logger logger = LoggerFactory.getLogger(PartnerAssignmentWriter.class);

    @Autowired
    private PartnerAssignmentRepository partnerAssignmentRepository;

    @Override
    public void write(Chunk<? extends PartnerAssignment> partnerAssignments) throws Exception {
        partnerAssignmentRepository.saveAll(partnerAssignments);
        for (PartnerAssignment partnerAssignment : partnerAssignments) {
            logger.debug("Writer executed.....Partner Assignment Id={}.....Partner Assignment Status={}", partnerAssignment.getPartnerAssignmentId(), partnerAssignment.getRecordStatusId());
        }
    }
}