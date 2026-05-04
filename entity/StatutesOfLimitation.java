package com.equabli.collectprism.entity;

import jakarta.persistence.*;
@Entity
@Table(schema = "conf", name = "statutes_of_limitation")
public class StatutesOfLimitation {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "statutes_of_limitation_id")
	private Integer statutesOfLimitationId;

	@Column(name = "sol_month")
	private Integer solMonth;


	public Integer getStatutesOfLimitationId() {
		return statutesOfLimitationId;
	}

	public void setStatutesOfLimitationId(Integer statutesOfLimitationId) {
		this.statutesOfLimitationId = statutesOfLimitationId;
	}

	public Integer getSolMonth() {
		return solMonth;
	}

	public void setSolMonth(Integer solMonth) {
		this.solMonth = solMonth;
	}
}