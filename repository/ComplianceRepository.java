package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.Compliance;

@Repository
public interface ComplianceRepository extends JpaRepository<Compliance, Long> {

	/**
     * Finds compliance by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of compliance as per search criteria.
     *          If no compliance is found, this method returns an empty list.
     */

	@Query(value="select comp from Compliance comp "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<Compliance> getComplianceToProcess(Integer rawRecordStatusId, Pageable pageable);
}