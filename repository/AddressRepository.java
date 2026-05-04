package com.equabli.collectprism.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	@Modifying
	@Transactional
	@Query("update Address set recordStatusId = :recordStatusId "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int suspectAddressByClientIdAndClientAccountNumber(Integer recordStatusId, Integer clientId, String clientAccountNumber, Integer enabledRecordStatusId);

	@Modifying
	@Transactional
	@Query("update Address set isPrimary = false "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and clientConsumerNumber = :clientConsumerNumber and addressId != :addressId and isPrimary = true ")
	int updateAddressIsPrimaryStatus(Integer clientId, String clientAccountNumber, Long clientConsumerNumber, Long addressId);

	@Query(value="select new Address(add.addressId, add.clientId, add.clientAccountNumber) "
			+ "from Address add "
			+ "join Account acc on acc.clientId = add.clientId and acc.clientAccountNumber = add.clientAccountNumber "
			+ "where add.errShortName = :shortName and (:clientId is null or add.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(add.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(add.dtmUtcCreate AS date), current_date) "
			+ "and add.recordStatusId = :recordStatusId ")
	public List<Address> getAddressDetailsForSuspectedInv(String shortName, Integer clientId, Integer clientJobScheduleId, Integer recordStatusId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);
	
	@Query(value="select new Address(add.addressId, add.clientId, add.clientAccountNumber) "
			+ "from ErrWarMessageAddress add "
			+ "where add.errwarType = :type and add.errwarShortName like %:code and (:clientId is null or add.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or add.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(add.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(add.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(add.dtmUtcCreate AS date), current_date) ")
	public List<Address> getAddressDetailsForSuspectedInvNew(@Param("type") String type,@Param("code") String code,@Param("clientId") Integer clientId,@Param("clientJobScheduleId") Integer clientJobScheduleId, @Param("placementDateFrom") @Nullable Date placementDateFrom, @Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value = "SELECT  data.update_address_account_id()", nativeQuery = true)
	void updateAddress();

}