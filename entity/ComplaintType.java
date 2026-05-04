package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "complaint_type")
public class ComplaintType {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "complaint_type_id")
	private Integer complaintTypeId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;


	public Integer getComplaintTypeId() {
		return complaintTypeId;
	}

	public void setComplaintTypeId(Integer complaintTypeId) {
		this.complaintTypeId = complaintTypeId;
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

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
}