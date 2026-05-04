package com.equabli.collectprism.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.collectprism.entity.Client;
import com.equabli.collectprism.entity.CountryState;
import com.equabli.domain.entity.ConfRecordStatus;

@Repository
public interface BankruptcyRepository extends JpaRepository<Bankruptcy, Long> {

	@Query(value="select bankruptcy from Bankruptcy bankruptcy "
    		+ "where bankruptcy.recordStatusId = :rawRecordStatusId ")
    public Page<Bankruptcy> getBankruptcyToProcess(Integer rawRecordStatusId, Pageable pageable);

	@Query(value = "select new Client(cl.clientId, cl.shortName) from Client cl join RecordStatus rs on cl.recordStatusId = rs.recordStatusId and rs.shortName = '"+ ConfRecordStatus.ENABLED + "' " +
			" where cl.clientId = :clientId")
	Client findClientById(Integer clientId);

	@Query(value = "select new CountryState(cs.countryStateId) "
			+ "from CountryState cs "
			+ "where lower(cs.stateCode) = lower(:stateCode)")
	CountryState findCountryStateByStateCode(String stateCode);

	@Query("SELECT new Bankruptcy(b.bankruptcyId) " +
			"FROM Bankruptcy b JOIN RecordStatus rs ON b.recordStatusId = rs.recordStatusId AND rs.shortName = 'Enabled' " +
			" WHERE b.clientId = :clientId and b.clientAccountNumber = :clientAccountNumber and b.clientBankruptcyId = :clientBankruptcyId")
	Bankruptcy getDataBankruptcy(Integer clientId, String clientAccountNumber, Long clientBankruptcyId);
}