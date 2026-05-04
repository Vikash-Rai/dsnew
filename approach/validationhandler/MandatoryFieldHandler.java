package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import com.equabli.domain.helpers.CommonUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class MandatoryFieldHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   EnrichmentContext ctx) {
        // Client ID check
        if (CommonUtils.isIntegerNullOrZero(account.getClientId())) {
            if (ctx.contains("E10101")) {
                markSuspected(account, "E10101", ctx);
                return ValidationResult.failed("E10101",
                        "Client ID is mandatory");
            }
            account.addErrWarJson(new ErrWarJson("w", "E10101"));
        }

        // Client Account Number check
        if (CommonUtils.isStringNullOrBlank(
                account.getClientAccountNumber())) {
            if (ctx.contains("E10102")) {
                markSuspected(account, "E10102", ctx);
                return ValidationResult.failed("E10102",
                        "Client Account Number is mandatory");
            }
            account.addErrWarJson(new ErrWarJson("w", "E10102"));
        }

        // Product ID check
        if (CommonUtils.isIntegerNullOrZero(account.getProductId())) {
            if (ctx.contains("E10104")) {
                markSuspected(account, "E10104", ctx);
                return ValidationResult.failed("E10104",
                        "Product ID is mandatory");
            }
            account.addErrWarJson(new ErrWarJson("w", "E10104"));
        }

        // If invalid, stop the chain
        if (!ctx.isAccountValid()) {
            return ValidationResult.failed("MANDATORY_FAILED",
                    "Mandatory validation failed");
        }

        return passToNext(account, ctx);
    }
}