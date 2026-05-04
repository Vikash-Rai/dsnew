package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "record_status")
public class RecordStatus {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "short_name")
	private String shortName;


	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}