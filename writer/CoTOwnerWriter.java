package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.CoTOwner;
import com.equabli.collectprism.repository.CoTOwnerRepository;

public class CoTOwnerWriter implements ItemWriter<CoTOwner> {

    private final Logger logger = LoggerFactory.getLogger(PartnerAssignmentWriter.class);

    @Autowired
    private CoTOwnerRepository coTOwnerRepository;

    @Override
    public void write(Chunk<? extends CoTOwner> coTOwners) throws Exception {
        coTOwnerRepository.saveAll(coTOwners);
        for (CoTOwner coTOwner : coTOwners) {
            logger.debug("Writer executed.....CoTOwner Id={}.....CoTOwner Status={}", coTOwner.getCotOwnerId(), coTOwner.getRecordStatusId());
        }
    }
}