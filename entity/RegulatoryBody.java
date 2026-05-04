package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "regulatorybody")
public class RegulatoryBody {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "regulatorybody_id")
	private Integer regulatoryBodyId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "days_firstsla")
	private Integer firstSlaDays;

	@Column(name = "days_lastsla")
	private Integer lastSlaDays;

	@Column(name = "scrubtype")
	private Integer scrubType;


	public Integer getRegulatoryBodyId() {
		return regulatoryBodyId;
	}

	public void setRegulatoryBodyId(Integer regulatoryBodyId) {
		this.regulatoryBodyId = regulatoryBodyId;
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

	public Integer getFirstSlaDays() {
		return firstSlaDays;
	}

	public void setFirstSlaDays(Integer firstSlaDays) {
		this.firstSlaDays = firstSlaDays;
	}

	public Integer getLastSlaDays() {
		return lastSlaDays;
	}

	public void setLastSlaDays(Integer lastSlaDays) {
		this.lastSlaDays = lastSlaDays;
	}

	public Integer getScrubType() {
		return scrubType;
	}

	public void setScrubType(Integer scrubType) {
		this.scrubType = scrubType;
	}
}