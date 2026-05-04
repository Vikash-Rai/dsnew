package com.equabli.collectprism.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "errwarmessage")
public class ErrWarMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "errwarmessage_id")
	private Long errwarmessageId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "description")
	private String description;

	@Column(name = "is_applicable")
	private Boolean isApplicable  = false;

	private Boolean isErrorCode  = true;

	private Boolean isReadOnly;

	private Boolean isApplicableForClient;
	
	@Column(name = "entity_short_name")
	private String entityShortName;
	
	private String entityFullName;

	@Transient
	private String errorCodeJson;

	@Transient
	private Long count;

	public Boolean getIsErrorCode() {
		return isErrorCode != null ? isErrorCode : true;
	}

	public void setIsErrorCode(Boolean errorCode) {
		isErrorCode = errorCode;
	}

	public Long getErrwarmessageId() {
		return errwarmessageId;
	}
	public void setErrwarmessageId(Long errwarmessageId) {
		this.errwarmessageId = errwarmessageId;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}

	public Boolean getIsApplicable() {
		return isApplicable;
	}
	public void setIsApplicable(Boolean isApplicable) {
		this.isApplicable = isApplicable;
	}

	public String getErrorCodeJson() {
		return errorCodeJson;
	}

	public void setErrorCodeJson(String errorCodeJson) {
		this.errorCodeJson = errorCodeJson;
	}

	public ErrWarMessage() {

	}

	public ErrWarMessage(String errorCodeJson, String shortName, String description) {
		this.shortName = shortName;
		this.description = description;
		this.errorCodeJson = errorCodeJson;
	}

	public ErrWarMessage(String shortName, String description, Long count) {
		this.shortName = shortName;
		this.description = description;
		this.count = count;
	}

	public ErrWarMessage(String shortName, String description, Boolean isApplicable) {
		this.shortName = shortName;
		this.description = description;
		this.isApplicable = isApplicable;
	}



	public Boolean getIsReadOnly() {
		return isReadOnly;
	}
	public void setIsReadOnly(Boolean isReadOnly) {
		this.isReadOnly = isReadOnly;
	}
	public Boolean getIsApplicableForClient() {
		return isApplicableForClient;
	}
	public void setIsApplicableForClient(Boolean isApplicableForClient) {
		this.isApplicableForClient = isApplicableForClient;
	}
	public ErrWarMessage(String shortName, String description, Boolean isReadOnly, Boolean isApplicableForClient) {
		this.shortName = shortName;
		this.description = description;
		this.isReadOnly = isReadOnly;
		this.isApplicableForClient = isApplicableForClient;
	}
	public String getEntityShortName() {
		return entityShortName;
	}
	public void setEntityShortName(String entityShortName) {
		this.entityShortName = entityShortName;
	}
	public String getEntityFullName() {
		return entityFullName;
	}
	public void setEntityFullName(String entityFullName) {
		this.entityFullName = entityFullName;
	}

}