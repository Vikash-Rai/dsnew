package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(5)
public class AutoLoanCheckHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   EnrichmentContext ctx) {
        if (ctx.contains("E70138") &&
                account.getProduct() != null &&
                "AL".equalsIgnoreCase(account.getProduct().getShortName()) &&
                account.getAutoAccountInfoIds() == null) {
            markSuspected(account, "E70138", ctx);
            return ValidationResult.failed("E70138",
                    "Auto loan requires AutoAccountInfo");
        }

        return passToNext(account, ctx);
    }
}