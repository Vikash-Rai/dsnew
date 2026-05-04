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
@Table(schema = "data", name = "dispute")
@Convert(attributeName = "json", converter = JsonType.class)
public class Dispute implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dispute_id")
    private Long disputeId;

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

    @Column(name = "client_dispute_id")
    private Long clientDisputeId;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_filling")
    private LocalDate dtDisputeFilling;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_report")
    private LocalDate dtDisputeReport;

    @Column(name = "dispute_type")
    private String disputeType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'dispute_type' and lu.keycode = dispute_type and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp disputeTypeLookUp;

    @Column(name = "dispute_channel")
    private String disputeChannel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_channel' and lu.keycode = dispute_channel and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp channelLookUp;

    @Column(name = "channel_mode")
    private String channelMode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_mode' and lu.keycode = channel_mode and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp channelModeLookUp;

    @Column(name = "description")
    private String disputeDescription;

    @Column(name = "is_debt_validation_required")
    private Boolean validationRequired;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_first_sla")
    private LocalDate dtFirstSLA;

    @Formula(value = "(select acv.configured_value from conf.appconfig_value acv join conf.appconfig_name acn on acv.appconfig_name_id = acn.appconfig_name_id where acn.short_name = 'DISPUTE_SLA1' and ((acv.configured_for = 'CL' and acv.client_id = client_id) or acv.configured_for = 'EQ') and acv.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private Integer dtFirstSLADeDup;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_last_sla")
    private LocalDate dtLastSLA;

    @Formula(value = "(select acv.configured_value from conf.appconfig_value acv join conf.appconfig_name acn on acv.appconfig_name_id = acn.appconfig_name_id where acn.short_name = 'DISPUTE_SLA2' and ((acv.configured_for = 'CL' and acv.client_id = client_id) or acv.configured_for = 'EQ') and acv.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private Integer dtLastSLADeDup;

    @Column(name = "dispute_status")
    private String disputeStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'dispute_status' and lu.keycode = dispute_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp disputeStatusLookUp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "err_code_json", columnDefinition = "jsonb")
    private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "record_source_id")
    private Integer recordSourceId;

    @Column(name = "dtm_utc_update", updatable = false)
    private LocalDateTime dtmUtcUpdate;


    public Dispute() {
    }

    public Dispute(Long disputeId) {
        this.disputeId = disputeId;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(Long disputeId) {
        this.disputeId = disputeId;
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

	public Long getClientDisputeId() {
        return clientDisputeId;
    }

    public void setClientDisputeId(Long clientDisputeId) {
        this.clientDisputeId = clientDisputeId;
    }

    public LocalDate getDtDisputeFilling() {
        return dtDisputeFilling;
    }

    public void setDtDisputeFilling(LocalDate dtDisputeFilling) {
        this.dtDisputeFilling = dtDisputeFilling;
    }

    public LocalDate getDtDisputeReport() {
        return dtDisputeReport;
    }

    public void setDtDisputeReport(LocalDate dtDisputeReport) {
        this.dtDisputeReport = dtDisputeReport;
    }

    public String getDisputeType() {
        return disputeType;
    }

    public void setDisputeType(String disputeType) {
        this.disputeType = disputeType;
    }

    public LookUp getDisputeTypeLookUp() {
        return disputeTypeLookUp;
    }

    public void setDisputeTypeLookUp(LookUp disputeTypeLookUp) {
        this.disputeTypeLookUp = disputeTypeLookUp;
    }

    public LookUp getChannelLookUp() {
        return channelLookUp;
    }

    public void setChannelLookUp(LookUp channelLookUp) {
        this.channelLookUp = channelLookUp;
    }

    public String getDisputeChannel() {
        return disputeChannel;
    }

    public void setDisputeChannel(String disputeChannel) {
        this.disputeChannel = disputeChannel;
    }

    public String getChannelMode() {
        return channelMode;
    }

    public void setChannelMode(String channelMode) {
        this.channelMode = channelMode;
    }

    public String getDisputeDescription() {
        return disputeDescription;
    }

    public void setDisputeDescription(String disputeDescription) {
        this.disputeDescription = disputeDescription;
    }

    public Boolean getValidationRequired() {
        return validationRequired;
    }

    public void setValidationRequired(Boolean validationRequired) {
        this.validationRequired = validationRequired;
    }

    public LocalDate getDtFirstSLA() {
        return dtFirstSLA;
    }

    public void setDtFirstSLA(LocalDate dtFirstSLA) {
        this.dtFirstSLA = dtFirstSLA;
    }

    public LocalDate getDtLastSLA() {
		return dtLastSLA;
	}

	public void setDtLastSLA(LocalDate dtLastSLA) {
		this.dtLastSLA = dtLastSLA;
	}

	public String getDisputeStatus() {
        return disputeStatus;
    }

    public void setDisputeStatus(String disputeStatus) {
        this.disputeStatus = disputeStatus;
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

    public Integer getRecordStatusId() {
        return recordStatusId;
    }

    public void setRecordStatusId(Integer recordStatusId) {
        this.recordStatusId = recordStatusId;
    }

    public LookUp getChannelModeLookUp() {
        return channelModeLookUp;
    }

    public void setChannelModeLookUp(LookUp channelModeLookUp) {
        this.channelModeLookUp = channelModeLookUp;
    }

    public LookUp getDisputeStatusLookUp() {
        return disputeStatusLookUp;
    }

    public void setDisputeStatusLookUp(LookUp disputeStatusLookUp) {
        this.disputeStatusLookUp = disputeStatusLookUp;
    }

    public Integer getDtFirstSLADeDup() {
        return dtFirstSLADeDup;
    }

    public void setDtFirstSLADeDup(Integer dtFirstSLADeDup) {
        this.dtFirstSLADeDup = dtFirstSLADeDup;
    }

    public Integer getDtLastSLADeDup() {
        return dtLastSLADeDup;
    }

    public void setDtLastSLADeDup(Integer dtLastSLADeDup) {
        this.dtLastSLADeDup = dtLastSLADeDup;
    }
}