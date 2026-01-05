package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.AuditLog;

/**
 * Output port for audit logging operations
 */
public interface AuditLogPort {

    void log(AuditLog auditLog);

    void logCreation(String entityType, Long entityId, String username, String details);

    void logUpdate(String entityType, Long entityId, String username, String details);

    void logDeletion(String entityType, Long entityId, String username, String details);
}
