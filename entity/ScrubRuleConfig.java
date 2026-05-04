package com.equabli.collectprism.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "scrub_rule_config")
public class ScrubRuleConfig  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 151999552985123390L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "scrub_rule_config_id")
	private Long scrubruleconfigId;

	@Column(name = "configured_for")
	private String configuredFor;

	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "errwar_short_name")
	private String errwarShortName;

	@Column(name = "is_applicable")
	private Boolean isApplicable= false;

	@Column(name = "is_error")
	private Boolean isErrorCode = true;
	
	@Column(name = "updated_by")
	private String updatedBy ;
	
	@Column(name = "record_source_id")
	private Integer recordSourceId;
	
	
	@Column(name = "app_id")
	private Integer appId;
	

	public ScrubRuleConfig(String errwarShortName, Boolean isApplicable, Boolean isErrorCode) {
		this.errwarShortName = errwarShortName;
		this.isApplicable = isApplicable;
		this.isErrorCode = isErrorCode;
	}

	public Boolean getIsErrorCode() {
		return isErrorCode;
	}

	public void setIsErrorCode(Boolean errorCode) {
		isErrorCode = errorCode;
	}

	public Long getScrubruleconfigId() {
		return scrubruleconfigId;
	}

	public void setScrubruleconfigId(Long scrubruleconfigId) {
		this.scrubruleconfigId = scrubruleconfigId;
	}

	public String getConfiguredFor() {
		return configuredFor;
	}

	public void setConfiguredFor(String configuredFor) {
		this.configuredFor = configuredFor;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getErrwarShortName() {
		return errwarShortName;
	}

	public void setErrwarShortName(String errwarShortName) {
		this.errwarShortName = errwarShortName;
	}

	public Boolean getIsApplicable() {
		return isApplicable;
	}

	public void setIsApplicable(Boolean isApplicable) {
		this.isApplicable = isApplicable;
	}
	
	public ScrubRuleConfig() {

	}
}
