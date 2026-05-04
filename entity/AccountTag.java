package com.equabli.collectprism.entity;

import com.equabli.domain.entity.ConfRecordStatus;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JoinFormula;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "data", name = "account_tag")
public class AccountTag implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "account_tag_id")
	private Long accountTagId;

	@Column(name = "record_type")
	private String recordType;

	@Column(name = "account_id")
	private Long accountId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "client_id")
	private Integer clientId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "entity_short_name")
	private String entityShortName;

	@Formula(value = "(select en.full_name from conf.entity en where en.short_name = entity_short_name and en.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private String entityName;

	@Column(name = "ref_entity_id")
	private Long refEntityId;

	@Column(name = "ref_entity_number")
	private String refEntityNumber;

	@Column(name = "tag_name")
	private String tagName;

	@Formula(value = "(select ea.entity_attribute_id from conf.entity_attribute ea where ea.entity_short_name = entity_short_name and ea.attribute_name = tag_name and ea.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private Integer entityAttributeId;

	@Column(name = "tag_value")
	private String tagValue;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select con.consumer_id from data.consumer con join conf.vwlookup lu on lu.lookup_id = con.contact_type and lu.lookup_group_value = 'contact_type' and lu.keycode = 'PD' join conf.record_status rs on con.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where con.client_id = client_id and con.client_account_number = client_account_number)")
	private Long consumerId;

	@Formula(value = "(select con.client_consumer_number from data.consumer con join conf.vwlookup lu on lu.lookup_id = con.contact_type and lu.lookup_group_value = 'contact_type' and lu.keycode = 'PD' join conf.record_status rs on con.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where con.client_id = client_id and con.client_account_number = client_account_number)")
	private Long clientConsumerNumber;


	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Long getAccountTagId() {
		return accountTagId;
	}

	public void setAccountTagId(Long accountTagId) {
		this.accountTagId = accountTagId;
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Integer getClientId() {
		return clientId;
	}

	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String getEntityShortName() {
		return entityShortName;
	}

	public void setEntityShortName(String entityShortName) {
		this.entityShortName = entityShortName;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getRefEntityId() {
		return refEntityId;
	}

	public void setRefEntityId(Long refEntityId) {
		this.refEntityId = refEntityId;
	}

	public String getRefEntityNumber() {
		return refEntityNumber;
	}

	public void setRefEntityNumber(String refEntityNumber) {
		this.refEntityNumber = refEntityNumber;
	}

	public Integer getEntityAttributeId() {
		return entityAttributeId;
	}

	public void setEntityAttributeId(Integer entityAttributeId) {
		this.entityAttributeId = entityAttributeId;
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

	public AccountTag(Long accountTagId, String recordType, Long accountId, Client client, Integer clientId, String clientAccountNumber, Integer recordStatusId, String tagName, String tagValue, Long accountIds, String entityShortName, String entityName, Long refEntityId, String refEntityNumber, Integer entityAttributeId, Long consumerId, Long clientConsumerNumber) {
		this.accountTagId = accountTagId;
		this.recordType = recordType;
		this.accountId = accountId;
		this.client = client;
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
		this.recordStatusId = recordStatusId;
		this.tagName = tagName;
		this.tagValue = tagValue;
		this.accountIds = accountIds;
		this.entityShortName = entityShortName;
		this.entityName = entityName;
		this.refEntityId = refEntityId;
		this.refEntityNumber = refEntityNumber;
		this.entityAttributeId = entityAttributeId;
		this.consumerId = consumerId;
		this.clientConsumerNumber = clientConsumerNumber;
	}

	public AccountTag() {
	}
}