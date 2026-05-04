package com.equabli.collectprism.entity;

import java.time.LocalDate;
import java.math.BigDecimal;

import java.time.LocalDate;

public record AccountEnrichmentDTO(
        Long accountId,
        Long clientId,
        Long productId,
        String clientCode,
        LocalDate lastPaymentDate,
        LocalDate chargeOffDate,
        LocalDate solDate,
        LocalDate delinquencyDate,
        LocalDate clientSolDate,
        LocalDate equabliSolDate,
        Integer recordStatusId
) {}