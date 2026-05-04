package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "emailconsentexclusion")
public class EmailConsentExclusion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "emailconsentexclusion_id")
	private Long emailConsentExclusionId;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "email_id")
	private Long emailId;

	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "weekdayno")
	private Integer weekDayNo;

	@Column(name = "tm_utc_from")
	private LocalTime tmUtcFrom;

	@Column(name = "tm_utc_till")
	private LocalTime tmUtcTill;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select em.email_id from data.email em join conf.record_status rs on em.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where em.client_id = client_id and em.client_account_number = client_account_number and em.client_consumer_number = client_consumer_number and lower(em.email_address) = lower(email_address))"))
	private Email emailDetails;


	public Long getEmailConsentExclusionId() {
		return emailConsentExclusionId;
	}

	public void setEmailConsentExclusionId(Long emailConsentExclusionId) {
		this.emailConsentExclusionId = emailConsentExclusionId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public Long getClientConsumerNumber() {
		return clientConsumerNumber;
	}

	public void setClientConsumerNumber(Long clientConsumerNumber) {
		this.clientConsumerNumber = clientConsumerNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Integer getWeekDayNo() {
		return weekDayNo;
	}

	public void setWeekDayNo(Integer weekDayNo) {
		this.weekDayNo = weekDayNo;
	}

	public LocalTime getTmUtcFrom() {
		return tmUtcFrom;
	}

	public void setTmUtcFrom(LocalTime tmUtcFrom) {
		this.tmUtcFrom = tmUtcFrom;
	}

	public LocalTime getTmUtcTill() {
		return tmUtcTill;
	}

	public void setTmUtcTill(LocalTime tmUtcTill) {
		this.tmUtcTill = tmUtcTill;
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

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
	}

	public Email getEmailDetails() {
		return emailDetails;
	}

	public void setEmailDetails(Email emailDetails) {
		this.emailDetails = emailDetails;
	}
}