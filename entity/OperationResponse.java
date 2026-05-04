package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import com.equabli.domain.entity.ConfRecordStatus;

@Entity
@Table(schema = "data", name = "operation_response")
public class OperationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "operation_response_id")
	private Long operationResponseId;

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

	@Column(name = "response_source")
	private String responseSource;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'record_source' and lu.keycode = response_source and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp responseSourceLookUp;

	@Column(name = "response_source_id")
	private Long responseSourceId;

	@Column(name = "request_number")
	private String requestNumber;

	@Column(name = "response_status")
	private String responseStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'response_status' and lu.keycode = response_status  and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp responseStatusLookUp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select opr.operation_request_id from data.operation_request opr join conf.record_status rs on opr.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where opr.client_id = client_id and opr.client_account_number = client_account_number and opr.request_number = request_number )")
	private OperationRequest operationRequest;

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

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "contact_id")
	private Integer contactId;

	@Column(name = "dt_response")
	private Date dtResponse;
	@Column(name = "description")
	private String description;

	@Column(name = "record_source_id")
	private Integer recordSourceId;

	@Column(name = "app_id")
	private Integer appId;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	public OperationResponse() {
	}

	public OperationResponse(Long operationRequestId, Integer clientId, Long clientConsumerNumber, Long consumerId, Long accountId, String clientAccountNumber,
							 String responseSource, Long responseSourceId, String requestNumber, String responseStatus, Integer recordStatusId,
							 String recordType, Integer contactId, Date dtResponse, String description, Integer recordSourceId, Integer appId, String createdBy,
							 String updatedBy, LocalDateTime dtmUtcUpdate) {
		this.operationRequestId = operationRequestId;
		this.clientId = clientId;
		this.clientConsumerNumber = clientConsumerNumber;
		this.consumerId = consumerId;
		this.accountId = accountId;
		this.clientAccountNumber = clientAccountNumber;
		this.responseSource = responseSource;
		this.responseSourceId = responseSourceId;
		this.requestNumber = requestNumber;
		this.responseStatus = responseStatus;
		this.recordStatusId = recordStatusId;
		this.recordType = recordType;
		this.contactId = contactId;
		this.dtResponse = dtResponse;
		this.description = description;
		this.recordSourceId = recordSourceId;
		this.appId = appId;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.dtmUtcUpdate = dtmUtcUpdate;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public Integer getContactId() {
		return contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Date getDtResponse() {
		return dtResponse;
	}

	public void setDtResponse(Date dtResponse) {
		this.dtResponse = dtResponse;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRecordSourceId() {
		return recordSourceId;
	}

	public void setRecordSourceId(Integer recordSourceId) {
		this.recordSourceId = recordSourceId;
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

	public Long getOperationResponseId() {
		return operationResponseId;
	}

	public void setOperationResponseId(Long operationResponseId) {
		this.operationResponseId = operationResponseId;
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

	public String getResponseSource() {
		return responseSource;
	}

	public void setResponseSource(String responseSource) {
		this.responseSource = responseSource;
	}

	public LookUp getResponseSourceLookUp() {
		return responseSourceLookUp;
	}

	public void setResponseSourceLookUp(LookUp responseSourceLookUp) {
		this.responseSourceLookUp = responseSourceLookUp;
	}

	public Long getResponseSourceId() {
		return responseSourceId;
	}

	public void setResponseSourceId(Long responseSourceId) {
		this.responseSourceId = responseSourceId;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public LookUp getResponseStatusLookUp() {
		return responseStatusLookUp;
	}

	public void setResponseStatusLookUp(LookUp responseStatusLookUp) {
		this.responseStatusLookUp = responseStatusLookUp;
	}

	public OperationRequest getOperationRequest() {
		return operationRequest;
	}

	public void setOperationRequest(OperationRequest operationRequest) {
		this.operationRequest = operationRequest;
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
}