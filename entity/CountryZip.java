package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "country_zip")
public class CountryZip {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "country_zip_id")
	private Integer countryZipId;

	@Column(name = "zip")
	private String zip;


	public Integer getCountryZipId() {
		return countryZipId;
	}

	public void setCountryZipId(Integer countryZipId) {
		this.countryZipId = countryZipId;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}