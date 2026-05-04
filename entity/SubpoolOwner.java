package com.equabli.collectprism.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "subpool_owner")
public class SubpoolOwner implements Serializable {

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "subpool_owner_id")
	private Integer subpoolOwnerId;

	@Column(name = "owner_name")
	private String ownerName;

	@Column(name = "record_status_id")
	private Integer recordStatusId;


	public Integer getSubpoolOwnerId() {
		return subpoolOwnerId;
	}

	public void setSubpoolOwnerId(Integer subpoolOwnerId) {
		this.subpoolOwnerId = subpoolOwnerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}
}