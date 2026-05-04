package com.equabli.collectprism.approach.validationhandler;

import com.equabli.collectprism.entity.Account;
import com.equabli.collectprism.approach.enrichmentservice.EnrichmentContext;
import com.equabli.domain.helpers.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
public class BusinessRuleHandler extends AbstractValidationHandler {

    @Override
    public ValidationResult handle(Account account,
                                   EnrichmentContext ctx) {
        // White space in account number
        if (ctx.contains("E70101") &&
                !CommonUtils.isStringNullOrBlank(
                        account.getClientAccountNumber()) &&
                StringUtils.containsWhitespace(
                        account.getClientAccountNumber())) {
            markSuspected(account, "E70101", ctx);
            return ValidationResult.failed("E70101",
                    "Account number contains whitespace");
        }

        // Amount validations
        if (ctx.contains("E70112") &&
                !CommonUtils.isDoubleNull(
                        account.getAmtPreChargeOffBalance()) &&
                account.getAmtPreChargeOffBalance() < 0) {
            markSuspected(account, "E70112", ctx);
            return ValidationResult.failed("E70112",
                    "Pre charge-off balance cannot be negative");
        }

        // Date validations
        if (ctx.contains("E70115") &&
                !CommonUtils.isDateNull(account.getDelinquencyDate()) &&
                !CommonUtils.isDateNull(account.getChargeOffDate()) &&
                account.getDelinquencyDate()
                        .isAfter(account.getChargeOffDate())) {
            markSuspected(account, "E70115", ctx);
            return ValidationResult.failed("E70115",
                    "Delinquency date after charge-off date");
        }

        if (!ctx.isAccountValid()) {
            return ValidationResult.failed("BUSINESS_RULE_FAILED",
                    "Business rule validation failed");
        }

        return passToNext(account, ctx);
    }
}