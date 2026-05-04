package com.equabli.collectprism.repository;

import com.equabli.collectprism.entity.ChainOfTitle;
import com.equabli.collectprism.entity.ChainOfTitles;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ChainOfTitleRepository extends JpaRepository<ChainOfTitle, Long> {

	/**
     * Finds ChainOfTitle by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of ChainOfTitle as per search criteria.
     *          If no ChainOfTitle is found, this method returns an empty list.
     */

	@Query(value="select chainOfTitle from ChainOfTitle chainOfTitle "
    		+ "where recordStatusId = :rawRecordStatusId or cotStatusId = :cotStatusId ")
	public Page<ChainOfTitle> getChainOfTitleToProcess(Integer rawRecordStatusId, Integer cotStatusId, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update ChainOfTitle set cotStatusId = :cotStatusId where clientId = :clientId and clientAccountNumber = :clientAccountNumber ")
	public void updateCotStatusIdForAllCot(Integer cotStatusId, Integer clientId, String clientAccountNumber);

	@Query(value="select new ChainOfTitles(ct.chainOfTitleId, ct.dtStart, ct.dtEnd) from ChainOfTitle ct where ct.clientId = :clientId "
			+ "and ct.cotType = :cotType and ct.clientAccountNumber = :clientAccountNumber and ct.chainOfTitleId != :chainOfTitleId "
			+ "order by ct.dtStart asc, ct.dtEnd asc ")
	public List<ChainOfTitles> getChainOfTitlesToProcess(Integer clientId, String cotType, String clientAccountNumber, Long chainOfTitleId);

}