package com.equabli.collectprism.entity;


import java.time.LocalDate;
import java.util.Map;

import jakarta.persistence.Column;

public class AccountUpdateResult {
	private Long accountId;
	private LocalDate calculatedSOLDate;
	private LocalDate currentSOLDate;
	private boolean isPrimaryStateExists;
	private boolean success;
	private String message;
	private LocalDate solDate;
	private LocalDate equabliSolDate;
	// Ledger
    private LocalDate ledgerDate;
    private Double amtLedger;
    private Double amtPrincipal;
    private Double amtInterest;
    private Double amtLatefee;
    private Double amtOtherfee;
    private Double amtCourtcost;
    private Double amtAttorneyfee;

    // Current Balance
    private LocalDate currentBalanceDate;
    private Double amtCurrentbalance;
    private Double amtPrincipalCurrentbalance;
    private Double amtInterestCurrentbalance;
    private Double amtLatefeeCurrentbalance;
    private Double amtOtherfeeCurrentbalance;
    private Double amtCourtcostCurrentbalance;
    private Double amtAttorneyfeeCurrentbalance;

    private Integer recordStatusId;
    
    private Integer clientId;

	private LocalDate transactionDate;
	private Long transactionId;

	private String transactionType;
	private String createdBy;

	private String updatedBy;


	private Integer recordSourceId;

	private Integer appId;
	
	
	
    
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

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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

	public LocalDate getEquabliSolDate() {
		return equabliSolDate;
	}

	public void setEquabliSolDate(LocalDate equabliSolDate) {
		this.equabliSolDate = equabliSolDate;
	}

	public LocalDate getSolDate() {
		return solDate;
	}

	public void setSolDate(LocalDate solDate) {
		this.solDate = solDate;
	}

	private AccountUpdateResult(Long accountId,String message) {
		this.accountId = accountId;
		this.message = message;
	}

	public static AccountUpdateResult success(Long accountId,String message) {
		return new AccountUpdateResult(accountId,message);
	}

	public static AccountUpdateResult failed(String message) {
		return new AccountUpdateResult(null,message);
	}

	public static AccountUpdateResult skipped(String message) {
		return new AccountUpdateResult(null, message);
	}
	
	public static AccountUpdateResult getOrCreate(
	        Map<Long, AccountUpdateResult> map,
	        Long accountId
	) {
	    return map.computeIfAbsent(accountId,
	            id -> AccountUpdateResult.success(id,"success"));
	}

	
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public void setCalculatedSOLDate(LocalDate calculatedSOLDate) {
		this.calculatedSOLDate = calculatedSOLDate;
	}

	public void setCurrentSOLDate(LocalDate currentSOLDate) {
		this.currentSOLDate = currentSOLDate;
	}

	public boolean isSuccess() {
		return success;
	}

	public Long getAccountId() {
		return accountId;
	}

	public LocalDate getCalculatedSOLDate() {
		return calculatedSOLDate;
	}

	public LocalDate getCurrentSOLDate() {
		return currentSOLDate;
	}

	public boolean isPrimaryStateExists() {
		return isPrimaryStateExists;
	}

	public String getMessage() {
		return message;
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

	public LocalDate getCurrentBalanceDate() {
		return currentBalanceDate;
	}

	public void setCurrentBalanceDate(LocalDate currentBalanceDate) {
		this.currentBalanceDate = currentBalanceDate;
	}

	public Double getAmtCurrentbalance() {
		return amtCurrentbalance;
	}

	public void setAmtCurrentbalance(Double amtCurrentbalance) {
		this.amtCurrentbalance = amtCurrentbalance;
	}

	public Double getAmtPrincipalCurrentbalance() {
		return amtPrincipalCurrentbalance;
	}

	public void setAmtPrincipalCurrentbalance(Double amtPrincipalCurrentbalance) {
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
	}

	public Double getAmtInterestCurrentbalance() {
		return amtInterestCurrentbalance;
	}

	public void setAmtInterestCurrentbalance(Double amtInterestCurrentbalance) {
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
	}

	public Double getAmtLatefeeCurrentbalance() {
		return amtLatefeeCurrentbalance;
	}

	public void setAmtLatefeeCurrentbalance(Double amtLatefeeCurrentbalance) {
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
	}

	public Double getAmtOtherfeeCurrentbalance() {
		return amtOtherfeeCurrentbalance;
	}

	public void setAmtOtherfeeCurrentbalance(Double amtOtherfeeCurrentbalance) {
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Double getAmtCourtcostCurrentbalance() {
		return amtCourtcostCurrentbalance;
	}

	public void setAmtCourtcostCurrentbalance(Double amtCourtcostCurrentbalance) {
		this.amtCourtcostCurrentbalance = amtCourtcostCurrentbalance;
	}

	public Double getAmtAttorneyfeeCurrentbalance() {
		return amtAttorneyfeeCurrentbalance;
	}

	public void setAmtAttorneyfeeCurrentbalance(Double amtAttorneyfeeCurrentbalance) {
		this.amtAttorneyfeeCurrentbalance = amtAttorneyfeeCurrentbalance;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
	
	public void merge(AccountUpdateResult other) {
	    if (other == null) return;

	    // ---- SOL ----
	    if (other.calculatedSOLDate != null) {
	        this.calculatedSOLDate = other.calculatedSOLDate;
	    }
	    if (other.currentSOLDate != null) {
	        this.currentSOLDate = other.currentSOLDate;
	    }

	    if (other.solDate != null) {
	        this.solDate = other.solDate;
	    }

	    if (other.equabliSolDate != null) {
	        this.equabliSolDate = other.equabliSolDate;
	    }
	    this.isPrimaryStateExists = other.isPrimaryStateExists;

	    // ---- Ledger ----
	    if (other.ledgerDate != null) this.ledgerDate = other.ledgerDate;
	    if (other.amtLedger != null) this.amtLedger = other.amtLedger;
	    if (other.amtPrincipal != null) this.amtPrincipal = other.amtPrincipal;
	    if (other.amtInterest != null) this.amtInterest = other.amtInterest;
	    if (other.amtLatefee != null) this.amtLatefee = other.amtLatefee;
	    if (other.amtOtherfee != null) this.amtOtherfee = other.amtOtherfee;
	    if (other.amtCourtcost != null) this.amtCourtcost = other.amtCourtcost;
	    if (other.amtAttorneyfee != null) this.amtAttorneyfee = other.amtAttorneyfee;

	    // ---- Current Balance ----
	    if (other.currentBalanceDate != null) this.currentBalanceDate = other.currentBalanceDate;
	    if (other.amtCurrentbalance != null) this.amtCurrentbalance = other.amtCurrentbalance;
	    if (other.amtPrincipalCurrentbalance != null) this.amtPrincipalCurrentbalance = other.amtPrincipalCurrentbalance;
	    if (other.amtInterestCurrentbalance != null) this.amtInterestCurrentbalance = other.amtInterestCurrentbalance;
	    if (other.amtLatefeeCurrentbalance != null) this.amtLatefeeCurrentbalance = other.amtLatefeeCurrentbalance;
	    if (other.amtOtherfeeCurrentbalance != null) this.amtOtherfeeCurrentbalance = other.amtOtherfeeCurrentbalance;
	    if (other.amtCourtcostCurrentbalance != null) this.amtCourtcostCurrentbalance = other.amtCourtcostCurrentbalance;
	    if (other.amtAttorneyfeeCurrentbalance != null) this.amtAttorneyfeeCurrentbalance = other.amtAttorneyfeeCurrentbalance;

	    // ---- Status ----
	    if (other.recordStatusId != null) {
	        this.recordStatusId = other.recordStatusId;
	    }

	    // ---- success ----
	    this.success = this.success || other.success;
	}
	
}
