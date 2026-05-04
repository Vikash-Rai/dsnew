package com.equabli.collectprism.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.equabli.common.configs.DownstreamProperties;
import com.equabli.common.configs.FeignClientConfig;
import com.equabli.common.constants.Constants;
import com.equabli.collectprism.service.StrategyManagerService;
import com.equabli.feignClients.BusinessProcessAutomation;
import com.equabli.feignClients.ConfigurationManagementService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FeignConfig {

	@Autowired
	private Environment environment;

	@Autowired
	private DownstreamProperties downstreamProperties;

//	@Bean
//	public FeignClientFactoryBean awsServiceFeignClientFactoryBean() {
//		return FeignClientConfig.createFeignClientFactoryBean(environment, AwsService.class,
//				downstreamProperties.getDownStreams().get(Constants.DownStream.AWS));
//	}
	
	@Bean
	public FeignClientFactoryBean businessProcessAutomationFeignClientFactoryBean() {
		return FeignClientConfig.createFeignClientFactoryBean(environment, BusinessProcessAutomation.class,
				downstreamProperties.getDownStreams().get(Constants.DownStream.BPA));
	}
	
	@Bean
	public FeignClientFactoryBean configurationManagementServiceFeignClientFactoryBean() {
		return FeignClientConfig.createFeignClientFactoryBean(environment, ConfigurationManagementService.class,
				downstreamProperties.getDownStreams().get(Constants.DownStream.CONGIGURATION));
	}
	
	@Bean
	public FeignClientFactoryBean strategyManagerServiceFeignClientFactoryBean() {
		return FeignClientConfig.createFeignClientFactoryBean(environment, StrategyManagerService.class,
				downstreamProperties.getDownStreams().get(Constants.DownStream.STRATEGY_MANAGER));
	}

}
