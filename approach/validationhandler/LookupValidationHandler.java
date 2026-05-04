package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import com.equabli.domain.helpers.CommonUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class LookupValidationHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   EnrichmentContext ctx) {
        // Client lookup
        if (!CommonUtils.isIntegerNullOrZero(account.getClientId())
                && account.getClient() == null) {
            if (ctx.contains("E20101")) {
                markSuspected(account, "E20101", ctx);
                return ValidationResult.failed("E20101",
                        "Invalid client ID");
            }
            account.addErrWarJson(new ErrWarJson("w", "E20101"));
        }

        // Product lookup
        if (!CommonUtils.isIntegerNullOrZero(account.getProductId())
                && account.getProduct() == null) {
            if (ctx.contains("E20104")) {
                markSuspected(account, "E20104", ctx);
                return ValidationResult.failed("E20104",
                        "Invalid product ID");
            }
            account.addErrWarJson(new ErrWarJson("w", "E20104"));
        }

        // Customer type lookup
        if (!CommonUtils.isStringNullOrBlank(account.getCustomerType())
                && account.getCustomerTypeLookUp() == null) {
            if (ctx.contains("E20106")) {
                markSuspected(account, "E20106", ctx);
                return ValidationResult.failed("E20106",
                        "Invalid customer type");
            }
            account.addErrWarJson(new ErrWarJson("w", "E20106"));
        }

        if (!ctx.isAccountValid()) {
            return ValidationResult.failed("LOOKUP_FAILED",
                    "Lookup validation failed");
        }

        return passToNext(account, ctx);
    }
}
