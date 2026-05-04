package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;

public interface ValidationHandler {
    ValidationResult handle(Account account, EnrichmentContext ctx);
    ValidationHandler setNext(ValidationHandler next);
}