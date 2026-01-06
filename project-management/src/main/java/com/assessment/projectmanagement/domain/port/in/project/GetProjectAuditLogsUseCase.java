package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.AuditLog;
import java.util.List;

public interface GetProjectAuditLogsUseCase {
    /**
     * Get audit logs related to a project (project events + task events)
     */
    List<AuditLog> getAuditLogs(Long projectId);
}
