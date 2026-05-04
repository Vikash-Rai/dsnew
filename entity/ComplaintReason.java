package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "complaint_reason")
public class ComplaintReason {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "complaint_reason_id")
	private Integer complaintReasonId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;


	public Integer getComplaintReasonId() {
		return complaintReasonId;
	}

	public void setComplaintReasonId(Integer complaintReasonId) {
		this.complaintReasonId = complaintReasonId;
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