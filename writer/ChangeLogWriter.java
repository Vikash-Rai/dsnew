package com.equabli.collectprism.writer;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.equabli.client.CommonRestClient;
import com.equabli.collectprism.entity.ChangeLog;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.entity.ErrWarMessage;
import com.equabli.collectprism.repository.ChangeLogRepository;
import com.equabli.collectprism.repository.ErrWarMessageRepository;
import com.equabli.domain.Response;
import com.equabli.domain.helpers.CommonConstants;
import com.equabli.domain.helpers.CommonUtils;


public class ChangeLogWriter  implements ItemWriter<ChangeLog>{

    private final Logger logger = LoggerFactory.getLogger(ChangeLogWriter.class);

    @Autowired
    private ChangeLogRepository changeLogRepository;
    
    @Autowired
	private CommonRestClient client;
    
    @Autowired
    ErrWarMessageRepository errWarMessageRepository;
    
    private String authHeader;
    
    
    public String getAuthHeader() {
		return authHeader;
	}

	public void setAuthHeader(String authHeader) {
		this.authHeader = authHeader;
	}

	@Override
    public void write(Chunk<? extends ChangeLog> changeLog) throws Exception {
    	changeLogRepository.saveAll(changeLog);
    	
    	HashMap<String, List<String>> map = new HashMap<>();
        for (ChangeLog changelog : changeLog) {
            logger.debug("Writer executed.....ChangeLog Id={}.....ChangeLog Status={}", changelog.getChangeLogId(), changelog.getRecordStatusId());
            List<String> newList = new ArrayList<>();
            Map<String, String> errorDescription=  getErrorCodes();
            if(!CommonUtils.isObjectNull(changelog.getErrCodeJson())) {
           for(ErrWarJson set :changelog.getErrCodeJson()) {
        	   newList.add(set.getValue()+":"+errorDescription.get(set.getValue()));
           }
            map.put(changelog.getClientAccountNumber(), newList);
            }
        }
        
        Response<Boolean> mailResponse = client.sendMail("Data Scrubbing - ChangeLog", "Records For  ChangeLog . Processed " + changeLog.size() + " Records." + "<br> Errorcode : " + map  + CommonConstants.EQUABLI_AUTO_GENERATED_MAILS_GENERAL_SIGNATURE,authHeader);
		if(mailResponse.getValidation()) {
			logger.info("Email Sent");
		}
    }
    
    private Map<String,String> getErrorCodes(){
    	List<ErrWarMessage> listOfErrWarMessages=errWarMessageRepository.getChangeLogErrorCodeDescription();
    	
    	Map<String, String> errorDescription = listOfErrWarMessages.stream()
                .collect(Collectors.toMap(ErrWarMessage::getShortName, ErrWarMessage::getDescription));

		return errorDescription;
    }
    
    

}
