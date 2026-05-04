package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "compliance")
public class Compliance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "compliance_id")
	private Long complianceId;

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

	@Column(name = "partner_id")
	private Integer partnerId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select pr.partner_id from conf.partner pr join conf.record_status rs on pr.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where pr.partner_id = partner_id)")
	private Partner partner;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "compliance_mode")
	private String complianceMode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_mode' and lu.keycode = compliance_mode and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceModeLookUp;

	@Column(name = "dt_event")
	private LocalDate eventDate;

	@Column(name = "dt_report")
	private LocalDate reportedDate;

	@Column(name = "dt_filing")
	private LocalDate fillingDate;

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

	@Column(name = "case_file_number")
	private String caseFileNumber;

	@Column(name = "consumer_identification_number")
	private String consumerIdentificationNumber;

	@Column(name = "amt_principle")
	private Double amtPrinciple;

	@Column(name = "amt_interest")
	private Double amtInterest;

	@Column(name = "amt_fee")
	private Double amtFee;

	@Column(name = "amt_total")
	private Double amtTotal;

	@Column(name = "court_city")
	private String courtCity;

	@Column(name = "contact_detail")
	private String contactDetail;

	@Column(name = "compliance_reason")
	private String complianceReason;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and LOWER(ch.compliance_reason) = LOWER(compliance_reason))")
	private Integer complianceReasonCount;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and ch.compliance_reason is not null and ch.compliance_reason not in ('NA'))")
	private Integer complianceReasonHierarcyCount;

	@Formula(value = "(select count(*) from conf.vwcompliancehierarchy ch where LOWER(ch.compliance_type) = LOWER(compliance_type) and ch.compliance_reason is not null and ch.compliance_reason in ('NA'))")
	private Integer isComplianceReasonHierarcyNA;

	@Column(name = "compliance_channel")
	private String complianceChannel;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_channel' and lu.keycode = compliance_channel and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceChannelLookUp;

	@Column(name = "description_reported")
	private String descriptionReported;

	@Column(name = "is_validation_required")
	private Boolean validationRequired;

	@Column(name = "regulatorybody_id")
	private Integer regulatoryBodyId;

	@Column(name = "regulatorybody_short_name")
	private String regulatoryBodyShortName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rb.regulatorybody_id from conf.regulatorybody rb join conf.record_status rs on rb.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where rb.short_name = regulatorybody_short_name)")
	private RegulatoryBody regulatoryBody;

	@Column(name = "is_regulatory")
	private Boolean regulatoryRequired;

	@Column(name = "dt_firstsla")
	private LocalDate firstSlaDate;

	@Column(name = "dt_lastsla")
	private LocalDate lastSlaDate;

	@Column(name = "description_resolution")
	private String description;

	@Column(name = "remark_comment")
	private String remark;

	@Column(name = "compliance_status")
	private String complianceStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_status' and lu.keycode = compliance_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceStatusLookUp;

	@Column(name = "bankruptcy_type")
	private String bankruptcyType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'bankruptcy_type' and lu.keycode = bankruptcy_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp bankruptcyTypeLookUp;

	@Column(name = "compliance_action")
	private String complianceAction;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_action' and lu.keycode = compliance_action and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceActionLookUp;

	@Column(name = "dt_resolution")
	private LocalDate resolutionDate;

	@Column(name = "defendant")
	private String defendant;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'record_source' and lu.keycode = defendant and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp defendantLookUp;

	@Column(name = "amt_event_balance")
	private Double amtEventBalance;

	@Column(name = "is_attorney_assigned")
	private Boolean attorneyAssignedRequired;

	@Column(name = "dt_attorney_assigned")
	private LocalDate attorneyAssignedDate;

	@Column(name = "attorney_type")
	private String attorneyType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'attorney_type' and lu.keycode = attorney_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp attorneyTypeLookUp;

	@Column(name = "attorney_fax")
	private String attorneyFax;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Formula(value = "(select confval.configured_value from conf.appconfig_value confval inner join conf.appconfig_name confname on confname.appconfig_name_id = confval.appconfig_name_id where confval.configured_for = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' and confname.short_name = 'CONSUMER_COMP_REPLICATION' and confval.client_id = client_id and confval.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and confname.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private String complianceReplicationValue;

	public Long getComplianceId() {
		return complianceId;
	}

	public void setComplianceId(Long complianceId) {
		this.complianceId = complianceId;
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

	public String getComplianceMode() {
		return complianceMode;
	}

	public void setComplianceMode(String complianceMode) {
		this.complianceMode = complianceMode;
	}

	public LookUp getComplianceModeLookUp() {
		return complianceModeLookUp;
	}

	public void setComplianceModeLookUp(LookUp complianceModeLookUp) {
		this.complianceModeLookUp = complianceModeLookUp;
	}

	public LocalDate getEventDate() {
		return eventDate;
	}

	public void setEventDate(LocalDate eventDate) {
		this.eventDate = eventDate;
	}

	public LocalDate getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(LocalDate reportedDate) {
		this.reportedDate = reportedDate;
	}

	public LocalDate getFillingDate() {
		return fillingDate;
	}

	public void setFillingDate(LocalDate fillingDate) {
		this.fillingDate = fillingDate;
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

	public String getCaseFileNumber() {
		return caseFileNumber;
	}

	public void setCaseFileNumber(String caseFileNumber) {
		this.caseFileNumber = caseFileNumber;
	}

	public String getConsumerIdentificationNumber() {
		return consumerIdentificationNumber;
	}

	public void setConsumerIdentificationNumber(String consumerIdentificationNumber) {
		this.consumerIdentificationNumber = consumerIdentificationNumber;
	}

	public Double getAmtPrinciple() {
		return amtPrinciple;
	}

	public void setAmtPrinciple(Double amtPrinciple) {
		this.amtPrinciple = amtPrinciple;
	}

	public Double getAmtInterest() {
		return amtInterest;
	}

	public void setAmtInterest(Double amtInterest) {
		this.amtInterest = amtInterest;
	}

	public Double getAmtFee() {
		return amtFee;
	}

	public void setAmtFee(Double amtFee) {
		this.amtFee = amtFee;
	}

	public Double getAmtTotal() {
		return amtTotal;
	}

	public void setAmtTotal(Double amtTotal) {
		this.amtTotal = amtTotal;
	}

	public String getCourtCity() {
		return courtCity;
	}

	public void setCourtCity(String courtCity) {
		this.courtCity = courtCity;
	}

	public String getContactDetail() {
		return contactDetail;
	}

	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}

	public String getComplianceReason() {
		return complianceReason;
	}

	public void setComplianceReason(String complianceReason) {
		this.complianceReason = complianceReason;
	}

	public Integer getComplianceReasonCount() {
		return complianceReasonCount;
	}

	public void setComplianceReasonCount(Integer complianceReasonCount) {
		this.complianceReasonCount = complianceReasonCount;
	}

	public Integer getComplianceReasonHierarcyCount() {
		return complianceReasonHierarcyCount;
	}

	public void setComplianceReasonHierarcyCount(Integer complianceReasonHierarcyCount) {
		this.complianceReasonHierarcyCount = complianceReasonHierarcyCount;
	}

	public Integer getIsComplianceReasonHierarcyNA() {
		return isComplianceReasonHierarcyNA;
	}

	public void setIsComplianceReasonHierarcyNA(Integer isComplianceReasonHierarcyNA) {
		this.isComplianceReasonHierarcyNA = isComplianceReasonHierarcyNA;
	}

	public String getComplianceChannel() {
		return complianceChannel;
	}

	public void setComplianceChannel(String complianceChannel) {
		this.complianceChannel = complianceChannel;
	}

	public LookUp getComplianceChannelLookUp() {
		return complianceChannelLookUp;
	}

	public void setComplianceChannelLookUp(LookUp complianceChannelLookUp) {
		this.complianceChannelLookUp = complianceChannelLookUp;
	}

	public String getDescriptionReported() {
		return descriptionReported;
	}

	public void setDescriptionReported(String descriptionReported) {
		this.descriptionReported = descriptionReported;
	}

	public Boolean getValidationRequired() {
		return validationRequired;
	}

	public void setValidationRequired(Boolean validationRequired) {
		this.validationRequired = validationRequired;
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

	public Boolean getRegulatoryRequired() {
		return regulatoryRequired;
	}

	public void setRegulatoryRequired(Boolean regulatoryRequired) {
		this.regulatoryRequired = regulatoryRequired;
	}

	public LocalDate getFirstSlaDate() {
		return firstSlaDate;
	}

	public void setFirstSlaDate(LocalDate firstSlaDate) {
		this.firstSlaDate = firstSlaDate;
	}

	public LocalDate getLastSlaDate() {
		return lastSlaDate;
	}

	public void setLastSlaDate(LocalDate lastSlaDate) {
		this.lastSlaDate = lastSlaDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getComplianceStatus() {
		return complianceStatus;
	}

	public void setComplianceStatus(String complianceStatus) {
		this.complianceStatus = complianceStatus;
	}

	public LookUp getComplianceStatusLookUp() {
		return complianceStatusLookUp;
	}

	public void setComplianceStatusLookUp(LookUp complianceStatusLookUp) {
		this.complianceStatusLookUp = complianceStatusLookUp;
	}

	public String getBankruptcyType() {
		return bankruptcyType;
	}

	public void setBankruptcyType(String bankruptcyType) {
		this.bankruptcyType = bankruptcyType;
	}

	public LookUp getBankruptcyTypeLookUp() {
		return bankruptcyTypeLookUp;
	}

	public void setBankruptcyTypeLookUp(LookUp bankruptcyTypeLookUp) {
		this.bankruptcyTypeLookUp = bankruptcyTypeLookUp;
	}

	public String getComplianceAction() {
		return complianceAction;
	}

	public void setComplianceAction(String complianceAction) {
		this.complianceAction = complianceAction;
	}

	public LookUp getComplianceActionLookUp() {
		return complianceActionLookUp;
	}

	public void setComplianceActionLookUp(LookUp complianceActionLookUp) {
		this.complianceActionLookUp = complianceActionLookUp;
	}

	public LocalDate getResolutionDate() {
		return resolutionDate;
	}

	public void setResolutionDate(LocalDate resolutionDate) {
		this.resolutionDate = resolutionDate;
	}

	public String getDefendant() {
		return defendant;
	}

	public void setDefendant(String defendant) {
		this.defendant = defendant;
	}

	public LookUp getDefendantLookUp() {
		return defendantLookUp;
	}

	public void setDefendantLookUp(LookUp defendantLookUp) {
		this.defendantLookUp = defendantLookUp;
	}

	public Double getAmtEventBalance() {
		return amtEventBalance;
	}

	public void setAmtEventBalance(Double amtEventBalance) {
		this.amtEventBalance = amtEventBalance;
	}

	public Boolean getAttorneyAssignedRequired() {
		return attorneyAssignedRequired;
	}

	public void setAttorneyAssignedRequired(Boolean attorneyAssignedRequired) {
		this.attorneyAssignedRequired = attorneyAssignedRequired;
	}

	public LocalDate getAttorneyAssignedDate() {
		return attorneyAssignedDate;
	}

	public void setAttorneyAssignedDate(LocalDate attorneyAssignedDate) {
		this.attorneyAssignedDate = attorneyAssignedDate;
	}

	public String getAttorneyType() {
		return attorneyType;
	}

	public void setAttorneyType(String attorneyType) {
		this.attorneyType = attorneyType;
	}

	public LookUp getAttorneyTypeLookUp() {
		return attorneyTypeLookUp;
	}

	public void setAttorneyTypeLookUp(LookUp attorneyTypeLookUp) {
		this.attorneyTypeLookUp = attorneyTypeLookUp;
	}

	public String getAttorneyFax() {
		return attorneyFax;
	}

	public void setAttorneyFax(String attorneyFax) {
		this.attorneyFax = attorneyFax;
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

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
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

	public String getComplianceReplicationValue() {
		return complianceReplicationValue;
	}

	public void setComplianceReplicationValue(String complianceReplicationValue) {
		this.complianceReplicationValue = complianceReplicationValue;
	}
}