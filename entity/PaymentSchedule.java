package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "payment_schedule")
public class PaymentSchedule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "payment_schedule_id")
	private Long paymentScheduleId;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "partner_id")
	private Integer partnerId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select pr.partner_id from conf.partner pr join conf.record_status rs on pr.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where pr.partner_id = partner_id)")
	private Partner partner;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "payment_method")
	private String paymentMethod;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_method' and lu.keycode = payment_method and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentMethodLookUp;

	@Column(name = "payment_schedule_status")
	private String paymentScheduleStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_schedule_status' and lu.keycode = payment_schedule_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentScheduleStatusLookUp;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Column(name = "partner_plan_number")
	private Long partnerPlanNumber;
	
	@Column(name = "amt_payment")
	private Double amtPayment;
	
	public Long getPartnerPlanNumber() {
		return partnerPlanNumber;
	}

	public void setPartnerPlanNumber(Long partnerPlanNumber) {
		this.partnerPlanNumber = partnerPlanNumber;
	}

	public Double getAmtPayment() {
		return amtPayment;
	}

	public void setAmtPayment(Double amtPayment) {
		this.amtPayment = amtPayment;
	}

	public Long getPaymentScheduleId() {
		return paymentScheduleId;
	}

	public void setPaymentScheduleId(Long paymentScheduleId) {
		this.paymentScheduleId = paymentScheduleId;
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

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
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

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public LookUp getPaymentMethodLookUp() {
		return paymentMethodLookUp;
	}

	public void setPaymentMethodLookUp(LookUp paymentMethodLookUp) {
		this.paymentMethodLookUp = paymentMethodLookUp;
	}

	public String getPaymentScheduleStatus() {
		return paymentScheduleStatus;
	}

	public void setPaymentScheduleStatus(String paymentScheduleStatus) {
		this.paymentScheduleStatus = paymentScheduleStatus;
	}

	public LookUp getPaymentScheduleStatusLookUp() {
		return paymentScheduleStatusLookUp;
	}

	public void setPaymentScheduleStatusLookUp(LookUp paymentScheduleStatusLookUp) {
		this.paymentScheduleStatusLookUp = paymentScheduleStatusLookUp;
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
}