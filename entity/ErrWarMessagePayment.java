package com.equabli.collectprism.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "data", name = "vwerrwarmessage_payment")
public class ErrWarMessagePayment implements Serializable{


	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "payment_id")
	private Long paymentId;

	@Column(name = "errwar_short_name")
	private String errwarShortName;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "errwar_description")
	private String errwarDescription;

	@Column(name  = "errwar_type")
	private String errwarType;

	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "client_job_schedule_id")
	private Long clientJobScheduleId;

	@Column(name = "dtm_utc_create")
	private LocalDateTime dtmUtcCreate;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public String getErrwarShortName() {
		return errwarShortName;
	}

	public void setErrwarShortName(String errwarShortName) {
		this.errwarShortName = errwarShortName;
	}

	public String getErrwarDescription() {
		return errwarDescription;
	}

	public void setErrwarDescription(String errwarDescription) {
		this.errwarDescription = errwarDescription;
	}

	public String getErrwarType() {
		return errwarType;
	}

	public void setErrwarType(String errwarType) {
		this.errwarType = errwarType;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Long getClientJobScheduleId() {
		return clientJobScheduleId;
	}

	public void setClientJobScheduleId(Long clientJobScheduleId) {
		this.clientJobScheduleId = clientJobScheduleId;
	}

	public LocalDateTime getDtmUtcCreate() {
		return dtmUtcCreate;
	}

	public void setDtmUtcCreate(LocalDateTime dtmUtcCreate) {
		this.dtmUtcCreate = dtmUtcCreate;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}
}
