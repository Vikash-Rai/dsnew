package com.equabli.collectprism.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import jakarta.persistence.*;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.JoinFormula;
import org.hibernate.type.SqlTypes;

import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.hypersistence.utils.hibernate.type.json.JsonType;

@Entity
@Table(schema = "data", name = "bankruptcy")
@Convert(attributeName = "json", converter = JsonType.class)
public class Bankruptcy implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bankruptcy_id")
    private Long bankruptcyId;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "client_account_number")
    private String clientAccountNumber;

    @Transient
    private String originalAccountNumber;

    @Column(name = "client_id")
    private Integer clientId;

    @Column(name = "account_id")
    private Long accountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumnOrFormula(formula = @JoinFormula(value = "(select cl.client_id from conf.client cl join conf.record_status rs on cl.record_status_id = rs.record_status_id and rs.short_name = '" + ConfRecordStatus.ENABLED + "' where cl.client_id = client_id)"))
    private Client client;

    @Transient
	private String clientName;

    @Transient
	private Integer partnerId;

    @Transient
	private String partnerName;

    @Formula(value = "(select acc.account_id from data.account acc join conf.record_status rs on acc.record_status_id = rs.record_status_id and rs.short_name in ('" + ConfRecordStatus.ENABLED + "', '" + ConfRecordStatus.SOLWAIT + "') where acc.client_id = client_id and acc.client_account_number = client_account_number)")
    private Long accountIds;

    @Column(name = "client_bankruptcy_id")
    private Long clientBankruptcyId;

    @Column(name = "client_attorney_id")
    private Long clientAttorneyId;

    @Column(name = "equabli_attorney_id")
    private Long equabliAttorneyId;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.lookup lu on lu.group_sequence = cons.contact_type join conf.lookup_group lg on lg.lookup_group_id = lu.lookup_group_id where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_attorney_id and lg.keyvalue = 'contact_type' and lu.keycode = 'BA' and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and cons.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"'))")
	private Long consumerAttorneyIds;

    @Column(name = "client_trustee_id")
    private Long clientTrusteeId;

    @Column(name = "equabli_trustee_id")
    private Long equabliTrusteeId;

	@Formula(value = "(select cons.consumer_id from data.consumer cons join conf.lookup lu on lu.group_sequence = cons.contact_type join conf.lookup_group lg on lg.lookup_group_id = lu.lookup_group_id where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_trustee_id and lg.keyvalue = 'contact_type' and lu.keycode = 'TT' and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') and cons.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"'))")
	private Long consumerTrusteeIds;

    @Column(name = "bankruptcy_chapter")
    private String bankruptcyChapter;

    @Transient
	private String bankruptcyChapterVal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'bankruptcy_chapter' and lu.keycode = bankruptcy_chapter and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp bankruptcyChapterLookUp;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_bankruptcy_filling")
    private LocalDate dtBankruptcyFilling;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_bankruptcy_report")
    private LocalDate dtBankruptcyReport;

    @Column(name = "bankruptcy_type")
    private String bankruptcyType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'bankruptcy_type' and lu.keycode = bankruptcy_type and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp bankruptcyTypeLookUp;

    @Transient
	private String bankruptcyTypeVal;

    @Column(name = "channel")
    private String channel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_channel' and lu.keycode = channel and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp channelLookUp;

    @Transient
	private String channelVal;

    @Column(name = "mode_of_receipt")
    private String modeOfReceipt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_mode' and lu.keycode = mode_of_receipt and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp modeOfReceiptLookUp;

    @Transient
	private String modeOfReceiptVal;

    @Column(name = "bankruptcy_case_number")
    private String bankruptcyCaseNumber;

    @Column(name = "amt_total_bankruptcy")
    private Double amtTotalBankruptcy;

    @Column(name = "amt_principal_bankruptcy")
    private Double amtPrincipalBankruptcy;

    @Column(name = "amt_interest_bankruptcy")
    private Double amtInterestBankruptcy;

    @Column(name = "amt_fee_amount_bankruptcy")
    private Double amtFeeAmountBankruptcy;

    @Column(name = "bankruptcy_court_city")
    private String bankruptcyCourtCity;

    @Column(name = "bankruptcy_court_state")
    private String bankruptcyCourtState;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumnOrFormula(formula = @JoinFormula(value = "(select cs.country_state_id from conf.country_state cs where lower(cs.state_code) = lower(bankruptcy_court_state))"))
	private CountryState countryState;

    @Transient
	private String bankruptcyCourtStateVal;

    @Column(name = "amt_balance_bankruptcy_filling")
    private Double amtBalanceBankruptcyFilling;

    @Column(name = "amt_balance_bankruptcy_notified")
    private Double amtBalanceBankruptcyNotified;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_claim_proof_due")
    private LocalDate dtClaimProofDue;

	@JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_claim_proof_filed")
    private LocalDate dtClaimProofFiled;

    @Column(name = "bankruptcy_petition_status")
    private String bankruptcyPetitionStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'bankruptcy_petition_status' and lu.keycode = bankruptcy_petition_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp bankruptcyPetitionStatusLookUp;

    @Transient
	private String bankruptcyPetitionStatusVal;

    @Column(name = "bankruptcy_process_status")
    private String bankruptcyProcessStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'bankruptcy_process_status' and lu.keycode = bankruptcy_process_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp bankruptcyProcessStatusLookUp;

    @Transient
	private String bankruptcyProcessStatusVal;

    @Column(name = "objection_status")
    private String objectionStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'objection_status' and lu.keycode = objection_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp objectionStatusLookUp;

    @Transient
	private String objectionStatusVal;

    @Column(name = "automatic_stay_status")
    private String automaticStayStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'automatic_stay_status' and lu.keycode = automatic_stay_status and lu.record_status_id = conf.df_record_status('" + ConfRecordStatus.ENABLED + "') )")
    private LookUp automaticStayStatusLookUp;

    @Transient
	private String automaticStayStatusVal;

    @Column(name = "record_status_id")
    private Integer recordStatusId;

    @Column(name = "bankruptcy_source")
    private String bankruptcySource;

    @Column(name = "bankruptcy_source_id")
    private Integer bankruptcySourceId;

	@Column(name = "bankruptcy_status")
	private String bankruptcyStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinFormula(value = "(select lu.lookup_id from conf.lookup lu inner join conf.lookup_group lug on lu.lookup_group_id = lug.lookup_group_id where lug.keyvalue = 'compliance_status' and lu.keycode = bankruptcy_status and lu.record_status_id = conf.df_record_status('"+ConfRecordStatus.ENABLED+"') )")
	private LookUp bankruptcyStatusLookUp;

	@JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "err_code_json", columnDefinition = "jsonb")
    private Set<ErrWarJson> errCodeJson = new HashSet<ErrWarJson>();

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "record_source_id")
    private Integer recordSourceId;

    @Column(name = "dtm_utc_update", updatable = false)
    private LocalDateTime dtmUtcUpdate;

    @Column(name = "consumer_id")
    private Long consumerId;

    @Column(name = "client_consumer_number")
    private Long clientConsumerNumber;

    @JsonFormat(pattern = CommonConstants.DT_FORMAT_MM_DD_YYYY)
    @Column(name = "dt_resolution")
    private LocalDate resolutionDate;

    @Formula(value = "(select cons.consumer_id from data.consumer cons join conf.record_status rs on cons.record_status_id = rs.record_status_id and rs.short_name = '"+ConfRecordStatus.ENABLED+"' where cons.client_id = client_id and cons.client_account_number = client_account_number and cons.client_consumer_number = client_consumer_number)")
    private Long consumerIds;

    @Transient
    private String consumerName;

    @Transient
	private List<Consumer> attorneyConsumer;

    @Transient
	private List<Consumer> trusteeConsumer;


    public Bankruptcy() {
    }

    public Bankruptcy(Long bankruptcyId, Long clientBankruptcyId, String clientAccountNumber, Integer clientId, String clientName, Integer partnerId, String partnerName, 
    		Long accountId, String bankruptcyChapter, String bankruptcyChapterVal, LocalDate dtBankruptcyFilling, LocalDate dtBankruptcyReport, 
    		String bankruptcyType, String bankruptcyTypeVal, String channel, String channelVal, String modeOfReceipt, String modeOfReceiptVal, 
    		String bankruptcyCaseNumber, Double amtTotalBankruptcy, Double amtPrincipalBankruptcy, Double amtInterestBankruptcy, 
    		Double amtFeeAmountBankruptcy, String bankruptcyCourtCity, String bankruptcyCourtState, String bankruptcyCourtStateVal, 
    		Double amtBalanceBankruptcyFilling, Double amtBalanceBankruptcyNotified, LocalDate dtClaimProofDue, LocalDate dtClaimProofFiled, 
    		String bankruptcyPetitionStatus, String bankruptcyPetitionStatusVal, String bankruptcyProcessStatus, String bankruptcyProcessStatusVal, 
    		String objectionStatus, String objectionStatusVal, String automaticStayStatus, String automaticStayStatusVal, 
    		Long clientAttorneyId, Long equabliAttorneyId, Long clientTrusteeId, Long equabliTrusteeId) {
        this.bankruptcyId = bankruptcyId;
        this.clientBankruptcyId = clientBankruptcyId;
        this.clientAccountNumber = clientAccountNumber;
        this.clientId = clientId;
		this.clientName = clientName;
        this.partnerId = partnerId;
		this.partnerName = partnerName;
        this.accountId = accountId;
        this.bankruptcyChapter = bankruptcyChapter;
        this.bankruptcyChapterVal = bankruptcyChapterVal;
        this.dtBankruptcyFilling = dtBankruptcyFilling;
        this.dtBankruptcyReport = dtBankruptcyReport;
        this.bankruptcyType = bankruptcyType;
        this.bankruptcyTypeVal = bankruptcyTypeVal;
        this.channel = channel;
        this.channelVal = channelVal;
        this.modeOfReceipt = modeOfReceipt;
		this.modeOfReceiptVal = modeOfReceiptVal;
        this.bankruptcyCaseNumber = bankruptcyCaseNumber;
        this.amtTotalBankruptcy = amtTotalBankruptcy;
        this.amtPrincipalBankruptcy = amtPrincipalBankruptcy;
        this.amtInterestBankruptcy = amtInterestBankruptcy;
        this.amtFeeAmountBankruptcy = amtFeeAmountBankruptcy;
        this.bankruptcyCourtCity = bankruptcyCourtCity;
        this.bankruptcyCourtState = bankruptcyCourtState;
        this.bankruptcyCourtStateVal = bankruptcyCourtStateVal;
        this.amtBalanceBankruptcyFilling = amtBalanceBankruptcyFilling;
        this.amtBalanceBankruptcyNotified = amtBalanceBankruptcyNotified;
        this.dtClaimProofDue = dtClaimProofDue;
        this.dtClaimProofFiled = dtClaimProofFiled;
        this.bankruptcyPetitionStatus = bankruptcyPetitionStatus;
        this.bankruptcyPetitionStatusVal = bankruptcyPetitionStatusVal;
        this.bankruptcyProcessStatus = bankruptcyProcessStatus;
        this.bankruptcyProcessStatusVal = bankruptcyProcessStatusVal;
        this.objectionStatus = objectionStatus;
        this.objectionStatusVal = objectionStatusVal;
        this.automaticStayStatus = automaticStayStatus;
        this.automaticStayStatusVal = automaticStayStatusVal;
        this.clientAttorneyId = clientAttorneyId;
        this.equabliAttorneyId = equabliAttorneyId;
        this.clientTrusteeId = clientTrusteeId;
        this.equabliTrusteeId = equabliTrusteeId;
    }

    public Bankruptcy(Long bankruptcyId) {
        this.bankruptcyId = bankruptcyId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Integer getRecordSourceId() {
        return recordSourceId;
    }

    public void setRecordSourceId(Integer recordSourceId) {
        this.recordSourceId = recordSourceId;
    }

    public LocalDateTime getDtmUtcUpdate() {
        return dtmUtcUpdate;
    }

    public void setDtmUtcUpdate(LocalDateTime dtmUtcUpdate) {
        this.dtmUtcUpdate = dtmUtcUpdate;
    }

    public Long getEquabliTrusteeId() {
        return equabliTrusteeId;
    }

    public void setEquabliTrusteeId(Long equabliTrusteeId) {
        this.equabliTrusteeId = equabliTrusteeId;
    }

    public Long getBankruptcyId() {
        return bankruptcyId;
    }

    public void setBankruptcyId(Long bankruptcyId) {
        this.bankruptcyId = bankruptcyId;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getClientAccountNumber() {
        return clientAccountNumber;
    }

    public void setClientAccountNumber(String clientAccountNumber) {
        this.clientAccountNumber = clientAccountNumber;
    }

    public String getOriginalAccountNumber() {
		return originalAccountNumber;
	}

	public void setOriginalAccountNumber(String originalAccountNumber) {
		this.originalAccountNumber = originalAccountNumber;
	}

	public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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

    public Long getClientBankruptcyId() {
        return clientBankruptcyId;
    }

    public void setClientBankruptcyId(Long clientBankruptcyId) {
        this.clientBankruptcyId = clientBankruptcyId;
    }

    public String getBankruptcyChapter() {
        return bankruptcyChapter;
    }

    public void setBankruptcyChapter(String bankruptcyChapter) {
        this.bankruptcyChapter = bankruptcyChapter;
    }

	public String getBankruptcyChapterVal() {
		return bankruptcyChapterVal;
	}

	public void setBankruptcyChapterVal(String bankruptcyChapterVal) {
		this.bankruptcyChapterVal = bankruptcyChapterVal;
	}

	public LocalDate getDtBankruptcyFilling() {
        return dtBankruptcyFilling;
    }

    public void setDtBankruptcyFilling(LocalDate dtBankruptcyFilling) {
        this.dtBankruptcyFilling = dtBankruptcyFilling;
    }

    public LocalDate getDtBankruptcyReport() {
        return dtBankruptcyReport;
    }

    public void setDtBankruptcyReport(LocalDate dtBankruptcyReport) {
        this.dtBankruptcyReport = dtBankruptcyReport;
    }

    public String getBankruptcyType() {
        return bankruptcyType;
    }

    public void setBankruptcyType(String bankruptcyType) {
        this.bankruptcyType = bankruptcyType;
    }

    public LookUp getBankruptcyTypeLookUp() {
        return bankruptcyTypeLookUp;
    }

    public void setBankruptcyTypeLookUp(LookUp bankruptcyTypeLookUp) {
        this.bankruptcyTypeLookUp = bankruptcyTypeLookUp;
    }

    public String getBankruptcyTypeVal() {
		return bankruptcyTypeVal;
	}

	public void setBankruptcyTypeVal(String bankruptcyTypeVal) {
		this.bankruptcyTypeVal = bankruptcyTypeVal;
	}

	public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelVal() {
		return channelVal;
	}

	public void setChannelVal(String channelVal) {
		this.channelVal = channelVal;
	}

	public String getModeOfReceipt() {
        return modeOfReceipt;
    }

    public void setModeOfReceipt(String modeOfReceipt) {
        this.modeOfReceipt = modeOfReceipt;
    }

    public String getBankruptcyCaseNumber() {
        return bankruptcyCaseNumber;
    }

    public void setBankruptcyCaseNumber(String bankruptcyCaseNumber) {
        this.bankruptcyCaseNumber = bankruptcyCaseNumber;
    }

    public Double getAmtPrincipalBankruptcy() {
        return amtPrincipalBankruptcy;
    }

    public void setAmtPrincipalBankruptcy(Double amtPrincipalBankruptcy) {
        this.amtPrincipalBankruptcy = amtPrincipalBankruptcy;
    }

    public Double getAmtInterestBankruptcy() {
        return amtInterestBankruptcy;
    }

    public void setAmtInterestBankruptcy(Double amtInterestBankruptcy) {
        this.amtInterestBankruptcy = amtInterestBankruptcy;
    }

    public Double getAmtFeeAmountBankruptcy() {
        return amtFeeAmountBankruptcy;
    }

    public void setAmtFeeAmountBankruptcy(Double amtFeeAmountBankruptcy) {
        this.amtFeeAmountBankruptcy = amtFeeAmountBankruptcy;
    }

    public Double getAmtTotalBankruptcy() {
        return amtTotalBankruptcy;
    }

    public void setAmtTotalBankruptcy(Double amtTotalBankruptcy) {
        this.amtTotalBankruptcy = amtTotalBankruptcy;
    }

    public String getBankruptcyCourtCity() {
        return bankruptcyCourtCity;
    }

    public void setBankruptcyCourtCity(String bankruptcyCourtCity) {
        this.bankruptcyCourtCity = bankruptcyCourtCity;
    }

    public String getBankruptcyCourtState() {
        return bankruptcyCourtState;
    }

    public void setBankruptcyCourtState(String bankruptcyCourtState) {
        this.bankruptcyCourtState = bankruptcyCourtState;
    }

    public CountryState getCountryState() {
		return countryState;
	}

	public void setCountryState(CountryState countryState) {
		this.countryState = countryState;
	}

	public String getBankruptcyCourtStateVal() {
		return bankruptcyCourtStateVal;
	}

	public void setBankruptcyCourtStateVal(String bankruptcyCourtStateVal) {
		this.bankruptcyCourtStateVal = bankruptcyCourtStateVal;
	}

	public Double getAmtBalanceBankruptcyFilling() {
        return amtBalanceBankruptcyFilling;
    }

    public void setAmtBalanceBankruptcyFilling(Double amtBalanceBankruptcyFilling) {
        this.amtBalanceBankruptcyFilling = amtBalanceBankruptcyFilling;
    }

    public Double getAmtBalanceBankruptcyNotified() {
        return amtBalanceBankruptcyNotified;
    }

    public void setAmtBalanceBankruptcyNotified(Double amtBalanceBankruptcyNotified) {
        this.amtBalanceBankruptcyNotified = amtBalanceBankruptcyNotified;
    }

    public LocalDate getDtClaimProofDue() {
        return dtClaimProofDue;
    }

    public void setDtClaimProofDue(LocalDate dtClaimProofDue) {
        this.dtClaimProofDue = dtClaimProofDue;
    }

    public LocalDate getDtClaimProofFiled() {
        return dtClaimProofFiled;
    }

    public void setDtClaimProofFiled(LocalDate dtClaimProofFiled) {
        this.dtClaimProofFiled = dtClaimProofFiled;
    }

    public String getBankruptcyPetitionStatus() {
        return bankruptcyPetitionStatus;
    }

    public void setBankruptcyPetitionStatus(String bankruptcyPetitionStatus) {
        this.bankruptcyPetitionStatus = bankruptcyPetitionStatus;
    }

    public String getBankruptcyProcessStatus() {
        return bankruptcyProcessStatus;
    }

    public void setBankruptcyProcessStatus(String bankruptcyProcessStatus) {
        this.bankruptcyProcessStatus = bankruptcyProcessStatus;
    }

    public String getObjectionStatus() {
        return objectionStatus;
    }

    public void setObjectionStatus(String objectionStatus) {
        this.objectionStatus = objectionStatus;
    }

    public String getAutomaticStayStatus() {
        return automaticStayStatus;
    }

    public void setAutomaticStayStatus(String automaticStayStatus) {
        this.automaticStayStatus = automaticStayStatus;
    }

    public Long getClientAttorneyId() {
        return clientAttorneyId;
    }

    public void setClientAttorneyId(Long clientAttorneyId) {
        this.clientAttorneyId = clientAttorneyId;
    }

    public Long getEquabliAttorneyId() {
        return equabliAttorneyId;
    }

    public void setEquabliAttorneyId(Long equabliAttorneyId) {
        this.equabliAttorneyId = equabliAttorneyId;
    }

    public Long getClientTrusteeId() {
        return clientTrusteeId;
    }

    public void setClientTrusteeId(Long clientTrusteeId) {
        this.clientTrusteeId = clientTrusteeId;
    }

    public Integer getRecordStatusId() {
        return recordStatusId;
    }

    public void setRecordStatusId(Integer recordStatusId) {
        this.recordStatusId = recordStatusId;
    }

    public String getBankruptcySource() {
		return bankruptcySource;
	}

	public void setBankruptcySource(String bankruptcySource) {
		this.bankruptcySource = bankruptcySource;
	}

	public Integer getBankruptcySourceId() {
		return bankruptcySourceId;
	}

	public void setBankruptcySourceId(Integer bankruptcySourceId) {
		this.bankruptcySourceId = bankruptcySourceId;
	}

	public String getBankruptcyStatus() {
		return bankruptcyStatus;
	}

	public void setBankruptcyStatus(String bankruptcyStatus) {
		this.bankruptcyStatus = bankruptcyStatus;
	}

	public LookUp getBankruptcyStatusLookUp() {
		return bankruptcyStatusLookUp;
	}

	public void setBankruptcyStatusLookUp(LookUp bankruptcyStatusLookUp) {
		this.bankruptcyStatusLookUp = bankruptcyStatusLookUp;
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

    public Long getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(Long accountIds) {
        this.accountIds = accountIds;
    }

    public LookUp getBankruptcyChapterLookUp() {
        return bankruptcyChapterLookUp;
    }

    public void setBankruptcyChapterLookUp(LookUp bankruptcyChapterLookUp) {
        this.bankruptcyChapterLookUp = bankruptcyChapterLookUp;
    }

    public LookUp getChannelLookUp() {
        return channelLookUp;
    }

    public void setChannelLookUp(LookUp channelLookUp) {
        this.channelLookUp = channelLookUp;
    }

    public LookUp getModeOfReceiptLookUp() {
        return modeOfReceiptLookUp;
    }

    public void setModeOfReceiptLookUp(LookUp modeOfReceiptLookUp) {
        this.modeOfReceiptLookUp = modeOfReceiptLookUp;
    }

    public String getModeOfReceiptVal() {
		return modeOfReceiptVal;
	}

	public void setModeOfReceiptVal(String modeOfReceiptVal) {
		this.modeOfReceiptVal = modeOfReceiptVal;
	}

	public LookUp getBankruptcyPetitionStatusLookUp() {
        return bankruptcyPetitionStatusLookUp;
    }

    public void setBankruptcyPetitionStatusLookUp(LookUp bankruptcyPetitionStatusLookUp) {
        this.bankruptcyPetitionStatusLookUp = bankruptcyPetitionStatusLookUp;
    }

    public String getBankruptcyPetitionStatusVal() {
		return bankruptcyPetitionStatusVal;
	}

	public void setBankruptcyPetitionStatusVal(String bankruptcyPetitionStatusVal) {
		this.bankruptcyPetitionStatusVal = bankruptcyPetitionStatusVal;
	}

	public LookUp getBankruptcyProcessStatusLookUp() {
        return bankruptcyProcessStatusLookUp;
    }

    public void setBankruptcyProcessStatusLookUp(LookUp bankruptcyProcessStatusLookUp) {
        this.bankruptcyProcessStatusLookUp = bankruptcyProcessStatusLookUp;
    }

    public String getBankruptcyProcessStatusVal() {
		return bankruptcyProcessStatusVal;
	}

	public void setBankruptcyProcessStatusVal(String bankruptcyProcessStatusVal) {
		this.bankruptcyProcessStatusVal = bankruptcyProcessStatusVal;
	}

	public LookUp getObjectionStatusLookUp() {
        return objectionStatusLookUp;
    }

    public void setObjectionStatusLookUp(LookUp objectionStatusLookUp) {
        this.objectionStatusLookUp = objectionStatusLookUp;
    }

    public String getObjectionStatusVal() {
		return objectionStatusVal;
	}

	public void setObjectionStatusVal(String objectionStatusVal) {
		this.objectionStatusVal = objectionStatusVal;
	}

	public LookUp getAutomaticStayStatusLookUp() {
        return automaticStayStatusLookUp;
    }

    public void setAutomaticStayStatusLookUp(LookUp automaticStayStatusLookUp) {
        this.automaticStayStatusLookUp = automaticStayStatusLookUp;
    }

	public String getAutomaticStayStatusVal() {
		return automaticStayStatusVal;
	}

	public void setAutomaticStayStatusVal(String automaticStayStatusVal) {
		this.automaticStayStatusVal = automaticStayStatusVal;
	}

	public Long getConsumerAttorneyIds() {
		return consumerAttorneyIds;
	}

	public void setConsumerAttorneyIds(Long consumerAttorneyIds) {
		this.consumerAttorneyIds = consumerAttorneyIds;
	}

	public Long getConsumerTrusteeIds() {
		return consumerTrusteeIds;
	}

	public void setConsumerTrusteeIds(Long consumerTrusteeIds) {
		this.consumerTrusteeIds = consumerTrusteeIds;
	}

	public List<Consumer> getAttorneyConsumer() {
		return attorneyConsumer;
	}

	public void setAttorneyConsumer(List<Consumer> attorneyConsumer) {
		this.attorneyConsumer = attorneyConsumer;
	}

	public List<Consumer> getTrusteeConsumer() {
		return trusteeConsumer;
	}

	public void setTrusteeConsumer(List<Consumer> trusteeConsumer) {
		this.trusteeConsumer = trusteeConsumer;
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

    public LocalDate getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(LocalDate resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public Long getConsumerIds() {
        return consumerIds;
    }

    public void setConsumerIds(Long consumerIds) {
        this.consumerIds = consumerIds;
    }

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}
}