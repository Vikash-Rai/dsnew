package com.equabli.collectprism.repository;

import com.equabli.collectprism.entity.AccountTag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTagRepository extends JpaRepository<AccountTag, Long> {

	/**
     * Finds accountTag by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of accountTag as per search criteria.
     *          If no accountTag is found, this method returns an empty list.
     */

	@Query(value="select accountTag from AccountTag accountTag "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AccountTag> getAccountTagToProcess(Integer rawRecordStatusId, Pageable pageable);
}