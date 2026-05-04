package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
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
@Table(schema = "data", name = "legal_placement")
@Convert(attributeName = "json", converter = JsonType.class)
public class LegalPlacement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static volatile Map<String, LegalPlacement> legalPlacementRecord = new HashMap<String, LegalPlacement>();

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "legal_placement_id")
	private Long legalPlacementId;

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

	@Column(name = "casenumber")
	private String caseNumber;

	@Column(name = "amt_placed_legal")
	private Double amtPlacedLegal;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_placed")
	private LocalDate dtPlaced;

	@Column(name = "attorney_firm")
	private String attorneyFirm;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_suit_filed")
	private LocalDate dtSuitFiled;

	@Column(name = "court_suit_filed")
	private String courtSuitFiled;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_served")
	private LocalDate dtServed;

	@Column(name = "amt_court_cost")
	private Double amtCourtCost;

	@Column(name = "amt_suit_debt")
	private Double amtSuitDebt;

	@Column(name = "amt_judgement")
	private Double amtJudgement;

	@Column(name = "amt_judgment_principal")
	private Double amtJudgmentPrincipal;

	@Column(name = "amt_judgment_interest")
	private Double amtJudgmentInterest;

	@Column(name = "amt_judgment_fees")
	private Double amtJudgmentFees;

	@Column(name = "amt_judgment_attorney")
	private Double amtJudgmentAttorney;

	@Column(name = "amt_judgment_other")
	private Double amtJudgmentOther;

	@Column(name = "pct_judgement_interestrate")
	private Double pctJudgmentInterestRate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_judgement")
	private LocalDate dtJudgment;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_judgment_expiration")
	private LocalDate dtJudgmentExpiration;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_judgment_renewal")
	private LocalDate dtJudgmentRenewal;

	@Column(name = "county_judgement")
	private String countyJudgment;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "dt_attorney_invoice")
	private LocalDate dtAttorneyInvoice;

	@Column(name = "is_judgment_available")
	private Boolean isJudgmentAvailable;

	@Column(name = "judgment_docket")
	private String judgmentDocket;

	@Column(name = "suit_docket")
	private String suitDocket;

	@Column(name = "state_suit_filed")
	private String stateSuitFiled;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_suit_answer_filed_date")
	private LocalDate judgmentSuitAnswerFiledDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_suit_dismissal_date")
	private LocalDate judgmentSuitDismissalDate;

	@Column(name = "judgment_status")
	private String judgmentStatus;

	@Column(name = "plaintiff_name")
	private String plaintiffName;

	@Column(name = "judgment_suit_outcome")
	private String judgmentSuitOutcome;

	@Column(name = "judgment_process_server")
	private String judgmentProcessServer;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_last_service_attempt")
	private LocalDate judgmentLastServiceAttempt;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_court_hearing")
	private LocalDate judgmentCourtHearing;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_satisfaction_date")
	private LocalDate judgmentSatisfactionDate;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
	@Column(name = "judgment_stipulation_date")
	private LocalDate judgmentStipulationDate;

	@Column(name = "suit_status")
	private Boolean suitStatus;

	@Column(name = "total_post_judgement_interest")
	private Double totalPostJudgementInterest;

	@Column(name = "total_post_judgement_credits")
	private Double totalPostJudgementCredits;

	@Column(name = "total_post_judgement_fees")
	private Double totalPostJudgementFees;

	@Column(name = "total_post_judgement_payments")
	private Double totalPostJudgementPayments;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Formula(value = "(select count(lp.legal_placement_id) from data.legal_placement lp where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.casenumber = casenumber)")
	private Integer caseNumberDeDup;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'judgment_status' and lu.keycode = judgment_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp judgmentStatusLookUp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'judgment_suit_outcome' and lu.keycode = judgment_suit_outcome and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp judgmentSuitOutcomeLookUp;

	@Formula(value = "(select acc.dt_original_account_open from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate dtOriginalAccountOpen;

	@Formula(value = "(select acc.dt_delinquency from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate dtDelinquency;

	@Formula(value = "(select count(lp.legal_placement_id) from data.legal_placement lp join conf.record_status rs on lp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.legal_placement_id != legal_placement_id and ((lp.suit_status is null or lp.suit_status = true) or (lp.judgment_status is null or (lp.judgment_status != 'DD' and lp.judgment_status != 'DP'))))")
	private Integer legalPlacementIdCount;

	@Formula(value = "(select count(lp.legal_lien_id) from data.legal_lien lp join conf.record_status rs on lp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.casenumber != casenumber and lp.lien_status = 'AE')")
	private Integer legalLienIdCount;

	@Formula(value = "(select count(lp.legal_garnishment_id) from data.legal_garnishment lp join conf.record_status rs on lp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.casenumber != casenumber and lp.garnishment_status = 'AE')")
	private Integer legalGarnishmentIdCount;

	@Formula(value = "(select count(doc.doc_id) from data.doc doc join data.account acc on acc.client_id = doc.client_id and acc.account_id = doc.account_id where acc.client_id = client_id and acc.client_account_number = client_account_number and doc.doctype = 'JG' and doc.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and acc.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"'))")
	private Integer docIdCount;

	@Formula(value = "(select acc.amt_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCurrentbalance;

	@Formula(value = "(select acc.amt_principal_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtPrincipalCurrentbalance;

	@Formula(value = "(select acc.amt_interest_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtInterestCurrentbalance;

	@Formula(value = "(select acc.amt_latefee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtLateCurrentbalance;

	@Formula(value = "(select acc.amt_otherfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtOtherfeeCurrentbalance;

	@Formula(value = "(select acc.amt_courtcost_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtCourtCostCurrentbalance;

	@Formula(value = "(select acc.amt_attorneyfee_currentbalance from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name = 'Enabled' where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Double amtAttorneyfeeCurrentbalance;


	public Long getLegalPlacementId() {
		return legalPlacementId;
	}

	public void setLegalPlacementId(Long legalPlacementId) {
		this.legalPlacementId = legalPlacementId;
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

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public Double getPctJudgmentInterestRate() {
		return pctJudgmentInterestRate;
	}

	public void setPctJudgmentInterestRate(Double pctJudgmentInterestRate) {
		this.pctJudgmentInterestRate = pctJudgmentInterestRate;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public Integer getCaseNumberDeDup() {
		return caseNumberDeDup;
	}

	public void setCaseNumberDeDup(Integer caseNumberDeDup) {
		this.caseNumberDeDup = caseNumberDeDup;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
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

	public Double getAmtPlacedLegal() {
		return amtPlacedLegal;
	}

	public void setAmtPlacedLegal(Double amtPlacedLegal) {
		this.amtPlacedLegal = amtPlacedLegal;
	}

	public LocalDate getDtPlaced() {
		return dtPlaced;
	}

	public void setDtPlaced(LocalDate dtPlaced) {
		this.dtPlaced = dtPlaced;
	}

	public String getAttorneyFirm() {
		return attorneyFirm;
	}

	public void setAttorneyFirm(String attorneyFirm) {
		this.attorneyFirm = attorneyFirm;
	}

	public LocalDate getDtSuitFiled() {
		return dtSuitFiled;
	}

	public void setDtSuitFiled(LocalDate dtSuitFiled) {
		this.dtSuitFiled = dtSuitFiled;
	}

	public String getCourtSuitFiled() {
		return courtSuitFiled;
	}

	public void setCourtSuitFiled(String courtSuitFiled) {
		this.courtSuitFiled = courtSuitFiled;
	}

	public LocalDate getDtServed() {
		return dtServed;
	}

	public void setDtServed(LocalDate dtServed) {
		this.dtServed = dtServed;
	}

	public Double getAmtCourtCost() {
		return amtCourtCost;
	}

	public void setAmtCourtCost(Double amtCourtCost) {
		this.amtCourtCost = amtCourtCost;
	}

	public Double getAmtSuitDebt() {
		return amtSuitDebt;
	}

	public void setAmtSuitDebt(Double amtSuitDebt) {
		this.amtSuitDebt = amtSuitDebt;
	}

	public Double getAmtJudgement() {
		return amtJudgement;
	}

	public void setAmtJudgement(Double amtJudgement) {
		this.amtJudgement = amtJudgement;
	}

	public Double getAmtJudgmentPrincipal() {
		return amtJudgmentPrincipal;
	}

	public void setAmtJudgmentPrincipal(Double amtJudgmentPrincipal) {
		this.amtJudgmentPrincipal = amtJudgmentPrincipal;
	}

	public Double getAmtJudgmentInterest() {
		return amtJudgmentInterest;
	}

	public void setAmtJudgmentInterest(Double amtJudgmentInterest) {
		this.amtJudgmentInterest = amtJudgmentInterest;
	}

	public Double getAmtJudgmentFees() {
		return amtJudgmentFees;
	}

	public void setAmtJudgmentFees(Double amtJudgmentFees) {
		this.amtJudgmentFees = amtJudgmentFees;
	}

	public Double getAmtJudgmentAttorney() {
		return amtJudgmentAttorney;
	}

	public void setAmtJudgmentAttorney(Double amtJudgmentAttorney) {
		this.amtJudgmentAttorney = amtJudgmentAttorney;
	}

	public Double getAmtJudgmentOther() {
		return amtJudgmentOther;
	}

	public void setAmtJudgmentOther(Double amtJudgmentOther) {
		this.amtJudgmentOther = amtJudgmentOther;
	}

	public LocalDate getDtJudgment() {
		return dtJudgment;
	}

	public void setDtJudgment(LocalDate dtJudgment) {
		this.dtJudgment = dtJudgment;
	}

	public LocalDate getDtJudgmentExpiration() {
		return dtJudgmentExpiration;
	}

	public void setDtJudgmentExpiration(LocalDate dtJudgmentExpiration) {
		this.dtJudgmentExpiration = dtJudgmentExpiration;
	}

	public LocalDate getDtJudgmentRenewal() {
		return dtJudgmentRenewal;
	}

	public void setDtJudgmentRenewal(LocalDate dtJudgmentRenewal) {
		this.dtJudgmentRenewal = dtJudgmentRenewal;
	}

	public String getCountyJudgment() {
		return countyJudgment;
	}

	public void setCountyJudgment(String countyJudgment) {
		this.countyJudgment = countyJudgment;
	}

	public LocalDate getDtAttorneyInvoice() {
		return dtAttorneyInvoice;
	}

	public void setDtAttorneyInvoice(LocalDate dtAttorneyInvoice) {
		this.dtAttorneyInvoice = dtAttorneyInvoice;
	}

	public Boolean getJudgmentAvailable() {
		return isJudgmentAvailable;
	}

	public void setJudgmentAvailable(Boolean judgmentAvailable) {
		this.isJudgmentAvailable = judgmentAvailable;
	}

	public String getJudgmentDocket() {
		return judgmentDocket;
	}

	public void setJudgmentDocket(String judgmentDocket) {
		this.judgmentDocket = judgmentDocket;
	}

	public String getSuitDocket() {
		return suitDocket;
	}

	public void setSuitDocket(String suitDocket) {
		this.suitDocket = suitDocket;
	}

	public String getStateSuitFiled() {
		return stateSuitFiled;
	}

	public void setStateSuitFiled(String stateSuitFiled) {
		this.stateSuitFiled = stateSuitFiled;
	}

	public LocalDate getJudgmentSuitAnswerFiledDate() {
		return judgmentSuitAnswerFiledDate;
	}

	public void setJudgmentSuitAnswerFiledDate(LocalDate judgmentSuitAnswerFiledDate) {
		this.judgmentSuitAnswerFiledDate = judgmentSuitAnswerFiledDate;
	}

	public LocalDate getJudgmentSuitDismissalDate() {
		return judgmentSuitDismissalDate;
	}

	public void setJudgmentSuitDismissalDate(LocalDate judgmentSuitDismissalDate) {
		this.judgmentSuitDismissalDate = judgmentSuitDismissalDate;
	}

	public String getJudgmentStatus() {
		return judgmentStatus;
	}

	public void setJudgmentStatus(String judgmentStatus) {
		this.judgmentStatus = judgmentStatus;
	}

	public String getPlaintiffName() {
		return plaintiffName;
	}

	public void setPlaintiffName(String plaintiffName) {
		this.plaintiffName = plaintiffName;
	}

	public String getJudgmentSuitOutcome() {
		return judgmentSuitOutcome;
	}

	public void setJudgmentSuitOutcome(String judgmentSuitOutcome) {
		this.judgmentSuitOutcome = judgmentSuitOutcome;
	}

	public String getJudgmentProcessServer() {
		return judgmentProcessServer;
	}

	public void setJudgmentProcessServer(String judgmentProcessServer) {
		this.judgmentProcessServer = judgmentProcessServer;
	}

	public LocalDate getJudgmentLastServiceAttempt() {
		return judgmentLastServiceAttempt;
	}

	public void setJudgmentLastServiceAttempt(LocalDate judgmentLastServiceAttempt) {
		this.judgmentLastServiceAttempt = judgmentLastServiceAttempt;
	}

	public LocalDate getJudgmentCourtHearing() {
		return judgmentCourtHearing;
	}

	public void setJudgmentCourtHearing(LocalDate judgmentCourtHearing) {
		this.judgmentCourtHearing = judgmentCourtHearing;
	}

	public LocalDate getJudgmentSatisfactionDate() {
		return judgmentSatisfactionDate;
	}

	public void setJudgmentSatisfactionDate(LocalDate judgmentSatisfactionDate) {
		this.judgmentSatisfactionDate = judgmentSatisfactionDate;
	}

	public LocalDate getJudgmentStipulationDate() {
		return judgmentStipulationDate;
	}

	public void setJudgmentStipulationDate(LocalDate judgmentStipulationDate) {
		this.judgmentStipulationDate = judgmentStipulationDate;
	}

	public Boolean getSuitStatus() {
		return suitStatus;
	}

	public void setSuitStatus(Boolean suitStatus) {
		this.suitStatus = suitStatus;
	}

	public Double getTotalPostJudgementInterest() {
		return totalPostJudgementInterest;
	}

	public void setTotalPostJudgementInterest(Double totalPostJudgementInterest) {
		this.totalPostJudgementInterest = totalPostJudgementInterest;
	}

	public Double getTotalPostJudgementCredits() {
		return totalPostJudgementCredits;
	}

	public void setTotalPostJudgementCredits(Double totalPostJudgementCredits) {
		this.totalPostJudgementCredits = totalPostJudgementCredits;
	}

	public Double getTotalPostJudgementFees() {
		return totalPostJudgementFees;
	}

	public void setTotalPostJudgementFees(Double totalPostJudgementFees) {
		this.totalPostJudgementFees = totalPostJudgementFees;
	}

	public Double getTotalPostJudgementPayments() {
		return totalPostJudgementPayments;
	}

	public void setTotalPostJudgementPayments(Double totalPostJudgementPayments) {
		this.totalPostJudgementPayments = totalPostJudgementPayments;
	}

	public LookUp getJudgmentStatusLookUp() {
		return judgmentStatusLookUp;
	}

	public void setJudgmentStatusLookUp(LookUp judgmentStatusLookUp) {
		this.judgmentStatusLookUp = judgmentStatusLookUp;
	}

	public LookUp getJudgmentSuitOutcomeLookUp() {
		return judgmentSuitOutcomeLookUp;
	}

	public void setJudgmentSuitOutcomeLookUp(LookUp judgmentSuitOutcomeLookUp) {
		this.judgmentSuitOutcomeLookUp = judgmentSuitOutcomeLookUp;
	}

	public Boolean getIsJudgmentAvailable() {
		return isJudgmentAvailable;
	}

	public void setIsJudgmentAvailable(Boolean isJudgmentAvailable) {
		this.isJudgmentAvailable = isJudgmentAvailable;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public LocalDate getDtOriginalAccountOpen() {
		return dtOriginalAccountOpen;
	}

	public void setDtOriginalAccountOpen(LocalDate dtOriginalAccountOpen) {
		this.dtOriginalAccountOpen = dtOriginalAccountOpen;
	}

	public LocalDate getDtDelinquency() {
		return dtDelinquency;
	}

	public void setDtDelinquency(LocalDate dtDelinquency) {
		this.dtDelinquency = dtDelinquency;
	}

	public Integer getLegalPlacementIdCount() {
		return legalPlacementIdCount;
	}

	public void setLegalPlacementIdCount(Integer legalPlacementIdCount) {
		this.legalPlacementIdCount = legalPlacementIdCount;
	}

	public Integer getLegalLienIdCount() {
		return legalLienIdCount;
	}

	public void setLegalLienIdCount(Integer legalLienIdCount) {
		this.legalLienIdCount = legalLienIdCount;
	}

	public Integer getLegalGarnishmentIdCount() {
		return legalGarnishmentIdCount;
	}

	public void setLegalGarnishmentIdCount(Integer legalGarnishmentIdCount) {
		this.legalGarnishmentIdCount = legalGarnishmentIdCount;
	}

	public Integer getDocIdCount() {
		return docIdCount;
	}

	public void setDocIdCount(Integer docIdCount) {
		this.docIdCount = docIdCount;
	}

	public Double getAmtCurrentbalance() {
		return amtCurrentbalance;
	}

	public void setAmtCurrentbalance(Double amtCurrentbalance) {
		this.amtCurrentbalance = amtCurrentbalance;
	}

	public Double getAmtPrincipalCurrentbalance() {
		return amtPrincipalCurrentbalance;
	}

	public void setAmtPrincipalCurrentbalance(Double amtPrincipalCurrentbalance) {
		this.amtPrincipalCurrentbalance = amtPrincipalCurrentbalance;
	}

	public Double getAmtInterestCurrentbalance() {
		return amtInterestCurrentbalance;
	}

	public void setAmtInterestCurrentbalance(Double amtInterestCurrentbalance) {
		this.amtInterestCurrentbalance = amtInterestCurrentbalance;
	}

	public Double getAmtLateCurrentbalance() {
		return amtLateCurrentbalance;
	}

	public void setAmtLateCurrentbalance(Double amtLateCurrentbalance) {
		this.amtLateCurrentbalance = amtLateCurrentbalance;
	}

	public Double getAmtOtherfeeCurrentbalance() {
		return amtOtherfeeCurrentbalance;
	}

	public void setAmtOtherfeeCurrentbalance(Double amtOtherfeeCurrentbalance) {
		this.amtOtherfeeCurrentbalance = amtOtherfeeCurrentbalance;
	}

	public Double getAmtCourtCostCurrentbalance() {
		return amtCourtCostCurrentbalance;
	}

	public void setAmtCourtCostCurrentbalance(Double amtCourtCostCurrentbalance) {
		this.amtCourtCostCurrentbalance = amtCourtCostCurrentbalance;
	}

	public Double getAmtAttorneyfeeCurrentbalance() {
		return amtAttorneyfeeCurrentbalance;
	}

	public void setAmtAttorneyfeeCurrentbalance(Double amtAttorneyfeeCurrentbalance) {
		this.amtAttorneyfeeCurrentbalance = amtAttorneyfeeCurrentbalance;
	}

	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public static Map<String, LegalPlacement> getLegalPlacementRecord() {
		return legalPlacementRecord;
	}

	public static void setLegalPlacementRecord(Map<String, LegalPlacement> legalPlacementRecord) {
		LegalPlacement.legalPlacementRecord = legalPlacementRecord;
	}
}