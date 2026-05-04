package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.ChangeLog;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>{

	@Query(value="select changeLog from ChangeLog changeLog "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<ChangeLog> getChangeLogToProcess(Integer rawRecordStatusId, Pageable pageable);

}