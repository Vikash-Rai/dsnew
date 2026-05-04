package com.equabli.collectprism.approach.builders;

import com.equabli.collectprism.approach.validationhandler.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import java.util.List;

// Decouples validation logic
// Easy to add/remove handlers
// Follows Open/Closed Principle
// Clean and scalable


/**
 * Each handler:
 *  checks something
 *  either fails -> stop
 *  or passes -> next handler
 */
@Component
public class ValidationChainBuilderNew {
    //primary -> mandatory -> lookup -> businessRule -> autoLoanCheck
    @Autowired
    private List<ValidationHandler> handlers;

    public ValidationHandler build() {
        if (handlers == null || handlers.isEmpty()) {
            throw new IllegalStateException("No validation handlers configured");
        }

        handlers.sort(AnnotationAwareOrderComparator.INSTANCE);
        ValidationHandler head = handlers.get(0);
        ValidationHandler current = head;
        for (int i = 1; i < handlers.size(); i++) {
            current = current.setNext(handlers.get(i));
        }
        return head;
    }
}