package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.AuditLog;
import java.util.List;

/**
 * Output port for audit logging operations
 */
public interface AuditLogPort {

    void log(AuditLog auditLog);

    void logCreation(String entityType, Long entityId, String username, String details);

    void logUpdate(String entityType, Long entityId, String username, String details);

    void logDeletion(String entityType, Long entityId, String username, String details);

    List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLog> findByEntityTypeAndEntityIdIn(String entityType, List<Long> entityIds);
}
