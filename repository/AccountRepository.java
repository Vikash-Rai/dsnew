package com.equabli.collectprism.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.AccountEnrichmentDTO;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.LookUp;
import com.equabli.collectprism.entity.Product;
import com.equabli.collectprism.entity.SolAccountDTO;
import com.equabli.domain.entity.ConfRecordStatus;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * Finds accounts by using the record status id as a search criteria.
	 * 
	 * @param recordStatusId
	 * @return A list of accounts whose record status id is an exact match with the
	 *         given record status id. If no account is found, this method returns
	 *         an empty list.
	 */
	@Query(value = "select acc from Account acc " + "where acc.recordStatusId = :recordStatusId ")
	public Page<Account> findByRecordStatusId(Integer recordStatusId, Pageable pageable);

	@Query(value = "select acc from Account acc "
			+ "join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
			+ "where con.recordStatusId = :recordStatusId " + "and rs.shortName not in ('Disabled', 'Deleted') "
			+ "and con.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
			+ "(select min(dtmUtcUpdate) from Account))")
	public Page<Account> getConsumerToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

	@Query(value = "select acc from Account acc "
			+ "join Address add on acc.clientId = add.clientId and acc.clientAccountNumber = add.clientAccountNumber "
			+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
			+ "where add.recordStatusId = :recordStatusId " + "and rs.shortName not in ('Disabled', 'Deleted') "
			+ "and add.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
			+ "(select min(dtmUtcUpdate) from Account))")
	public Page<Account> getAddressToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

	@Query(value = "select acc from Account acc "
			+ "join Phone ph on acc.clientId = ph.clientId and acc.clientAccountNumber = ph.clientAccountNumber "
			+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
			+ "where ph.recordStatusId = :recordStatusId " + "and rs.shortName not in ('Disabled', 'Deleted') "
			+ "and ph.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
			+ "(select min(dtmUtcUpdate) from Account))")
	public Page<Account> getPhoneToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

	@Query(value = "select acc from Account acc "
			+ "join Email em on acc.clientId = em.clientId and acc.clientAccountNumber = em.clientAccountNumber "
			+ "join RecordStatus rs on acc.recordStatusId = rs.recordStatusId "
			+ "where em.recordStatusId = :recordStatusId " + "and rs.shortName not in ('Disabled', 'Deleted') "
			+ "and em.dtmUtcUpdate >= coalesce((select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName and dtmUtcAction not in (select max(dtmUtcAction) from BatchJobInstance where jobName = :jobName)), "
			+ "(select min(dtmUtcUpdate) from Account))")
	public Page<Account> getEmailToReprocess(Integer recordStatusId, String jobName, Pageable pageable);

	@Query(value = "select new Account(acc.accountId, acc.clientId, acc.clientAccountNumber, acc.originalAccountNumber) "
			+ "from Account acc "
			+ "where acc.errShortName = :shortName and (:clientId is null or acc.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and acc.recordStatusId = :recordStatusId ")
	public List<Account> getAccDetailsForSuspectedInv(String shortName, @Param("clientId") Integer clientId,
			Integer clientJobScheduleId, Integer recordStatusId,
			@Param("placementDateFrom") @Nullable Date placementDateFrom,
			@Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value = "select new Account(acc.accountId, acc.clientId, acc.clientAccountNumber, acc.originalAccountNumber) "
			+ "from ErrWarMessageAccount acc "
			+ "where acc.errwarType = :type and acc.errwarShortName like %:code and (:clientId is null or acc.clientId = :clientId) "
			+ "and (:clientJobScheduleId is null or acc.clientJobScheduleId = :clientJobScheduleId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date)  ")
	public List<Account> getAccDetailsForSuspectedInvNew(@Param("type") String type, @Param("code") String code,
			@Param("clientId") Integer clientId, @Param("clientJobScheduleId") Integer clientJobScheduleId,
			@Param("placementDateFrom") @Nullable Date placementDateFrom,
			@Param("placementDateTo") @Nullable Date placementDateTo);

	@Query(value = "select new Account(acc.accountId, cl.fullName, acc.clientAccountNumber, acc.originalAccountNumber, ewm.shortName, ewm.description) "
			+ "from Account acc " + "join ErrWarMessage ewm on acc.errShortName = ewm.shortName "
			+ "join Client cl on acc.clientId = cl.clientId " + "where (:clientId is null or acc.clientId = :clientId) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) >= coalesce(:placementDateFrom, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and coalesce(CAST(acc.dtmUtcCreate AS date), current_date) <= coalesce(:placementDateTo, CAST(acc.dtmUtcCreate AS date), current_date) "
			+ "and acc.recordStatusId = :recordStatusId " + "order by acc.accountId asc ")
	public List<Account> downloadSuspectedAccounts(@Param("clientId") Integer clientId, Integer recordStatusId,
			@Param("placementDateFrom") @Nullable Date placementDateFrom,
			@Param("placementDateTo") @Nullable Date placementDateTo);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :tlv and clientId = :clientId")
	int accountScrubRejectedForClient(Integer newRecordStatusId, Integer prevRecordStatusId, Integer tlv,
			@Param("clientId") Integer clientId);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :equabliLevelVal and clientId not in (:clientIds)")
	public int accountScrubRejectedAtEquabliLevelForRemainingClients(Integer newRecordStatusId,
			Integer prevRecordStatusId, Integer equabliLevelVal, List<Integer> clientIds);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :newRecordStatusId "
			+ "where recordStatusId = :prevRecordStatusId and DATE_PART('day', current_timestamp - dtmUtcUpdate) >= :equabliLevelVal")
	public int accountScrubRejectedAtEquabliLevelForAllClients(Integer newRecordStatusId, Integer prevRecordStatusId,
			Integer equabliLevelVal);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :recordStatusId, errShortName = :errShortName, errCodeJson = :errCodeJson "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int accountSuspectedByClientIdAndClientAccountNumber(Integer recordStatusId, String errShortName,
			Set<ErrWarJson> errCodeJson, @Param("clientId") Integer clientId, String clientAccountNumber,
			Integer enabledRecordStatusId);

	@Modifying
	@Transactional
	@Query("update Account set recordStatusId = :recordStatusId, errShortName = :errShortName, errCodeJson = :errCodeJson "
			+ "where clientId = :clientId and originalAccountNumber = :originalAccountNumber and originalLenderCreditor = :originalLenderCreditor and recordStatusId not in (:recordStatusId, :enabledRecordStatusId) ")
	int accountSuspectedByClientIdAndOriginalAccountNumber(Integer recordStatusId, String errShortName,
			Set<ErrWarJson> errCodeJson, @Param("clientId") Integer clientId, String originalAccountNumber,
			String originalLenderCreditor, Integer enabledRecordStatusId);

	@Query(value = "select new Account(acc.clientId, acc.clientAccountNumber, acc.currentLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance) "
			+ "from Account acc join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "group by acc.clientId, acc.clientAccountNumber, acc.currentLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance "
			+ "having count(*) > 1 AND (MAX(acc.dtmUtcUpdate) > :lastSuccessfulDateTime or MAX(con.dtmUtcUpdate) > :lastSuccessfulDateTime) ")
	public Page<Account> getClientAccountConsumerDeDup(LocalDateTime lastSuccessfulDateTime, Pageable pageable);

	@Query(value = "select new Account(acc.originalAccountNumber, acc.clientId, acc.originalLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance) "
			+ "from Account acc join Consumer con on acc.clientId = con.clientId and acc.clientAccountNumber = con.clientAccountNumber "
			+ "group by acc.clientId, acc.originalAccountNumber, acc.originalLenderCreditor, con.firstName, con.middleName, con.lastName, con.identificationNumber, acc.amtPreChargeOffBalance "
			+ "having count(*) > 1 AND (MAX(acc.dtmUtcUpdate) > :lastSuccessfulDateTime or MAX(con.dtmUtcUpdate) > :lastSuccessfulDateTime) ")
	public Page<Account> getOriginalAccountConsumerDeDup(LocalDateTime lastSuccessfulDateTime, Pageable pageable);

	@Modifying
	@Transactional
	@Query("update Account set queueId = :queueId, queueStatusId = :queueStatusId, queueReasonId = :queueReasonId, partnerId = null, partnerAssignmentDate = null "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber ")
	int updateQueueAccount(Integer queueId, Integer queueStatusId, Integer queueReasonId,
			@Param("clientId") Integer clientId, String clientAccountNumber);

	@Query("select new Account(acc.accountId, acc.clientId, acc.partnerId, acc.partnerType, acc.clientAccountNumber, acc.originalAccountNumber, acc.currentLenderCreditor,"
			+ " acc.originalLenderCreditor, acc.productId, acc.productSubTypeId, acc.productSubTypeCount, acc.clientJobScheduleId, acc.originalAccountOpenDate,"
			+ " acc.assignedDate, acc.delinquencyDate, acc.chargeOffDate, acc.lastPaymentDate, acc.lastPurchaseDate, acc.lastCashAdvanceDate,"
			+ " acc.lastBalanceTransferDate, acc.solDate, acc.clientSolDate, acc.equabliSolDate,"
			+ " acc.customerType, acc.debtType, acc.portfolioCode, acc.originalAccountApplicationType, acc.amtLastPayment, acc.amtLastPurchase, acc.amtLastCashAdvance, acc.amtLastBalanceTransfer, acc.amtPreChargeOffBalance,"
			+ " acc.amtPreChargeOffPrinciple, acc.amtPreChargeOffInterest, acc.amtPreChargeOffFees, acc.amtPostChargeOffInterest, acc.pctPostChargeOffInterest,"
			+ " acc.amtPostChargeOffFee, acc.pctPostChargeOffFee, acc.amtPostChargeOffPayment, acc.amtPostChargeOffCredit, acc.amtAssigned, acc.amtPrincipalAssigned,"
			+ " acc.amtInterestAssigned, acc.amtLatefeeAssigned, acc.amtOtherfeeAssigned, acc.amtCourtcostAssigned, acc.amtAttorneyfeeAssigned, acc.productAffinity,"
			+ " acc.currentbalanceDate, acc.amtCurrentbalance, acc.amtPrincipalCurrentbalance, acc.amtInterestCurrentbalance, acc.amtLatefeeCurrentbalance,"
			+ " acc.amtOtherfeeCurrentbalance, acc.amtCourtcostCurrentbalance, acc.amtAttorneyfeeCurrentbalance, acc.saleReviewStatus, acc.partnerAssignmentDate)"
			+ " from Account acc where acc.accountId=:accountId ")
	Account getAccountByAccountId(Long accountId);

	@Query(value = "select new Product(pr.productId, pr.shortName) from Product pr join RecordStatus rs on pr.recordStatusId = rs.recordStatusId and rs.shortName = '"
			+ ConfRecordStatus.ENABLED + "'  where pr.productId = :productId ")
	Product getProduct(Integer productId);

	@Query(value = "select count(prd.productsubtype_id) from conf.productsubtype prd inner join conf.record_status rs on rs.record_status_id = prd.record_status_id and rs.short_name = '"
			+ ConfRecordStatus.ENABLED
			+ "' where prd.product_id = :productId and prd.subproduct_id = :productSubTypeId ", nativeQuery = true)
	Integer productSubTypeCount(Integer productId, Integer productSubTypeId);

	@Query(value = "select lu.lookupId from LookUp lu inner join LookUpGroup lug on lu.lookupGroupId = lug.lookupGroupId "
			+ " join RecordStatus rs on lu.recordStatusId = rs.recordStatusId and rs.shortName = '"
			+ ConfRecordStatus.ENABLED + "' " + " where lug.keyvalue = :keyValue and lu.keycode = :debtType ")
	LookUp getLookUpByKeyValue(String keyValue, String debtType);

	@Query(value = "select count(acc.account_id) from data.account acc where acc.client_id = :clientId and acc.original_account_number = :originalAccountNumber and acc.original_lender_creditor = :originalLenderCreditor", nativeQuery = true)
	Integer originalAccountNoDeDup(@Param("clientId") Integer clientId, String originalAccountNumber,
			String originalLenderCreditor);

	@Modifying
	@Transactional
	@Query("update Account set delinquencyDate = null, firstDelinquencyDate = null "
			+ "where clientId = :clientId and clientAccountNumber = :clientAccountNumber ")
	int updateAccountDelinquencyDate(@Param("clientId") Integer clientId, String clientAccountNumber);

	@Query("select acc  from Account acc where acc.clientAccountNumber=:clientAccountNumber ")
	Account getAccountByClientAccountNumber(String clientAccountNumber);

	@Query(value = """
			    SELECT a.client_account_number
			    FROM data.account a
			    WHERE a.record_status_id = 1
			      AND NOT EXISTS (
			          SELECT 1
			          FROM data.consumer c
			          WHERE c.client_account_number = a.client_account_number and c.client_id = a.client_id
			            AND c.contact_type = 1
			      )
			      FOR UPDATE SKIP LOCKED
			    LIMIT :limit
			""", nativeQuery = true)
	List<String> findAccountsWithoutPrimary(@Param("limit") int limit);

	@Modifying
	@Query(value = "UPDATE data.account SET record_status_id = :recordStatusId WHERE client_account_number IN (:accList)  ", nativeQuery = true)
	int updateAccountToSuspectedByList(@Param("accList") List<String> accList, Integer recordStatusId);

	@Modifying
	@Query(value = "UPDATE data.consumer SET record_status_id = :recordStatusId WHERE client_account_number IN (:accList) ", nativeQuery = true)
	int updateConsumerToSuspectedByList(@Param("accList") List<String> accList, Integer recordStatusId);

	@Modifying
	@Query(value = "UPDATE data.address SET record_status_id = :recordStatusId WHERE client_account_number IN (:accList) ", nativeQuery = true)
	int updateAddressToSuspectedByList(@Param("accList") List<String> accList, Integer recordStatusId);

	@Modifying
	@Query(value = "UPDATE data.phone SET record_status_id = :recordStatusId WHERE client_account_number IN (:accList)  ", nativeQuery = true)
	int updatePhoneToSuspectedByList(@Param("accList") List<String> accList, Integer recordStatusId);

	@Modifying
	@Query(value = "UPDATE data.email SET record_status_id = :recordStatusId WHERE client_account_number IN (:accList) ", nativeQuery = true)
	int updateEmailToSuspectedByList(@Param("accList") List<String> accList, Integer recordStatusId);

	@Query(value = """
			SELECT a.account_id FROM data.account a 
			WHERE a.record_status_id = :rawStatus
			ORDER BY a.account_id LIMIT :batchSize FOR UPDATE SKIP LOCKED
			""", nativeQuery = true)
	List<Long> claimRawAccountIds(@Param("rawStatus") Integer rawStatus, @Param("batchSize") int batchSize);

	/**
	 * Bulk status update — used for RAW -> INPROGRESS
	 * INPROGRESS -> FAILED, and INPROGRESS -> RAW (recovery).
	 */
	@Modifying
	@Query("""
			    UPDATE Account a SET a.recordStatusId = :status
			    WHERE a.accountId IN :ids
			""")
	void updateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

	/**
	 * Recovery query dtm_utc_update has updatable=false in entity, so Hibernate
	 * never writes it on updates. The recoverStaleRecords query using dtmUtcUpdate
	 * < threshold would compare against the ORIGINAL INSERT time, not when the
	 * record was claimed as INPROGRESS. This would incorrectly requeue ALL records
	 * that were inserted more than 30 min ago.
	 *
	 * SHORT-TERM FIX (no schema change): Use dtmUtcCreate as a proxy. Records
	 * inserted more than (recoveryMinutes + expected_processing_time) ago that are
	 * still INPROGRESS are genuinely stuck. This is slightly conservative but safe.
	 *
	 * PROPER FIX (recommended): Add a new column dtm_enrichment_start to
	 * data.account. Set it when marking INPROGRESS. Use it here. This requires a DB
	 * migration (ALTER TABLE ADD COLUMN).
	 */
	@Modifying
	@Query("""
			UPDATE Account a SET a.recordStatusId = :rawStatus
			WHERE a.recordStatusId = :inProgressStatus AND a.dtmUtcCreate < :threshold
			""")
	int recoverStaleRecords(@Param("inProgressStatus") Integer inProgressStatus, @Param("rawStatus") Integer rawStatus,
			@Param("threshold") LocalDateTime threshold);

	/**
	 * Optimized partial fetch for Ledger and Current Balance enrichment. Loads ONLY
	 * the specific fields needed to generate the Ledger and Balance.
	 */
	@Query("""
			    SELECT a FROM Account a
			    WHERE a.accountId IN :ids
			""")
	List<Account> findAccountsWithAllRelations(@Param("ids") List<Long> ids);

	@Query(value = """
			    SELECT *
			    FROM data.account acc
			    WHERE acc.record_status_id = :recordStatusId
			    FOR UPDATE SKIP LOCKED
			    LIMIT :limit
			""", nativeQuery = true)
	List<Account> getAccountByRecordStatusIdLimit(Integer recordStatusId, @Param("limit") int limit);

//	@Query(value = """
//						   SELECT DISTINCT ON (a.account_id)
//
//			a.account_id,
//			a.client_id,
//			a.product_id,
//			a.partner_id,
//
//			cl.short_name AS clientName,
//			pr.short_name AS partnerName,
//			prd.debtcategory_id AS debtCategoryId,
//			addr.state_code AS stateCode,
//			cs.country_state_id AS countryStateId,
//			csol.client_statutes_of_limitation_id AS solId,
//			CASE
//			WHEN addr.is_primary = true and addr.state_code IS NOT NULL THEN sol.sol_month
//			WHEN addr.is_primary = false THEN sm.min_sol_month
//			ELSE 0
//			END AS solMonth,
//			CASE
//			WHEN addr.is_primary = true and addr.state_code IS NOT NULL THEN csol.sol_day
//			ELSE 0
//			END AS solDay,
//			cav.custom_appconfig_value_id AS cycleDay
//
//			FROM data.account a
//
//			LEFT JOIN data.consumer c
//			ON c.client_id = a.client_id
//			AND c.client_account_number = a.client_account_number
//			and c.contact_type = 1
//
//			LEFT JOIN data.address addr
//			ON addr.consumer_id = c.consumer_id and
//			addr.client_id = a.client_id
//			AND c.client_account_number = addr.client_account_number
//
//			LEFT JOIN conf.country_state cs
//			ON cs.state_code = addr.state_code
//
//			LEFT JOIN conf.custom_appconfig_value cav
//							    ON cav.client_id = a.client_id
//							   AND cav.reference_id = a.product_id
//							   AND cav.reference_type = 'PRODUCT'
//
//					LEFT JOIN conf.statutes_of_limitation sol
//				  ON sol.country_state_id = cs.country_state_id
//				 AND sol.debtcategory_id = prd.debtcategory_id
//				 AND sol.record_status_id = conf.df_record_status('Enabled')
//
//
//				LEFT JOIN conf.client_statutes_of_limitation csol
//				  ON csol.country_state_id = cs.country_state_id
//				 AND csol.debtcategory_id = prd.debtcategory_id
//				 AND csol.client_id = a.client_id
//				 AND csol.record_status_id = conf.df_record_status('Enabled')
//
//				 LEFT JOIN (
//			SELECT
//			debtcategory_id,
//			MIN(sol_month) AS min_sol_month
//			FROM conf.statutes_of_limitation
//			WHERE record_status_id = conf.df_record_status('Enabled')
//			GROUP BY debtcategory_id
//			) sm
//			ON sm.debtcategory_id = prd.debtcategory_id
//
//							WHERE a.account_id IN (:accountIds)
//												""", nativeQuery = true)
//	List<SolAccountDTO> fetchSolAccounts(@Param("accountIds") List<Long> accountIds);

	@Modifying
	@Transactional
	@Query(value = """
			UPDATE data.account
			SET
			dt_statute = :solDate,
			dt_equabli_statute = :equabliSolDate,
			record_status_id = :recordStatusId,
			dt_currentbalance = :currentBalanceDate,
			amt_currentbalance = :amtCurrentbalance,
			amt_principal_currentbalance = :amtPrincipalCurrentbalance,
			amt_interest_currentbalance = :amtInterestCurrentbalance,
			amt_latefee_currentbalance = :amtLatefeeCurrentbalance,
			amt_otherfee_currentbalance = :amtOtherfeeCurrentbalance,
			amt_courtcost_currentbalance = :amtCourtcostCurrentbalance,
			amt_attorneyfee_currentbalance = :amtAttorneyfeeCurrentbalance
			WHERE account_id = :accountId
			""", nativeQuery = true)
	int updateAccount(Long accountId, LocalDate solDate, LocalDate equabliSolDate, Integer recordStatusId,
			LocalDate currentBalanceDate, Double amtCurrentbalance, Double amtPrincipalCurrentbalance,
			Double amtInterestCurrentbalance, Double amtLatefeeCurrentbalance, Double amtOtherfeeCurrentbalance,
			Double amtCourtcostCurrentbalance, Double amtAttorneyfeeCurrentbalance);

	@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
	@Query(value = """
            SELECT DISTINCT ON (a.account_id)
                a.account_id                                   AS accountId,
                addr.state_code                                AS stateCode,
                cs.country_state_id                            AS countryStateId,

                CASE
                    WHEN addr.address_id IS NULL
                        THEN NULL
                    WHEN addr.is_primary = true
                         AND addr.state_code IS NOT NULL
                        THEN sol.sol_month
                    WHEN addr.is_primary = true
                         AND addr.state_code IS NULL
                        THEN NULL
                    ELSE sm.min_sol_month
                END                                            AS solMonth,
 
                CASE
                    WHEN addr.address_id IS NULL
                        THEN NULL
                    WHEN addr.is_primary = true
                         AND addr.state_code IS NOT NULL
                        THEN csol.sol_day
                    ELSE NULL
                END                                            AS solDay
 
            FROM data.account a
 
            /* ── Primary consumer only (contact_type = 1) ── */
            LEFT JOIN data.consumer c
                   ON c.client_id             = a.client_id
                  AND c.client_account_number = a.client_account_number
                  AND c.contact_type          = 1
 
            /* ── Address of that consumer ── */
            LEFT JOIN data.address addr
                   ON addr.consumer_id           = c.consumer_id
                  AND addr.client_id             = a.client_id
                  AND addr.client_account_number = c.client_account_number
 
            /* ── State lookup ── */
            LEFT JOIN conf.country_state cs
                   ON cs.state_code = addr.state_code
 
            /* ── Product: JOIN key only — debtcategory_id needed for sol/csol/sm.
                  debtCategoryId is NOT selected here; it is already in
                  ctx.productDebtCategoryId via Caffeine (getProductDebtCategoryId). ── */
            LEFT JOIN conf.product prd
                   ON prd.product_id       = a.product_id
                  AND prd.record_status_id = conf.df_record_status('Enabled')
 
            /* ── National SOL table ── */
            LEFT JOIN conf.statutes_of_limitation sol
                   ON sol.country_state_id  = cs.country_state_id
                  AND sol.debtcategory_id   = prd.debtcategory_id
                  AND sol.record_status_id  = conf.df_record_status('Enabled')
 
            /* ── Client-specific SOL override ── */
            LEFT JOIN conf.client_statutes_of_limitation csol
                   ON csol.country_state_id  = cs.country_state_id
                  AND csol.debtcategory_id   = prd.debtcategory_id
                  AND csol.client_id         = a.client_id
                  AND csol.record_status_id  = conf.df_record_status('Enabled')
 
            /* ── Min SOL month fallback for non-primary-address accounts ──
                  Pre-aggregated once per query execution, not per row. ── */
            LEFT JOIN (
                SELECT debtcategory_id,
                       MIN(sol_month) AS min_sol_month
                FROM   conf.statutes_of_limitation
                WHERE  record_status_id = conf.df_record_status('Enabled')
                GROUP  BY debtcategory_id
            ) sm ON sm.debtcategory_id = prd.debtcategory_id
 
            WHERE a.account_id IN (:accountIds)
 
            /* ── Deterministic row selection for DISTINCT ON ──────────────────
               PostgreSQL DISTINCT ON keeps the FIRST row per (account_id) after
               ORDER BY. This ordering guarantees:
                 1st: primary address with known state_code  → correct SOL lookup
                 2nd: primary address with null state_code   → NULL result (safe)
                 3rd: non-primary address                    → min_sol_month fallback
                 4th: no address at all                      → all addr cols null
               addr.address_id DESC as final tiebreaker ensures newest address wins
               when multiple rows are tied on is_primary + state_code presence.
            ── */
            ORDER BY a.account_id,
                     addr.is_primary     DESC NULLS LAST,
                     addr.state_code     NULLS LAST,
                     addr.address_id     DESC NULLS LAST
            """, nativeQuery = true)
	List<SolAccountDTO> fetchSolAccounts(@Param("accountIds") List<Long> accountIds);

}