package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "country_state")
public class CountryState {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_state_id")
	private Integer countryStateId;

	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;


	public CountryState() {
	}

	public CountryState(Integer countryStateId) {
		this.countryStateId = countryStateId;
	}

	public Integer getCountryStateId() {
		return countryStateId;
	}

	public void setCountryStateId(Integer countryStateId) {
		this.countryStateId = countryStateId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
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
}