package com.equabli.collectprism.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "conf", name = "client_statutes_of_limitation")
public class ClientStatutesOfLimitation {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "client_statutes_of_limitation_id")
	private Integer clientStatutesOfLimitationId;

	@Column(name = "sol_day")
	private Integer solDay;


	public Integer getClientStatutesOfLimitationId() {
		return clientStatutesOfLimitationId;
	}

	public void setClientStatutesOfLimitationId(Integer clientStatutesOfLimitationId) {
		this.clientStatutesOfLimitationId = clientStatutesOfLimitationId;
	}

	public Integer getSolDay() {
		return solDay;
	}

	public void setSolDay(Integer solDay) {
		this.solDay = solDay;
	}
}