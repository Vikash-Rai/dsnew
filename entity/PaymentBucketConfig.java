package com.equabli.collectprism.entity;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentBucketConfig {

	private String paymentDistributionType;
	private String configuredValue;
	private Integer clientId;
	
	public String getPaymentDistributionType() {
		return paymentDistributionType;
	}
	public void setPaymentDistributionType(String paymentDistributionType) {
		this.paymentDistributionType = paymentDistributionType;
	}
	public String getConfiguredValue() {
		return configuredValue;
	}
	public void setConfiguredValue(String configuredValue) {
		this.configuredValue = configuredValue;
	}
	public Integer getClientId() {
		return clientId;
	}
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}
	
	public static List<PaymentBucketConfig> getClientBucketConfig(List<PaymentBucketConfig> paymentBucketConfigList) {
		List<PaymentBucketConfig> filteredList = paymentBucketConfigList.stream()
			    .filter(config -> config.getClientId() != null)
			    .collect(Collectors.toList());
		return filteredList;
	}
	
	public static List<PaymentBucketConfig> getEQBucketConfig(List<PaymentBucketConfig> paymentBucketConfigList) {
		List<PaymentBucketConfig> filteredList = paymentBucketConfigList.stream()
			    .filter(config -> config.getClientId() == null)
			    .collect(Collectors.toList());
		return filteredList;
	}
	
	public static PaymentBucketConfig getPaymentBucketConfigFromPaymentDistributionType(List<PaymentBucketConfig> paymentBucketConfigLs, String paymentDistributionType) {
		return paymentBucketConfigLs.stream()
				.filter(c -> paymentDistributionType.equalsIgnoreCase(c.getPaymentDistributionType()))
				.findFirst()
				.orElse(null);
		
	}
}	
