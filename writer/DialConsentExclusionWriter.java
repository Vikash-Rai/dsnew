package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.DialConsentExclusion;
import com.equabli.collectprism.repository.DialConsentExclusionRepository;

public class DialConsentExclusionWriter implements ItemWriter<DialConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(DialConsentExclusionWriter.class);

    @Autowired
    private DialConsentExclusionRepository dialConsentExclusionRepository;

    @Override
    public void write(Chunk<? extends DialConsentExclusion> dialConsentExclusion) throws Exception {
    	dialConsentExclusionRepository.saveAll(dialConsentExclusion);
        for (DialConsentExclusion dce : dialConsentExclusion) {
            logger.debug("Writer executed.....DialConsentExclusion Id={}.....DialConsentExclusion Status={}", dce.getDialConsentExclusionId(), dce.getRecordStatusId());
        }
    }
}