package com.equabli.collectprism.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.Complaint;
import com.equabli.collectprism.repository.ComplaintRepository;

public class ComplaintWriter implements ItemWriter<Complaint> {

    @Autowired
    private ComplaintRepository complaintRepository;

    private final Logger logger = LoggerFactory.getLogger(ComplaintWriter.class);

    @Override
    public void write(Chunk<? extends Complaint> complaints) throws Exception {
        complaintRepository.saveAll(complaints);
        for (Complaint complaint : complaints) {
            logger.debug("Writer executed.....Complaint Id={}.....Complaint Status={}", complaint.getComplaintId(), complaint.getRecordStatusId());
        }
    }
}
