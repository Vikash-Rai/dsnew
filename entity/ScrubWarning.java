package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "scrubwarning")
public class ScrubWarning implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scrubwarning_id")
	private Long scrubwarningId;

	@Column(name = "job_instance_id")
	private Long jobInstanceId;

	@Column(name = "entity_name")
	private String entityName;

	@Column(name = "entity_record_id")
	private String entityRecordId;

	@Column(name = "warning_short_name")
	private String warningShortName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "record_source_id")
	private Integer recordSourceId;

	@Column(name = "app_id")
	private Integer appId;


	public Long getScrubwarningId() {
		return scrubwarningId;
	}

	public void setScrubwarningId(Long scrubwarningId) {
		this.scrubwarningId = scrubwarningId;
	}

	public Long getJobInstanceId() {
		return jobInstanceId;
	}

	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityRecordId() {
		return entityRecordId;
	}

	public void setEntityRecordId(String entityRecordId) {
		this.entityRecordId = entityRecordId;
	}

	public String getWarningShortName() {
		return warningShortName;
	}

	public void setWarningShortName(String warningShortName) {
		this.warningShortName = warningShortName;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public Integer getRecordSourceId() {
		return recordSourceId;
	}

	public void setRecordSourceId(Integer recordSourceId) {
		this.recordSourceId = recordSourceId;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public static void setScrubWarning(Map<String, Object> validationMap, String entityName, String entityRecordId, String warningShortName, List<ScrubWarning> scrubWarningList, Integer appId, Integer recordSourceId) {
		if(validationMap.containsKey("jobExecutionId")) {
			ScrubWarning warning = new ScrubWarning(); 
			warning.setJobInstanceId(Long.parseLong(validationMap.get("jobExecutionId").toString()));
			warning.setEntityName(entityName);
			warning.setEntityRecordId(entityRecordId);
			warning.setWarningShortName(warningShortName);
			warning.setRecordStatusId(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId());
			warning.setRecordSourceId(recordSourceId);
			warning.setAppId(appId);

			scrubWarningList.add(warning);
		}
	}
}