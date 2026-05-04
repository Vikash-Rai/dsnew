package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "charge_off_reason")
public class ChargeOffReason {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "charge_off_reason_id")
	private Integer chargeOffReasonId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "scrubtype")
	private Integer scrubType;


	public Integer getChargeOffReasonId() {
		return chargeOffReasonId;
	}

	public void setChargeOffReasonId(Integer chargeOffReasonId) {
		this.chargeOffReasonId = chargeOffReasonId;
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

	public Integer getScrubType() {
		return scrubType;
	}

	public void setScrubType(Integer scrubType) {
		this.scrubType = scrubType;
	}
}