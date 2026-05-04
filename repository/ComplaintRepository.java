package com.equabli.collectprism.repository;

import com.equabli.collectprism.entity.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	@Query(value = " select c from Complaint c where c.recordStatusId = :rawRecordStatusId ")
    Page<Complaint> getComplaintToProcess(Integer rawRecordStatusId, Pageable pageable);
}