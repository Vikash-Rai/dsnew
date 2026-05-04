package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "client")
public class Client {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	public Client(Integer clientId) {
		this.clientId = clientId;
	}

	public Client(Integer clientId, String shortName) {
		this.clientId = clientId;
		this.shortName = shortName;
	}

	public Client() {
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
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