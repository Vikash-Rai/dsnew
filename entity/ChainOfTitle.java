package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(schema = "data", name = "chainoftitle")
public class ChainOfTitle implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static volatile Map<String, ChainOfTitle> chainOfTitleRecord = new HashMap<String, ChainOfTitle>();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "chainoftitle_id")
	private Long chainOfTitleId;

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

	@Column(name = "cot_external_system_id")
	private Integer clientCoTId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cs.cot_owner_id from data.cot_owner cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.client_id = client_id and cs.external_system_id= cot_external_system_id)")
	private CoTOwner cotOwner;

	@Column(name = "dt_from", updatable = false)
	private LocalDate dtStart;

	@Transient
	private LocalDate dtStartAfter;

	@Column(name = "dt_till", updatable = true)
	private LocalDate dtEnd;

	@Transient
	private List<ChainOfTitles> chainOfTitles;

	@Transient
	private LocalDate dtEndPrevious;

	@Transient
	private Integer dtEndPreviousCount;

	@Transient
	private Integer dtFromCount;

	@Transient
	private Integer dtTillCount;

	@Column(name = "remark_comment")
	private String comments;

	@Column(name = "cot_status_id")
	private Integer cotStatusId;

	@Column(name = "cot_owner_type")
	private String cotType;

	@Column(name = "cot_owner_id")
	private Long ownerId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'cot_owner_type' and lu.keycode = cot_owner_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp ownerTypes;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select co.cot_owner_id from conf.client_subpool cs join data.cot_owner co on case when cot_owner_type = '"+CommonConstants.COT_OWNER_TYPE_ACCOUNT_OWNER+"' then co.subpool_owner_id = cs.owner_id when cot_owner_type = '"+CommonConstants.COT_OWNER_TYPE_RECEIVABLE_OWNER+"' then co.subpool_owner_id = cs.beneficiary_id end join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.external_system_id = external_subpool_id)")
	private CoTOwner subpoolCotOwner;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select cs.cot_owner_id from data.cot_owner cs join conf.record_status rs on cs.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cs.cot_owner_id = cot_owner_id)")
	private CoTOwner cotOwners;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumns({
		@JoinColumn(name = "client_id", referencedColumnName = "client_id", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false),
        @JoinColumn(name = "client_account_number", referencedColumnName = "client_account_number", foreignKey = @jakarta.persistence.ForeignKey(name="none", value = ConstraintMode.NO_CONSTRAINT), insertable = false, updatable = false)
	})
	private Accounts accounts;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

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

	public String getCotType() {
		return cotType;
	}

	public void setCotType(String cotType) {
		this.cotType = cotType;
	}

	public CoTOwner getCotOwner() {
		return cotOwner;
	}

	public void setCotOwner(CoTOwner cotOwner) {
		this.cotOwner = cotOwner;
	}

	public LocalDate getDtStartAfter() {
		return dtStartAfter;
	}

	public void setDtStartAfter(LocalDate dtStartAfter) {
		this.dtStartAfter = dtStartAfter;
	}

	public List<ChainOfTitles> getChainOfTitles() {
		return chainOfTitles;
	}

	public void setChainOfTitles(List<ChainOfTitles> chainOfTitles) {
		this.chainOfTitles = chainOfTitles;
	}

	public LocalDate getDtEndPrevious() {
		return dtEndPrevious;
	}

	public void setDtEndPrevious(LocalDate dtEndPrevious) {
		this.dtEndPrevious = dtEndPrevious;
	}

	public Integer getDtEndPreviousCount() {
		return dtEndPreviousCount;
	}

	public void setDtEndPreviousCount(Integer dtEndPreviousCount) {
		this.dtEndPreviousCount = dtEndPreviousCount;
	}

	public Integer getDtFromCount() {
		return dtFromCount;
	}

	public void setDtFromCount(Integer dtFromCount) {
		this.dtFromCount = dtFromCount;
	}

	public Integer getDtTillCount() {
		return dtTillCount;
	}

	public void setDtTillCount(Integer dtTillCount) {
		this.dtTillCount = dtTillCount;
	}

	public Long getChainOfTitleId() {
		return chainOfTitleId;
	}

	public void setChainOfTitleId(Long chainOfTitleId) {
		this.chainOfTitleId = chainOfTitleId;
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

	public Integer getCotStatusId() {
		return cotStatusId;
	}

	public void setCotStatusId(Integer cotStatusId) {
		this.cotStatusId = cotStatusId;
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

	public Integer getClientCoTId() {
		return clientCoTId;
	}

	public void setClientCoTId(Integer clientCoTId) {
		this.clientCoTId = clientCoTId;
	}

	public LocalDate getDtStart() {
		return dtStart;
	}

	public void setDtStart(LocalDate dtStart) {
		this.dtStart = dtStart;
	}

	public LocalDate getDtEnd() {
		return dtEnd;
	}

	public void setDtEnd(LocalDate dtEnd) {
		this.dtEnd = dtEnd;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public LookUp getOwnerTypes() {
		return ownerTypes;
	}

	public void setOwnerTypes(LookUp ownerTypes) {
		this.ownerTypes = ownerTypes;
	}

	public CoTOwner getSubpoolCotOwner() {
		return subpoolCotOwner;
	}

	public void setSubpoolCotOwner(CoTOwner subpoolCotOwner) {
		this.subpoolCotOwner = subpoolCotOwner;
	}

	public CoTOwner getCotOwners() {
		return cotOwners;
	}

	public void setCotOwners(CoTOwner cotOwners) {
		this.cotOwners = cotOwners;
	}

	public Accounts getAccounts() {
		return accounts;
	}

	public void setAccounts(Accounts accounts) {
		this.accounts = accounts;
	}

	public ChainOfTitle(Long chainOfTitleId, String recordType, Long accountId, Client client, Integer clientId, String clientAccountNumber,
						Integer recordStatusId, Integer clientCoTId, CoTOwner cotOwner, String cotType, Long ownerId, LocalDate dtStart, LocalDate dtEnd,
						String comments, LookUp ownerTypes, CoTOwner subpoolCotOwner) {
		this.chainOfTitleId = chainOfTitleId;
		this.recordType = recordType;
		this.accountId = accountId;
		this.client = client;
		this.clientId = clientId;
		this.clientAccountNumber = clientAccountNumber;
		this.recordStatusId = recordStatusId;
		this.clientCoTId = clientCoTId;
		this.cotOwner = cotOwner;
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
		this.comments = comments;
		this.cotType = cotType;
		this.ownerId = ownerId;
		this.ownerTypes = ownerTypes;
		this.subpoolCotOwner = subpoolCotOwner;
	}

	public ChainOfTitle() {
	}
}