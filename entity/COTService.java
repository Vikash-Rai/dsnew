package com.equabli.collectprism.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(schema = "data", name = "cot_servicer")
@Convert(attributeName = "json", converter = JsonType.class)
public class COTService {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cot_servicer_id")
    private Long cotServicerId;

    @Column(name = "dt_from")
    private LocalDate dtFrom;

    @Column(name = "dt_till")
    private LocalDate dtTill;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "partner_id")
    private Integer partnerId;

    public Long getCotServicerId() {
        return cotServicerId;
    }

    public void setCotServicerId(Long cotServicerId) {
        this.cotServicerId = cotServicerId;
    }

    public LocalDate getDtFrom() {
        return dtFrom;
    }

    public void setDtFrom(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
    }

    public LocalDate getDtTill() {
        return dtTill;
    }

    public void setDtTill(LocalDate dtTill) {
        this.dtTill = dtTill;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getRecordStatusId() {
        return recordStatusId;
    }

    public void setRecordStatusId(Integer recordStatusId) {
        this.recordStatusId = recordStatusId;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public COTService() {
    }

    public COTService(LocalDate dtFrom) {
        this.dtFrom = dtFrom;
    }

    public COTService(LocalDate dtFrom, LocalDate dtTill) {
        this.dtFrom = dtFrom;
        this.dtTill = dtTill;
    }
}
