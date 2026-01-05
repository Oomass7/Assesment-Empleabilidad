package com.assessment.projectmanagement.domain.exception;

/**
 * Exception for when a requested resource is not found
 */
public class ResourceNotFoundException extends DomainException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Long id) {
        super(String.format("%s with id %d not found", resourceType, id));
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
