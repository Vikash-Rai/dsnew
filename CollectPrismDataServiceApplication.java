package com.equabli.collectprism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.equabli.config.CommonComponentScanConfig;

@SpringBootApplication
@EnableCaching
@Import(CommonComponentScanConfig.class)
@EnableFeignClients(basePackages = "com.equabli.*")
public class CollectPrismDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectPrismDataServiceApplication.class, args);
	}
}