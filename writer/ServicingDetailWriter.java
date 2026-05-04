package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.ServicingDetail;
import com.equabli.collectprism.repository.ServicingDetailRepository;

public class ServicingDetailWriter implements ItemWriter<ServicingDetail> {

    private final Logger logger = LoggerFactory.getLogger(ServicingDetailWriter.class);

    @Autowired
    private ServicingDetailRepository servicingDetailRepository;

    @Override
    public void write(Chunk<? extends ServicingDetail> servicingDetail) throws Exception {
    	servicingDetailRepository.saveAll(servicingDetail);
        for (ServicingDetail sd : servicingDetail) {
            logger.debug("Writer executed.....Servicing Detail Id={}.....Servicing Detail Status={}", sd.getServicingDetailId(), sd.getRecordStatusId());
        }
    }
}