package com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.assessment.projectmanagement.domain.model.AuditLog;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditLogResponse {
    private Long id;
    private String action;
    private String entityType;
    private Long entityId;
    private String username;
    private String details;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public static AuditLogResponse fromDomain(AuditLog log) {
        return AuditLogResponse.builder()
                .id(log.getId())
                .action(log.getAction())
                .entityType(log.getEntityType())
                .entityId(log.getEntityId())
                .username(log.getUsername())
                .details(log.getDetails())
                .timestamp(log.getTimestamp())
                .build();
    }
}
