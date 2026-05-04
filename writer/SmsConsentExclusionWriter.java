package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.SmsConsentExclusion;
import com.equabli.collectprism.repository.SmsConsentExclusionRepository;

public class SmsConsentExclusionWriter implements ItemWriter<SmsConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(SmsConsentExclusionWriter.class);

    @Autowired
    private SmsConsentExclusionRepository smsConsentExclusionRepository;

    @Override
    public void write(Chunk<? extends SmsConsentExclusion> smsConsentExclusion) throws Exception {
    	smsConsentExclusionRepository.saveAll(smsConsentExclusion);
        for (SmsConsentExclusion sce : smsConsentExclusion) {
            logger.debug("Writer executed.....SmsConsentExclusion Id={}.....SmsConsentExclusion Status={}", sce.getSmsConsentExclusionId(), sce.getRecordStatusId());
        }
    }
}