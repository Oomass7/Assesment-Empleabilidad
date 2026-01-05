package com.assessment.projectmanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AuditLog domain model - Tracks all important actions in the system
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    private Long id;
    private String action;
    private String entityType;
    private Long entityId;
    private String username;
    private String details;
    private LocalDateTime timestamp;

    /**
     * Factory method: Create audit log for entity creation
     */
    public static AuditLog forCreation(String entityType, Long entityId, String username, String details) {
        return AuditLog.builder()
                .action("CREATE")
                .entityType(entityType)
                .entityId(entityId)
                .username(username)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Factory method: Create audit log for entity update
     */
    public static AuditLog forUpdate(String entityType, Long entityId, String username, String details) {
        return AuditLog.builder()
                .action("UPDATE")
                .entityType(entityType)
                .entityId(entityId)
                .username(username)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Factory method: Create audit log for entity deletion
     */
    public static AuditLog forDeletion(String entityType, Long entityId, String username, String details) {
        return AuditLog.builder()
                .action("DELETE")
                .entityType(entityType)
                .entityId(entityId)
                .username(username)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
