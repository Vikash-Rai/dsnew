package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "operation_requesttype")
public class OperationRequestType {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "operation_requesttype_id")
	private Integer operationRequestTypeId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;


	public Integer getOperationRequestTypeId() {
		return operationRequestTypeId;
	}

	public void setOperationRequestTypeId(Integer operationRequestTypeId) {
		this.operationRequestTypeId = operationRequestTypeId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
}