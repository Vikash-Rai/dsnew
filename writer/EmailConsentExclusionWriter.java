package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.EmailConsentExclusion;
import com.equabli.collectprism.repository.EmailConsentExclusionRepository;

public class EmailConsentExclusionWriter implements ItemWriter<EmailConsentExclusion> {

    private final Logger logger = LoggerFactory.getLogger(EmailConsentExclusionWriter.class);

    @Autowired
    private EmailConsentExclusionRepository emailConsentExclusionRepository;

    @Override
    public void write(Chunk<? extends EmailConsentExclusion> emailConsentExclusion) throws Exception {
    	emailConsentExclusionRepository.saveAll(emailConsentExclusion);
        for (EmailConsentExclusion ece : emailConsentExclusion) {
            logger.debug("Writer executed.....EmailConsentExclusion Id={}.....EmailConsentExclusion Status={}", ece.getEmailConsentExclusionId(), ece.getRecordStatusId());
        }
    }
}