package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.entity.ErrWarJson;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class PrimaryConsumerHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   EnrichmentContext ctx) {
        if (account.getConsumer() == null ||
                account.getConsumer().isEmpty()) {
            if (ctx.contains("E60102")) {
                markSuspected(account, "E60102", ctx);
                return ValidationResult.failed("E60102",
                        "No consumer found for account");
            }
            // Warning only — continue chain
            account.addErrWarJson(new ErrWarJson("w", "E60102"));
            return passToNext(account, ctx);
        }

        boolean hasPrimaryDebtor = account.getConsumer().stream()
                .anyMatch(c -> c.getContactTypeLookUp() != null &&
                        "PD".equalsIgnoreCase(
                                c.getContactTypeLookUp().getKeycode()));

        if (!hasPrimaryDebtor) {
            if (ctx.contains("E60102")) {
                markSuspected(account, "E60102", ctx);
                return ValidationResult.failed("E60102",
                        "No primary debtor found");
            }
            account.addErrWarJson(new ErrWarJson("w", "E60102"));
        } else {
            ctx.setPrimaryDebtorFound(true);
        }

        return passToNext(account, ctx);
    }
}