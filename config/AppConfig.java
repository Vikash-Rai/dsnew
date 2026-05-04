package com.equabli.collectprism.config;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.equabli.config.AwsConfig;
import com.equabli.config.SecretConfig;
import com.equabli.config.ServicesHostUrls;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfig {

	@Autowired
	AwsConfig awsConfig;

	@Bean
	DataSource dataSource() {
		Map<String, String> secrets = awsConfig.getSecret();

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(secrets.get("dbUrl"));
		driverManagerDataSource.setUsername(AwsConfig.decrypt(secrets.get("dbUser")));
		driverManagerDataSource.setPassword(AwsConfig.decrypt(secrets.get("dbPassword")));
		driverManagerDataSource.setDriverClassName(secrets.get("dbDriver"));
		new WebConfig(driverManagerDataSource);
		return driverManagerDataSource;
	}

	@Bean
	ServicesHostUrls serviceHostUrls() {
		Map<String, String> secrets = awsConfig.getSecret();
		ServicesHostUrls serviceHostUrls = new ServicesHostUrls(secrets);
		return serviceHostUrls;
	}
	
	@Bean
	SecretConfig secretConfig() {
		Map<String, String> secrets = awsConfig.getSecret();
		SecretConfig secretConfig = new SecretConfig(secrets);
		return secretConfig;
	}
	
}