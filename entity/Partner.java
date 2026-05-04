package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "partner")
public class Partner {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "partner_id")
	private Integer partnerId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	public Partner() {
	}

	public Partner(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
}