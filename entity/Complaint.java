package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinColumnOrFormula;
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
@Table(schema = "data", name = "complaint")
@Convert(attributeName = "json", converter = JsonType.class)
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "complaint_id")
    private Long complaintId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "client_account_number")
    private String clientAccountNumber;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumnOrFormula(formula = @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)"))
    private Client client;

    @Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('" + ConfRecordStatus.ENABLED + "', '" + ConfRecordStatus.SOLWAIT + "') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
    private Long accountIds;

    @Column(name = "client_consumer_number")
    private Long clientConsumerNumber;

    @Column(name = "consumer_id")
    private Long consumerId;

    @Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
    private Long consumerIds;

    @Column(name = "client_complaint_id")
    private Long clientComplaintId;

    @Column(name = "complaint_type")
    private String complaintType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select ct.complaint_type_id from conf.complaint_type ct where ct.short_name = complaint_type and ct.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private ComplaintType complaintTypeVal;

    @Column(name = "complaint_reason")
    private String complaintReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select cr.complaint_reason_id from conf.complaint_reason cr where cr.short_name = complaint_reason and cr.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private ComplaintReason complaintReasonVal;

    @Formula(value = "(select ctr.map_complaint_type_reason_id from conf.map_complaint_type_reason ctr join conf.complaint_type ct on ct.complaint_type_id = ctr.complaint_type_id join conf.complaint_reason cr on cr.complaint_reason_id = ctr.complaint_reason_id where ct.short_name = complaint_type and cr.short_name = complaint_reason and ctr.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private Long mapComplaintTypeReasonId;

    @Column(name = "complaint_channel")
    private String complaintChannel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_channel' and lu.keycode = complaint_channel and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp complaintChannelLookUp;

    @Column(name = "channel_mode")
    private String channelMode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_mode' and lu.keycode = channel_mode and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp channelModeLookUp;

    @Column(name = "complainant")
    private String complainant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'complainant' and lu.keycode = complainant and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp complainantLookUp;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_filling")
    private LocalDate dtFilling;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_report")
    private LocalDate dtReport;

    @Column(name = "description")
    private String description;

    @Column(name = "is_debt_validation_required")
    private Boolean isDebtValidationRequired;

    @Column(name = "regulatory_body")
    private String regulatoryBody;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select rb.regulatorybody_id from conf.regulatorybody rb join conf.record_status rs on rb.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where rb.short_name = regulatory_body)")
    private RegulatoryBody regulatoryBodyLookUp;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_regulatory_draft_due")
    private LocalDate dtRegulatoryDraftDue;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_regulatory_due")
    private LocalDate dtRegulatoryDue;

    @Column(name = "complaint_status")
    private String complaintStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'complaint_status' and lu.keycode = complaint_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp complaintStatusLookUp;

    @Column(name = "is_valid_complaint")
    private Boolean isValidComplaint;

    @Column(name = "complaint_review")
    private String complaintReview;

    @Column(name = "error_type")
    private String errorType;

    @Column(name = "error_description")
    private String errorDescription;

    @Column(name = "is_remediation_required")
    private Boolean isRemediationRequired;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_remediation_due")
    private LocalDate dtRemediationDue;

    @Column(name = "remediation_type")
    private String remediationType;

    @Column(name = "remediation_description")
    private String remediationDescription;

    @Column(name = "remediation_summary")
    private String remediationSummary;

    @Column(name = "complaint_source")
    private String complaintSource;

    @Column(name = "complaint_source_id")
    private Integer complaintSourceId;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "err_code_json", columnDefinition = "jsonb")
    private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "record_source_id")
    private Integer recordSourceId;

    @Column(name = "dtm_utc_update", updatable = false)
    private LocalDateTime dtmUtcUpdate;

    public  void addErrWarJson( ErrWarJson errWarJson) {
        if(this.errCodeJson == null)
            this.errCodeJson = new HashSet<ErrWarJson>();
        this.errCodeJson.add(errWarJson);
    }

    public Complaint() {
    }

    public Long getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Long complaintId) {
        this.complaintId = complaintId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Long getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(Long accountIds) {
        this.accountIds = accountIds;
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

    public Long getConsumerIds() {
        return consumerIds;
    }

    public void setConsumerIds(Long consumerIds) {
        this.consumerIds = consumerIds;
    }

    public Long getClientComplaintId() {
        return clientComplaintId;
    }

    public void setClientComplaintId(Long clientComplaintId) {
        this.clientComplaintId = clientComplaintId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public ComplaintType getComplaintTypeVal() {
		return complaintTypeVal;
	}

	public void setComplaintTypeVal(ComplaintType complaintTypeVal) {
		this.complaintTypeVal = complaintTypeVal;
	}

	public String getComplaintReason() {
        return complaintReason;
    }

    public void setComplaintReason(String complaintReason) {
        this.complaintReason = complaintReason;
    }

    public ComplaintReason getComplaintReasonVal() {
		return complaintReasonVal;
	}

	public void setComplaintReasonVal(ComplaintReason complaintReasonVal) {
		this.complaintReasonVal = complaintReasonVal;
	}

	public Long getMapComplaintTypeReasonId() {
		return mapComplaintTypeReasonId;
	}

	public void setMapComplaintTypeReasonId(Long mapComplaintTypeReasonId) {
		this.mapComplaintTypeReasonId = mapComplaintTypeReasonId;
	}

	public String getComplaintChannel() {
        return complaintChannel;
    }

    public void setComplaintChannel(String complaintChannel) {
        this.complaintChannel = complaintChannel;
    }

    public LookUp getComplaintChannelLookUp() {
        return complaintChannelLookUp;
    }

    public void setComplaintChannelLookUp(LookUp complaintChannelLookUp) {
        this.complaintChannelLookUp = complaintChannelLookUp;
    }

    public String getChannelMode() {
        return channelMode;
    }

    public void setChannelMode(String channelMode) {
        this.channelMode = channelMode;
    }

    public LookUp getChannelModeLookUp() {
        return channelModeLookUp;
    }

    public void setChannelModeLookUp(LookUp channelModeLookUp) {
        this.channelModeLookUp = channelModeLookUp;
    }

    public String getComplainant() {
        return complainant;
    }

    public void setComplainant(String complainant) {
        this.complainant = complainant;
    }

    public LookUp getComplainantLookUp() {
        return complainantLookUp;
    }

    public void setComplainantLookUp(LookUp complainantLookUp) {
        this.complainantLookUp = complainantLookUp;
    }

    public LocalDate getDtFilling() {
        return dtFilling;
    }

    public void setDtFilling(LocalDate dtFilling) {
        this.dtFilling = dtFilling;
    }

    public LocalDate getDtReport() {
        return dtReport;
    }

    public void setDtReport(LocalDate dtReport) {
        this.dtReport = dtReport;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDebtValidationRequired() {
		return isDebtValidationRequired;
	}

	public void setIsDebtValidationRequired(Boolean isDebtValidationRequired) {
		this.isDebtValidationRequired = isDebtValidationRequired;
	}

	public String getRegulatoryBody() {
        return regulatoryBody;
    }

    public void setRegulatoryBody(String regulatoryBody) {
        this.regulatoryBody = regulatoryBody;
    }

    public RegulatoryBody getRegulatoryBodyLookUp() {
        return regulatoryBodyLookUp;
    }

    public void setRegulatoryBodyLookUp(RegulatoryBody regulatoryBodyLookUp) {
        this.regulatoryBodyLookUp = regulatoryBodyLookUp;
    }

    public LocalDate getDtRegulatoryDraftDue() {
        return dtRegulatoryDraftDue;
    }

    public void setDtRegulatoryDraftDue(LocalDate dtRegulatoryDraftDue) {
        this.dtRegulatoryDraftDue = dtRegulatoryDraftDue;
    }

    public LocalDate getDtRegulatoryDue() {
        return dtRegulatoryDue;
    }

    public void setDtRegulatoryDue(LocalDate dtRegulatoryDue) {
        this.dtRegulatoryDue = dtRegulatoryDue;
    }

    public String getComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(String complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public LookUp getComplaintStatusLookUp() {
        return complaintStatusLookUp;
    }

    public void setComplaintStatusLookUp(LookUp complaintStatusLookUp) {
        this.complaintStatusLookUp = complaintStatusLookUp;
    }

    public Boolean getIsValidComplaint() {
		return isValidComplaint;
	}

	public void setIsValidComplaint(Boolean isValidComplaint) {
		this.isValidComplaint = isValidComplaint;
	}

	public String getComplaintReview() {
        return complaintReview;
    }

    public void setComplaintReview(String complaintReview) {
        this.complaintReview = complaintReview;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public Boolean getIsRemediationRequired() {
		return isRemediationRequired;
	}

	public void setIsRemediationRequired(Boolean isRemediationRequired) {
		this.isRemediationRequired = isRemediationRequired;
	}

	public LocalDate getDtRemediationDue() {
        return dtRemediationDue;
    }

    public void setDtRemediationDue(LocalDate dtRemediationDue) {
        this.dtRemediationDue = dtRemediationDue;
    }

    public String getRemediationType() {
        return remediationType;
    }

    public void setRemediationType(String remediationType) {
        this.remediationType = remediationType;
    }

    public String getRemediationDescription() {
        return remediationDescription;
    }

    public void setRemediationDescription(String remediationDescription) {
        this.remediationDescription = remediationDescription;
    }

    public String getRemediationSummary() {
        return remediationSummary;
    }

    public void setRemediationSummary(String remediationSummary) {
        this.remediationSummary = remediationSummary;
    }

    public String getComplaintSource() {
        return complaintSource;
    }

    public void setComplaintSource(String complaintSource) {
        this.complaintSource = complaintSource;
    }

    public Integer getComplaintSourceId() {
        return complaintSourceId;
    }

    public void setComplaintSourceId(Integer complaintSourceId) {
        this.complaintSourceId = complaintSourceId;
    }

    public Integer getRecordStatusId() {
        return recordStatusId;
    }

    public void setRecordStatusId(Integer recordStatusId) {
        this.recordStatusId = recordStatusId;
    }

    public Set<ErrWarJson> getErrCodeJson() {
        return errCodeJson;
    }

    public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
        this.errCodeJson = errCodeJson;
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

    public Integer getRecordSourceId() {
        return recordSourceId;
    }

    public void setRecordSourceId(Integer recordSourceId) {
        this.recordSourceId = recordSourceId;
    }

    public LocalDateTime getDtmUtcUpdate() {
        return dtmUtcUpdate;
    }

    public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
        this.dtmUtcUpdate = dtmUtcUpdate;
    }
}
