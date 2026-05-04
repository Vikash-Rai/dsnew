package com.equabli.collectprism.writer;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.collectprism.entity.OperationRequest;
import com.equabli.collectprism.entity.OperationResponse;
import com.equabli.collectprism.repository.AccountRepository;
import com.equabli.collectprism.repository.CoTServicerRepository;
import com.equabli.collectprism.repository.OperationRequestRepository;
import com.equabli.collectprism.repository.OperationResponseRepository;
import com.equabli.domain.Queue;
import com.equabli.domain.QueueReason;
import com.equabli.domain.QueueStatus;
import com.equabli.domain.entity.ConfRecordStatus;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

public class OperationRequestWriter implements ItemWriter<OperationRequest> {

    private final Logger logger = LoggerFactory.getLogger(OperationRequestWriter.class);

    @Autowired
    private OperationRequestRepository operationRequestRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OperationResponseRepository operationResponseRepository;

    @Autowired
    private CoTServicerRepository coTServicerRepository;

    @Autowired
    HttpServletRequest request;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private JobParameters jobParameters;

	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        jobParameters = stepExecution.getJobParameters();
    }

    @Override
    public void write(Chunk<? extends OperationRequest> operationRequest) throws Exception {
		String updatedBy = jobParameters.getString("updatedBy");
		Integer appId = Integer.parseInt(jobParameters.getString("appId"));
		Integer recordSourceId = Integer.parseInt(jobParameters.getString("recordSourceId"));

        operationRequestRepository.saveAll(operationRequest);
        for (OperationRequest or : operationRequest) {
            if(or.getRecordStatusId().equals(ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId())
            		&& !CommonUtils.isBooleanNull(or.getIsAutoResponseOnOpRequest()) && or.getIsAutoResponseOnOpRequest()
                    && or.getRequestSource().equalsIgnoreCase(CommonConstants.RECORD_SOURCE_PARTNER)
                    && or.getOperationRequestType().equalsIgnoreCase(CommonConstants.OPERATION_REQUEST_TYPE_PARTNER_REQUEST_TO_RECALL)) {
                if(or.getComplianceIsOpen() > 0) {
                    OperationResponse operationResponse = new OperationResponse(or.getOperationRequestId(), or.getClientId(), or.getClientConsumerNumber(),
                            or.getConsumerId(), or.getAccountId(), or.getClientAccountNumber(), or.getRequestSource(), or.getRequestSourceId(), or.getRequestNumber(),
                            CommonConstants.OPERATION_RESPONSE_STATUS_CANCEL, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
                            "opresponse", or.getContactId(), simpleDateFormat.parse(simpleDateFormat.format(new Date())), "Please retain the account until the compliance event is closed",
                            recordSourceId, appId, updatedBy, updatedBy, LocalDateTime.now());

                    operationResponseRepository.save(operationResponse);
                } else if(!CommonUtils.isIntegerNull(or.getSifPifDays()) && !CommonUtils.isIntegerNull(or.getAutoRecallDays()) && or.getAutoRecallDays() > or.getSifPifDays()) {
                    OperationResponse operationResponse = new OperationResponse(or.getOperationRequestId(), or.getClientId(), or.getClientConsumerNumber(),
                            or.getConsumerId(), or.getAccountId(), or.getClientAccountNumber(), or.getRequestSource(), or.getRequestSourceId(), or.getRequestNumber(),
                            CommonConstants.OPERATION_RESPONSE_STATUS_CANCEL, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
                            "opresponse", or.getContactId(), simpleDateFormat.parse(simpleDateFormat.format(new Date())), "EQ Collect will initiate a recall request after "+or.getAutoRecallDays()+" days have passed from the payment closing the account out",
                            recordSourceId, appId, updatedBy, updatedBy, LocalDateTime.now());

                    operationResponseRepository.save(operationResponse);
                } else {
                    accountRepository.updateQueueAccount(Queue.confQueue.get(Queue.QUEUE_RCL).getQueueId(),
                            QueueStatus.confQueueStatus.get(QueueStatus.QUEUESTATUS_RCL).getQueueStatusId(), QueueReason.confQueueReason.get(or.getQueueReason()).getQueueReasonId(),
                            or.getClientId(), or.getClientAccountNumber());

                    coTServicerRepository.updateDtTillCotServicerOperationResponse(or.getAccountId());

                    OperationResponse operationResponse = new OperationResponse(or.getOperationRequestId(), or.getClientId(), or.getClientConsumerNumber(),
                            or.getConsumerId(), or.getAccountId(), or.getClientAccountNumber(), or.getRequestSource(), or.getRequestSourceId(), or.getRequestNumber(),
                            CommonConstants.OPERATION_RESPONSE_STATUS_SUCCESSFUL, ConfRecordStatus.confRecordStatus.get(ConfRecordStatus.ENABLED).getRecordStatusId(),
                            "opresponse", or.getContactId(), simpleDateFormat.parse(simpleDateFormat.format(new Date())), or.getDescription(),
                            recordSourceId, appId, updatedBy, updatedBy, LocalDateTime.now());

                    operationResponseRepository.save(operationResponse);
                }
            }

            logger.debug("Writer executed.....OperationRequest Id={}.....OperationRequest Status={}", or.getOperationRequestId(), or.getRecordStatusId());
        }
    }
}