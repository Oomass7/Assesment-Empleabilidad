package com.assessment.projectmanagement.infrastructure.adapter.out.audit;

import com.assessment.projectmanagement.domain.model.AuditLog;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.AuditLogEntity;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository.AuditLogJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter for audit logging
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogAdapter implements AuditLogPort {

    private final AuditLogJpaRepository auditLogRepository;

    @Override
    public void log(AuditLog auditLog) {
        AuditLogEntity entity = AuditLogEntity.builder()
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .username(auditLog.getUsername())
                .details(auditLog.getDetails())
                .timestamp(auditLog.getTimestamp())
                .build();

        auditLogRepository.save(entity);
        log.info("Audit log created: {} {} by {}",
                auditLog.getAction(), auditLog.getEntityType(), auditLog.getUsername());
    }

    @Override
    public void logCreation(String entityType, Long entityId, String username, String details) {
        log(AuditLog.forCreation(entityType, entityId, username, details));
    }

    @Override
    public void logUpdate(String entityType, Long entityId, String username, String details) {
        log(AuditLog.forUpdate(entityType, entityId, username, details));
    }

    @Override
    public void logDeletion(String entityType, Long entityId, String username, String details) {
        log(AuditLog.forDeletion(entityType, entityId, username, details));
    }

    @Override
    public java.util.List<AuditLog> findByEntityTypeAndEntityId(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public java.util.List<AuditLog> findByEntityTypeAndEntityIdIn(String entityType, java.util.List<Long> entityIds) {
        return auditLogRepository.findByEntityTypeAndEntityIdIn(entityType, entityIds).stream()
                .map(this::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

    private AuditLog toDomain(AuditLogEntity entity) {
        return AuditLog.builder()
                .id(entity.getId())
                .action(entity.getAction())
                .entityType(entity.getEntityType())
                .entityId(entity.getEntityId())
                .username(entity.getUsername())
                .details(entity.getDetails())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
