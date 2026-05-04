package com.equabli.collectprism.entity;

import java.io.Serializable;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "entity_attribute")
public class EntityAttribute   implements Serializable{

	private static final long serialVersionUID = 5631261585276442562L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "entity_attribute_id")
	private Integer entityAttributeId;
	
	@Column(name = "entity_short_name")
	private String entityShortName;
	
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Column(name = "is_client_inbound")
	private Boolean isClientInbound;
	
	@Column(name = "is_partner_inbound")
	private Boolean isPartnerInbound;

	public Integer getEntityAttributeId() {
		return entityAttributeId;
	}

	public void setEntityAttributeId(Integer entityAttributeId) {
		this.entityAttributeId = entityAttributeId;
	}

	public String getEntityShortName() {
		return entityShortName;
	}

	public void setEntityShortName(String entityShortName) {
		this.entityShortName = entityShortName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public Boolean getIsClientInbound() {
		return isClientInbound;
	}

	public void setIsClientInbound(Boolean isClientInbound) {
		this.isClientInbound = isClientInbound;
	}

	public Boolean getIsPartnerInbound() {
		return isPartnerInbound;
	}

	public void setIsPartnerInbound(Boolean isPartnerInbound) {
		this.isPartnerInbound = isPartnerInbound;
	}
	
	
}
