package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(schema = "data", name = "chainoftitle")
public class ChainOfTitles implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chainoftitle_id")
	private Long chainOfTitleId;

	@Column(name = "dt_from", updatable = false)
	private LocalDate dtStart;

	@Column(name = "dt_till", updatable = false)
	private LocalDate dtEnd;


	public Long getChainOfTitleId() {
		return chainOfTitleId;
	}

	public void setChainOfTitleId(Long chainOfTitleId) {
		this.chainOfTitleId = chainOfTitleId;
	}

	public LocalDate getDtStart() {
		return dtStart;
	}

	public void setDtStart(LocalDate dtStart) {
		this.dtStart = dtStart;
	}

	public LocalDate getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(LocalDate dtEnd) {
		this.dtEnd = dtEnd;
	}

	public ChainOfTitles() {
	}

	public ChainOfTitles(Long chainOfTitleId, LocalDate dtStart, LocalDate dtEnd) {
		this.chainOfTitleId = chainOfTitleId;
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
	}
}