package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;

@Entity
@Table(schema = "data", name = "ledger")
public class Ledger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ledger_id")
	private Long ledgerId;

	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "dt_transaction")
	private LocalDate transactionDate;

	@Column(name = "transaction_id")
	private Long transactionId;

	@Column(name = "transaction_type")
	private String transactionType;

	@Column(name = "dt_ledger")
	private LocalDate ledgerDate;

	@Column(name = "amt_ledger")
	private Double amtLedger;

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

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "record_source_id")
	private Integer recordSourceId;

	@Column(name = "app_id")
	private Integer appId;


	public Long getLedgerId() {
		return ledgerId;
	}

	public void setLedgerId(Long ledgerId) {
		this.ledgerId = ledgerId;
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

	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public LocalDate getLedgerDate() {
		return ledgerDate;
	}

	public void setLedgerDate(LocalDate ledgerDate) {
		this.ledgerDate = ledgerDate;
	}

	public Double getAmtLedger() {
		return amtLedger;
	}

	public void setAmtLedger(Double amtLedger) {
		this.amtLedger = amtLedger;
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

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}


	public static Ledger setLedger(Account account, String updatedBy, Integer recordSourceId, Integer appId) {
		Ledger led = new Ledger();
		led.setClientId(account.getClientId());
		led.setAccountId(account.getAccountId());
		led.setTransactionDate(account.getAssignedDate());
		led.setTransactionId(account.getAccountId());
		led.setTransactionType(CommonConstants.ACCOUNT_TRANSACTION_TYPE);
		led.setLedgerDate(LocalDate.now());
		led.setAmtLedger(account.getAmtAssigned());
		led.setAmtPrincipal(account.getAmtPrincipalAssigned());
		led.setAmtInterest(account.getAmtInterestAssigned());
		led.setAmtLatefee(account.getAmtLatefeeAssigned());
		led.setAmtOtherfee(account.getAmtOtherfeeAssigned());
		led.setAmtCourtcost(account.getAmtCourtcostAssigned());
		led.setAmtAttorneyfee(account.getAmtAttorneyfeeAssigned());
		led.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
		led.setCreatedBy(updatedBy);
		led.setUpdatedBy(updatedBy);
		led.setRecordSourceId(recordSourceId);
		led.setAppId(appId);

		return led;
	}
}