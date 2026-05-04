package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "account_ucc")
public class AccountUcc implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_ucc_id")
	private Long accountUccId;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Column(name = "file_number")
	private String uccFilingNumber;
		
	public String getUccFilingNumber() {
		return uccFilingNumber;
	}

	public void setUccFilingNumber(String uccFilingNumber) {
		this.uccFilingNumber = uccFilingNumber;
	}

	public Long getAccountUccId() {
		return accountUccId;
	}

	public void setAccountUccId(Long accountUccId) {
		this.accountUccId = accountUccId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
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
}