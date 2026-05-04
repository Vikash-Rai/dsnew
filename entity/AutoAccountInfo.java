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
@Table(schema = "data", name = "autoAccountInfo")
@Convert(attributeName = "json", converter = JsonType.class)
public class AutoAccountInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "auto_account_info_id")
	private Long autoAccountInfoId;
	
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
	
	@Column(name = "repossession_eligible")
	private Boolean repossessionEligible;
	
	@Column(name = "repossession_case_status")
	private String repossessionCaseStatus;
	
	@Column(name = "repossession_case_id")
	private Long repossessionCaseId;
	
	@Column(name = "auto_sale_date")
	private LocalDate autoSaleDate;
	
	@Column(name = "auto_sale_price")
	private Double autoSalePrice;
	
	@Column(name = "repossession_sale_complete")
	private Boolean repossession_sale_complete;
	
	@Column(name = "repossession_override")
	private Boolean reposessionOverride;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select prd.product_id from data.account acc join conf.product prd on prd.product_id = acc.product_id and prd.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
	private Product product;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

	@Column(name = "dtm_utc_create", updatable = false)
	private LocalDateTime dtmUtcCreate;

	@Column(name = "dtm_utc_update", updatable = false)
	private LocalDateTime dtmUtcUpdate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'repossession_case_status' and lu.keycode = repossession_case_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp repossesssionCaseStatusLookUp;
	
	public AutoAccountInfo() {
    }
	
	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

    public AutoAccountInfo(Long autoAccountInfoId, Integer clientId, String clientAccountNumber) {
        this.autoAccountInfoId = autoAccountInfoId;
        this.clientId = clientId;
        this.clientAccountNumber = clientAccountNumber;
    }

	public Long getAutoAccountInfoId() {
		return autoAccountInfoId;
	}

	public void setAutoAccountInfoId(Long autoAccountInfoId) {
		this.autoAccountInfoId = autoAccountInfoId;
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

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public Boolean getRepossessionEligible() {
		return repossessionEligible;
	}

	public void setRepossessionEligible(Boolean repossessionEligible) {
		this.repossessionEligible = repossessionEligible;
	}

	public String getRepossessionCaseStatus() {
		return repossessionCaseStatus;
	}

	public void setRepossessionCaseStatus(String repossessionCaseStatus) {
		this.repossessionCaseStatus = repossessionCaseStatus;
	}

	public Long getRepossessionCaseId() {
		return repossessionCaseId;
	}

	public void setRepossessionCaseId(Long repossessionCaseId) {
		this.repossessionCaseId = repossessionCaseId;
	}

	public LocalDate getAutoSaleDate() {
		return autoSaleDate;
	}

	public void setAutoSaleDate(LocalDate autoSaleDate) {
		this.autoSaleDate = autoSaleDate;
	}

	

	public Double getAutoSalePrice() {
		return autoSalePrice;
	}

	public void setAutoSalePrice(Double autoSalePrice) {
		this.autoSalePrice = autoSalePrice;
	}

	public Boolean getRepossession_sale_complete() {
		return repossession_sale_complete;
	}

	public void setRepossession_sale_complete(Boolean repossession_sale_complete) {
		this.repossession_sale_complete = repossession_sale_complete;
	}

	public Boolean getReposessionOverride() {
		return reposessionOverride;
	}

	public void setReposessionOverride(Boolean reposessionOverride) {
		this.reposessionOverride = reposessionOverride;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
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

	public LookUp getRepossesssionCaseStatusLookUp() {
		return repossesssionCaseStatusLookUp;
	}

	public void setRepossesssionCaseStatusLookUp(LookUp repossesssionCaseStatusLookUp) {
		this.repossesssionCaseStatusLookUp = repossesssionCaseStatusLookUp;
	}
    

}