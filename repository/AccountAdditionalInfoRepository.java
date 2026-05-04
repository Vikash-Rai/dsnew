package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.entity.AccountAdditionalInfo;

@Repository
public interface AccountAdditionalInfoRepository extends JpaRepository<AccountAdditionalInfo, Long>{

	@Query(value="select accountAdditionalInfo from AccountAdditionalInfo accountAdditionalInfo "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<AccountAdditionalInfo> getAccountAdditionalInfoToProcess(Integer rawRecordStatusId, Pageable pageable);
	
	 @Modifying
	    @Transactional
	    @Query("""
	        UPDATE AccountAdditionalInfo a
	        SET a.preChargeOffClientBucket = :bucket
	        WHERE a.clientId = :clientId
	        AND a.clientAccountNumber = :accountNumber
	    """)
	    int updatePreChargeOffClientBucket(
	            @Param("clientId") Integer clientId,
	            @Param("accountNumber") String accountNumber,
	            @Param("bucket") String bucket
	    );

}