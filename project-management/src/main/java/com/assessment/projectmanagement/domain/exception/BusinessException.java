package com.assessment.projectmanagement.domain.exception;

/**
 * Exception for business rule violations
 */
public class BusinessException extends DomainException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
