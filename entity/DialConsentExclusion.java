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
@Table(schema = "data", name = "dialconsentexclusion")
public class DialConsentExclusion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dialconsentexclusion_id")
	private Long dialConsentExclusionId;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "phone_id")
	private Long phoneId;

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

	@Column(name = "phone")
	private String phone;

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
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select ph.phone_id from data.phone ph join conf.record_status rs on ph.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where ph.client_id = client_id and ph.client_account_number = client_account_number and ph.client_consumer_number = client_consumer_number and ph.phone = phone)"))
	private Phone phoneDetails;


	public Long getDialConsentExclusionId() {
		return dialConsentExclusionId;
	}

	public void setDialConsentExclusionId(Long dialConsentExclusionId) {
		this.dialConsentExclusionId = dialConsentExclusionId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Long getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Long phoneId) {
		this.phoneId = phoneId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Phone getPhoneDetails() {
		return phoneDetails;
	}

	public void setPhoneDetails(Phone phoneDetails) {
		this.phoneDetails = phoneDetails;
	}
}