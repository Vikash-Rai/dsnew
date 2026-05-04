package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "product")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "short_name")
	private String shortName;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "debtcategory_id")
	private Integer debtCategoryId;

	@Column(name = "scrubtype")
	private Integer scrubType;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	public Product() {
		
	}

	public Product(Integer productId, String shortName) {
		this.productId = productId;
		this.shortName = shortName;
	}

	public Product(Integer productId) {
		this.productId = productId;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public Integer getDebtCategoryId() {
		return debtCategoryId;
	}

	public void setDebtCategoryId(Integer debtCategoryId) {
		this.debtCategoryId = debtCategoryId;
	}

	public Integer getScrubType() {
		return scrubType;
	}

	public void setScrubType(Integer scrubType) {
		this.scrubType = scrubType;
	}
}