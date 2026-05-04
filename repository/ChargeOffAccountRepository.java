package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.equabli.collectprism.entity.ChargeOffAccount;

@Repository
public interface ChargeOffAccountRepository  extends JpaRepository<ChargeOffAccount, Long> {
	
	@Query(value="select chargeOff from ChargeOffAccount chargeOff "
    		+ "where recordStatusId = :rawRecordStatusId ")
	public Page<ChargeOffAccount> getChargeOffAccountToProcess(Integer rawRecordStatusId, Pageable pageable);

}
