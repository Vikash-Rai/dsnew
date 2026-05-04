package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.AccountAsset;

@Repository
public interface AccountAssetRepository extends JpaRepository<AccountAsset, Long> {

	/**
     * Finds accountAsset by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of accountAsset as per search criteria.
     *          If no accountAsset is found, this method returns an empty list.
     */

	@Query(value="select accAsset from AccountAsset accAsset "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AccountAsset> getAccountAssetToProcess(Integer rawRecordStatusId, Pageable pageable);
}