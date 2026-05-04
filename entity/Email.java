package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinColumnOrFormula;
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
@Table(schema = "data", name = "email")
@Convert(attributeName = "json", converter = JsonType.class)
public class Email implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "email_id")
	private Long emailId;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)"))
	private Client client;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "email_address")
	private String emailAddress;

	@Column(name = "is_primary")
	private Boolean isPrimary;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "is_consent")
	private Boolean consent;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "dtm_consent")
	private LocalDateTime consentDate;

	@Column(name = "consent_source_id")
	private Integer consentSourceId;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "err_short_name")
	private String errShortName;

	@Formula(value = "(select count(email.email_id) from data.email email where email.client_id = client_id and email.client_account_number = client_account_number and email.client_consumer_number = client_consumer_number and email.email_address = email_address)")
	private Integer emailDeDup;

	@Formula(value = "(select count(email.email_id) from data.email email where email.client_id = client_id and email.client_account_number = client_account_number and email.client_consumer_number = client_consumer_number and email.is_primary = true)")
	private Integer isPrimaryDeDup;

	@Column(name = "status_code")
	private String emailStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'email_status' and lu.keycode = status_code and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp emailStatusLookUp;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
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

	public String getErrShortName() {
		return errShortName;
	}

	public void setErrShortName(String errShortName) {
		this.errShortName = errShortName;
	}

	public Boolean getConsent() {
		return consent;
	}

	public void setConsent(Boolean consent) {
		this.consent = consent;
	}

	public LocalDateTime getConsentDate() {
		return consentDate;
	}

	public void setConsentDate(LocalDateTime consentDate) {
		this.consentDate = consentDate;
	}

	public Integer getConsentSourceId() {
		return consentSourceId;
	}

	public void setConsentSourceId(Integer consentSourceId) {
		this.consentSourceId = consentSourceId;
	}

	public Integer getEmailDeDup() {
		return emailDeDup;
	}

	public void setEmailDeDup(Integer emailDeDup) {
		this.emailDeDup = emailDeDup;
	}

	public Integer getIsPrimaryDeDup() {
		return isPrimaryDeDup;
	}

	public void setIsPrimaryDeDup(Integer isPrimaryDeDup) {
		this.isPrimaryDeDup = isPrimaryDeDup;
	}

	public Email() {
    	
    }

    public Email(Long emailId, Integer clientId, String clientAccountNumber) {
        this.emailId = emailId;
        this.clientId = clientId;
        this.clientAccountNumber = clientAccountNumber;
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

	public String getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	public LookUp getEmailStatusLookUp() {
		return emailStatusLookUp;
	}

	public void setEmailStatusLookUp(LookUp emailStatusLookUp) {
		this.emailStatusLookUp = emailStatusLookUp;
	}
}