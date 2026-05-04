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
@Table(schema = "data", name = "address")
@Convert(attributeName = "json", converter = JsonType.class)
public class Address implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "address_id")
	private Long addressId;

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

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address3")
	private String address3;

	@Column(name = "city")
	private String city;

	@Column(name = "state_code")
	private String stateCode;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(state_code))"))
	private CountryState countryState;

	@Column(name = "country")
	private String country;

	@Column(name = "zip")
	private String zip;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = substring(zip,1,5))"))
	private CountryZip countryZip;

	@Column(name = "address_status")
	private String addressStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'address_status' and lu.keycode = address_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp addressStatusLookUp;

	@Column(name = "address_type")
	private String addressType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'address_type' and lu.keycode = address_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )"))
	private LookUp addressTypeLookUp;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select sol.statutes_of_limitation_id from conf.statutes_of_limitation sol join conf.country_state cs on cs.country_state_id = sol.country_state_id join data.account acc on acc.client_id = client_id and acc.client_account_number = client_account_number join conf.product prd on prd.product_id = acc.product_id and prd.debtcategory_id = sol.debtcategory_id join conf.record_status rs on sol.record_status_id = rs.record_status_id and rs.short_name = ('"+ConfRecordStatus.ENABLED+"') where cs.state_code = state_code)")
	private StatutesOfLimitation statutesOfLimitation;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select sol.client_statutes_of_limitation_id from conf.client_statutes_of_limitation sol join conf.country_state cs on cs.country_state_id = sol.country_state_id join data.account acc on acc.client_id = client_id and acc.client_account_number = client_account_number join conf.product prd on prd.product_id = acc.product_id and prd.debtcategory_id = sol.debtcategory_id join conf.record_status rs on sol.record_status_id = rs.record_status_id and rs.short_name = ('"+ConfRecordStatus.ENABLED+"') where cs.state_code = state_code and sol.client_id = client_id)")
	private ClientStatutesOfLimitation clientStatutesOfLimitation;

	@Column(name = "is_primary")
	private Boolean isPrimary;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@Column(name = "err_short_name")
	private String errShortName;

	@Formula(value = "(select count(address.address_id) from data.address address where address.client_id = client_id and address.client_account_number = client_account_number and address.client_consumer_number = client_consumer_number and address.is_primary = true)")
	private Integer isPrimaryDeDup;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();


	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "( SELECT sol.statutes_of_limitation_id FROM conf.statutes_of_limitation sol JOIN conf.product prd ON prd.debtcategory_id = sol.debtcategory_id JOIN data.account acc ON acc.product_id = prd.product_id AND acc.client_id = client_id AND acc.client_account_number = client_account_number JOIN conf.record_status rs ON sol.record_status_id = rs.record_status_id AND rs.short_name = '" + ConfRecordStatus.ENABLED + "' ORDER BY sol.sol_month ASC LIMIT 1 )")
	private StatutesOfLimitation statutesOfLimitationMinMonthForProduct;
	
	
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
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

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
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

	public CountryState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryState countryState) {
		this.countryState = countryState;
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

	public CountryZip getCountryZip() {
		return countryZip;
	}

	public void setCountryZip(CountryZip countryZip) {
		this.countryZip = countryZip;
	}

	public String getAddressStatus() {
		return addressStatus;
	}

	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}

	public LookUp getAddressStatusLookUp() {
		return addressStatusLookUp;
	}

	public void setAddressStatusLookUp(LookUp addressStatusLookUp) {
		this.addressStatusLookUp = addressStatusLookUp;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public LookUp getAddressTypeLookUp() {
		return addressTypeLookUp;
	}

	public void setAddressTypeLookUp(LookUp addressTypeLookUp) {
		this.addressTypeLookUp = addressTypeLookUp;
	}

	public StatutesOfLimitation getStatutesOfLimitation() {
		return statutesOfLimitation;
	}

	public void setStatutesOfLimitation(StatutesOfLimitation statutesOfLimitation) {
		this.statutesOfLimitation = statutesOfLimitation;
	}

	public ClientStatutesOfLimitation getClientStatutesOfLimitation() {
		return clientStatutesOfLimitation;
	}

	public void setClientStatutesOfLimitation(ClientStatutesOfLimitation clientStatutesOfLimitation) {
		this.clientStatutesOfLimitation = clientStatutesOfLimitation;
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

	public Integer getIsPrimaryDeDup() {
		return isPrimaryDeDup;
	}

	public void setIsPrimaryDeDup(Integer isPrimaryDeDup) {
		this.isPrimaryDeDup = isPrimaryDeDup;
	}

	public Address() {
    	
    }

    public Address(Long addressId, Integer clientId, String clientAccountNumber) {
        this.addressId = addressId;
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

	public StatutesOfLimitation getStatutesOfLimitationMinMonthForProduct() {
		return statutesOfLimitationMinMonthForProduct;
	}

	public void setStatutesOfLimitationMinMonthForProduct(StatutesOfLimitation statutesOfLimitationMinMonthForProduct) {
		this.statutesOfLimitationMinMonthForProduct = statutesOfLimitationMinMonthForProduct;
	}
}