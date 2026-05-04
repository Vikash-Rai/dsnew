package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.AccountUcc;

@Repository
public interface AccountUccRepository extends JpaRepository<AccountUcc, Long> {

	/**
     * Finds accountUcc by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of accountUcc as per search criteria.
     *          If no accountUcc is found, this method returns an empty list.
     */

	@Query(value="select accUcc from AccountUcc accUcc "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AccountUcc> getAccountUccToProcess(Integer rawRecordStatusId, Pageable pageable);
}