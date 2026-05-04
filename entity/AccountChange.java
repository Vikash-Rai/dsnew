package com.equabli.collectprism.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "data", name = "account_change")
public class AccountChange {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_change_id")
	private Long accountChangeId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;
	
	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "current_lendor_creditor")
	private String currentLenderCreditor;

	@Column(name = "charge_off_date")
	private LocalDate chargeOffDate;

	@Column(name = "debt_stage")
	private String debtStage;

	@Column(name = "debt_status")
	private String debtStatus;

	@Column(name = "pre_charge_off_client_bucket")
	private String preChargeOffClientBucket;

	@Column(name = "client_sol_date")
	private LocalDate clientSOLDate;
	
	@Column(name = "sub_pool_id")
	private Integer subpoolId;

	@Column(name = "additional_account_number")
	private String additionalAccountNumber;

	@Column(name = "original_lender_creditor")
	private String originalLenderCreditor;

	@Column(name = "delinquency_date")
	private LocalDate delinquencyDate;

	@Column(name = "dt_debt_due")
	private LocalDate debtDueDate;

	@Column(name = "blankornull")
	private String blankOrNull;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	//private LocalDateTime createdAt = LocalDateTime.now();

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	// Getters and Setters
	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public Long getAccountChangeId() {
		return accountChangeId;
	}

	public void setAccountChangeId(Long accountChangeId) {
		this.accountChangeId = accountChangeId;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public String getCurrentLenderCreditor() {
		return currentLenderCreditor;
	}

	public void setCurrentLenderCreditor(String currentLenderCreditor) {
		this.currentLenderCreditor = currentLenderCreditor;
	}

	public LocalDate getChargeOffDate() {
		return chargeOffDate;
	}

	public void setChargeOffDate(LocalDate chargeOffDate) {
		this.chargeOffDate = chargeOffDate;
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

	public String getPreChargeOffClientBucket() {
		return preChargeOffClientBucket;
	}

	public void setPreChargeOffClientBucket(String preChargeOffClientBucket) {
		this.preChargeOffClientBucket = preChargeOffClientBucket;
	}

	public LocalDate getClientSOLDate() {
		return clientSOLDate;
	}

	public void setClientSOLDate(LocalDate clientSOLDate) {
		this.clientSOLDate = clientSOLDate;
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

	public String getOriginalLenderCreditor() {
		return originalLenderCreditor;
	}

	public void setOriginalLenderCreditor(String originalLenderCreditor) {
		this.originalLenderCreditor = originalLenderCreditor;
	}

	public LocalDate getDelinquencyDate() {
		return delinquencyDate;
	}

	public void setDelinquencyDate(LocalDate delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}

	public LocalDate getDebtDueDate() {
		return debtDueDate;
	}

	public void setDebtDueDate(LocalDate debtDueDate) {
		this.debtDueDate = debtDueDate;
	}

	public String getBlankOrNull() {
		return blankOrNull;
	}

	public void setBlankOrNull(String blankOrNull) {
		this.blankOrNull = blankOrNull;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	
	

}
