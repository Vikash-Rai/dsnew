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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(schema = "data", name = "employer")
public class Employer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employer_id")
	private Integer employerId;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "client_employer_number")
	private String clientEmployerNumber;

	@Column(name = "full_name")
	private String fullName;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "state_code")
	private String stateCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where cs.state_code = state_code)"))
	private CountryState countryState;

	@Column(name = "city")
	private String city;

	@Column(name = "zip")
	private String zip;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = zip)"))
	private CountryZip countryZip;

	@Column(name = "phone")
	private String phone;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select rs.record_status_id from conf.record_status rs where rs.record_status_id = record_status_id)"))
	private RecordStatus recordStatus;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "consumer_identification_number")
	private String consumerIdentificationNumber;

	@Formula(value = "(select count(emp.employer_id) from data.employer emp where emp.client_id = client_id and emp.client_employer_number = client_employer_number)")
	private Integer clientEmployerNumberDeDup;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIdsByConsumerNumber;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.identification_number = consumer_identification_number)")
	private Long consumerIdsByIdentificationNumber;

	@Column(name = "employer_append_status")
	private String employerStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'employer_append_status' and lu.keycode = employer_append_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp employerStatusLookUp;

	@Column(name = "consumer_wagepay_frequency")
	private String consumerWagepayFrequency;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'consumer_wagepay_frequency' and lu.keycode = consumer_wagepay_frequency and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp consumerWagepayFrequencyLookUp;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();


	public Integer getEmployerId() {
		return employerId;
	}

	public void setEmployerId(Integer employerId) {
		this.employerId = employerId;
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

	public String getClientEmployerNumber() {
		return clientEmployerNumber;
	}

	public void setClientEmployerNumber(String clientEmployerNumber) {
		this.clientEmployerNumber = clientEmployerNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public CountryState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryState countryState) {
		this.countryState = countryState;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public CountryZip getCountryZip() {
		return countryZip;
	}

	public void setCountryZip(CountryZip countryZip) {
		this.countryZip = countryZip;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Integer getClientEmployerNumberDeDup() {
		return clientEmployerNumberDeDup;
	}

	public void setClientEmployerNumberDeDup(Integer clientEmployerNumberDeDup) {
		this.clientEmployerNumberDeDup = clientEmployerNumberDeDup;
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

	public String getConsumerIdentificationNumber() {
		return consumerIdentificationNumber;
	}

	public void setConsumerIdentificationNumber(String consumerIdentificationNumber) {
		this.consumerIdentificationNumber = consumerIdentificationNumber;
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

	public Long getConsumerIdsByConsumerNumber() {
		return consumerIdsByConsumerNumber;
	}

	public void setConsumerIdsByConsumerNumber(Long consumerIdsByConsumerNumber) {
		this.consumerIdsByConsumerNumber = consumerIdsByConsumerNumber;
	}

	public Long getConsumerIdsByIdentificationNumber() {
		return consumerIdsByIdentificationNumber;
	}

	public void setConsumerIdsByIdentificationNumber(Long consumerIdsByIdentificationNumber) {
		this.consumerIdsByIdentificationNumber = consumerIdsByIdentificationNumber;
	}

	public String getEmployerStatus() {
		return employerStatus;
	}

	public void setEmployerStatus(String employerStatus) {
		this.employerStatus = employerStatus;
	}

	public LookUp getEmployerStatusLookUp() {
		return employerStatusLookUp;
	}

	public void setEmployerStatusLookUp(LookUp employerStatusLookUp) {
		this.employerStatusLookUp = employerStatusLookUp;
	}

	public String getConsumerWagepayFrequency() {
		return consumerWagepayFrequency;
	}

	public void setConsumerWagepayFrequency(String consumerWagepayFrequency) {
		this.consumerWagepayFrequency = consumerWagepayFrequency;
	}

	public LookUp getConsumerWagepayFrequencyLookUp() {
		return consumerWagepayFrequencyLookUp;
	}

	public void setConsumerWagepayFrequencyLookUp(LookUp consumerWagepayFrequencyLookUp) {
		this.consumerWagepayFrequencyLookUp = consumerWagepayFrequencyLookUp;
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