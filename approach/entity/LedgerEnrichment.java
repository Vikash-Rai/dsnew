package com.equabli.collectprism.approach.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "data", name = "ledger")
public class LedgerEnrichment implements Serializable {
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


    public static LedgerEnrichment setLedger(AccountEnrichment account, String updatedBy, Integer recordSourceId, Integer appId) {
        LedgerEnrichment led = new LedgerEnrichment();
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
