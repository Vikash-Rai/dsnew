package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;

import com.equabli.domain.helpers.CommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "adjustment")
public class Adjustment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adjustment_id")
	private Long adjustmentId;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "partner_id")
	private Integer partnerId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select pr.partner_id from conf.partner pr join conf.record_status rs on pr.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where pr.partner_id = partner_id)")
	private Partner partner;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "adjustment_type")
	private String adjustmentType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'adjustment_type' and LOWER(lu.keycode) = LOWER(adjustment_type) and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp adjustmentTypeLookUp;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_adjustment")
	private LocalDate adjustmentDate;

	@Column(name = "amt_adjustment")
	private Double amtAdjustment;

	@Column(name = "amt_principal")
	private Double amtPrincipal;

	@Column(name = "amt_interest")
	private Double amtInterest;

	@Column(name = "amt_latefee")
	private Double amtLatefee;

	@Column(name = "amt_otherfee")
	private Double amtOtherfee;

	@Column(name = "amt_courtcost")
	private Double amtCourtcost;

	@Column(name = "amt_attorneyfee")
	private Double amtAttorneyfee;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select acc.amt_otherfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtOtherfeeCurrentbalance;

	@Formula(value = "(select acc.amt_interest_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtInterestCurrentbalance;

	@Formula(value = "(select acc.amt_principal_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPrincipalCurrentbalance;

	@Formula(value = "(select acc.amt_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCurrentbalance;

	@Formula(value = "(select acc.amt_pre_charge_off_principle from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffPrinciple;

	@Formula(value = "(select acc.amt_pre_charge_off_interest from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffInterest;

	@Formula(value = "(select acc.amt_pre_charge_off_fee from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffFee;

	@Formula(value = "(select acc.amt_latefee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtLatefeeCurrentbalance;

	@Formula(value = "(select acc.amt_courtcost_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCourtCostCurrentbalance;

	@Formula(value = "(select acc.amt_attorneyfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtAttorneyFeeCurrentBalance;

	@Formula(value = "(select acc.amt_pre_charge_off_balance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffBalance;
	@Formula(value = "(select acc.amt_post_charge_off_interest from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffInterest;

	@Formula(value = "(select acc.amt_post_charge_off_fees from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffFees;

	@Formula(value = "(select acc.amt_post_charge_off_payment from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffPayment;

	@Formula(value = "(select acc.amt_post_charge_off_credit from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffCredit;

	@Formula(value = "(select acc.dt_charge_off from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate dtChargeOff;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Column(name = "app_id")
	private Integer appId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "record_source_id")
	private Integer recordSourceId;

	public Adjustment() {
		
	}
	
	public Adjustment(String recordType, Integer clientId, Long accountId, String adjustmentType, LocalDate adjustmentDate, Double amtAdjustment, Double amtPrincipal,
			Double amtInterest, Double amtLatefee, Double amtOtherfee, Double amtCourtcost, Double amtAttorneyfee, Integer recordStatusId) {
		this.recordType = recordType;
		this.clientId = clientId;
		this.accountId = accountId;
		this.adjustmentType = adjustmentType;
		this.adjustmentDate = adjustmentDate;
		this.amtAdjustment = amtAdjustment;
		this.amtPrincipal = amtPrincipal;
		this.amtInterest = amtInterest;
		this.amtLatefee = amtLatefee;
		this.amtOtherfee = amtOtherfee;
		this.amtCourtcost = amtCourtcost;
		this.amtAttorneyfee = amtAttorneyfee;
		this.recordStatusId = recordStatusId;
	}
	
	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public Double getAmtOtherfeeCurrentbalance() {
		return amtOtherfeeCurrentbalance;
	}

	public void setAmtOtherfeeCurrentbalance(Double amtOtherfeeCurrentbalance) {
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Double getAmtInterestCurrentbalance() {
		return amtInterestCurrentbalance;
	}

	public void setAmtInterestCurrentbalance(Double amtInterestCurrentbalance) {
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
	}

	public Double getAmtPrincipalCurrentbalance() {
		return amtPrincipalCurrentbalance;
	}

	public void setAmtPrincipalCurrentbalance(Double amtPrincipalCurrentbalance) {
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
	}

	public Double getAmtCurrentbalance() {
		return amtCurrentbalance;
	}

	public void setAmtCurrentbalance(Double amtCurrentbalance) {
		this.amtCurrentbalance = amtCurrentbalance;
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

	public Double getAmtPreChargeOffFee() {
		return amtPreChargeOffFee;
	}

	public void setAmtPreChargeOffFee(Double amtPreChargeOffFee) {
		this.amtPreChargeOffFee = amtPreChargeOffFee;
	}

	public Double getAmtLatefeeCurrentbalance() {
		return amtLatefeeCurrentbalance;
	}

	public void setAmtLatefeeCurrentbalance(Double amtLatefeeCurrentbalance) {
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
	}

	public Double getAmtCourtCostCurrentbalance() {
		return amtCourtCostCurrentbalance;
	}

	public void setAmtCourtCostCurrentbalance(Double amtCourtCostCurrentbalance) {
		this.amtCourtCostCurrentbalance = amtCourtCostCurrentbalance;
	}

	public Double getAmtAttorneyFeeCurrentBalance() {
		return amtAttorneyFeeCurrentBalance;
	}

	public void setAmtAttorneyFeeCurrentBalance(Double amtAttorneyFeeCurrentBalance) {
		this.amtAttorneyFeeCurrentBalance = amtAttorneyFeeCurrentBalance;
	}

	public Double getAmtPreChargeOffBalance() {
		return amtPreChargeOffBalance;
	}

	public void setAmtPreChargeOffBalance(Double amtPreChargeOffBalance) {
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Double getAmtPostChargeOffInterest() {
		return amtPostChargeOffInterest;
	}

	public void setAmtPostChargeOffInterest(Double amtPostChargeOffInterest) {
		this.amtPostChargeOffInterest = amtPostChargeOffInterest;
	}

	public Double getAmtPostChargeOffFees() {
		return amtPostChargeOffFees;
	}

	public void setAmtPostChargeOffFees(Double amtPostChargeOffFees) {
		this.amtPostChargeOffFees = amtPostChargeOffFees;
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

	public LocalDate getDtChargeOff() {
		return dtChargeOff;
	}

	public void setDtChargeOff(LocalDate dtChargeOff) {
		this.dtChargeOff = dtChargeOff;
	}

	public Long getAdjustmentId() {
		return adjustmentId;
	}

	public void setAdjustmentId(Long adjustmentId) {
		this.adjustmentId = adjustmentId;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
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

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public String getAdjustmentType() {
		return adjustmentType;
	}

	public void setAdjustmentType(String adjustmentType) {
		this.adjustmentType = adjustmentType;
	}

	public LookUp getAdjustmentTypeLookUp() {
		return adjustmentTypeLookUp;
	}

	public void setAdjustmentTypeLookUp(LookUp adjustmentTypeLookUp) {
		this.adjustmentTypeLookUp = adjustmentTypeLookUp;
	}

	public LocalDate getAdjustmentDate() {
		return adjustmentDate;
	}

	public void setAdjustmentDate(LocalDate adjustmentDate) {
		this.adjustmentDate = adjustmentDate;
	}

	public Double getAmtAdjustment() {
		return amtAdjustment;
	}

	public void setAmtAdjustment(Double amtAdjustment) {
		this.amtAdjustment = amtAdjustment;
	}

	public Double getAmtPrincipal() {
		return amtPrincipal;
	}

	public void setAmtPrincipal(Double amtPrincipal) {
		this.amtPrincipal = amtPrincipal;
	}

	public Double getAmtInterest() {
		return amtInterest;
	}

	public void setAmtInterest(Double amtInterest) {
		this.amtInterest = amtInterest;
	}

	public Double getAmtLatefee() {
		return amtLatefee;
	}

	public void setAmtLatefee(Double amtLatefee) {
		this.amtLatefee = amtLatefee;
	}

	public Double getAmtOtherfee() {
		return amtOtherfee;
	}

	public void setAmtOtherfee(Double amtOtherfee) {
		this.amtOtherfee = amtOtherfee;
	}

	public Double getAmtCourtcost() {
		return amtCourtcost;
	}

	public void setAmtCourtcost(Double amtCourtcost) {
		this.amtCourtcost = amtCourtcost;
	}

	public Double getAmtAttorneyfee() {
		return amtAttorneyfee;
	}

	public void setAmtAttorneyfee(Double amtAttorneyfee) {
		this.amtAttorneyfee = amtAttorneyfee;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public LocalDateTime getDtmUtcUpdate() {
		return dtmUtcUpdate;
	}

	public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
		this.dtmUtcUpdate = dtmUtcUpdate;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getRecordSourceId() {
		return recordSourceId;
	}

	public void setRecordSourceId(Integer recordSourceId) {
		this.recordSourceId = recordSourceId;
	}
}