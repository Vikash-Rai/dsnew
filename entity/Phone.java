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
@Table(schema = "data", name = "phone")
@Convert(attributeName = "json", converter = JsonType.class)
public class Phone implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "phone_id")
	private Long phoneId;

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

	@Column(name = "phone")
	private String phone;

	@Column(name = "phone_type")
	private String phoneType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'phone_type' and lu.keycode = phone_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp phoneTypeLookUp;

	@Column(name = "phone_status")
	private String phoneStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'phone_number_status' and lu.keycode = phone_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp phoneStatusLookUp;

	@Column(name = "is_consent")
	private Boolean isConsent;

	@Column(name = "is_primary")
	private Boolean isPrimary;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "is_autodialconsent")
	private Boolean autoDialConsent;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "dtm_autodialconsent")
	private LocalDateTime autoDialConsentDate;

	@Column(name = "autodialconsent_source_id")
	private Integer autoDialConsentSourceId;

	@Column(name = "is_smsconsent")
	private Boolean smsConsent;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "dtm_smsconsent")
	private LocalDateTime smsConsentDate;

	@Column(name = "smsconsent_source_id")
	private Integer smsConsentSourceId;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "err_short_name")
	private String errShortName;

	@Formula(value = "(select count(phone.phone_id) from data.phone phone where phone.client_id = client_id and phone.client_account_number = client_account_number and phone.client_consumer_number = client_consumer_number and phone.phone = phone)")
	private Integer phoneNoDeDup;

	@Formula(value = "(select count(phone.phone_id) from data.phone phone where phone.client_id = client_id and phone.client_account_number = client_account_number and phone.client_consumer_number = client_consumer_number and phone.is_primary = true)")
	private Integer isPrimaryDeDup;

	@Formula(value = "(select cpt.timezone_name from conf.country_phone_timezone cpt join conf.record_status rs on cpt.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where left(phone, 3) = cpt.phonearea)")
	private String phoneNoTimeZone;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();
	
	public Long getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Long phoneId) {
		this.phoneId = phoneId;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public LookUp getPhoneTypeLookUp() {
		return phoneTypeLookUp;
	}

	public void setPhoneTypeLookUp(LookUp phoneTypeLookUp) {
		this.phoneTypeLookUp = phoneTypeLookUp;
	}

	public String getPhoneStatus() {
		return phoneStatus;
	}

	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}

	public LookUp getPhoneStatusLookUp() {
		return phoneStatusLookUp;
	}

	public void setPhoneStatusLookUp(LookUp phoneStatusLookUp) {
		this.phoneStatusLookUp = phoneStatusLookUp;
	}

	public Boolean getIsConsent() {
		return isConsent;
	}

	public void setIsConsent(Boolean isConsent) {
		this.isConsent = isConsent;
	}

	public Boolean getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(Boolean isPrimary) {
		this.isPrimary = isPrimary;
	}

	public Boolean getAutoDialConsent() {
		return autoDialConsent;
	}

	public void setAutoDialConsent(Boolean autoDialConsent) {
		this.autoDialConsent = autoDialConsent;
	}

	public LocalDateTime getAutoDialConsentDate() {
		return autoDialConsentDate;
	}

	public void setAutoDialConsentDate(LocalDateTime autoDialConsentDate) {
		this.autoDialConsentDate = autoDialConsentDate;
	}

	public Integer getAutoDialConsentSourceId() {
		return autoDialConsentSourceId;
	}

	public void setAutoDialConsentSourceId(Integer autoDialConsentSourceId) {
		this.autoDialConsentSourceId = autoDialConsentSourceId;
	}

	public Boolean getSmsConsent() {
		return smsConsent;
	}

	public void setSmsConsent(Boolean smsConsent) {
		this.smsConsent = smsConsent;
	}

	public LocalDateTime getSmsConsentDate() {
		return smsConsentDate;
	}

	public void setSmsConsentDate(LocalDateTime smsConsentDate) {
		this.smsConsentDate = smsConsentDate;
	}

	public Integer getSmsConsentSourceId() {
		return smsConsentSourceId;
	}

	public void setSmsConsentSourceId(Integer smsConsentSourceId) {
		this.smsConsentSourceId = smsConsentSourceId;
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

	public Integer getPhoneNoDeDup() {
		return phoneNoDeDup;
	}

	public void setPhoneNoDeDup(Integer phoneNoDeDup) {
		this.phoneNoDeDup = phoneNoDeDup;
	}

	public Integer getIsPrimaryDeDup() {
		return isPrimaryDeDup;
	}

	public void setIsPrimaryDeDup(Integer isPrimaryDeDup) {
		this.isPrimaryDeDup = isPrimaryDeDup;
	}

	public String getPhoneNoTimeZone() {
		return phoneNoTimeZone;
	}

	public void setPhoneNoTimeZone(String phoneNoTimeZone) {
		this.phoneNoTimeZone = phoneNoTimeZone;
	}

	public Phone() {
    	
    }

    public Phone(Long phoneId, Integer clientId, String clientAccountNumber) {
        this.phoneId = phoneId;
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
}