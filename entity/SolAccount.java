package com.equabli.collectprism.entity;

import lombok.Data;

import java.time.LocalDate;


@Data
public class SolAccount {
     private Long accountId;
     private Integer clientId;
     private Integer partnerId;
     private Integer productId;
     private LocalDate lastPaymentDate;
     private LocalDate chargeOffDate;
     private LocalDate solDate;
     private LocalDate delinquencyDate;
     private LocalDate clientSolDate;
     private LocalDate equabliSolDate;
     private String stateCode;
     private Integer countryStateId;
     private Integer solMonth;
 	 private Integer solDay;
}