package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(schema = "data", name = "consumer")
@Convert(attributeName = "json", converter = JsonType.class)
public class Consumer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "consumer_id")
	private Long consumerId;

	@Column(name = "client_id")
	private Integer clientId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '"+ ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)")
	private Client client;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "employer_id")
	private Integer employerId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinFormula(value = "(select emp.employer_id from data.employer emp where emp.client_id = client_id and emp.client_employer_number = client_employer_number and emp.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private Employer employer;

	@Column(name = "client_account_number")
	private String clientAccountNumber;

	@Column(name = "client_consumer_number")
	private Long clientConsumerNumber;

	@Column(name = "client_employer_number")
	private String clientEmployerNumber;

	@Column(name = "contact_type")
	private Integer contactType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu join conf.lookup_group lg on lg.lookup_group_id = lu.lookup_group_id where lu.group_sequence = contact_type and lg.keyvalue = 'contact_type' and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp contactTypeLookUp;

	@Column(name = "preferred_language")
	private String preferredLanguage;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'preferred_language' and lu.keycode = preferred_language and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp preferredLanguageLookUp;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "dt_birth")
	private LocalDate dob;
	
	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "business_name")
	private String businessName;

	@Column(name = "identification_number")
	private String identificationNumber;

	@Column(name = "service_branch")
	private String serviceBranch;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "err_short_name")
	private String errShortName;

	@Column(name = "dl_number")
	private String driverLicenseNumber;

	@Formula(value = "(select count(cns.consumer_id) from data.consumer cns where cns.client_id = client_id and cns.client_account_number = client_account_number and cns.client_consumer_number = client_consumer_number)")
	private Integer clientConsumerNoDeDup;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_consumer_number", referencedColumnName = "client_consumer_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Phone> phone;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
    	 @JoinColumn(name = "consumer_id", referencedColumnName = "consumer_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Phone> phoneConsumer;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_consumer_number", referencedColumnName = "client_consumer_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Address> address;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
    	 @JoinColumn(name = "consumer_id", referencedColumnName = "consumer_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Address> addressConsumers;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_consumer_number", referencedColumnName = "client_consumer_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Email> email;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
    	@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT)),
    	@JoinColumn(name = "consumer_id", referencedColumnName = "consumer_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT))
	})
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Email> emailConsumer;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Transient
	private boolean isEntityPrimary;

	@Transient
	private Long addressId;

	@Transient
	private Long emailId;

	@Transient
	private Long phoneId;

	@Transient
	private String address1;

	@Transient
	private String address2;

	@Transient
	private String city;

	@Transient
	private String stateCode;

	@Transient
	private String country;

	@Transient
	private String zip;

	@Transient
	private String phoneNo;

	@Transient
	private String fax;

	@Transient
	private String emailAddress;


	public Long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(Long consumerId) {
		this.consumerId = consumerId;
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

	public Integer getEmployerId() {
		return employerId;
	}

	public void setEmployerId(Integer employerId) {
		this.employerId = employerId;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
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

	public String getClientEmployerNumber() {
		return clientEmployerNumber;
	}

	public void setClientEmployerNumber(String clientEmployerNumber) {
		this.clientEmployerNumber = clientEmployerNumber;
	}

	public Integer getContactType() {
		return contactType;
	}

	public void setContactType(Integer contactType) {
		this.contactType = contactType;
	}

	public LookUp getContactTypeLookUp() {
		return contactTypeLookUp;
	}

	public void setContactTypeLookUp(LookUp contactTypeLookUp) {
		this.contactTypeLookUp = contactTypeLookUp;
	}

	public String getPreferredLanguage() {
		return preferredLanguage;
	}

	public void setPreferredLanguage(String preferredLanguage) {
		this.preferredLanguage = preferredLanguage;
	}

	public LookUp getPreferredLanguageLookUp() {
		return preferredLanguageLookUp;
	}

	public void setPreferredLanguageLookUp(LookUp preferredLanguageLookUp) {
		this.preferredLanguageLookUp = preferredLanguageLookUp;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getServiceBranch() {
		return serviceBranch;
	}

	public void setServiceBranch(String serviceBranch) {
		this.serviceBranch = serviceBranch;
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

	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public Integer getClientConsumerNoDeDup() {
		return clientConsumerNoDeDup;
	}

	public void setClientConsumerNoDeDup(Integer clientConsumerNoDeDup) {
		this.clientConsumerNoDeDup = clientConsumerNoDeDup;
	}

	public List<Phone> getPhone() {
//		if(this.phoneConsumer != null && this.phoneConsumer.size() > 0)
//			this.phone.addAll(this.phoneConsumer);
		return phone;
	}

	public void setPhone(List<Phone> phone) {
		this.phone = phone;
	}

	public List<Address> getAddress() {
//		if(this.addressConsumers != null && this.addressConsumers.size() > 0)
//			this.address.addAll(this.addressConsumers);
		return address;
	}

	public void setAddress(List<Address> address) {
		this.address = address;
	}

	public List<Email> getEmail() {
//		if(this.emailConsumer != null && this.emailConsumer.size() > 0)
//			this.email.addAll(this.emailConsumer);
		return email;
	}

	public void setEmail(List<Email> email) {
		this.email = email;
	}

	public boolean isEntityPrimary() {
		return isEntityPrimary;
	}

	public void setEntityPrimary(boolean isEntityPrimary) {
		this.isEntityPrimary = isEntityPrimary;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public Long getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(Long phoneId) {
		this.phoneId = phoneId;
	}
	
	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Consumer() {
    	
    }

    public Consumer(Integer clientId, String clientAccountNumber, Long clientConsumerNumber) {
        this.clientId = clientId;
        this.clientAccountNumber = clientAccountNumber;
        this.clientConsumerNumber = clientConsumerNumber;
    }

    public Consumer(Long consumerId, Integer clientId, String clientAccountNumber) {
        this.consumerId = consumerId;
        this.clientId = clientId;
        this.clientAccountNumber = clientAccountNumber;
    }

	public Consumer(String firstName, String middleName, String lastName, String address1, String address2, String city, String stateCode, String country, 
			String zip, String phoneNo, String fax, String emailAddress) {
    	this.firstName = firstName;
    	this.middleName = middleName;
    	this.lastName = lastName;
    	this.address1 = address1;
    	this.address2 = address2;
    	this.city = city;
    	this.stateCode = stateCode;
    	this.country = country;
    	this.zip = zip;
    	this.phoneNo = phoneNo;
    	this.fax = fax;
    	this.emailAddress = emailAddress;
    }
	
	public Consumer(Long consumerId, Integer clientId, Long accountId, String clientAccountNumber, Long clientConsumerNumber ) {
		this.consumerId = consumerId;
		this.clientId = clientId;
		this.accountId = accountId;
		this.clientAccountNumber = clientAccountNumber;
		this.clientConsumerNumber = clientConsumerNumber;
	}

    public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public List<Phone> getPhoneConsumer() {
		return phoneConsumer;
	}

	public void setPhoneConsumer(List<Phone> phoneConsumer) {
		this.phoneConsumer = phoneConsumer;
	}

	public List<Address> getAddressConsumers() {
		return addressConsumers;
	}

	public void setAddressConsumers(List<Address> addressConsumers) {
		this.addressConsumers = addressConsumers;
	}

	public List<Email> getEmailConsumer() {
		return emailConsumer;
	}

	public void setEmailConsumer(List<Email> emailConsumer) {
		this.emailConsumer = emailConsumer;
	}

}