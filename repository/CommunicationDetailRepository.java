package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.CommunicationDetail;

@Repository
public interface CommunicationDetailRepository extends JpaRepository<CommunicationDetail, Long> {

	/**
     * Finds communication detail by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of communication detail as per search criteria.
     *          If no communication detail is found, this method returns an empty list.
     */

	@Query(value="select comm from CommunicationDetail comm "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<CommunicationDetail> getCommunicationDetailToProcess(Integer rawRecordStatusId, Pageable pageable);
}