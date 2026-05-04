package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "payment_plan")
public class PaymentPlan implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "payment_plan_id")
	private Long paymentPlanId;

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

	@Column(name = "partner_plan_number")
	private Long partnerPlanNumber;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "payment_method")
	private String paymentMethod;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_method' and lu.keycode = payment_method and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentMethodLookUp;

	@Column(name = "payment_plan_status")
	private String paymentPlanStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_status' and lu.keycode = payment_plan_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentPlanStatusLookUp;

	@Column(name = "payment_plan_type")
	private String paymentPlanType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_type' and lu.keycode = payment_plan_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentPlanTypeLookUp;

	@Column(name = "pct_discount")
	private Double pctDiscount;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Column(name = "amt_settlement")
	private Double paymentSettlementAmt;

	@Column(name = "payment_plan_interval")
	private String paymentPlanInterval;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_interval' and lu.keycode = payment_plan_interval and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentPlanIntervalLookUp;

	@Column(name = "payment_plan_reason")
	private String paymentPlanReason;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_reason' and lu.keycode = payment_plan_reason and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentPlanReasonLookUp;

	@Column(name = "payment_plan_broken_reason")
	private String paymentPlanBrokenReason;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_broken_reason' and lu.keycode = payment_plan_broken_reason and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentPlanBrokenReasonLookUp;

	public Double getPaymentSettlementAmt() {
		return paymentSettlementAmt;
	}

	public void setPaymentSettlementAmt(Double paymentSettlementAmt) {
		this.paymentSettlementAmt = paymentSettlementAmt;
	}

	@Column(name = "count_payment")
	private Integer paymentCount;
	
	public Integer getPaymentCount() {
		return paymentCount;
	}

	public void setPaymentCount(Integer paymentCount) {
		this.paymentCount = paymentCount;
	}

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();
	
	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}
	
	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public PaymentPlan() {
	}

	public PaymentPlan(Long paymentPlanId) {
		this.paymentPlanId = paymentPlanId;
	}

	public Long getPaymentPlanId() {
		return paymentPlanId;
	}

	public void setPaymentPlanId(Long paymentPlanId) {
		this.paymentPlanId = paymentPlanId;
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

	public Long getPartnerPlanNumber() {
		return partnerPlanNumber;
	}

	public void setPartnerPlanNumber(Long partnerPlanNumber) {
		this.partnerPlanNumber = partnerPlanNumber;
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

	public String getPaymentPlanStatus() {
		return paymentPlanStatus;
	}

	public void setPaymentPlanStatus(String paymentPlanStatus) {
		this.paymentPlanStatus = paymentPlanStatus;
	}

	public LookUp getPaymentPlanStatusLookUp() {
		return paymentPlanStatusLookUp;
	}

	public void setPaymentPlanStatusLookUp(LookUp paymentPlanStatusLookUp) {
		this.paymentPlanStatusLookUp = paymentPlanStatusLookUp;
	}

	public String getPaymentPlanType() {
		return paymentPlanType;
	}

	public void setPaymentPlanType(String paymentPlanType) {
		this.paymentPlanType = paymentPlanType;
	}

	public LookUp getPaymentPlanTypeLookUp() {
		return paymentPlanTypeLookUp;
	}

	public void setPaymentPlanTypeLookUp(LookUp paymentPlanTypeLookUp) {
		this.paymentPlanTypeLookUp = paymentPlanTypeLookUp;
	}

	public Double getPctDiscount() {
		return pctDiscount;
	}

	public void setPctDiscount(Double pctDiscount) {
		this.pctDiscount = pctDiscount;
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

	public String getPaymentPlanInterval() {
		return paymentPlanInterval;
	}

	public void setPaymentPlanInterval(String paymentPlanInterval) {
		this.paymentPlanInterval = paymentPlanInterval;
	}

	public LookUp getPaymentPlanIntervalLookUp() {
		return paymentPlanIntervalLookUp;
	}

	public void setPaymentPlanIntervalLookUp(LookUp paymentPlanIntervalLookUp) {
		this.paymentPlanIntervalLookUp = paymentPlanIntervalLookUp;
	}

	public String getPaymentPlanReason() {
		return paymentPlanReason;
	}

	public void setPaymentPlanReason(String paymentPlanReason) {
		this.paymentPlanReason = paymentPlanReason;
	}

	public LookUp getPaymentPlanReasonLookUp() {
		return paymentPlanReasonLookUp;
	}

	public void setPaymentPlanReasonLookUp(LookUp paymentPlanReasonLookUp) {
		this.paymentPlanReasonLookUp = paymentPlanReasonLookUp;
	}

	public String getPaymentPlanBrokenReason() {
		return paymentPlanBrokenReason;
	}

	public void setPaymentPlanBrokenReason(String paymentPlanBrokenReason) {
		this.paymentPlanBrokenReason = paymentPlanBrokenReason;
	}

	public LookUp getPaymentPlanBrokenReasonLookUp() {
		return paymentPlanBrokenReasonLookUp;
	}

	public void setPaymentPlanBrokenReasonLookUp(LookUp paymentPlanBrokenReasonLookUp) {
		this.paymentPlanBrokenReasonLookUp = paymentPlanBrokenReasonLookUp;
	}
}