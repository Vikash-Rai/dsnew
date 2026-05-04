package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "custom_appconfig_value")
public class CustomAppConfigValue {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "custom_appconfig_value_id")
	private Integer customAppConfigValueId;

	@Column(name = "reference_type")
	private String referenceType;

	@Column(name = "reference_id")
	private Integer referenceId;

	@Column(name = "reference_short_name")
	private String referenceShortName;


	public Integer getCustomAppConfigValueId() {
		return customAppConfigValueId;
	}

	public void setCustomAppConfigValueId(Integer customAppConfigValueId) {
		this.customAppConfigValueId = customAppConfigValueId;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	public Integer getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Integer referenceId) {
		this.referenceId = referenceId;
	}

	public String getReferenceShortName() {
		return referenceShortName;
	}

	public void setReferenceShortName(String referenceShortName) {
		this.referenceShortName = referenceShortName;
	}
}