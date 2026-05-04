package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "cost")
public class Cost  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2357365005305869088L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cost_id")
	private Long costId;
	
	@Column(name = "record_type")
	private String recordType;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;
	
	@Column(name = "cost_type")
	private String costType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'cost_type' and lu.keycode = cost_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp costTypeLookUp;
	
	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;
	
	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;
	
	@Column(name = "dt_cost")
	private LocalDate dtCost;

	@Column(name = "amt_cost")
	private Double amtCost;

	@Column(name = "amt_principal")
	private Double amtPrincipal;

	@Column(name = "amt_interest")
	private Double amtInterest;

	@Column(name = "amt_latefee")
	private Double amtLatefee;

	@Column(name = "amt_otherfee")
	private Double amtOtherfee;

	@Column(name = "amt_courtcost")
	private Double amtCourtcost;

	@Column(name = "amt_attorneyfee")
	private Double amtAttorneyfee;

	@Formula(value = "(select js.dtm_utc_create from data.account account left join conf.job_schedule js on account.partner_job_schedule_id = js.job_schedule_id where account.client_id = client_id and account.client_account_number = client_account_number)")
	private LocalDate partnerPlacementDate;
	
	public LocalDate getPartnerPlacementDate() {
		return partnerPlacementDate;
	}

	public void setPartnerPlacementDate(LocalDate partnerPlacementDate) {
		this.partnerPlacementDate = partnerPlacementDate;
	}

	public Long getCostId() {
		return costId;
	}

	public void setCostId(Long costId) {
		this.costId = costId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public LookUp getCostTypeLookUp() {
		return costTypeLookUp;
	}

	public void setCostTypeLookUp(LookUp costTypeLookUp) {
		this.costTypeLookUp = costTypeLookUp;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public LocalDateTime getDtmUtcUpdate() {
		return dtmUtcUpdate;
	}

	public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
		this.dtmUtcUpdate = dtmUtcUpdate;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public LocalDate getDtCost() {
		return dtCost;
	}

	public void setDtCost(LocalDate dtCost) {
		this.dtCost = dtCost;
	}

	public Double getAmtCost() {
		return amtCost;
	}

	public void setAmtCost(Double amtCost) {
		this.amtCost = amtCost;
	}

	public Double getAmtPrincipal() {
		return amtPrincipal;
	}

	public void setAmtPrincipal(Double amtPrincipal) {
		this.amtPrincipal = amtPrincipal;
	}

	public Double getAmtInterest() {
		return amtInterest;
	}

	public void setAmtInterest(Double amtInterest) {
		this.amtInterest = amtInterest;
	}

	public Double getAmtLatefee() {
		return amtLatefee;
	}

	public void setAmtLatefee(Double amtLatefee) {
		this.amtLatefee = amtLatefee;
	}

	public Double getAmtOtherfee() {
		return amtOtherfee;
	}

	public void setAmtOtherfee(Double amtOtherfee) {
		this.amtOtherfee = amtOtherfee;
	}

	public Double getAmtCourtcost() {
		return amtCourtcost;
	}

	public void setAmtCourtcost(Double amtCourtcost) {
		this.amtCourtcost = amtCourtcost;
	}

	public Double getAmtAttorneyfee() {
		return amtAttorneyfee;
	}

	public void setAmtAttorneyfee(Double amtAttorneyfee) {
		this.amtAttorneyfee = amtAttorneyfee;
	}	
}