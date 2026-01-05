package com.assessment.projectmanagement.domain.exception;

/**
 * Exception for validation errors
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
