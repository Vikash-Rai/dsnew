package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.CommunicationDetail;
import com.equabli.collectprism.repository.CommunicationDetailRepository;

public class CommunicationDetailWriter implements ItemWriter<CommunicationDetail> {

    private final Logger logger = LoggerFactory.getLogger(CommunicationDetailWriter.class);

    @Autowired
    private CommunicationDetailRepository communicationDetailRepository;

    @Override
    public void write(Chunk<? extends CommunicationDetail> communicationDetails) throws Exception {
    	communicationDetailRepository.saveAll(communicationDetails);
        for (CommunicationDetail comm : communicationDetails) {
            logger.debug("Writer executed.....CommunicationDetail Id={}.....CommunicationDetail Status={}", comm.getCommunicationDetailId(), comm.getRecordStatusId());
        }
    }
}