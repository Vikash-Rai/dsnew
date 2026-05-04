package com.equabli.collectprism.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.collectprism.entity.Partner;
import com.equabli.domain.entity.ConfRecordStatus;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {

	/**
     * Finds adjustment by using either the raw record status id or suspected record status id and job name when process run last time as a search criteria.
     * @param rawRecordStatusId, suspectedRecordStatusId, jobName
     * @return A list of adjustment as per search criteria.
     *          If no adjustment is found, this method returns an empty list.
     */

	@Query(value="select ad from Adjustment ad "
    		+ "where recordStatusId = :rawRecordStatusId ")
    public Page<Adjustment> getAdjustmentToProcess(Integer rawRecordStatusId, Pageable pageable);

	@Modifying
	@Transactional
	@Query(value = " insert into data.adjustment (record_type, client_id,client_account_number, account_id, adjustment_type, dt_adjustment, amt_adjustment, amt_principal, amt_interest, amt_latefee, record_status_id) "
			+ " values (:recordType, :clientId,:clientAccountNumber,:accountId, :adjustmentType, :adjustmentDate, :amtAdjustment, :amtPrincipal, :amtInterest, :amtLatefee, :recordStatusId) ", nativeQuery = true)
	public void insertIntoAdjustment(String recordType, Integer clientId,String clientAccountNumber, Long accountId, String adjustmentType, LocalDate adjustmentDate, Double amtAdjustment, Double amtPrincipal, Double amtInterest, Double amtLatefee, Integer recordStatusId );

	@Modifying
	@Transactional
	@Query(value = " insert into data.adjustment (record_type, client_id, client_account_number, account_id, adjustment_type, dt_adjustment, amt_adjustment, amt_principal, amt_interest, amt_latefee, amt_otherfee, amt_attorneyfee, record_status_id) "
			+ " values (:recordType, :clientId, :clientAccountNumber, :accountId, :adjustmentType, :adjustmentDate, :amtAdjustment, :amtPrincipal, :amtInterest, :amtLatefee, :amtOtherfee, :amtAttorneyfee, :recordStatusId) ", nativeQuery = true)
	public void insertIntoAdjustment(String recordType, Integer clientId,String clientAccountNumber, Long accountId, String adjustmentType, LocalDate adjustmentDate, 
			Double amtAdjustment, Double amtPrincipal, Double amtInterest, Double amtLatefee, Double amtOtherfee, Double amtAttorneyfee, Integer recordStatusId );

    @Query("select pr.partnerId from Partner pr join RecordStatus rs on pr.recordStatusId = rs.recordStatusId and rs.shortName = '"+ ConfRecordStatus.ENABLED + "' where pr.partnerId = :partnerId")
    Partner findPartner(Integer partnerId);
    
	@Query("select max(adjustmentId) from Adjustment")
	Long getAdjustmentId();
    
    @Query("select cl.shortName from Client cl join RecordStatus rs on cl.recordStatusId = rs.recordStatusId and rs.shortName = '"+ ConfRecordStatus.ENABLED + "' where cl.clientId = :clientId")
	String getClientCode(Integer clientId);

}