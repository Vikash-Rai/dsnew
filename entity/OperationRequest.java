package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.equabli.domain.helpers.CommonConstants;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "operation_request")
public class OperationRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "operation_request_id")
	private Long operationRequestId;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "request_source")
	private String requestSource;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'record_source' and lu.keycode = request_source and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp requestSourceLookUp;

	@Column(name = "request_source_id")
	private Long requestSourceId;

	@Column(name = "request_number")
	private String requestNumber;

	@Column(name = "operation_requesttype_id")
	private Integer operationRequesttypeId;

	@Column(name = "request_status")
	private String requestStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'request_status' and lu.keycode = request_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp requestStatusLookUp;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "operation_requesttype")
	private String operationRequestType;

	@Column(name = "priority")
	private Integer priority;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select ort.operation_requesttype_id from conf.operation_requesttype ort join conf.record_status rs on ort.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where ort.short_name = operation_requesttype)")
	private OperationRequestType operationRequestTypeMap;

	@Column(name = "queue_id")
	private Integer queueId;

	@Column(name = "queuereason_id")
	private Integer queueReasonId;

	@Column(name = "queuestatus_id")
	private Integer queueStatusId;

	@Column(name = "new_queuereason_id")
	private Integer newQueueReasonId;

	@Column(name = "queuereason")
	private String queueReason;

	@Formula(value = "(select qr.queuereason_id from conf.operation_requesttype ort join conf.record_status rs on ort.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' join conf.vwqueuereason qr on ort.queue_id = qr.queue_id and qr.record_status = '"+ConfRecordStatus.ENABLED+"' where qr.reason_short_name = queuereason limit 1)")
	private Integer queueReasonIds;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@Formula(value = "(select acc.queue_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer queueIds;

	@Formula(value = "(select acc.queuereason_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer queueReasonsIds;

	@Formula(value = "(select acc.queuestatus_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer queueStatusIds;

	@Column(name = "contact_id")
	private Integer contactId;

	@Column(name = "description")
	private String description;

	@Formula(value = "(select count(*) from data.compliance comp where comp.client_id = client_id and comp.client_account_number = client_account_number and comp.compliance_status IN ('"+CommonConstants.COMPLIANCE_STATUS_NEW+"', '"+CommonConstants.COMPLIANCE_STATUS_IN_PROGRESS+"', '"+CommonConstants.COMPLIANCE_STATUS_INFO_REQUEST+"', '"+CommonConstants.COMPLIANCE_STATUS_PENDING_PARTNER+"', '"+CommonConstants.COMPLIANCE_STATUS_PENDING_CLIENT+"') and comp.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private Integer complianceIsOpen;

	@Formula(value = "(select (current_date - pay.dt_payment) from data.account acc join data.payment pay on pay.client_id = acc.client_id and pay.client_account_number = acc.client_account_number where acc.amt_currentbalance = 0 and acc.client_account_number = client_account_number and pay.payment_settlement_type in ('"+CommonConstants.PAYMENT_SETTLEMENT_TYPE_PF+"', '"+CommonConstants.PAYMENT_SETTLEMENT_TYPE_SF+"') and pay.client_id = client_id and acc.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and pay.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') order by pay.dtm_utc_update desc limit 1)")
	private Integer sifPifDays;

	@Formula(value = "(select confval.configured_value from conf.appconfig_value confval inner join conf.appconfig_name confname on confname.appconfig_name_id = confval.appconfig_name_id where confval.configured_for = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' and confname.short_name = 'AUTO_RECALL_DELAY' and confval.client_id = client_id and confval.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and confname.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private Integer autoRecallDays;

	@Formula(value = "(select cast(confval.configured_value as Boolean) from conf.appconfig_value confval inner join conf.appconfig_name confname on confname.appconfig_name_id = confval.appconfig_name_id where ((confval.configured_for = '"+CommonConstants.RECORD_SOURCE_CLIENT+"' and confval.client_id = client_id) or (confval.configured_for = '"+CommonConstants.RECORD_SOURCE_EQUABLI+"')) and confname.short_name = 'AUTORESPONSE_ON_OPREQUEST' and confval.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and confname.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') order by confval.configured_for asc limit 1 )")
	private Boolean isAutoResponseOnOpRequest;

	@Column(name = "dt_fulfillment")
	private LocalDate fulfillmentDate;


	public Integer getComplianceIsOpen() {
		return complianceIsOpen;
	}

	public void setComplianceIsOpen(Integer countComplianceIsOpen) {
		this.complianceIsOpen = countComplianceIsOpen;
	}

	public Integer getSifPifDays() {
		return sifPifDays;
	}

	public void setSifPifDays(Integer sifPifDays) {
		this.sifPifDays = sifPifDays;
	}

	public Integer getAutoRecallDays() {
		return autoRecallDays;
	}

	public void setAutoRecallDays(Integer autoRecallDays) {
		this.autoRecallDays = autoRecallDays;
	}

	public Boolean getIsAutoResponseOnOpRequest() {
		return isAutoResponseOnOpRequest;
	}

	public void setIsAutoResponseOnOpRequest(Boolean isAutoResponseOnOpRequest) {
		this.isAutoResponseOnOpRequest = isAutoResponseOnOpRequest;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Long getOperationRequestId() {
		return operationRequestId;
	}

	public void setOperationRequestId(Long operationRequestId) {
		this.operationRequestId = operationRequestId;
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

	public String getRequestSource() {
		return requestSource;
	}

	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}

	public LookUp getRequestSourceLookUp() {
		return requestSourceLookUp;
	}

	public void setRequestSourceLookUp(LookUp requestSourceLookUp) {
		this.requestSourceLookUp = requestSourceLookUp;
	}

	public Long getRequestSourceId() {
		return requestSourceId;
	}

	public void setRequestSourceId(Long requestSourceId) {
		this.requestSourceId = requestSourceId;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Integer getOperationRequesttypeId() {
		return operationRequesttypeId;
	}

	public void setOperationRequesttypeId(Integer operationRequesttypeId) {
		this.operationRequesttypeId = operationRequesttypeId;
	}

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String requestStatus) {
		this.requestStatus = requestStatus;
	}

	public LookUp getRequestStatusLookUp() {
		return requestStatusLookUp;
	}

	public void setRequestStatusLookUp(LookUp requestStatusLookUp) {
		this.requestStatusLookUp = requestStatusLookUp;
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

	public String getOperationRequestType() {
		return operationRequestType;
	}

	public void setOperationRequestType(String operationRequestType) {
		this.operationRequestType = operationRequestType;
	}

	public OperationRequestType getOperationRequestTypeMap() {
		return operationRequestTypeMap;
	}

	public void setOperationRequestTypeMap(OperationRequestType operationRequestTypeMap) {
		this.operationRequestTypeMap = operationRequestTypeMap;
	}

	public Integer getQueueId() {
		return queueId;
	}

	public void setQueueId(Integer queueId) {
		this.queueId = queueId;
	}

	public Integer getQueueReasonId() {
		return queueReasonId;
	}

	public void setQueueReasonId(Integer queueReasonId) {
		this.queueReasonId = queueReasonId;
	}

	public Integer getQueueStatusId() {
		return queueStatusId;
	}

	public void setQueueStatusId(Integer queueStatusId) {
		this.queueStatusId = queueStatusId;
	}

	public Integer getNewQueueReasonId() {
		return newQueueReasonId;
	}

	public void setNewQueueReasonId(Integer newQueueReasonId) {
		this.newQueueReasonId = newQueueReasonId;
	}

	public String getQueueReason() {
		return queueReason;
	}

	public void setQueueReason(String queueReason) {
		this.queueReason = queueReason;
	}

	public Integer getQueueReasonIds() {
		return queueReasonIds;
	}

	public void setQueueReasonIds(Integer queueReasonIds) {
		this.queueReasonIds = queueReasonIds;
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

	public Integer getQueueIds() {
		return queueIds;
	}

	public void setQueueIds(Integer queueIds) {
		this.queueIds = queueIds;
	}

	public Integer getQueueReasonsIds() {
		return queueReasonsIds;
	}

	public void setQueueReasonsIds(Integer queueReasonsIds) {
		this.queueReasonsIds = queueReasonsIds;
	}

	public Integer getQueueStatusIds() {
		return queueStatusIds;
	}

	public void setQueueStatusIds(Integer queueStatusIds) {
		this.queueStatusIds = queueStatusIds;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public LocalDate getFulfillmentDate() {
		return fulfillmentDate;
	}

	public void setFulfillmentDate(LocalDate fulfillmentDate) {
		this.fulfillmentDate = fulfillmentDate;
	}
}