package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.OperationResponse;
import com.equabli.collectprism.repository.OperationResponseRepository;

public class OperationResponseWriter implements ItemWriter<OperationResponse> {

    private final Logger logger = LoggerFactory.getLogger(OperationResponseWriter.class);

    @Autowired
    private OperationResponseRepository operationResponseRepository;

    @Override
    public void write(Chunk<? extends OperationResponse> operationResponse) throws Exception {
    	operationResponseRepository.saveAll(operationResponse);
        for (OperationResponse or: operationResponse) {
            logger.debug("Writer executed.....OperationResponse Id={}.....OperationResponse Status={}", or.getOperationResponseId(), or.getRecordStatusId());
        }
    }
}