package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "partner_assignment")
public class PartnerAssignment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "partner_assignment_id")
    private Long partnerAssignmentId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
    private Client client;

    @Column(name = "client_id")
    private Integer clientId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select pr.partner_id from conf.partner pr join conf.record_status rs on pr.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where pr.partner_id = partner_id)")
    private Partner partner;

    @Column(name = "partner_id")
    private Integer partnerId;

    @Column(name = "client_account_number")
    private String clientAccountNumber;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "dtm_utc_create", updatable = false)
    private LocalDateTime dtmUtcCreate;

    @Column(name = "dtm_utc_update", updatable = false)
    private LocalDateTime dtmUtcUpdate;

    @Column(name = "dt_partner_assignment", updatable = false)
    private LocalDateTime dtPartnerAssignment;

    @Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
    private Long accountIds;

    public Long getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(Long accountIds) {
        this.accountIds = accountIds;
    }

    public Long getPartnerAssignmentId() {
        return partnerAssignmentId;
    }

    public void setPartnerAssignmentId(Long partnerAssignmentId) {
        this.partnerAssignmentId = partnerAssignmentId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getClientAccountNumber() {
        return clientAccountNumber;
    }

    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
    }

    public Integer getRecordStatusId() {
        return recordStatusId;
    }

    public void setRecordStatusId(Integer recordStatusId) {
        this.recordStatusId = recordStatusId;
    }

    public LocalDateTime getDtmUtcCreate() {
        return dtmUtcCreate;
    }

    public void setDtmUtcCreate(LocalDateTime dtmUtcCreate) {
        this.dtmUtcCreate = dtmUtcCreate;
    }

    public LocalDateTime getDtmUtcUpdate() {
        return dtmUtcUpdate;
    }

    public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
        this.dtmUtcUpdate = dtmUtcUpdate;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public PartnerAssignment() {
    }

    public LocalDateTime getDtPartnerAssignment() {
        return dtPartnerAssignment;
    }

    public void setDtPartnerAssignment(LocalDateTime dtPartnerAssignment) {
        this.dtPartnerAssignment = dtPartnerAssignment;
    }

    public Partner getPartner() {
        return partner;
    }

    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    public PartnerAssignment(Long partnerAssignmentId, String recordType, Long accountId, Client client, Integer clientId, Partner partner, Integer partnerId, String clientAccountNumber, Integer recordStatusId, LocalDateTime dtmUtcCreate, LocalDateTime dtmUtcUpdate, LocalDateTime dtPartnerAssignment, Long accountIds) {
        this.partnerAssignmentId = partnerAssignmentId;
        this.recordType = recordType;
        this.accountId = accountId;
        this.client = client;
        this.clientId = clientId;
        this.partner = partner;
        this.partnerId = partnerId;
        this.clientAccountNumber = clientAccountNumber;
        this.recordStatusId = recordStatusId;
        this.dtmUtcCreate = dtmUtcCreate;
        this.dtmUtcUpdate = dtmUtcUpdate;
        this.dtPartnerAssignment = dtPartnerAssignment;
        this.accountIds = accountIds;
    }
}
