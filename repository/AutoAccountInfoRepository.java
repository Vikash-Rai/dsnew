package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.AutoAccountInfo;

@Repository
public interface AutoAccountInfoRepository extends JpaRepository<AutoAccountInfo, Long>{

	@Query(value="select autoAccountInfo from AutoAccountInfo autoAccountInfo "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AutoAccountInfo> getAutoAccountInfoToProcess(Integer rawRecordStatusId, Pageable pageable);

}