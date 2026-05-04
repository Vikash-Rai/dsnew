package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@DynamicUpdate
@Table(schema = "data", name = "account")
@Convert(attributeName = "json", converter = JsonType.class)
public class Account implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_id", updatable = false)
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"
			+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "partner_id")
	private Integer partnerId;

	@Formula(value = "(select mcp.partner_type from conf.map_clientpartner mcp join conf.partner pr on mcp.partner_id = pr.partner_id where mcp.client_id = client_id and pr.partner_id = partner_id and pr.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') and mcp.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "'))")
	private String partnerType;

	@Column(name = "client_account_number", updatable = false)
	private String clientAccountNumber;

	@Column(name = "original_account_number", updatable = false)
	private String originalAccountNumber;

	@Column(name = "current_lender_creditor")
	private String currentLenderCreditor;

	@Column(name = "original_lender_creditor")
	private String originalLenderCreditor;

	@Column(name = "initial_lender_creditor")
	private String initialLenderCreditor;

	@Column(name = "product_id")
	private Integer productId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select prd.product_id from conf.product prd where prd.product_id = product_id and prd.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private Product product;

	@Column(name = "productsubtype_id")
	private Integer productSubTypeId;

	@Formula(value = "(select count(prd.productsubtype_id) from conf.productsubtype prd where prd.product_id = product_id and prd.subproduct_id = productsubtype_id and prd.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private Integer productSubTypeCount;

	@Column(name = "client_job_schedule_id")
	private Integer clientJobScheduleId;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_original_account_open")
	private LocalDate originalAccountOpenDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_assigned")
	private LocalDate assignedDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_delinquency")
	private LocalDate delinquencyDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_first_delinquency")
	private LocalDate firstDelinquencyDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_charge_off")
	private LocalDate chargeOffDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_last_payment")
	private LocalDate lastPaymentDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_last_purchase")
	private LocalDate lastPurchaseDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_last_cash_advance")
	private LocalDate lastCashAdvanceDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_last_balance_transfer")
	private LocalDate lastBalanceTransferDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_statute")
	private LocalDate solDate;

	@Column(name = "dt_client_statute")
	private LocalDate clientSolDate;

	@Column(name = "dt_equabli_statute")
	private LocalDate equabliSolDate;

	@Column(name = "queue_id")
	private Integer queueId;

	@Column(name = "queuestatus_id")
	private Integer queueStatusId;

	@Column(name = "queuereason_id")
	private Integer queueReasonId;

	@Column(name = "customer_type")
	private String customerType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'customer_type' and lu.keycode = customer_type and lu.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private LookUp customerTypeLookUp;

	@Column(name = "debt_type")
	private String debtType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'debt_type' and lu.keycode = debt_type and lu.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private LookUp debtTypeLookUp;

	@Column(name = "portfolio_code")
	private String portfolioCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'portfolio_code' and lu.keycode = portfolio_code and lu.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private LookUp portfolioCodeLookup;

	@Column(name = "original_account_application_type")
	private String originalAccountApplicationType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'account_application_type' and lu.keycode = original_account_application_type and lu.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private LookUp originalAccountApplicationTypeLookUp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cav.custom_appconfig_value_id from conf.custom_appconfig_value cav join conf.appconfig_name an on cav.appconfig_name_id = an.appconfig_name_id and an.short_name = 'CYCLE_DAYS' where cav.client_id = client_id and cav.reference_id = product_id and cav.reference_type ='PRODUCT')")
	private CustomAppConfigValue customAppConfigValue;

	@Formula(value = "(select confval.configured_value from conf.appconfig_value confval inner join conf.appconfig_name confname on confname.appconfig_name_id = confval.appconfig_name_id where confval.configured_for = '"
			+ CommonConstants.RECORD_SOURCE_CLIENT
			+ "' and confname.short_name = 'MERCHANT_NAME' and confval.client_id = client_id and confval.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') and confname.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private String merchantName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "err_short_name")
	private String errShortName;

	@Column(name = "debt_stage")
	private String debtStage;

	@Column(name = "debt_status")
	private String debtStatus;

	@Formula(value = "(select count(acc.account_id) from data.account acc where acc.client_id = client_id and acc.original_account_number = original_account_number and acc.original_lender_creditor = original_lender_creditor)")
	private Integer originalAccountNoDeDup;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
			@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)),
			@JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)) })
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Consumer> consumer;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "amt_last_payment")
	private Double amtLastPayment;

	@Column(name = "amt_last_purchase")
	private Double amtLastPurchase;

	@Column(name = "amt_last_cash_advance")
	private Double amtLastCashAdvance;

	@Column(name = "amt_last_balance_transfer")
	private Double amtLastBalanceTransfer;

	@Column(name = "amt_pre_charge_off_balance")
	private Double amtPreChargeOffBalance;

	@Column(name = "amt_pre_charge_off_principle")
	private Double amtPreChargeOffPrinciple;

	@Column(name = "amt_pre_charge_off_interest")
	private Double amtPreChargeOffInterest;

	@Column(name = "amt_pre_charge_off_fee")
	private Double amtPreChargeOffFees;

	@Column(name = "amt_post_charge_off_interest")
	private Double amtPostChargeOffInterest;

	@Column(name = "pct_post_charge_off_interest")
	private Double pctPostChargeOffInterest;

	@Column(name = "amt_post_charge_off_fees")
	private Double amtPostChargeOffFee;

	@Column(name = "pct_post_charge_off_feerate")
	private Double pctPostChargeOffFee;

	@Column(name = "amt_post_charge_off_payment")
	private Double amtPostChargeOffPayment;

	@Column(name = "amt_post_charge_off_credit")
	private Double amtPostChargeOffCredit;

	@Column(name = "amt_assigned")
	private Double amtAssigned;

	@Column(name = "amt_principal_assigned")
	private Double amtPrincipalAssigned;

	@Column(name = "amt_interest_assigned")
	private Double amtInterestAssigned;

	@Column(name = "amt_latefee_assigned")
	private Double amtLatefeeAssigned;

	@Column(name = "amt_otherfee_assigned")
	private Double amtOtherfeeAssigned;

	@Column(name = "amt_courtcost_assigned")
	private Double amtCourtcostAssigned;

	@Column(name = "amt_attorneyfee_assigned")
	private Double amtAttorneyfeeAssigned;

	@Column(name = "product_affinity")
	private String productAffinity;

	@Column(name = "dt_currentbalance")
	private LocalDate currentbalanceDate;

	@Column(name = "amt_currentbalance")
	private Double amtCurrentbalance;

	@Column(name = "amt_principal_currentbalance")
	private Double amtPrincipalCurrentbalance;

	@Column(name = "amt_interest_currentbalance")
	private Double amtInterestCurrentbalance;

	@Column(name = "amt_latefee_currentbalance")
	private Double amtLatefeeCurrentbalance;

	@Column(name = "amt_otherfee_currentbalance")
	private Double amtOtherfeeCurrentbalance;

	@Column(name = "amt_courtcost_currentbalance")
	private Double amtCourtcostCurrentbalance;

	@Column(name = "amt_attorneyfee_currentbalance")
	private Double amtAttorneyfeeCurrentbalance;

	@Column(name = "sale_review_status")
	private String saleReviewStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'sale_review_status' and lu.keycode = sale_review_status and lu.record_status_id = conf.df_record_status('"
			+ ConfRecordStatus.ENABLED + "') )")
	private LookUp saleReviewStatusLookUp;

	@Formula(value = "(select acc.auto_account_info_id from data.auto_account_info acc where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long autoAccountInfoIds;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Column(name = "dt_partner_assignment")
	private LocalDate partnerAssignmentDate;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "record_source_id")
	private Integer recordSourceId;

	@Column(name = "app_id")
	private Integer appId;
		
	@Column(name = "is_archived")
	private boolean isArchive;

	@Column(name = "dtm_utc_archived")
	private LocalDateTime dtmUtcArchieve;
	
	@Column(name = "subpool_id")
	private Integer subpoolId;

	@Column(name = "additional_account_number")
	private String additionalAccountNumber;

	@Column(name = "dt_debt_due_date")
	private LocalDate debtDueDate;

	@Transient
	private Date placementDateFrom;

	@Transient
	private Date placementDateTo;

	@Transient
	private String entity;

	@Transient
	private List<ScrubWarning> scrubWarnings;

	@Transient
	private String shortName;

	@Transient
	private String description;

	@Transient
	private Ledger ledger;

	// private String preChargeOffClientBucket;


//	public String getPreChargeOffClientBucket() {
//		return preChargeOffClientBucket;
//	}
//
//	public void setPreChargeOffClientBucket(String preChargeOffClientBucket) {
//		this.preChargeOffClientBucket = preChargeOffClientBucket;
//	}


	public Account(Long accountId, Double amtOtherfeeCurrentbalance, Double amtInterestCurrentbalance,
			Double amtPrincipalCurrentbalance, Double amtCurrentbalance) {
		this.accountId = accountId;
		this.amtCurrentbalance = amtCurrentbalance;
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Account(Long accountId, Double amtOtherfeeCurrentbalance, Double amtInterestCurrentbalance,
			Double amtPrincipalCurrentbalance, Double amtCurrentbalance, LocalDate chargeOffDate,
			Double amtPreChargeOffPrinciple, Double amtPreChargeOffInterest, Double amtPreChargeOffFees,
			Double amtLatefeeCurrentbalance, Double amtCourtcostCurrentbalance, Double amtAttorneyfeeCurrentbalance,
			Double amtPreChargeOffBalance, Double amtPostChargeOffInterest, Double amtPostChargeOffFee,
			Double amtPostChargeOffPayment, Double amtPostChargeOffCredit) {
		this.accountId = accountId;
		this.amtCurrentbalance = amtCurrentbalance;
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
		this.chargeOffDate = chargeOffDate;
		this.amtPreChargeOffPrinciple = amtPreChargeOffPrinciple;
		this.amtPreChargeOffInterest = amtPreChargeOffInterest;
		this.amtPreChargeOffFees = amtPreChargeOffFees;
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
		this.amtCourtcostCurrentbalance = amtCourtcostCurrentbalance;
		this.amtAttorneyfeeCurrentbalance = amtAttorneyfeeCurrentbalance;
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
		this.amtPostChargeOffInterest = amtPostChargeOffInterest;
		this.amtPostChargeOffFee = amtPostChargeOffFee;
		this.amtPostChargeOffPayment = amtPostChargeOffPayment;
		this.amtPostChargeOffCredit = amtPostChargeOffCredit;
	}

	public Account(Long accountId, Integer clientId, Integer partnerId, String partnerType, String clientAccountNumber,
			String originalAccountNumber, String currentLenderCreditor, String originalLenderCreditor,
			Integer productId, Integer productSubTypeId, Integer productSubTypeCount, Integer clientJobScheduleId,
			LocalDate originalAccountOpenDate, LocalDate assignedDate, LocalDate delinquencyDate,
			LocalDate chargeOffDate, LocalDate lastPaymentDate, LocalDate lastPurchaseDate,
			LocalDate lastCashAdvanceDate, LocalDate lastBalanceTransferDate, LocalDate solDate,
			LocalDate clientSolDate, LocalDate equabliSolDate, String customerType, String debtType,
			String portfolioCode, String originalAccountApplicationType, Double amtLastPayment, Double amtLastPurchase,
			Double amtLastCashAdvance, Double amtLastBalanceTransfer, Double amtPreChargeOffBalance,
			Double amtPreChargeOffPrinciple, Double amtPreChargeOffInterest, Double amtPreChargeOffFees,
			Double amtPostChargeOffInterest, Double pctPostChargeOffInterest, Double amtPostChargeOffFee,
			Double pctPostChargeOffFee, Double amtPostChargeOffPayment, Double amtPostChargeOffCredit,
			Double amtAssigned, Double amtPrincipalAssigned, Double amtInterestAssigned, Double amtLatefeeAssigned,
			Double amtOtherfeeAssigned, Double amtCourtcostAssigned, Double amtAttorneyfeeAssigned,
			String productAffinity, LocalDate currentbalanceDate, Double amtCurrentbalance,
			Double amtPrincipalCurrentbalance, Double amtInterestCurrentbalance, Double amtLatefeeCurrentbalance,
			Double amtOtherfeeCurrentbalance, Double amtCourtcostCurrentbalance, Double amtAttorneyfeeCurrentbalance,
			String saleReviewStatus, LocalDate partnerAssignmentDate) {
		this.accountId = accountId;
		this.clientId = clientId;
		this.partnerId = partnerId;
		this.partnerType = partnerType;
		this.clientAccountNumber = clientAccountNumber;
		this.originalAccountNumber = originalAccountNumber;
		this.currentLenderCreditor = currentLenderCreditor;
		this.originalLenderCreditor = originalLenderCreditor;
		this.productId = productId;
		this.productSubTypeId = productSubTypeId;
		this.productSubTypeCount = productSubTypeCount;
		this.clientJobScheduleId = clientJobScheduleId;
		this.originalAccountOpenDate = originalAccountOpenDate;
		this.assignedDate = assignedDate;
		this.delinquencyDate = delinquencyDate;
		this.chargeOffDate = chargeOffDate;
		this.lastPaymentDate = lastPaymentDate;
		this.lastPurchaseDate = lastPurchaseDate;
		this.lastCashAdvanceDate = lastCashAdvanceDate;
		this.lastBalanceTransferDate = lastBalanceTransferDate;
		this.solDate = solDate;
		this.clientSolDate = clientSolDate;
		this.equabliSolDate = equabliSolDate;
		this.customerType = customerType;
		this.debtType = debtType;
		this.portfolioCode = portfolioCode;
		this.originalAccountApplicationType = originalAccountApplicationType;
		this.amtLastPayment = amtLastPayment;
		this.amtLastPurchase = amtLastPurchase;
		this.amtLastCashAdvance = amtLastCashAdvance;
		this.amtLastBalanceTransfer = amtLastBalanceTransfer;
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
		this.amtPreChargeOffPrinciple = amtPreChargeOffPrinciple;
		this.amtPreChargeOffInterest = amtPreChargeOffInterest;
		this.amtPreChargeOffFees = amtPreChargeOffFees;
		this.amtPostChargeOffInterest = amtPostChargeOffInterest;
		this.pctPostChargeOffInterest = pctPostChargeOffInterest;
		this.amtPostChargeOffFee = amtPostChargeOffFee;
		this.pctPostChargeOffFee = pctPostChargeOffFee;
		this.amtPostChargeOffPayment = amtPostChargeOffPayment;
		this.amtPostChargeOffCredit = amtPostChargeOffCredit;
		this.amtAssigned = amtAssigned;
		this.amtPrincipalAssigned = amtPrincipalAssigned;
		this.amtInterestAssigned = amtInterestAssigned;
		this.amtLatefeeAssigned = amtLatefeeAssigned;
		this.amtOtherfeeAssigned = amtOtherfeeAssigned;
		this.amtCourtcostAssigned = amtCourtcostAssigned;
		this.amtAttorneyfeeAssigned = amtAttorneyfeeAssigned;
		this.productAffinity = productAffinity;
		this.currentbalanceDate = currentbalanceDate;
		this.amtCurrentbalance = amtCurrentbalance;
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
		this.amtCourtcostCurrentbalance = amtCourtcostCurrentbalance;
		this.amtAttorneyfeeCurrentbalance = amtAttorneyfeeCurrentbalance;
		this.saleReviewStatus = saleReviewStatus;
		this.partnerAssignmentDate = partnerAssignmentDate;
	}

	public Integer getRecordSourceId() {
		return recordSourceId;
	}

	public void setRecordSourceId(Integer recordSourceId) {
		this.recordSourceId = recordSourceId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getDebtStage() {
		return debtStage;
	}

	public void setDebtStage(String debtStage) {
		this.debtStage = debtStage;
	}

	public String getDebtStatus() {
		return debtStatus;
	}

	public void setDebtStatus(String debtStatus) {
		this.debtStatus = debtStatus;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}
	

	public Integer getSubpoolId() {
		return subpoolId;
	}

	public void setSubpoolId(Integer subpoolId) {
		this.subpoolId = subpoolId;
	}

	public String getAdditionalAccountNumber() {
		return additionalAccountNumber;
	}

	public void setAdditionalAccountNumber(String additionalAccountNumber) {
		this.additionalAccountNumber = additionalAccountNumber;
	}

	public LocalDate getDebtDueDate() {
		return debtDueDate;
	}

	public void setDebtDueDate(LocalDate debtDueDate) {
		this.debtDueDate = debtDueDate;
	}

	public LocalDateTime getDtmUtcArchieve() {
		return dtmUtcArchieve;
	}

	public void setDtmUtcArchieve(LocalDateTime dtmUtcArchieve) {
		this.dtmUtcArchieve = dtmUtcArchieve;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public String getOriginalAccountNumber() {
		return originalAccountNumber;
	}

	public void setOriginalAccountNumber(String originalAccountNumber) {
		this.originalAccountNumber = originalAccountNumber;
	}

	public String getCurrentLenderCreditor() {
		return currentLenderCreditor;
	}

	public void setCurrentLenderCreditor(String currentLenderCreditor) {
		this.currentLenderCreditor = currentLenderCreditor;
	}

	public String getOriginalLenderCreditor() {
		return originalLenderCreditor;
	}

	public void setOriginalLenderCreditor(String originalLenderCreditor) {
		this.originalLenderCreditor = originalLenderCreditor;
	}

	public String getInitialLenderCreditor() {
		return initialLenderCreditor;
	}

	public void setInitialLenderCreditor(String initialLenderCreditor) {
		this.initialLenderCreditor = initialLenderCreditor;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getProductSubTypeId() {
		return productSubTypeId;
	}

	public void setProductSubTypeId(Integer productSubTypeId) {
		this.productSubTypeId = productSubTypeId;
	}

	public Integer getProductSubTypeCount() {
		return productSubTypeCount;
	}

	public void setProductSubTypeCount(Integer productSubTypeCount) {
		this.productSubTypeCount = productSubTypeCount;
	}

	public Integer getClientJobScheduleId() {
		return clientJobScheduleId;
	}

	public void setClientJobScheduleId(Integer clientJobScheduleId) {
		this.clientJobScheduleId = clientJobScheduleId;
	}

	public LocalDate getOriginalAccountOpenDate() {
		return originalAccountOpenDate;
	}

	public void setOriginalAccountOpenDate(LocalDate originalAccountOpenDate) {
		this.originalAccountOpenDate = originalAccountOpenDate;
	}

	public LocalDate getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(LocalDate assignedDate) {
		this.assignedDate = assignedDate;
	}

	public LocalDate getDelinquencyDate() {
		return delinquencyDate;
	}

	public void setDelinquencyDate(LocalDate delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}

	public LocalDate getFirstDelinquencyDate() {
		return firstDelinquencyDate;
	}

	public void setFirstDelinquencyDate(LocalDate firstDelinquencyDate) {
		this.firstDelinquencyDate = firstDelinquencyDate;
	}

	public LocalDate getChargeOffDate() {
		return chargeOffDate;
	}

	public void setChargeOffDate(LocalDate chargeOffDate) {
		this.chargeOffDate = chargeOffDate;
	}

	public LocalDate getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(LocalDate lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public LocalDate getLastCashAdvanceDate() {
		return lastCashAdvanceDate;
	}

	public void setLastCashAdvanceDate(LocalDate lastCashAdvanceDate) {
		this.lastCashAdvanceDate = lastCashAdvanceDate;
	}

	public LocalDate getLastBalanceTransferDate() {
		return lastBalanceTransferDate;
	}

	public void setLastBalanceTransferDate(LocalDate lastBalanceTransferDate) {
		this.lastBalanceTransferDate = lastBalanceTransferDate;
	}

	public LocalDate getSolDate() {
		return solDate;
	}

	public void setSolDate(LocalDate solDate) {
		this.solDate = solDate;
	}

	public LocalDate getClientSolDate() {
		return clientSolDate;
	}

	public void setClientSolDate(LocalDate clientSolDate) {
		this.clientSolDate = clientSolDate;
	}

	public LocalDate getEquabliSolDate() {
		return equabliSolDate;
	}

	public void setEquabliSolDate(LocalDate equabliSolDate) {
		this.equabliSolDate = equabliSolDate;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public Integer getQueueStatusId() {
		return queueStatusId;
	}

	public void setQueueStatusId(Integer queueStatusId) {
		this.queueStatusId = queueStatusId;
	}

	public Integer getQueueReasonId() {
		return queueReasonId;
	}

	public void setQueueReasonId(Integer queueReasonId) {
		this.queueReasonId = queueReasonId;
	}

	public LocalDate getLastPurchaseDate() {
		return lastPurchaseDate;
	}

	public void setLastPurchaseDate(LocalDate lastPurchaseDate) {
		this.lastPurchaseDate = lastPurchaseDate;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public LookUp getCustomerTypeLookUp() {
		return customerTypeLookUp;
	}

	public void setCustomerTypeLookUp(LookUp customerTypeLookUp) {
		this.customerTypeLookUp = customerTypeLookUp;
	}

	public String getDebtType() {
		return debtType;
	}

	public void setDebtType(String debtType) {
		this.debtType = debtType;
	}

	public LookUp getDebtTypeLookUp() {
		return debtTypeLookUp;
	}

	public void setDebtTypeLookUp(LookUp debtTypeLookUp) {
		this.debtTypeLookUp = debtTypeLookUp;
	}

	public String getPortfolioCode() {
		return portfolioCode;
	}

	public void setPortfolioCode(String portfolioCode) {
		this.portfolioCode = portfolioCode;
	}

	public String getOriginalAccountApplicationType() {
		return originalAccountApplicationType;
	}

	public void setOriginalAccountApplicationType(String originalAccountApplicationType) {
		this.originalAccountApplicationType = originalAccountApplicationType;
	}

	public LookUp getOriginalAccountApplicationTypeLookUp() {
		return originalAccountApplicationTypeLookUp;
	}

	public void setOriginalAccountApplicationTypeLookUp(LookUp originalAccountApplicationTypeLookUp) {
		this.originalAccountApplicationTypeLookUp = originalAccountApplicationTypeLookUp;
	}

	public CustomAppConfigValue getCustomAppConfigValue() {
		return customAppConfigValue;
	}

	public void setCustomAppConfigValue(CustomAppConfigValue customAppConfigValue) {
		this.customAppConfigValue = customAppConfigValue;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getErrShortName() {
		return errShortName;
	}

	public void setErrShortName(String errShortName) {
		this.errShortName = errShortName;
	}

	public Integer getOriginalAccountNoDeDup() {
		return originalAccountNoDeDup;
	}

	public void setOriginalAccountNoDeDup(Integer originalAccountNoDeDup) {
		this.originalAccountNoDeDup = originalAccountNoDeDup;
	}

	public List<Consumer> getConsumer() {
		return consumer;
	}

	public void setConsumer(List<Consumer> consumer) {
		this.consumer = consumer;
	}

	public LocalDateTime getDtmUtcCreate() {
		return dtmUtcCreate;
	}

	public void setDtmUtcCreate(LocalDateTime dtmUtcCreate) {
		this.dtmUtcCreate = dtmUtcCreate;
	}

	public LocalDateTime getDtmUtcUpdate() {
		return dtmUtcUpdate;
	}

	public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
		this.dtmUtcUpdate = dtmUtcUpdate;
	}

	public Double getAmtLastPayment() {
		return amtLastPayment;
	}

	public void setAmtLastPayment(Double amtLastPayment) {
		this.amtLastPayment = amtLastPayment;
	}

	public Double getAmtLastPurchase() {
		return amtLastPurchase;
	}

	public void setAmtLastPurchase(Double amtLastPurchase) {
		this.amtLastPurchase = amtLastPurchase;
	}

	public Double getAmtLastCashAdvance() {
		return amtLastCashAdvance;
	}

	public void setAmtLastCashAdvance(Double amtLastCashAdvance) {
		this.amtLastCashAdvance = amtLastCashAdvance;
	}

	public Double getAmtLastBalanceTransfer() {
		return amtLastBalanceTransfer;
	}

	public void setAmtLastBalanceTransfer(Double amtLastBalanceTransfer) {
		this.amtLastBalanceTransfer = amtLastBalanceTransfer;
	}

	public Double getAmtPreChargeOffBalance() {
		return amtPreChargeOffBalance;
	}

	public void setAmtPreChargeOffBalance(Double amtPreChargeOffBalance) {
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Double getAmtPreChargeOffPrinciple() {
		return amtPreChargeOffPrinciple;
	}

	public void setAmtPreChargeOffPrinciple(Double amtPreChargeOffPrinciple) {
		this.amtPreChargeOffPrinciple = amtPreChargeOffPrinciple;
	}

	public Double getAmtPreChargeOffInterest() {
		return amtPreChargeOffInterest;
	}

	public void setAmtPreChargeOffInterest(Double amtPreChargeOffInterest) {
		this.amtPreChargeOffInterest = amtPreChargeOffInterest;
	}

	public Double getAmtPreChargeOffFees() {
		return amtPreChargeOffFees;
	}

	public void setAmtPreChargeOffFees(Double amtPreChargeOffFees) {
		this.amtPreChargeOffFees = amtPreChargeOffFees;
	}

	public Double getAmtPostChargeOffInterest() {
		return amtPostChargeOffInterest;
	}

	public void setAmtPostChargeOffInterest(Double amtPostChargeOffInterest) {
		this.amtPostChargeOffInterest = amtPostChargeOffInterest;
	}

	public Double getPctPostChargeOffInterest() {
		return pctPostChargeOffInterest;
	}

	public void setPctPostChargeOffInterest(Double pctPostChargeOffInterest) {
		this.pctPostChargeOffInterest = pctPostChargeOffInterest;
	}

	public Double getAmtPostChargeOffFee() {
		return amtPostChargeOffFee;
	}

	public void setAmtPostChargeOffFee(Double amtPostChargeOffFee) {
		this.amtPostChargeOffFee = amtPostChargeOffFee;
	}

	public Double getPctPostChargeOffFee() {
		return pctPostChargeOffFee;
	}

	public void setPctPostChargeOffFee(Double pctPostChargeOffFee) {
		this.pctPostChargeOffFee = pctPostChargeOffFee;
	}

	public Double getAmtPostChargeOffPayment() {
		return amtPostChargeOffPayment;
	}

	public void setAmtPostChargeOffPayment(Double amtPostChargeOffPayment) {
		this.amtPostChargeOffPayment = amtPostChargeOffPayment;
	}

	public Double getAmtPostChargeOffCredit() {
		return amtPostChargeOffCredit;
	}

	public void setAmtPostChargeOffCredit(Double amtPostChargeOffCredit) {
		this.amtPostChargeOffCredit = amtPostChargeOffCredit;
	}

	public Double getAmtAssigned() {
		return amtAssigned;
	}

	public void setAmtAssigned(Double amtAssigned) {
		this.amtAssigned = amtAssigned;
	}

	public Double getAmtPrincipalAssigned() {
		return amtPrincipalAssigned;
	}

	public void setAmtPrincipalAssigned(Double amtPrincipalAssigned) {
		this.amtPrincipalAssigned = amtPrincipalAssigned;
	}

	public Double getAmtInterestAssigned() {
		return amtInterestAssigned;
	}

	public void setAmtInterestAssigned(Double amtInterestAssigned) {
		this.amtInterestAssigned = amtInterestAssigned;
	}

	public Double getAmtLatefeeAssigned() {
		return amtLatefeeAssigned;
	}

	public void setAmtLatefeeAssigned(Double amtLatefeeAssigned) {
		this.amtLatefeeAssigned = amtLatefeeAssigned;
	}

	public Double getAmtOtherfeeAssigned() {
		return amtOtherfeeAssigned;
	}

	public void setAmtOtherfeeAssigned(Double amtOtherfeeAssigned) {
		this.amtOtherfeeAssigned = amtOtherfeeAssigned;
	}

	public Double getAmtCourtcostAssigned() {
		return amtCourtcostAssigned;
	}

	public void setAmtCourtcostAssigned(Double amtCourtcostAssigned) {
		this.amtCourtcostAssigned = amtCourtcostAssigned;
	}

	public Double getAmtAttorneyfeeAssigned() {
		return amtAttorneyfeeAssigned;
	}

	public void setAmtAttorneyfeeAssigned(Double amtAttorneyfeeAssigned) {
		this.amtAttorneyfeeAssigned = amtAttorneyfeeAssigned;
	}

	public String getProductAffinity() {
		return productAffinity;
	}

	public void setProductAffinity(String productAffinity) {
		this.productAffinity = productAffinity;
	}

	public LocalDate getCurrentbalanceDate() {
		return currentbalanceDate;
	}

	public void setCurrentbalanceDate(LocalDate currentbalanceDate) {
		this.currentbalanceDate = currentbalanceDate;
	}

	public Double getAmtCurrentbalance() {
		return amtCurrentbalance;
	}

	public void setAmtCurrentbalance(Double amtCurrentbalance) {
		this.amtCurrentbalance = amtCurrentbalance;
	}

	public Double getAmtPrincipalCurrentbalance() {
		return amtPrincipalCurrentbalance;
	}

	public void setAmtPrincipalCurrentbalance(Double amtPrincipalCurrentbalance) {
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
	}

	public Double getAmtInterestCurrentbalance() {
		return amtInterestCurrentbalance;
	}

	public void setAmtInterestCurrentbalance(Double amtInterestCurrentbalance) {
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
	}

	public Double getAmtLatefeeCurrentbalance() {
		return amtLatefeeCurrentbalance;
	}

	public void setAmtLatefeeCurrentbalance(Double amtLatefeeCurrentbalance) {
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
	}

	public Double getAmtOtherfeeCurrentbalance() {
		return amtOtherfeeCurrentbalance;
	}

	public void setAmtOtherfeeCurrentbalance(Double amtOtherfeeCurrentbalance) {
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Double getAmtCourtcostCurrentbalance() {
		return amtCourtcostCurrentbalance;
	}

	public void setAmtCourtcostCurrentbalance(Double amtCourtcostCurrentbalance) {
		this.amtCourtcostCurrentbalance = amtCourtcostCurrentbalance;
	}

	public Double getAmtAttorneyfeeCurrentbalance() {
		return amtAttorneyfeeCurrentbalance;
	}

	public void setAmtAttorneyfeeCurrentbalance(Double amtAttorneyfeeCurrentbalance) {
		this.amtAttorneyfeeCurrentbalance = amtAttorneyfeeCurrentbalance;
	}

	public LocalDate getPartnerAssignmentDate() {
		return partnerAssignmentDate;
	}

	public void setPartnerAssignmentDate(LocalDate partnerAssignmentDate) {
		this.partnerAssignmentDate = partnerAssignmentDate;
	}

	public Date getPlacementDateFrom() {
		return placementDateFrom;
	}

	public void setPlacementDateFrom(Date placementDateFrom) {
		this.placementDateFrom = placementDateFrom;
	}

	public Date getPlacementDateTo() {
		return placementDateTo;
	}

	public void setPlacementDateTo(Date placementDateTo) {
		this.placementDateTo = placementDateTo;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public List<ScrubWarning> getScrubWarnings() {
		return scrubWarnings;
	}

	public void setScrubWarnings(List<ScrubWarning> scrubWarnings) {
		this.scrubWarnings = scrubWarnings;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public LookUp getPortfolioCodeLookup() {
		return portfolioCodeLookup;
	}

	public String getSaleReviewStatus() {
		return saleReviewStatus;
	}

	public void setSaleReviewStatus(String saleReviewStatus) {
		this.saleReviewStatus = saleReviewStatus;
	}

	public LookUp getSaleReviewStatusLookUp() {
		return saleReviewStatusLookUp;
	}

	public void setSaleReviewStatusLookUp(LookUp saleReviewStatusLookUp) {
		this.saleReviewStatusLookUp = saleReviewStatusLookUp;
	}

	public void setPortfolioCodeLookup(LookUp portfolioCodeLookup) {
		this.portfolioCodeLookup = portfolioCodeLookup;
	}

	public Account() {

	}

	public Account(Long accountId, Integer clientId, String clientShortName, LocalDate assignedDate, LocalDate chargeOffDate,
			Double amtAssigned, Double amtPrincipalAssigned, Double amtInterestAssigned, 
			Double amtLatefeeAssigned, Double amtOtherfeeAssigned, Double amtCourtcostAssigned, 
			Double amtAttorneyfeeAssigned) {
		this.accountId = accountId;
		this.clientId = clientId;
		this.client = new com.equabli.collectprism.entity.Client();
		this.client.setShortName(clientShortName);
		this.assignedDate = assignedDate;
		this.chargeOffDate = chargeOffDate;
		this.amtAssigned = amtAssigned;
		this.amtPrincipalAssigned = amtPrincipalAssigned;
		this.amtInterestAssigned = amtInterestAssigned;
		this.amtLatefeeAssigned = amtLatefeeAssigned;
		this.amtOtherfeeAssigned = amtOtherfeeAssigned;
		this.amtCourtcostAssigned = amtCourtcostAssigned;
		this.amtAttorneyfeeAssigned = amtAttorneyfeeAssigned;
	}

	public Account(Long accountId, Integer clientId, String clientAccountNumber, String originalAccountNumber) {
		this.accountId = accountId;
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
		this.originalAccountNumber = originalAccountNumber;
	}

	public Account(Integer clientId, String clientAccountNumber, String currentLenderCreditor, String firstName,
			String middleName, String lastName, String identificationNumber, Double amtPreChargeOffBalance) {
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
		this.currentLenderCreditor = currentLenderCreditor;
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Account(String originalAccountNumber, Integer clientId, String originalLenderCreditor, String firstName,
			String middleName, String lastName, String identificationNumber, Double amtPreChargeOffBalance) {
		this.originalAccountNumber = originalAccountNumber;
		this.clientId = clientId;
		this.originalLenderCreditor = originalLenderCreditor;
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Account(Long accountId, String clientName, String clientAccountNumber, String originalAccountNumber,
			String shortName, String description) {
		this.accountId = accountId;
		this.client = new Client();
		this.client.setFullName(clientName);
		this.clientAccountNumber = clientAccountNumber;
		this.originalAccountNumber = originalAccountNumber;
		this.shortName = shortName;
		this.description = description;
	}

	public Account(Long accountId, Double amtPrincipalCurrentbalance, Double amtInterestCurrentbalance,
			Double amtOtherfeeCurrentbalance) {
		this.accountId = accountId;
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public void setCurrentBalanceData(Account account) {
		account.setCurrentbalanceDate(LocalDate.now());
		account.setAmtCurrentbalance(account.getAmtAssigned());
		account.setAmtPrincipalCurrentbalance(account.getAmtPrincipalAssigned());
		account.setAmtInterestCurrentbalance(account.getAmtInterestAssigned());
		account.setAmtLatefeeCurrentbalance(account.getAmtLatefeeAssigned());
		account.setAmtOtherfeeCurrentbalance(account.getAmtOtherfeeAssigned());
		account.setAmtCourtcostCurrentbalance(account.getAmtCourtcostAssigned());
		account.setAmtAttorneyfeeCurrentbalance(account.getAmtAttorneyfeeAssigned());
	}

	public void setPreChargeOffBuckets(Account account) {
		account.setAmtPreChargeOffBalance(account.getAmtAssigned());
		account.setAmtPreChargeOffPrinciple(account.getAmtPrincipalAssigned());
		account.setAmtPreChargeOffInterest(account.getAmtInterestAssigned());
		if (account.getClient().getShortName().equals(CommonConstants.CLIENT_MARLETTE_LOANPRO_SHORT_NAME)) {
			Double sum = (CommonUtils.isDoubleNull(account.getAmtLatefeeAssigned()) ? 0.00
					: account.getAmtLatefeeAssigned())
					+ (CommonUtils.isDoubleNull(account.getAmtOtherfeeAssigned()) ? 0.00
							: account.getAmtOtherfeeAssigned())
					+ (CommonUtils.isDoubleNull(account.getAmtCourtcostAssigned()) ? 0.00
							: account.getAmtCourtcostAssigned())
					+ (CommonUtils.isDoubleNull(account.getAmtAttorneyfeeAssigned()) ? 0.00
							: account.getAmtAttorneyfeeAssigned());

			account.setAmtPreChargeOffFees(sum);
		} else {
			account.setAmtPreChargeOffFees(
					CommonUtils.isDoubleNull(account.getAmtLatefeeAssigned()) ? 0.00 : account.getAmtLatefeeAssigned());
		}
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public void addErrWarJson(ErrWarJson errWarJson) {
		if (this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public Long getAutoAccountInfoIds() {
		return autoAccountInfoIds;
	}

	public void setAutoAccountInfoIds(Long autoAccountInfoIds) {
		this.autoAccountInfoIds = autoAccountInfoIds;
	}

	public static MapSqlParameterSource getParamsValue(Account account) {
		MapSqlParameterSource sqlParamValue = new MapSqlParameterSource();

		Field[] allFields = account.getClass().getDeclaredFields();
		for (Field field : allFields) {
			try {
				sqlParamValue.addValue(field.getName(), field.get(account));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sqlParamValue;
	}

}