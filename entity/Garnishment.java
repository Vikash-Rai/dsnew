package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "legal_garnishment")
@Convert(attributeName = "json", converter = JsonType.class)
public class Garnishment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "legal_garnishment_id")
	private Long legalGarnishmentId;

    @Column(name = "record_type")
    private String recordType;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "legal_placement_id")
	private Long legalPlacementId;

	@Column(name  = "casenumber")
	private String caseNumber;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dt_garnishment", updatable = false)
	private LocalDate dtGarnishment;

	@Column(name  = "garnishment_type")
	private String garnishmentType;

	@Column(name = "amt_garnishment")
	private Double amtGarnishment;

	@Column(name  = "garnishment_approver")
	private String garnishmentApprover;

	@Column(name  = "garnishment_frequency")
	private String garnishmentFrequency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'garnishment_frequency' and lu.keycode = garnishment_frequency and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp garnishmentFrequencyLookUp;

	@Column(name = "garnishment_status")
	private String garnishmentStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'garnishment_status' and lu.keycode = garnishment_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp garnishmentStatusLookUp;

	@Column(name = "employer_id")
	private Long employerId;

	@Column(name = "account_asset_id")
	private Long accountAssetId;

	@Column(name = "asset_external_system_id")
	private Long assetExternalSystemId;

	@Column(name = "payment_plan_id")
	private Long paymentPlanId;
	
	@Column(name = "partner_plan_number")
	private Long partnerPlanNumber;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select lp.legal_placement_id from data.legal_placement lp join conf.record_status rs on lp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.casenumber = casenumber and (lp.suit_status is null or lp.suit_status = true) and (lp.judgment_status is null or (lp.judgment_status != 'DD' and lp.judgment_status != 'DP')))")
	private Long legalPlacementIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@Formula(value = "(select emp.employer_id from data.employer emp join conf.record_status rs on emp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where emp.client_id = client_id and emp.client_account_number = client_account_number and emp.client_consumer_number = client_consumer_number)")
	private Long employerIds;

	@Formula(value = "(select aa.account_asset_id from data.account_asset aa join conf.record_status rs on aa.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where aa.client_id = client_id and aa.client_account_number = client_account_number and aa.external_system_id = asset_external_system_id)")
	private Long accountAssetIds;


	public Garnishment() {
	}

	public Long getLegalGarnishmentId() {
		return legalGarnishmentId;
	}

	public void setLegalGarnishmentId(Long legalGarnishmentId) {
		this.legalGarnishmentId = legalGarnishmentId;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
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

	public Long getClientConsumerNumber() {
		return clientConsumerNumber;
	}

	public void setClientConsumerNumber(Long clientConsumerNumber) {
		this.clientConsumerNumber = clientConsumerNumber;
	}

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public Long getLegalPlacementId() {
		return legalPlacementId;
	}

	public void setLegalPlacementId(Long legalPlacementId) {
		this.legalPlacementId = legalPlacementId;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public LocalDate getDtGarnishment() {
		return dtGarnishment;
	}

	public void setDtGarnishment(LocalDate dtGarnishment) {
		this.dtGarnishment = dtGarnishment;
	}

	public String getGarnishmentType() {
		return garnishmentType;
	}

	public void setGarnishmentType(String garnishmentType) {
		this.garnishmentType = garnishmentType;
	}

	public Double getAmtGarnishment() {
		return amtGarnishment;
	}

	public void setAmtGarnishment(Double amtGarnishment) {
		this.amtGarnishment = amtGarnishment;
	}

	public String getGarnishmentApprover() {
		return garnishmentApprover;
	}

	public void setGarnishmentApprover(String garnishmentApprover) {
		this.garnishmentApprover = garnishmentApprover;
	}

	public String getGarnishmentFrequency() {
		return garnishmentFrequency;
	}

	public void setGarnishmentFrequency(String garnishmentFrequency) {
		this.garnishmentFrequency = garnishmentFrequency;
	}

	public String getGarnishmentStatus() {
		return garnishmentStatus;
	}

	public void setGarnishmentStatus(String garnishmentStatus) {
		this.garnishmentStatus = garnishmentStatus;
	}

	public LookUp getGarnishmentFrequencyLookUp() {
		return garnishmentFrequencyLookUp;
	}

	public void setGarnishmentFrequencyLookUp(LookUp garnishmentFrequencyLookUp) {
		this.garnishmentFrequencyLookUp = garnishmentFrequencyLookUp;
	}

	public LookUp getGarnishmentStatusLookUp() {
		return garnishmentStatusLookUp;
	}

	public void setGarnishmentStatusLookUp(LookUp garnishmentStatusLookUp) {
		this.garnishmentStatusLookUp = garnishmentStatusLookUp;
	}

	public Long getPaymentPlanId() {
		return paymentPlanId;
	}

	public void setPaymentPlanId(Long paymentPlanId) {
		this.paymentPlanId = paymentPlanId;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Long getLegalPlacementIds() {
		return legalPlacementIds;
	}

	public void setLegalPlacementIds(Long legalPlacementIds) {
		this.legalPlacementIds = legalPlacementIds;
	}

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
	}

	public Long getEmployerId() {
		return employerId;
	}

	public void setEmployerId(Long employerId) {
		this.employerId = employerId;
	}

	public Long getAccountAssetId() {
		return accountAssetId;
	}

	public void setAccountAssetId(Long accountAssetId) {
		this.accountAssetId = accountAssetId;
	}

	public Long getAssetExternalSystemId() {
		return assetExternalSystemId;
	}

	public void setAssetExternalSystemId(Long assetExternalSystemId) {
		this.assetExternalSystemId = assetExternalSystemId;
	}

	public Long getEmployerIds() {
		return employerIds;
	}

	public void setEmployerIds(Long employerIds) {
		this.employerIds = employerIds;
	}

	public Long getAccountAssetIds() {
		return accountAssetIds;
	}

	public void setAccountAssetIds(Long accountAssetIds) {
		this.accountAssetIds = accountAssetIds;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void addErrWarJson(ErrWarJson errWarJson) {
		if (this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public Long getPartnerPlanNumber() {
		return partnerPlanNumber;
	}

	public void setPartnerPlanNumber(Long partnerPlanNumber) {
		this.partnerPlanNumber = partnerPlanNumber;
	}
	
	
}