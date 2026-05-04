package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;

import com.equabli.domain.helpers.CommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "account")
@Convert(attributeName = "json", converter = JsonType.class)
public class Accounts implements Serializable {
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

	@Column(name = "client_account_number", updatable = false)
	private String clientAccountNumber;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_charge_off")
	private LocalDate chargeOffDate;

	@Column(name = "current_lender_creditor")
	private String currentLenderCreditor;

	@Column(name = "original_lender_creditor")
	private String originalLenderCreditor;

	@Column(name = "initial_lender_creditor")
	private String initialLenderCreditor;


	public Accounts() {
    	
    }

	public Accounts(Long accountId, Integer clientId, String clientAccountNumber, LocalDate chargeOffDate, String currentLenderCreditor, 
			String originalLenderCreditor, String initialLenderCreditor) {
		this.accountId = accountId;
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
		this.chargeOffDate = chargeOffDate;
		this.currentLenderCreditor = currentLenderCreditor;
		this.originalLenderCreditor = originalLenderCreditor;
		this.initialLenderCreditor = initialLenderCreditor;
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

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public LocalDate getChargeOffDate() {
		return chargeOffDate;
	}

	public void setChargeOffDate(LocalDate chargeOffDate) {
		this.chargeOffDate = chargeOffDate;
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
}