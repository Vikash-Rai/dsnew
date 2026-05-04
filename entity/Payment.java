package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(schema = "data", name = "payment")
public class Payment implements Serializable {
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	public static volatile Map<String, Payment> paymentRecord = new HashMap<String, Payment>();

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id")
	private Long paymentId;

	@Column(name = "payment_plan_id")
	private Long paymentPlanId;

	@Column(name = "partner_plan_number")
	private Long partnerPlanNumber;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select pp.payment_plan_id from data.payment_plan pp join conf.record_status rs on pp.record_status_id = rs.record_status_id and rs.short_name = ('"+ConfRecordStatus.ENABLED+"') where pp.partner_id = partner_id and pp.partner_plan_number = partner_plan_number)")
	private PaymentPlan paymentPlan;

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

	@Column(name = "payment_type")
	private String paymentType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_type' and lu.keycode = payment_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentTypeLookUp;

	@Column(name = "payment_status")
	private String paymentStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_status' and lu.keycode = payment_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentStatusLookUp;

	@Column(name = "payment_broken_reason")
	private String paymentBrokenReason;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_plan_broken_reason' and lu.keycode = payment_broken_reason and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentBrokenReasonLookUp;

	@Column(name = "dt_payment")
	private LocalDate paymentDate;

	@Column(name = "amt_payment")
	private Double amtPayment;

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

	@Column(name = "amt_balance")
	private Double amtBalance;

	@Column(name = "amt_attorneyfee")
	private Double amtAttorneyfee;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "partner_system_id")
	private String partnerSystemId;

	@Column(name = "reversal_parent_id")
	private String reversalParentId;

	@Column(name = "dt_reversal")
	private LocalDateTime reversalDate;

	@Column(name = "parent_payment_id")
	private Long parentPaymentId;

	@Column(name = "payment_source")
	private String paymentSource;
	
	@Column(name = "external_system_id")
	private String clientPaymentId;

	@Column(name = "amt_currentbalance")
	private Double paymentAmtCurrentBalance;

	@Transient
	private Double parentOtherFees;

	@Transient
	private Double parentInterest;

	@Transient
	private Double parentPrincipal;

	@Transient
	private Double parentLateFees;
	
	@Transient
	private Double parentCourtCost;
	
	@Transient
	private Double parentAttorneyFees;

	@Transient
	private Long parentPaymentIds;

	@Formula(value = "(select count(pa.payment_id) from data.payment pa where case when payment_source = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' then pa.client_id = client_id and pa.external_system_id = external_system_id else pa.partner_id = partner_id and pa.partner_system_id = partner_system_id end)")
	private Integer isParentPaymentExist;

	@Formula(value = "(select count(pa.payment_id) from data.payment pa where case when payment_source = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' then pa.client_id = client_id and pa.reversal_parent_id = reversal_parent_id else pa.partner_id = partner_id and pa.reversal_parent_id = reversal_parent_id end and pa.payment_type = '"+CommonConstants.PAYMENT_TYPE_NSF+"')")
	private Integer isNSFDeDup;

	@Formula(value = "(select cl.full_name from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private String clientName;

	@Formula(value = "(select pr.full_name from conf.partner pr join conf.record_status rs on pr.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where pr.partner_id = partner_id)")
	private String partnerName;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Column(name = "record_type")
	private String recordType;
	
	@Column(name = "payment_serial")
	private Integer paymentSerial;
	
	@Column(name = "dt_payment_posting")
	private LocalDate dtPaymentPosting;
	
	@Column(name = "approval_code")
	private String approvalCode;
	
	@Column(name = "partner_batch_number")
	private String partnerBatchNumber;
	
	@Column(name = "posting_number")
	private String postingNumber;
	
	@Column(name = "gateway_vendor")
	private String gatewayVendor;
	
	@Column(name = "count_balance_payment")
	private Integer countBalancePayment;

	@Column(name = "record_source_id")
	private Integer recordSourceId;
	
	@Column(name = "payment_settlement_type")
	private String paymentSettlementType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'payment_settlement_type' and lu.keycode = payment_settlement_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp paymentSettlementTypeLookUp;

	@Column(name = "is_commissionable")
	private Boolean isCommissionable;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select acc.partner_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer partnerIds;

	@Formula(value = "(select acc.amt_otherfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtOtherfeeCurrentbalance;

	@Formula(value = "(select acc.amt_interest_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtInterestCurrentbalance;

	@Formula(value = "(select acc.amt_principal_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPrincipalCurrentbalance;

	@Formula(value = "(select acc.amt_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCurrentbalance;

	@Formula(value = "(select cs.dt_from from data.cot_servicer cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.account_id = (select acc.account_id from data.account acc where acc.client_id = client_id and acc.client_account_number = client_account_number) and cs.partner_id = partner_id order by cs.dt_from desc limit 1)")
	private LocalDate cotDtFrom;

	@Formula(value = "(select cs.dt_till from data.cot_servicer cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.account_id = (select acc.account_id from data.account acc where acc.client_id = client_id and acc.client_account_number = client_account_number) and cs.partner_id = partner_id order by cs.dt_till desc limit 1)")
	private LocalDate cotDtTill;

	@Formula(value = "(select acc.amt_pre_charge_off_principle from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffPrinciple;

	@Formula(value = "(select acc.amt_pre_charge_off_interest from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffInterest;

	@Formula(value = "(select acc.amt_pre_charge_off_fee from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffFee;

	@Formula(value = "(select acc.amt_latefee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtLatefeeCurrentbalance;

	@Formula(value = "(select acc.amt_courtcost_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCourtCostCurrentbalance;

	@Formula(value = "(select acc.amt_attorneyfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtAttorneyFeeCurrentBalance;

	@Formula(value = "(select acc.amt_pre_charge_off_balance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPreChargeOffBalance;

	@Formula(value = "(select acc.amt_post_charge_off_interest from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffInterest;

	@Formula(value = "(select acc.amt_post_charge_off_fees from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffFees;

	@Formula(value = "(select acc.amt_post_charge_off_payment from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffPayment;

	@Formula(value = "(select acc.amt_post_charge_off_credit from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPostChargeOffCredit;

	@Formula(value = "(select acc.dt_charge_off from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate dtChargeOff;
	
	@Column(name = "app_id")
	private Integer appId;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "pct_partner_commission")
	private Double pctPartnerCommission;


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select pp.payment_plan_id from data.payment_plan pp join conf.record_status rs on pp.record_status_id = rs.record_status_id and rs.short_name = ('"+ConfRecordStatus.ENABLED+"') where pp.client_id = client_id and pp.client_account_number = client_account_number and pp.payment_plan_status in ('I', 'V', 'C', 'O', 'Z') )")
	private PaymentPlan activePaymentPlan;
	
	@Formula(value = "(select acc.pct_partner_commission from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double accPctPartnerCommission;
	
	
	public Double getAccPctPartnerCommission() {
		return accPctPartnerCommission;
	}

	public void setAccPctPartnerCommission(Double accPctPartnerCommission) {
		this.accPctPartnerCommission = accPctPartnerCommission;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Double getAmtPreChargeOffPrinciple() {
		return amtPreChargeOffPrinciple;
	}

	public void setAmtPreChargeOffPrinciple(Double amtPreChargeOffPrinciple) {
		this.amtPreChargeOffPrinciple = amtPreChargeOffPrinciple;
	}

	public Double getAmtPreChargeOffInterest() {
		return amtPreChargeOffInterest;
	}

	public void setAmtPreChargeOffInterest(Double amtPreChargeOffInterest) {
		this.amtPreChargeOffInterest = amtPreChargeOffInterest;
	}

	public Double getAmtPreChargeOffFee() {
		return amtPreChargeOffFee;
	}

	public void setAmtPreChargeOffFee(Double amtPreChargeOffFee) {
		this.amtPreChargeOffFee = amtPreChargeOffFee;
	}

	public Double getAmtLatefeeCurrentbalance() {
		return amtLatefeeCurrentbalance;
	}

	public void setAmtLatefeeCurrentbalance(Double amtLatefeeCurrentbalance) {
		this.amtLatefeeCurrentbalance = amtLatefeeCurrentbalance;
	}

	public Double getAmtCourtCostCurrentbalance() {
		return amtCourtCostCurrentbalance;
	}

	public void setAmtCourtCostCurrentbalance(Double amtCourtCostCurrentbalance) {
		this.amtCourtCostCurrentbalance = amtCourtCostCurrentbalance;
	}

	public Double getAmtAttorneyFeeCurrentBalance() {
		return amtAttorneyFeeCurrentBalance;
	}

	public void setAmtAttorneyFeeCurrentBalance(Double amtAttorneyFeeCurrentBalance) {
		this.amtAttorneyFeeCurrentBalance = amtAttorneyFeeCurrentBalance;
	}

	public Double getAmtPreChargeOffBalance() {
		return amtPreChargeOffBalance;
	}

	public void setAmtPreChargeOffBalance(Double amtPreChargeOffBalance) {
		this.amtPreChargeOffBalance = amtPreChargeOffBalance;
	}

	public Double getAmtPostChargeOffInterest() {
		return amtPostChargeOffInterest;
	}

	public void setAmtPostChargeOffInterest(Double amtPostChargeOffInterest) {
		this.amtPostChargeOffInterest = amtPostChargeOffInterest;
	}

	public Double getAmtPostChargeOffFees() {
		return amtPostChargeOffFees;
	}

	public void setAmtPostChargeOffFees(Double amtPostChargeOffFees) {
		this.amtPostChargeOffFees = amtPostChargeOffFees;
	}

	public Double getAmtPostChargeOffPayment() {
		return amtPostChargeOffPayment;
	}

	public void setAmtPostChargeOffPayment(Double amtPostChargeOffPayment) {
		this.amtPostChargeOffPayment = amtPostChargeOffPayment;
	}

	public Double getAmtPostChargeOffCredit() {
		return amtPostChargeOffCredit;
	}

	public void setAmtPostChargeOffCredit(Double amtPostChargeOffCredit) {
		this.amtPostChargeOffCredit = amtPostChargeOffCredit;
	}

	public LocalDate getDtChargeOff() {
		return dtChargeOff;
	}

	public void setDtChargeOff(LocalDate dtChargeOff) {
		this.dtChargeOff = dtChargeOff;
	}

	public LocalDate getCotDtFrom() {
		return cotDtFrom;
	}

	public void setCotDtFrom(LocalDate cotDtFrom) {
		this.cotDtFrom = cotDtFrom;
	}

	public LocalDate getCotDtTill() {
		return cotDtTill;
	}

	public void setCotDtTill(LocalDate cotDtTill) {
		this.cotDtTill = cotDtTill;
	}

	public Boolean getIsCommissionable() {
		return isCommissionable;
	}

	public void setIsCommissionable(Boolean isCommissionable) {
		this.isCommissionable = isCommissionable;
	}

	public String getPaymentSettlementType() {
		return paymentSettlementType;
	}

	public void setPaymentSettlementType(String paymentSettlementType) {
		this.paymentSettlementType = paymentSettlementType;
	}

	public LookUp getPaymentSettlementTypeLookUp() {
		return paymentSettlementTypeLookUp;
	}

	public void setPaymentSettlementTypeLookUp(LookUp paymentSettlementTypeLookUp) {
		this.paymentSettlementTypeLookUp = paymentSettlementTypeLookUp;
	}

	public Long getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	public Long getPaymentPlanId() {
		return paymentPlanId;
	}

	public void setPaymentPlanId(Long paymentPlanId) {
		this.paymentPlanId = paymentPlanId;
	}

	public PaymentPlan getPaymentPlan() {
		return paymentPlan;
	}

	public void setPaymentPlan(PaymentPlan paymentPlan) {
		this.paymentPlan = paymentPlan;
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

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public LookUp getPaymentTypeLookUp() {
		return paymentTypeLookUp;
	}

	public void setPaymentTypeLookUp(LookUp paymentTypeLookUp) {
		this.paymentTypeLookUp = paymentTypeLookUp;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public LookUp getPaymentStatusLookUp() {
		return paymentStatusLookUp;
	}

	public void setPaymentStatusLookUp(LookUp paymentStatusLookUp) {
		this.paymentStatusLookUp = paymentStatusLookUp;
	}

	public String getPaymentBrokenReason() {
		return paymentBrokenReason;
	}

	public void setPaymentBrokenReason(String paymentBrokenReason) {
		this.paymentBrokenReason = paymentBrokenReason;
	}

	public LookUp getPaymentBrokenReasonLookUp() {
		return paymentBrokenReasonLookUp;
	}

	public void setPaymentBrokenReasonLookUp(LookUp paymentBrokenReasonLookUp) {
		this.paymentBrokenReasonLookUp = paymentBrokenReasonLookUp;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Double getAmtPayment() {
		return amtPayment;
	}

	public void setAmtPayment(Double amtPayment) {
		this.amtPayment = amtPayment;
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

	public Double getAmtBalance() {
		return amtBalance;
	}

	public void setAmtBalance(Double amtBalance) {
		this.amtBalance = amtBalance;
	}

	public Double getAmtAttorneyfee() {
		return amtAttorneyfee;
	}

	public void setAmtAttorneyfee(Double amtAttorneyfee) {
		this.amtAttorneyfee = amtAttorneyfee;
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

	public String getReversalParentId() {
		return reversalParentId;
	}

	public void setReversalParentId(String reversalParentId) {
		this.reversalParentId = reversalParentId;
	}

	public String getPartnerSystemId() {
		return partnerSystemId;
	}

	public void setPartnerSystemId(String partnerSystemId) {
		this.partnerSystemId = partnerSystemId;
	}

	public LocalDateTime getReversalDate() {
		return reversalDate;
	}

	public void setReversalDate(LocalDateTime reversalDate) {
		this.reversalDate = reversalDate;
	}

	public Long getParentPaymentId() {
		return parentPaymentId;
	}

	public void setParentPaymentId(Long parentPaymentId) {
		this.parentPaymentId = parentPaymentId;
	}

	public String getPaymentSource() {
		return paymentSource;
	}

	public void setPaymentSource(String paymentSource) {
		this.paymentSource = paymentSource;
	}

	public Long getParentPaymentIds() {
		return parentPaymentIds;
	}

	public void setParentPaymentIds(Long parentPaymentIds) {
		this.parentPaymentIds = parentPaymentIds;
	}

	public Integer getIsParentPaymentExist() {
		return isParentPaymentExist;
	}

	public void setIsParentPaymentExist(Integer isParentPaymentExist) {
		this.isParentPaymentExist = isParentPaymentExist;
	}

	public Integer getIsNSFDeDup() {
		return isNSFDeDup;
	}

	public void setIsNSFDeDup(Integer isNSFDeDup) {
		this.isNSFDeDup = isNSFDeDup;
	}

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

	public Long getPartnerPlanNumber() {
		return partnerPlanNumber;
	}

	public void setPartnerPlanNumber(Long partnerPlanNumber) {
		this.partnerPlanNumber = partnerPlanNumber;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Integer getPaymentSerial() {
		return paymentSerial;
	}

	public void setPaymentSerial(Integer paymentSerial) {
		this.paymentSerial = paymentSerial;
	}

	public LocalDate getDtPaymentPosting() {
		return dtPaymentPosting;
	}

	public void setDtPaymentPosting(LocalDate dtPaymentPosting) {
		this.dtPaymentPosting = dtPaymentPosting;
	}

	public String getApprovalCode() {
		return approvalCode;
	}

	public void setApprovalCode(String approvalCode) {
		this.approvalCode = approvalCode;
	}

	public String getPartnerBatchNumber() {
		return partnerBatchNumber;
	}

	public void setPartnerBatchNumber(String partnerBatchNumber) {
		this.partnerBatchNumber = partnerBatchNumber;
	}

	public String getPostingNumber() {
		return postingNumber;
	}

	public void setPostingNumber(String postingNumber) {
		this.postingNumber = postingNumber;
	}

	public String getGatewayVendor() {
		return gatewayVendor;
	}

	public void setGatewayVendor(String gatewayVendor) {
		this.gatewayVendor = gatewayVendor;
	}

	public Integer getCountBalancePayment() {
		return countBalancePayment;
	}

	public void setCountBalancePayment(Integer countBalancePayment) {
		this.countBalancePayment = countBalancePayment;
	}

	public Integer getRecordSourceId() {
		return recordSourceId;
	}

	public void setRecordSourceId(Integer recordSourceId) {
		this.recordSourceId = recordSourceId;
	}

	public Double getParentOtherFees() {
		return parentOtherFees;
	}

	public void setParentOtherFees(Double parentOtherFees) {
		this.parentOtherFees = parentOtherFees;
	}

	public Double getParentInterest() {
		return parentInterest;
	}

	public void setParentInterest(Double parentInterest) {
		this.parentInterest = parentInterest;
	}

	public Double getParentPrincipal() {
		return parentPrincipal;
	}

	public void setParentPrincipal(Double parentPrincipal) {
		this.parentPrincipal = parentPrincipal;
	}

	public Double getParentLateFees() {
		return parentLateFees;
	}

	public void setParentLateFees(Double parentLateFees) {
		this.parentLateFees = parentLateFees;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Integer getPartnerIds() {
		return partnerIds;
	}

	public void setPartnerIds(Integer partnerIds) {
		this.partnerIds = partnerIds;
	}

	public Double getAmtOtherfeeCurrentbalance() {
		return amtOtherfeeCurrentbalance;
	}

	public void setAmtOtherfeeCurrentbalance(Double amtOtherfeeCurrentbalance) {
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Double getAmtInterestCurrentbalance() {
		return amtInterestCurrentbalance;
	}

	public void setAmtInterestCurrentbalance(Double amtInterestCurrentbalance) {
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
	}

	public Double getAmtPrincipalCurrentbalance() {
		return amtPrincipalCurrentbalance;
	}

	public void setAmtPrincipalCurrentbalance(Double amtPrincipalCurrentbalance) {
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
	}

	public Double getAmtCurrentbalance() {
		return amtCurrentbalance;
	}

	public void setAmtCurrentbalance(Double amtCurrentbalance) {
		this.amtCurrentbalance = amtCurrentbalance;
	}

	public String getClientPaymentId() {
		return clientPaymentId;
	}

	public void setClientPaymentId(String clientPaymentId) {
		this.clientPaymentId = clientPaymentId;
	}

	public static Map<String, Payment> getPaymentRecord() {
		return paymentRecord;
	}

	public static void setPaymentRecord(Map<String, Payment> paymentRecord) {
		Payment.paymentRecord = paymentRecord;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Double getPaymentAmtCurrentBalance() {
		return paymentAmtCurrentBalance;
	}

	public void setPaymentAmtCurrentBalance(Double paymentAmtCurrentBalance) {
		this.paymentAmtCurrentBalance = paymentAmtCurrentBalance;
	}

	public static MapSqlParameterSource getParamsValue(Payment payment) {
		MapSqlParameterSource sqlParamValue = new MapSqlParameterSource();

	    Field[] allFields = payment.getClass().getDeclaredFields();
	    for (Field field : allFields) {
			try {
		        sqlParamValue.addValue(field.getName(), field.get(payment));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
		return sqlParamValue;
	}

	public Payment() {

	}

	public Payment(Long paymentId, Double amtOtherfee, Double amtInterest, Double amtPrincipal, Double amtLatefee, Double amtCourtcost, Double amtAttorneyfee,Double pctPartnerCommission) {
		this.paymentId = paymentId;
		this.amtPrincipal = amtPrincipal;
		this.amtInterest = amtInterest;
		this.amtLatefee = amtLatefee;
		this.amtOtherfee = amtOtherfee;
		this.pctPartnerCommission = pctPartnerCommission;
	}

	public Payment(Long paymentId, Integer clientId, String clientAccountNumber) {
		this.paymentId = paymentId;
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
	}

	public Payment(String errwarShortName, String errwarDescription, Long accountId, Long paymentId, String clientAccountNumber, String clientName,String partnerName, Long paymentPlanId, 
			Integer paymentSerial, Double amtPayment, String paymentMethod, LocalDate paymentDate,String paymentStatus, String paymentType, LocalDate dtPaymentPosting, Double amtBalance, 
			String paymentBrokenReason, String approvalCode, String partnerBatchNumber, String postingNumber, String gatewayVendor,Integer countBalancePayment, Double amtPrincipal, 
			Double amtInterest, Double amtLatefee, Double amtOtherfee, Double amtCourtcost, Double amtAttorneyfee, String clientPaymentId, String reversalParentId, 
			String paymentSettlementType,Boolean isCommissionable, String paymentSource, Integer clientId) {
		ErrWarJson errWarJson = new ErrWarJson();
		errWarJson.setKey(errwarShortName);
		errWarJson.setValue(errwarDescription);
		this.addErrWarJson(errWarJson);
		this.accountId = accountId;
		this.paymentId = paymentId;
		this.clientAccountNumber = clientAccountNumber;
		this.clientName = clientName;
		this.partnerName = partnerName;
		this.paymentPlanId = paymentPlanId;
		this.paymentSerial = paymentSerial;
		this.amtPayment = amtPayment;
		this.paymentMethod = paymentMethod;
		this.paymentDate = (!CommonUtils.isDateNull(paymentDate)) ? LocalDate.parse(paymentDate.format(CommonConstants.DATE_FORMAT_MM_DD_YYYY), CommonConstants.DATE_FORMAT_MM_DD_YYYY) : null;
		this.paymentStatus = paymentStatus;
		this.paymentType = paymentType;
		this.dtPaymentPosting = (!CommonUtils.isDateNull(dtPaymentPosting)) ? LocalDate.parse(dtPaymentPosting.format(CommonConstants.DATE_FORMAT_MM_DD_YYYY), CommonConstants.DATE_FORMAT_MM_DD_YYYY) : null;
		this.amtBalance = amtBalance;
		this.paymentBrokenReason = paymentBrokenReason;
		this.approvalCode = approvalCode;
		this.partnerBatchNumber = partnerBatchNumber;
		this.postingNumber = postingNumber;
		this.gatewayVendor = gatewayVendor;
		this.countBalancePayment = countBalancePayment;
		this.amtPrincipal = amtPrincipal;
		this.amtInterest = amtInterest;
		this.amtLatefee = amtLatefee;
		this.amtOtherfee = amtOtherfee;
		this.amtCourtcost = amtCourtcost;
		this.amtAttorneyfee = amtAttorneyfee;
		this.clientPaymentId = clientPaymentId;
		this.reversalParentId = reversalParentId;
		this.paymentSettlementType = paymentSettlementType;
		this.isCommissionable = isCommissionable;
		this.paymentSource = paymentSource;
		this.clientId = clientId;
	}

	public Payment(PaymentPlan paymentPlan, Client client, Partner partner, LookUp paymentMethodLookUp, LookUp paymentTypeLookUp, LookUp paymentStatusLookUp, LookUp paymentBrokenReasonLookUp,
				   RecordStatus recordStatus, Long parentPaymentId, Double parentOtherFees, Double parentInterest, Double parentPrincipal, Double parentLateFees, Long parentPaymentIds,
				   Integer isNSFDeDup, Long accountIds, Integer partnerIds, Double amtOtherfeeCurrentbalance, Double amtInterestCurrentbalance, Double amtPrincipalCurrentbalance,
				   Double amtCurrentbalance, LocalDate cotDtFrom, LocalDate cotDtTill) {
		this.paymentPlan = paymentPlan;
		this.client = client;
		this.partner = partner;
		this.paymentMethodLookUp = paymentMethodLookUp;
		this.paymentTypeLookUp = paymentTypeLookUp;
		this.paymentStatusLookUp = paymentStatusLookUp;
		this.paymentBrokenReasonLookUp = paymentBrokenReasonLookUp;
		this.recordStatus = recordStatus;
		this.parentPaymentId = parentPaymentId;
		this.parentOtherFees = parentOtherFees;
		this.parentInterest = parentInterest;
		this.parentPrincipal = parentPrincipal;
		this.parentLateFees = parentLateFees;
		this.parentPaymentIds = parentPaymentIds;
		this.isNSFDeDup = isNSFDeDup;
		this.accountIds = accountIds;
		this.partnerIds = partnerIds;
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
		this.amtCurrentbalance = amtCurrentbalance;
		this.cotDtFrom = cotDtFrom;
		this.cotDtTill = cotDtTill;
	}
	
	public static Payment getCancelPayment(Payment payment) {
		Payment canPayment = new Payment();
		canPayment.setPaymentType(CommonConstants.PAYMENT_TYPE_CANCEL);
		canPayment.setRecordType(payment.getRecordType());
		canPayment.setPaymentPlanId(payment.getPaymentPlanId());
		canPayment.setClientId(payment.getClientId());
		canPayment.setPartnerId(payment.getPartnerId());
		canPayment.setPartnerPlanNumber(payment.getPartnerPlanNumber());
		canPayment.setAccountId(payment.getAccountId());
		canPayment.setClientAccountNumber(payment.getClientAccountNumber());
		canPayment.setPaymentSerial(payment.getPaymentSerial());
		canPayment.setPaymentDate(payment.getPaymentDate());
		canPayment.setAmtPayment(payment.getAmtPayment());
		canPayment.setDtPaymentPosting(payment.getDtPaymentPosting());
		canPayment.setAmtBalance(payment.getAmtBalance());
		canPayment.setPaymentMethod(payment.getPaymentMethod());
		canPayment.setPaymentStatus(payment.getPaymentStatus());
		canPayment.setPaymentBrokenReason(payment.getPaymentBrokenReason());
		canPayment.setApprovalCode(payment.getApprovalCode());
		canPayment.setPartnerBatchNumber(payment.getPartnerBatchNumber());
		canPayment.setPostingNumber(payment.getPostingNumber());
		canPayment.setGatewayVendor(payment.getGatewayVendor());
		canPayment.setCountBalancePayment(payment.getCountBalancePayment());
		canPayment.setRecordStatusId(payment.getRecordStatusId());
		canPayment.setRecordSourceId(payment.getRecordSourceId());
		canPayment.setAmtPrincipal(payment.getAmtPrincipal());
		canPayment.setAmtInterest(payment.getAmtInterest());
		canPayment.setAmtLatefee(payment.getAmtLatefee());
		canPayment.setAmtCourtcost(payment.getAmtCourtcost());
		canPayment.setAmtAttorneyfee(payment.getAmtAttorneyfee());
		canPayment.setPaymentSource(payment.getPaymentSource());
		return canPayment;
	}

	public PaymentPlan getActivePaymentPlan() {
		return activePaymentPlan;
	}

	public void setActivePaymentPlan(PaymentPlan activePaymentPlan) {
		this.activePaymentPlan = activePaymentPlan;
	}

	public Double getParentCourtCost() {
		return parentCourtCost;
	}

	public void setParentCourtCost(Double parentCourtCost) {
		this.parentCourtCost = parentCourtCost;
	}

	public Double getParentAttorneyFees() {
		return parentAttorneyFees;
	}

	public void setParentAttorneyFees(Double parentAttorneyFees) {
		this.parentAttorneyFees = parentAttorneyFees;
	}

	public Double getPctPartnerCommission() {
		return pctPartnerCommission;
	}

	public void setPctPartnerCommission(Double pctPartnerCommission) {
		this.pctPartnerCommission = pctPartnerCommission;
	}
}
