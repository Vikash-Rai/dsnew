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
@Table(schema = "data", name = "legal_lien")
@Convert(attributeName = "json", converter = JsonType.class)
public class Lien implements Serializable {
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "legal_lien_id")
    private Long lienId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "client_account_number")
    private String clientAccountNumber;

    @Column(name = "client_id")
    private Integer clientId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumnOrFormula(formula = @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)"))
    private Client client;

    @Column(name = "client_consumer_number")
    private Long clientConsumerNumber;

    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "legal_placement_id")
    private Long legalPlacementId;

    @Column(name = "casenumber")
    private String caseNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "dt_lien_placed")
    private LocalDate dtLienPlaced;

    @Column(name = "lien_type")
    private String lienType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'lien_type' and lu.keycode = lien_type and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
    private LookUp lienTypeLookUp;

    @Column(name = "lien_asset")
    private String lienAsset;

    @Column(name = "amt_lien")
    private Double amtLien;

    @Column(name = "lien_status")
    private String lienStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'lien_status' and lu.keycode = lien_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
    private LookUp lienStatusLookUp;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "err_code_json", columnDefinition = "jsonb")
	private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

    @Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('" + ConfRecordStatus.ENABLED + "', '" + ConfRecordStatus.SOLWAIT + "') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
    private Long accountIds;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
	private Long consumerIds;

	@Formula(value = "(select lp.legal_placement_id from data.legal_placement lp join conf.record_status rs on lp.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where lp.client_id = client_id and lp.client_account_number = client_account_number and lp.casenumber = casenumber and (lp.suit_status is null or lp.suit_status = true) and (lp.judgment_status is null or (lp.judgment_status != 'DD' and lp.judgment_status != 'DP')))")
	private Long legalPlacementIds;


    public Lien() {
    }

	public Long getLienId() {
		return lienId;
	}

	public void setLienId(Long lienId) {
		this.lienId = lienId;
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

	public String getClientAccountNumber() {
		return clientAccountNumber;
	}

	public void setClientAccountNumber(String clientAccountNumber) {
		this.clientAccountNumber = clientAccountNumber;
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

	public Long getLegalPlacementId() {
		return legalPlacementId;
	}

	public void setLegalPlacementId(Long legalPlacementId) {
		this.legalPlacementId = legalPlacementId;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public LocalDate getDtLienPlaced() {
		return dtLienPlaced;
	}

	public void setDtLienPlaced(LocalDate dtLienPlaced) {
		this.dtLienPlaced = dtLienPlaced;
	}

	public String getLienType() {
		return lienType;
	}

	public void setLienType(String lienType) {
		this.lienType = lienType;
	}

	public LookUp getLienTypeLookUp() {
		return lienTypeLookUp;
	}

	public void setLienTypeLookUp(LookUp lienTypeLookUp) {
		this.lienTypeLookUp = lienTypeLookUp;
	}

	public String getLienAsset() {
		return lienAsset;
	}

	public void setLienAsset(String lienAsset) {
		this.lienAsset = lienAsset;
	}

	public Double getAmtLien() {
		return amtLien;
	}

	public void setAmtLien(Double amtLien) {
		this.amtLien = amtLien;
	}

	public String getLienStatus() {
		return lienStatus;
	}

	public void setLienStatus(String lienStatus) {
		this.lienStatus = lienStatus;
	}

	public LookUp getLienStatusLookUp() {
		return lienStatusLookUp;
	}

	public void setLienStatusLookUp(LookUp lienStatusLookUp) {
		this.lienStatusLookUp = lienStatusLookUp;
	}

	public Integer getRecordStatusId() {
		return recordStatusId;
	}

	public void setRecordStatusId(Integer recordStatusId) {
		this.recordStatusId = recordStatusId;
	}

	public Set<ErrWarJson> getErrCodeJson() {
		return errCodeJson;
	}

	public  void addErrWarJson( ErrWarJson errWarJson) {
		if(this.errCodeJson == null)
			this.errCodeJson = new HashSet<ErrWarJson>();
		this.errCodeJson.add(errWarJson);
	}

	public void setErrCodeJson(Set<ErrWarJson> errCodeJson) {
		this.errCodeJson = errCodeJson;
	}

	public Long getAccountIds() {
		return accountIds;
	}

	public void setAccountIds(Long accountIds) {
		this.accountIds = accountIds;
	}

	public Long getConsumerIds() {
		return consumerIds;
	}

	public void setConsumerIds(Long consumerIds) {
		this.consumerIds = consumerIds;
	}

	public Long getLegalPlacementIds() {
		return legalPlacementIds;
	}

	public void setLegalPlacementIds(Long legalPlacementIds) {
		this.legalPlacementIds = legalPlacementIds;
	}
}
