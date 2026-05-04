package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "chargeoff_account")
@Convert(attributeName = "json", converter = JsonType.class)
public class ChargeOffAccount implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chargeoff_account_id")
	private Long chargeOffAccountId;
	
	@Column(name = "record_type")
	private String recordType;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;
	
	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;
	
	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;
	
	@Column(name = "amt_pre_charge_off_balance")
	private Double amtPreChargeOffBalance;

	@Column(name = "amt_pre_charge_off_principal")
	private Double amtPreChargeOffPrincipal;

	@Column(name = "amt_pre_charge_off_interest")
	private Double amtPreChargeOffInterest;

	@Column(name = "amt_pre_charge_off_fee")
	private Double amtPreChargeOffFees;
	

	@Column(name = "dt_charge_off")
	private LocalDate chargeOffDate;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;
	
	@Column(name = "job_schedule_id")
	private Long jobScheduleId;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();
	
	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public Long getChargeOffAccountId() {
		return chargeOffAccountId;
	}

	public void setChargeOffAccountId(Long chargeOffAccountId) {
		this.chargeOffAccountId = chargeOffAccountId;
	}

	public String getRecordType() {
		return recordType;
	}

	public Double getAmtPreChargeOffPrincipal() {
		return amtPreChargeOffPrincipal;
	}

	public void setAmtPreChargeOffPrincipal(Double amtPreChargeOffPrincipal) {
		this.amtPreChargeOffPrincipal = amtPreChargeOffPrincipal;
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

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
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

	public Double getAmtPreChargeOffBalance() {
		return amtPreChargeOffBalance;
	}

	public void setAmtPreChargeOffBalance(Double amtPreChargeOffBalance) {
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Double getAmtPreChargeOffPrinciple() {
		return amtPreChargeOffPrincipal;
	}

	public void setAmtPreChargeOffPrinciple(Double amtPreChargeOffPrincipal) {
		this.amtPreChargeOffPrincipal = amtPreChargeOffPrincipal;
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

	public LocalDate getChargeOffDate() {
		return chargeOffDate;
	}

	public void setChargeOffDate(LocalDate chargeOffDate) {
		this.chargeOffDate = chargeOffDate;
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

	public Long getJobScheduleId() {
		return jobScheduleId;
	}

	public void setJobScheduleId(Long jobScheduleId) {
		this.jobScheduleId = jobScheduleId;
	}

	
}
