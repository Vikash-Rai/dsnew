package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.PlacedAccountLog;
import com.equabli.collectprism.repository.PlacedAccountLogRepository;

public class PlacedAccountLogWriter implements ItemWriter<PlacedAccountLog> {

    private final Logger logger = LoggerFactory.getLogger(PlacedAccountLogWriter.class);

    @Autowired
    private PlacedAccountLogRepository placedAccountLogRepository;

    @Override
    public void write(Chunk<? extends PlacedAccountLog> placedAccountLog) throws Exception {
    	placedAccountLogRepository.saveAll(placedAccountLog);
        for (PlacedAccountLog pal : placedAccountLog) {
            logger.debug("Writer executed.....PlacedAccountLog Id={}.....PlacedAccountLog Status={}", pal.getPlacedAccountLogId(), pal.getRecordStatusId());
        }
    }
}