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
@Table(schema = "data", name = "changelog")
@Convert(attributeName = "json", converter = JsonType.class)
public class ChangeLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "changelog_id")
	private Long changeLogId;
	
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

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;
	
	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)")
	private RecordStatus recordStatus;

	@Column(name = "entity_short_name")
	private String entityShortName;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select e.entity_id from conf.entity e where e.short_name = entity_short_name)")
	private ConfEntity confEntity;
	
	@Column(name = "attribute_name")
	private String attributeName;
	
	@Column(name = "external_source_type")
	private String externalSourceType;
	
	@Column(name = "external_source_id")
	private Integer externalSourceId;
	
	@Column(name = "external_system_id")
	private Long externalSystemId;
	
	@Column(name = "previous_value")
	private String previousValue;
	
	@Column(name = "new_value")
	private String newValue;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select e.entity_attribute_id from conf.entity_attribute e where e.entity_short_name = entity_short_name and e.attribute_name = attribute_name)")
	private EntityAttribute entityAttribute;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.dt_last_payment from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate lastPaymentDate;
	
	@Formula(value = "(select acc.dt_delinquency from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private LocalDate delinquencyDate;

	@Formula(value = "(select acc.partner_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Integer partnerId;
	
	@Formula(value = "(select confval.configured_value from conf.appconfig_value confval inner join conf.appconfig_name confname on confname.appconfig_name_id = confval.appconfig_name_id  left join data.account acc on acc.client_id = client_id and acc.client_account_number = client_account_number where confval.configured_for = '"+CommonConstants.RECORD_SOURCE_PARTNER+"' and confname.short_name = 'PARTNER_SOL' and confval.partner_id = acc.partner_id and confval.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and confname.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private Boolean solDateConfiguration;
	

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Boolean getSolDateConfiguration() {
		return solDateConfiguration;
	}

	public void setSolDateConfiguration(Boolean solDateConfiguration) {
		this.solDateConfiguration = solDateConfiguration;
	}

	public LocalDate getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(LocalDate lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public LocalDate getDelinquencyDate() {
		return delinquencyDate;
	}

	public void setDelinquencyDate(LocalDate delinquencyDate) {
		this.delinquencyDate = delinquencyDate;
	}

	public Long getChangeLogId() {
		return changeLogId;
	}

	public void setChangeLogId(Long changeLogId) {
		this.changeLogId = changeLogId;
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

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public String getEntityShortName() {
		return entityShortName;
	}

	public void setEntityShortName(String entityShortName) {
		this.entityShortName = entityShortName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getExternalSourceType() {
		return externalSourceType;
	}

	public void setExternalSourceType(String externalSourceType) {
		this.externalSourceType = externalSourceType;
	}

	public Integer getExternalSourceId() {
		return externalSourceId;
	}

	public void setExternalSourceId(Integer externalSourceId) {
		this.externalSourceId = externalSourceId;
	}

	public Long getExternalSystemId() {
		return externalSystemId;
	}

	public void setExternalSystemId(Long externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	public String getPreviousValue() {
		return previousValue;
	}

	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
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
	
	public LocalDateTime getDtmUtcCreate() {
		return dtmUtcCreate;
	}

	public void setDtmUtcCreate(LocalDateTime dtmUtcCreate) {
		this.dtmUtcCreate = dtmUtcCreate;
	}

	public LocalDateTime getDtmUtcUpdate() {
		return dtmUtcUpdate;
	}

	public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
		this.dtmUtcUpdate = dtmUtcUpdate;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public RecordStatus getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(RecordStatus recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public ConfEntity getConfEntity() {
		return confEntity;
	}

	public void setConfEntity(ConfEntity confEntity) {
		this.confEntity = confEntity;
	}

	public EntityAttribute getEntityAttribute() {
		return entityAttribute;
	}

	public void setEntityAttribute(EntityAttribute entityAttribute) {
		this.entityAttribute = entityAttribute;
	}

	public ChangeLog() {
    }

    public ChangeLog(Long changeLogId, Integer clientId, String clientAccountNumber) {
        this.changeLogId = changeLogId;
        this.clientId = clientId;
        this.clientAccountNumber = clientAccountNumber;
    }
}