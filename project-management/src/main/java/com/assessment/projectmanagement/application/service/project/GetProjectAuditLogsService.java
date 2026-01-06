package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.model.AuditLog;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectAuditLogsUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetProjectAuditLogsService implements GetProjectAuditLogsUseCase {

    private final AuditLogPort auditLogPort;
    private final TaskRepositoryPort taskRepository;

    @Override
    public List<AuditLog> getAuditLogs(Long projectId) {
        // 1. Logs de Proyecto (Matches 'Project' case used in services)
        List<AuditLog> projectLogs = auditLogPort.findByEntityTypeAndEntityId("Project", projectId);

        // 2. Logs de Tareas
        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<Long> taskIds = tasks.stream().map(Task::getId).collect(Collectors.toList());

        List<AuditLog> taskLogs = new ArrayList<>();
        if (!taskIds.isEmpty()) {
            // Matches 'Task' case used in services
            taskLogs = auditLogPort.findByEntityTypeAndEntityIdIn("Task", taskIds);
        }

        // 3. Unir y ordenar
        List<AuditLog> allLogs = new ArrayList<>();
        allLogs.addAll(projectLogs);
        allLogs.addAll(taskLogs);

        allLogs.sort(Comparator.comparing(AuditLog::getTimestamp).reversed());

        return allLogs;
    }
}
