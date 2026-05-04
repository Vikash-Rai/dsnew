package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "cot_owner")
public class CoTOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cot_owner_id")
    private Long cotOwnerId;

    @Column(name = "record_type")
    private String recordType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
    private Client client;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "external_system_id")
    private Long externalSystemId;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "dtm_utc_create", updatable = false)
    private LocalDateTime dtmUtcCreate;

    @Column(name = "dtm_utc_update", updatable = false)
    private LocalDateTime dtmUtcUpdate;

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address1")
    private String address1;

    @Column(name = "city")
    private String city;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "country")
    private String country;

    @Column(name = "zip")
    private String zip;


	public CoTOwner() {
    }

    public CoTOwner(Long cotOwnerId, String recordType, Client client, Integer clientId, Long externalSystemId, Integer recordStatusId, LocalDateTime dtmUtcCreate, LocalDateTime dtmUtcUpdate, String ownerName, String address2, String address1, String city, String stateCode, String country, String zip) {
        this.cotOwnerId = cotOwnerId;
        this.recordType = recordType;
        this.client = client;
        this.clientId = clientId;
        this.externalSystemId = externalSystemId;
        this.recordStatusId = recordStatusId;
        this.dtmUtcCreate = dtmUtcCreate;
        this.dtmUtcUpdate = dtmUtcUpdate;
        this.ownerName = ownerName;
        this.address2 = address2;
        this.address1 = address1;
        this.city = city;
        this.stateCode = stateCode;
        this.country = country;
        this.zip = zip;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Long getCotOwnerId() {
        return cotOwnerId;
    }

    public void setCotOwnerId(Long cotOwnerId) {
        this.cotOwnerId = cotOwnerId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Long getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(Long externalSystemId) {
        this.externalSystemId = externalSystemId;
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
}