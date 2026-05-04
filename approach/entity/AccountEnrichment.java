package com.equabli.collectprism.approach.entity;

import com.equabli.collectprism.entity.*;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(schema = "data", name = "account")
@DynamicUpdate
@Getter
@Setter
public class AccountEnrichment implements Serializable {

    @Id
    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "client_id", updatable = false)
    private Integer clientId;

    /**
     * partnerId is a scalar column — kept as-is.
     * Used as cache key for EnrichmentCacheService.getPartnerType(clientId, partnerId).
     * No subquery. No join. Just a plain column read.
     */
    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "client_account_number", updatable = false)
    private String clientAccountNumber;

    @Column(name = "product_id")
    private Integer productId;

    // Dates
    @Column(name = "dt_assigned")
    private LocalDate assignedDate;

    @Column(name = "dt_delinquency")
    private LocalDate delinquencyDate;

    @Column(name = "dt_charge_off")
    private LocalDate chargeOffDate;

    @Column(name = "dt_last_payment")
    private LocalDate lastPaymentDate;

    @Column(name = "dt_statute")
    private LocalDate solDate;

    @Column(name = "dt_client_statute")
    private LocalDate clientSolDate;

    @Column(name = "dt_equabli_statute")
    private LocalDate equabliSolDate;

    // Assigned amounts
    @Column(name = "amt_assigned")
    private Double amtAssigned;

    @Column(name = "amt_principal_assigned")
    private Double amtPrincipalAssigned;

    @Column(name = "amt_interest_assigned")
    private Double amtInterestAssigned;

    @Column(name = "amt_latefee_assigned")
    private Double amtLatefeeAssigned;

    @Column(name = "amt_otherfee_assigned")
    private Double amtOtherfeeAssigned;

    @Column(name = "amt_courtcost_assigned")
    private Double amtCourtcostAssigned;

    @Column(name = "amt_attorneyfee_assigned")
    private Double amtAttorneyfeeAssigned;

    // Written by CurrentBalanceHandler
    @Column(name = "dt_currentbalance")
    private LocalDate currentbalanceDate;

    @Column(name = "amt_currentbalance")
    private Double amtCurrentbalance;

    @Column(name = "amt_principal_currentbalance")
    private Double amtPrincipalCurrentbalance;

    @Column(name = "amt_interest_currentbalance")
    private Double amtInterestCurrentbalance;

    @Column(name = "amt_latefee_currentbalance")
    private Double amtLatefeeCurrentbalance;

    @Column(name = "amt_otherfee_currentbalance")
    private Double amtOtherfeeCurrentbalance;

    @Column(name = "amt_courtcost_currentbalance")
    private Double amtCourtcostCurrentbalance;

    @Column(name = "amt_attorneyfee_currentbalance")
    private Double amtAttorneyfeeCurrentbalance;

    // Written by PreChargeOffHandler
    @Column(name = "amt_pre_charge_off_balance")
    private Double amtPreChargeOffBalance;

    @Column(name = "amt_pre_charge_off_principle")
    private Double amtPreChargeOffPrinciple;

    @Column(name = "amt_pre_charge_off_interest")
    private Double amtPreChargeOffInterest;

    @Column(name = "amt_pre_charge_off_fee")
    private Double amtPreChargeOffFees;

    // Written by SOLHandler
    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "err_short_name")
    private String errShortName;

    @Column(name = "queue_id")
    private Integer queueId;

    @Column(name = "queuestatus_id")
    private Integer queueStatusId;

    @Column(name = "queuereason_id")
    private Integer queueReasonId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "err_code_json", columnDefinition = "jsonb")
    private Set<ErrWarJson> errCodeJson;

    // Written by orchestrator
    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "record_source_id")
    private Integer recordSourceId;

    @Column(name = "app_id")
    private Integer appId;

    // Transient - saved separately by EnrichmentTransactionService
    @Transient
    private LedgerEnrichment ledger;
}