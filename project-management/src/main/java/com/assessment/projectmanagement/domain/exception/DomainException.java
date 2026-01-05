package com.assessment.projectmanagement.domain.exception;

/**
 * Base domain exception - All domain exceptions extend from this
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
