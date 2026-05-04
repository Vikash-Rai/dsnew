package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
@Table(schema = "data", name = "communication_detail")
public class CommunicationDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "communication_detail_id")
	private Long communicationDetailId;

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

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "compliance_type")
	private String complianceType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_type' and LOWER(lu.keycode) = LOWER(compliance_type) and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceTypeLookUp;

	@Column(name = "compliance_subtype")
	private String complianceSubtype;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and LOWER(ch.compliance_subtype) = LOWER(compliance_subtype))")
	private Integer complianceSubtypeCount;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and ch.compliance_subtype is not null and ch.compliance_subtype not in ('NA'))")
	private Integer complianceSubtypeHierarcyCount;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and ch.compliance_subtype is not null and ch.compliance_subtype in ('NA'))")
	private Integer isComplianceSubtypeHierarcyNA;

	@Column(name = "dt_compliance")
	private LocalDate dtCompliance;

	@Column(name = "regulatorybody_id")
	private Integer regulatoryBodyId;

	@Column(name = "regulatorybody_short_name")
	private String regulatoryBodyShortName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rb.regulatorybody_id from conf.regulatorybody rb join conf.record_status rs on rb.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where rb.short_name = regulatorybody_short_name)")
	private RegulatoryBody regulatoryBody;

	@Column(name = "communication_channel")
	private String communicationChannel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'communication_channel' and LOWER(lu.keycode) = LOWER(communication_channel) and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp communicationChannelLookUp;

	@Column(name = "communication_reason")
	private String communicationReason;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'communication_reason' and LOWER(lu.keycode) = LOWER(communication_reason) and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp communicationReasonLookUp;

	@Column(name = "communication_subreason")
	private String communicationSubreason;

	@Column(name = "communication_source")
	private String communicationSource;

	@Column(name = "communication_source_id")
	private Integer communicationSourceId;

	@Formula(value = "(select count(*) from conf.vwcommunicationreasonsubreason rs where LOWER(rs.reason_short_name) = LOWER(communication_reason) and LOWER(rs.subreason_short_name) = LOWER(communication_subreason))")
	private Integer communicationSubreasonCount;

	@Formula(value = "(select count(*) from conf.vwcommunicationreasonsubreason rs where LOWER(rs.reason_short_name) = LOWER(communication_reason) and rs.subreason_short_name is not null and rs.subreason_short_name not in ('OT'))")
	private Integer communicationSubreasonHierarcyCount;

	@Column(name = "communication_outcome")
	private String communicationOutcome;

	@Formula(value = "(select count(*) from conf.vwchanneloutcome co where LOWER(co.channel_short_name) = LOWER(communication_channel) and LOWER(co.outcome_short_name) = LOWER(communication_outcome))")
	private Integer communicationOutcomeCount;

	@Formula(value = "(select count(*) from conf.vwchanneloutcome co where LOWER(co.channel_short_name) = LOWER(communication_channel) and co.outcome_short_name is not null and co.outcome_short_name not in ('OT'))")
	private Integer communicationOutcomeHierarcyCount;

	@Column(name = "details")
	private String details;

	@Column(name = "direction")
	private String direction;

	@Column(name = "disposition")
	private String disposition;

	@Column(name = "pct_discount")
	private Double pctDiscount;

	@Column(name = "dt_rpc")
	private LocalDate reportedDate;

	@Column(name = "dt_communication")
	private LocalDate dtCommunication;

	@Column(name = "tm_utc_communication")
	private LocalTime tmUtcCommunication;

	@Column(name = "dt_outcome")
	private LocalDate dtOutcome;

	@Column(name = "tm_utc_outcome")
	private LocalTime tmUtcOutcome;

	@Column(name = "compliance_id")
	private Long complianceId;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select acc.partner_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer partnerIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@Formula(value = "(select com.compliance_id from data.compliance com join conf.record_status rs on com.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where com.client_id = client_id and com.client_account_number = client_account_number and com.compliance_type = compliance_type and com.compliance_subtype = compliance_subtype and com.dt_report = dt_compliance and com.regulatorybody_id = regulatorybody_id)")
	private Long complianceIds;

	@Formula(value = "(select cs.dt_from from data.cot_servicer cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.account_id = (select acc.account_id from data.account acc where acc.client_id = client_id and acc.client_account_number = client_account_number) and cs.partner_id = communication_source_id order by cs.dt_till desc limit 1)")
	private LocalDate cotDtFrom;

	@Formula(value = "(select cs.dt_till from data.cot_servicer cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.account_id = (select acc.account_id from data.account acc where acc.client_id = client_id and acc.client_account_number = client_account_number) and cs.partner_id = communication_source_id order by cs.dt_till desc limit 1)")
	private LocalDate cotDtTill;

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

	public Long getCommunicationDetailId() {
		return communicationDetailId;
	}

	public void setCommunicationDetailId(Long communicationDetailId) {
		this.communicationDetailId = communicationDetailId;
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

	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
	}

	public Long getClientConsumerNumber() {
		return clientConsumerNumber;
	}

	public void setClientConsumerNumber(Long clientConsumerNumber) {
		this.clientConsumerNumber = clientConsumerNumber;
	}

	public String getComplianceType() {
		return complianceType;
	}

	public void setComplianceType(String complianceType) {
		this.complianceType = complianceType;
	}

	public LookUp getComplianceTypeLookUp() {
		return complianceTypeLookUp;
	}

	public void setComplianceTypeLookUp(LookUp complianceTypeLookUp) {
		this.complianceTypeLookUp = complianceTypeLookUp;
	}

	public String getComplianceSubtype() {
		return complianceSubtype;
	}

	public void setComplianceSubtype(String complianceSubtype) {
		this.complianceSubtype = complianceSubtype;
	}

	public Integer getComplianceSubtypeCount() {
		return complianceSubtypeCount;
	}

	public void setComplianceSubtypeCount(Integer complianceSubtypeCount) {
		this.complianceSubtypeCount = complianceSubtypeCount;
	}

	public Integer getComplianceSubtypeHierarcyCount() {
		return complianceSubtypeHierarcyCount;
	}

	public void setComplianceSubtypeHierarcyCount(Integer complianceSubtypeHierarcyCount) {
		this.complianceSubtypeHierarcyCount = complianceSubtypeHierarcyCount;
	}

	public Integer getIsComplianceSubtypeHierarcyNA() {
		return isComplianceSubtypeHierarcyNA;
	}

	public void setIsComplianceSubtypeHierarcyNA(Integer isComplianceSubtypeHierarcyNA) {
		this.isComplianceSubtypeHierarcyNA = isComplianceSubtypeHierarcyNA;
	}

	public LocalDate getDtCompliance() {
		return dtCompliance;
	}

	public void setDtCompliance(LocalDate dtCompliance) {
		this.dtCompliance = dtCompliance;
	}

	public Integer getRegulatoryBodyId() {
		return regulatoryBodyId;
	}

	public void setRegulatoryBodyId(Integer regulatoryBodyId) {
		this.regulatoryBodyId = regulatoryBodyId;
	}

	public String getRegulatoryBodyShortName() {
		return regulatoryBodyShortName;
	}

	public void setRegulatoryBodyShortName(String regulatoryBodyShortName) {
		this.regulatoryBodyShortName = regulatoryBodyShortName;
	}

	public RegulatoryBody getRegulatoryBody() {
		return regulatoryBody;
	}

	public void setRegulatoryBody(RegulatoryBody regulatoryBody) {
		this.regulatoryBody = regulatoryBody;
	}

	public String getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(String communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

	public LookUp getCommunicationChannelLookUp() {
		return communicationChannelLookUp;
	}

	public void setCommunicationChannelLookUp(LookUp communicationChannelLookUp) {
		this.communicationChannelLookUp = communicationChannelLookUp;
	}

	public String getCommunicationReason() {
		return communicationReason;
	}

	public void setCommunicationReason(String communicationReason) {
		this.communicationReason = communicationReason;
	}

	public LookUp getCommunicationReasonLookUp() {
		return communicationReasonLookUp;
	}

	public void setCommunicationReasonLookUp(LookUp communicationReasonLookUp) {
		this.communicationReasonLookUp = communicationReasonLookUp;
	}

	public String getCommunicationSubreason() {
		return communicationSubreason;
	}

	public void setCommunicationSubreason(String communicationSubreason) {
		this.communicationSubreason = communicationSubreason;
	}

	public String getCommunicationSource() {
		return communicationSource;
	}

	public void setCommunicationSource(String communicationSource) {
		this.communicationSource = communicationSource;
	}

	public Integer getCommunicationSourceId() {
		return communicationSourceId;
	}

	public void setCommunicationSourceId(Integer communicationSourceId) {
		this.communicationSourceId = communicationSourceId;
	}

	public Integer getCommunicationSubreasonCount() {
		return communicationSubreasonCount;
	}

	public void setCommunicationSubreasonCount(Integer communicationSubreasonCount) {
		this.communicationSubreasonCount = communicationSubreasonCount;
	}

	public Integer getCommunicationSubreasonHierarcyCount() {
		return communicationSubreasonHierarcyCount;
	}

	public void setCommunicationSubreasonHierarcyCount(Integer communicationSubreasonHierarcyCount) {
		this.communicationSubreasonHierarcyCount = communicationSubreasonHierarcyCount;
	}

	public String getCommunicationOutcome() {
		return communicationOutcome;
	}

	public void setCommunicationOutcome(String communicationOutcome) {
		this.communicationOutcome = communicationOutcome;
	}

	public Integer getCommunicationOutcomeCount() {
		return communicationOutcomeCount;
	}

	public void setCommunicationOutcomeCount(Integer communicationOutcomeCount) {
		this.communicationOutcomeCount = communicationOutcomeCount;
	}

	public Integer getCommunicationOutcomeHierarcyCount() {
		return communicationOutcomeHierarcyCount;
	}

	public void setCommunicationOutcomeHierarcyCount(Integer communicationOutcomeHierarcyCount) {
		this.communicationOutcomeHierarcyCount = communicationOutcomeHierarcyCount;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDisposition() {
		return disposition;
	}

	public void setDisposition(String disposition) {
		this.disposition = disposition;
	}

	public Double getPctDiscount() {
		return pctDiscount;
	}

	public void setPctDiscount(Double pctDiscount) {
		this.pctDiscount = pctDiscount;
	}

	public LocalDate getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(LocalDate reportedDate) {
		this.reportedDate = reportedDate;
	}

	public LocalDate getDtCommunication() {
		return dtCommunication;
	}

	public void setDtCommunication(LocalDate dtCommunication) {
		this.dtCommunication = dtCommunication;
	}

	public LocalTime getTmUtcCommunication() {
		return tmUtcCommunication;
	}

	public void setTmUtcCommunication(LocalTime tmUtcCommunication) {
		this.tmUtcCommunication = tmUtcCommunication;
	}

	public LocalDate getDtOutcome() {
		return dtOutcome;
	}

	public void setDtOutcome(LocalDate dtOutcome) {
		this.dtOutcome = dtOutcome;
	}

	public LocalTime getTmUtcOutcome() {
		return tmUtcOutcome;
	}

	public void setTmUtcOutcome(LocalTime tmUtcOutcome) {
		this.tmUtcOutcome = tmUtcOutcome;
	}

	public Long getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(Long complianceId) {
		this.complianceId = complianceId;
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

	public Integer getPartnerIds() {
		return partnerIds;
	}

	public void setPartnerIds(Integer partnerIds) {
		this.partnerIds = partnerIds;
	}

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
	}

	public Long getComplianceIds() {
		return complianceIds;
	}

	public void setComplianceIds(Long complianceIds) {
		this.complianceIds = complianceIds;
	}
}