package com.equabli.collectprism.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(schema = "audt", name = "batch_job_instance")
public class BatchJobInstance {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "job_instance_id")
	private Long jobInstanceId;

	@Column(name = "job_name")
	private String jobName;

	@Column(name = "dtm_utc_action", updatable = false)
	private LocalDateTime dtmUtcAction;


	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public LocalDateTime getDtmUtcAction() {
		return dtmUtcAction;
	}

	public void setDtmUtcAction(LocalDateTime dtmUtcAction) {
		this.dtmUtcAction = dtmUtcAction;
	}
}