package com.assessment.projectmanagement.domain.exception;

/**
 * Exception for unauthorized access attempts
 */
public class UnauthorizedException extends DomainException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
