package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "placed_account")
public class PlacedAccountLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "placed_account_id")
	private Long placedAccountLogId;

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

	@Column(name = "job_schedule_id")
	private Long jobScheduleId;

	@Column(name = "dt_confirm")
	private LocalDate confirmDate;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number  and acc.partner_id = partner_id)")
	private Long accountIds;

	public Long getPlacedAccountLogId() {
		return placedAccountLogId;
	}

	public void setPlacedAccountLogId(Long placedAccountLogId) {
		this.placedAccountLogId = placedAccountLogId;
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

	public Long getJobScheduleId() {
		return jobScheduleId;
	}

	public void setJobScheduleId(Long jobScheduleId) {
		this.jobScheduleId = jobScheduleId;
	}

	public LocalDate getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(LocalDate confirmDate) {
		this.confirmDate = confirmDate;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
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

}
