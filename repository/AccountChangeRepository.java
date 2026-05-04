package com.equabli.collectprism.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.entity.AccountChange;


@Repository
public interface AccountChangeRepository extends JpaRepository<AccountChange, Long>{

	@Query(value="select acc from AccountChange acc "
    		+ "where acc.recordStatusId = :recordStatusId ")
    public List<AccountChange> findByRecordStatusId(Integer recordStatusId, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update AccountChange set recordStatusId = :recordStatusId "
			+ "where clientAccountNumber = :clientAccountNumber ")
	int updateAccountDelinquencyDate(String clientAccountNumber, Integer recordStatusId);

}
