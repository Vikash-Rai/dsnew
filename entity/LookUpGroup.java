package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "lookup_group")
public class LookUpGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "lookup_group_id")
	private Integer lookupGroupId;

	@Column(name = "keyvalue")
	private String keyvalue;

	@Column(name = "record_status_id")
	private Integer recordStatusId;


	public Integer getLookupGroupId() {
		return lookupGroupId;
	}

	public void setLookupGroupId(Integer lookupGroupId) {
		this.lookupGroupId = lookupGroupId;
	}

	public String getKeyvalue() {
		return keyvalue;
	}

	public void setKeyvalue(String keyvalue) {
		this.keyvalue = keyvalue;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
}