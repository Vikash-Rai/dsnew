package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Lien;
import com.equabli.collectprism.repository.LienRepository;

public class LienWriter implements ItemWriter<Lien> {
    private final Logger logger = LoggerFactory.getLogger(LienWriter.class);

    @Autowired
    private LienRepository lienRepository;

    @Override
    public void write(Chunk<? extends Lien> liens) throws Exception {
        lienRepository.saveAll(liens);
        for (Lien lien : liens) {
            logger.debug("Writer executed.....Lien Id={}.....Lien Status={}", lien.getLienId(), lien.getRecordStatusId());
        }
    }
}
