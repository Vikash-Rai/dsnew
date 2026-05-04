package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(schema = "data", name = "deceased")
@Convert(attributeName = "json", converter = JsonType.class)
public class Deceased implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "deceased_id")
	private Long deceasedId;

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

	@Column(name = "partner_id")
	private Integer partnerId;

	@Column(name = "client_deceased_id")
	private Integer clientDeceasedId;


	@Column(name  = "decedent")
	private Long decedent;

	@Column(name = "record_status_id")
	private Integer recordStatusId;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dt_report")
	private LocalDate dtReport;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dt_deceased")
	private LocalDate dtDeceased;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dt_resolution")
	private LocalDate dtResolution;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Column(name = "dt_filing")
	private LocalDate dtFiling;
	
	@Column(name  = "deceased_case_number")
	private String deceasedCaseNumber;

	@Column(name = "channel")
	private String channel;

	@Column(name = "mode")
	private String mode;
	
	@Column(name = "amt_balance_deceased")
	private Double amtBalanceDeceased;
	
	@Column(name = "amt_balance_deceased_notified_partner")
	private Double amtBalanceDeceasedNotifiedpartner;
	
	@Column(name = "amt_dc_principal")
	private Double amtDcPrincipal;
	
	@Column(name = "amt_dc_interest")
	private Double amtDcInterest;
	
	@Column(name = "amt_dc_fees")
	private Double amtDcFees;
	
	@Column(name = "amt_dc_total")
	private Double amtDcTotal;
	
	@Column(name = "dc_status")
	private String dcStatus;
	
	@Column(name = "probate_firstname")
	private String probateFirstName;
	
	@Column(name = "probate_middlename")
	private String probateMiddleName;
	
	@Column(name = "probate_lastname")
	private String probateLastName;
	
	@Column(name = "probate_address")
	private String probateAddress;
	
	@Column(name = "probate_city")
	private String probateCity;
	
	@Column(name = "probate_state")
	private String probateState;
	
	@Column(name = "probate_zip")
	private String probateZip;
	
	@Column(name = "probate_phone")
	private String probatePhone;
	
	@Column(name = "probate_email")
	private String probateEmail;
	
	@Column(name = "probateFirmName")
	private String probate_firm_name;
	
	@Column(name = "probate_firm_address")
	private String probateFirmAddress;

	@Column(name = "probate_firm_city")
	private String probateFirmCity;
	
	
	@Column(name = "probate_firm_zip")
	private String probateFirmZip;
	
	@Column(name = "probate_firm_state")
	private String probateFirmState;
	
	@Column(name = "probate_firm_phone")
	private String probateFirmPhone;
	
	@Column(name = "probate_firm_email")
	private String probateFirmEmail;
	
	@Column(name = "executor_firstname")
	private String executorFirstName;
	
	@Column(name = "executor_middlename")
	private String executorMiddleName;
	
	@Column(name = "executor_lastname")
	private String executorLastName;
	
	@Column(name = "executor_address")
	private String executorAddress;
	
	@Column(name = "executor_city")
	private String executorCity;
	
	@Column(name = "executor_state")
	private String executorState;
	
	@Column(name = "executor_zip")
	private String executorZip;
	
	
	@Column(name = "executor_phone")
	private String executorPhone;
	
	@Column(name = "executor_email")
	private String executorEmail;
	
	@Column(name = "executor_firm_name")
	private String executorFirmName;
	
	@Column(name = "executor_firm_address")
	private String executorFirmAddress;
	
	@Column(name = "executor_firm_city")
	private String executorFirmCity;
	
	@Column(name = "executor_firm_zip")
	private String executorFirmZip;
	
	
	@Column(name = "executor_firm_state")
	private String executorFirmState;
	
	@Column(name = "executor_firm_phone")
	private String executorFirmPhone;
	
	@Column(name = "executor_firm_email")
	private String executorFirmEmail;
	
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_channel' and lu.keycode = channel and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceChannelLookUp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_mode' and lu.keycode = mode and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp complianceModeLookUp;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'deceased_status' and lu.keycode = dc_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp dcStatusLookUp;
	
	@Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('"+ConfRecordStatus.ENABLED+"', '"+ConfRecordStatus.SOLWAIT+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Long accountIds;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = substring(probate_zip,1,5))"))
	private CountryZip countryZip;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(probate_state))"))
	private CountryState countryState;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = substring(probate_firm_zip,1,5))"))
	private CountryZip probateCountryZip;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(probate_firm_state))"))
	private CountryState probateCountryState;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = substring(executor_zip,1,5))"))
	private CountryZip executorCountryZip;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(executor_state))"))
	private CountryState executorCountryState;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cz.country_zip_id from conf.country_zip cz where cz.zip = substring(executor_firm_zip,1,5))"))
	private CountryZip executorFirmCountryZip;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(executor_firm_state))"))
	private CountryState executorFirmCountryState;
	
	@Column(name = "client_decendent_id")
	private Long clientDecedentId  ;
	
	@Formula(value = "(select consumer.consumer_id from data.consumer consumer where consumer.client_id = client_id and consumer.client_account_number = client_account_number and consumer.client_consumer_number = client_decendent_id )")
	private Long decencentlookup;
	
	public LookUp getComplianceModeLookUp() {
		return complianceModeLookUp;
	}

	public void setComplianceModeLookUp(LookUp complianceModeLookUp) {
		this.complianceModeLookUp = complianceModeLookUp;
	}

	public Long getClientDecedentId() {
		return clientDecedentId;
	}

	public void setClientDecedentId(Long clientDecedentId) {
		this.clientDecedentId = clientDecedentId;
	}

	public String getExecutorFirmPhone() {
		return executorFirmPhone;
	}

	public void setExecutorFirmPhone(String executorFirmPhone) {
		this.executorFirmPhone = executorFirmPhone;
	}

	public CountryZip getProbateCountryZip() {
		return probateCountryZip;
	}

	public void setProbateCountryZip(CountryZip probateCountryZip) {
		this.probateCountryZip = probateCountryZip;
	}

	public CountryState getProbateCountryState() {
		return probateCountryState;
	}

	public void setProbateCountryState(CountryState probateCountryState) {
		this.probateCountryState = probateCountryState;
	}

	public CountryZip getExecutorCountryZip() {
		return executorCountryZip;
	}

	public void setExecutorCountryZip(CountryZip executorCountryZip) {
		this.executorCountryZip = executorCountryZip;
	}

	public CountryState getExecutorCountryState() {
		return executorCountryState;
	}

	public void setExecutorCountryState(CountryState executorCountryState) {
		this.executorCountryState = executorCountryState;
	}

	public CountryZip getExecutorFirmCountryZip() {
		return executorFirmCountryZip;
	}

	public void setExecutorFirmCountryZip(CountryZip executorFirmCountryZip) {
		this.executorFirmCountryZip = executorFirmCountryZip;
	}

	public CountryState getExecutorFirmCountryState() {
		return executorFirmCountryState;
	}

	public void setExecutorFirmCountryState(CountryState executorFirmCountryState) {
		this.executorFirmCountryState = executorFirmCountryState;
	}

	public String getProbateFirmPhone() {
		return probateFirmPhone;
	}

	public void setProbateFirmPhone(String probateFirmPhone) {
		this.probateFirmPhone = probateFirmPhone;
	}

	public CountryZip getCountryZip() {
		return countryZip;
	}

	public void setCountryZip(CountryZip countryZip) {
		this.countryZip = countryZip;
	}

	public CountryState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryState countryState) {
		this.countryState = countryState;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public LookUp getDcStatusLookUp() {
		return dcStatusLookUp;
	}

	public void setDcStatusLookUp(LookUp dcStatusLookUp) {
		this.dcStatusLookUp = dcStatusLookUp;
	}

	public LookUp getComplianceChannelLookUp() {
		return complianceChannelLookUp;
	}

	public void setComplianceChannelLookUp(LookUp complianceChannelLookUp) {
		this.complianceChannelLookUp = complianceChannelLookUp;
	}

	public Long getDecencentlookup() {
		return decencentlookup;
	}

	public void setDecencentlookup(Long decencentlookup) {
		this.decencentlookup = decencentlookup;
	}

	public Long getDeceasedId() {
		return deceasedId;
	}

	public void setDeceasedId(Long deceasedId) {
		this.deceasedId = deceasedId;
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

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getClientDeceasedId() {
		return clientDeceasedId;
	}

	public void setClientDeceasedId(Integer clientDeceasedId) {
		this.clientDeceasedId = clientDeceasedId;
	}


	public Long getDecedent() {
		return decedent;
	}

	public void setDecedent(Long decedent) {
		this.decedent = decedent;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public LocalDate getDtReport() {
		return dtReport;
	}

	public void setDtReport(LocalDate dtReport) {
		this.dtReport = dtReport;
	}

	public LocalDate getDtDeceased() {
		return dtDeceased;
	}

	public void setDtDeceased(LocalDate dtDeceased) {
		this.dtDeceased = dtDeceased;
	}

	public LocalDate getDtResolution() {
		return dtResolution;
	}

	public void setDtResolution(LocalDate dtResolution) {
		this.dtResolution = dtResolution;
	}

	public LocalDate getDtFiling() {
		return dtFiling;
	}

	public void setDtFiling(LocalDate dtFiling) {
		this.dtFiling = dtFiling;
	}

	public String getDeceasedCaseNumber() {
		return deceasedCaseNumber;
	}

	public void setDeceasedCaseNumber(String deceasedCaseNumber) {
		this.deceasedCaseNumber = deceasedCaseNumber;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Double getAmtBalanceDeceased() {
		return amtBalanceDeceased;
	}

	public void setAmtBalanceDeceased(Double amtBalanceDeceased) {
		this.amtBalanceDeceased = amtBalanceDeceased;
	}

	public Double getAmtBalanceDeceasedNotifiedpartner() {
		return amtBalanceDeceasedNotifiedpartner;
	}

	public void setAmtBalanceDeceasedNotifiedpartner(Double amtBalanceDeceasedNotifiedpartner) {
		this.amtBalanceDeceasedNotifiedpartner = amtBalanceDeceasedNotifiedpartner;
	}

	public Double getAmtDcPrincipal() {
		return amtDcPrincipal;
	}

	public void setAmtDcPrincipal(Double amtDcPrincipal) {
		this.amtDcPrincipal = amtDcPrincipal;
	}

	public Double getAmtDcInterest() {
		return amtDcInterest;
	}

	public void setAmtDcInterest(Double amtDcInterest) {
		this.amtDcInterest = amtDcInterest;
	}

	public Double getAmtDcFees() {
		return amtDcFees;
	}

	public void setAmtDcFees(Double amtDcFees) {
		this.amtDcFees = amtDcFees;
	}

	public Double getAmtDcTotal() {
		return amtDcTotal;
	}

	public void setAmtDcTotal(Double amtDcTotal) {
		this.amtDcTotal = amtDcTotal;
	}

	public String getDcStatus() {
		return dcStatus;
	}

	public void setDcStatus(String dcStatus) {
		this.dcStatus = dcStatus;
	}

	public String getProbateFirstName() {
		return probateFirstName;
	}

	public void setProbateFirstName(String probateFirstName) {
		this.probateFirstName = probateFirstName;
	}

	public String getProbateMiddleName() {
		return probateMiddleName;
	}

	public void setProbateMiddleName(String probateMiddleName) {
		this.probateMiddleName = probateMiddleName;
	}

	public String getProbateLastName() {
		return probateLastName;
	}

	public void setProbateLastName(String probateLastName) {
		this.probateLastName = probateLastName;
	}

	public String getProbateAddress() {
		return probateAddress;
	}

	public void setProbateAddress(String probateAddress) {
		this.probateAddress = probateAddress;
	}

	public String getProbateCity() {
		return probateCity;
	}

	public void setProbateCity(String probateCity) {
		this.probateCity = probateCity;
	}

	public String getProbateState() {
		return probateState;
	}

	public void setProbateState(String probateState) {
		this.probateState = probateState;
	}

	public String getProbateZip() {
		return probateZip;
	}

	public void setProbateZip(String probateZip) {
		this.probateZip = probateZip;
	}

	public String getProbatePhone() {
		return probatePhone;
	}

	public void setProbatePhone(String probatePhone) {
		this.probatePhone = probatePhone;
	}

	public String getProbateEmail() {
		return probateEmail;
	}

	public void setProbateEmail(String probateEmail) {
		this.probateEmail = probateEmail;
	}

	public String getProbate_firm_name() {
		return probate_firm_name;
	}

	public void setProbate_firm_name(String probate_firm_name) {
		this.probate_firm_name = probate_firm_name;
	}

	public String getProbateFirmAddress() {
		return probateFirmAddress;
	}

	public void setProbateFirmAddress(String probateFirmAddress) {
		this.probateFirmAddress = probateFirmAddress;
	}

	public String getProbateFirmCity() {
		return probateFirmCity;
	}

	public void setProbateFirmCity(String probateFirmCity) {
		this.probateFirmCity = probateFirmCity;
	}

	public String getProbateFirmZip() {
		return probateFirmZip;
	}

	public void setProbateFirmZip(String probateFirmZip) {
		this.probateFirmZip = probateFirmZip;
	}

	public String getProbateFirmState() {
		return probateFirmState;
	}

	public void setProbateFirmState(String probateFirmState) {
		this.probateFirmState = probateFirmState;
	}

	public String getProbateFirmEmail() {
		return probateFirmEmail;
	}

	public void setProbateFirmEmail(String probateFirmEmail) {
		this.probateFirmEmail = probateFirmEmail;
	}

	public String getExecutorFirstName() {
		return executorFirstName;
	}

	public void setExecutorFirstName(String executorFirstName) {
		this.executorFirstName = executorFirstName;
	}

	public String getExecutorMiddleName() {
		return executorMiddleName;
	}

	public void setExecutorMiddleName(String executorMiddleName) {
		this.executorMiddleName = executorMiddleName;
	}

	public String getExecutorLastName() {
		return executorLastName;
	}

	public void setExecutorLastName(String executorLastName) {
		this.executorLastName = executorLastName;
	}

	public String getExecutorAddress() {
		return executorAddress;
	}

	public void setExecutorAddress(String executorAddress) {
		this.executorAddress = executorAddress;
	}

	public String getExecutorCity() {
		return executorCity;
	}

	public void setExecutorCity(String executorCity) {
		this.executorCity = executorCity;
	}

	public String getExecutorState() {
		return executorState;
	}

	public void setExecutorState(String executorState) {
		this.executorState = executorState;
	}

	public String getExecutorZip() {
		return executorZip;
	}

	public void setExecutorZip(String executorZip) {
		this.executorZip = executorZip;
	}

	public String getExecutorPhone() {
		return executorPhone;
	}

	public void setExecutorPhone(String executorPhone) {
		this.executorPhone = executorPhone;
	}

	public String getExecutorEmail() {
		return executorEmail;
	}

	public void setExecutorEmail(String executorEmail) {
		this.executorEmail = executorEmail;
	}

	public String getExecutorFirmName() {
		return executorFirmName;
	}

	public void setExecutorFirmName(String executorFirmName) {
		this.executorFirmName = executorFirmName;
	}

	public String getExecutorFirmAddress() {
		return executorFirmAddress;
	}

	public void setExecutorFirmAddress(String executorFirmAddress) {
		this.executorFirmAddress = executorFirmAddress;
	}

	public String getExecutorFirmCity() {
		return executorFirmCity;
	}

	public void setExecutorFirmCity(String executorFirmCity) {
		this.executorFirmCity = executorFirmCity;
	}

	public String getExecutorFirmZip() {
		return executorFirmZip;
	}

	public void setExecutorFirmZip(String executorFirmZip) {
		this.executorFirmZip = executorFirmZip;
	}

	public String getExecutorFirmState() {
		return executorFirmState;
	}

	public void setExecutorFirmState(String executorFirmState) {
		this.executorFirmState = executorFirmState;
	}

	public String getExecutorFirmEmail() {
		return executorFirmEmail;
	}

	public void setExecutorFirmEmail(String executorFirmEmail) {
		this.executorFirmEmail = executorFirmEmail;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}
	
	public void addErrWarJson(ErrWarJson errWarJson) {
		if (this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}
	
	
}
