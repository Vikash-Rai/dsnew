package com.equabli.collectprism.approach.validationhandler;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationResult {
    private final boolean passed;
    private final String errorCode;
    private final String message;

    public static ValidationResult passed() {
        return ValidationResult.builder().passed(true).build();
    }

    public static ValidationResult failed(String errorCode, String msg) {
        return ValidationResult.builder()
                .passed(false)
                .errorCode(errorCode)
                .message(msg)
                .build();
    }
}
