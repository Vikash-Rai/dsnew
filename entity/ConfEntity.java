package com.equabli.collectprism.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "entity")
public class ConfEntity  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4259681222481892980L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_id")
	private Long entityId;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "full_name")
	private String fullName;

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
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

	public ConfEntity() {
		
	}
	
	public ConfEntity(Long entityId, String shortName, String fullName) {
		this.entityId = entityId;
		this.shortName = shortName;
		this.fullName = fullName;
	}
	
	
}
