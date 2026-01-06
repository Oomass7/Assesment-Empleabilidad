package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.project.ActivateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

/**
 * Service implementing ActivateProjectUseCase
 * BUSINESS RULE: A project can only be activated if it has at least one active
 * task
 */
@RequiredArgsConstructor
public class ActivateProjectService implements ActivateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final TaskRepositoryPort taskRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    @Override
    public Project activateProject(Long projectId) {
        // 1. Get current user
        User currentUser = currentUserPort.getCurrentUser();

        // 2. Find project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        // 3. BUSINESS RULE: Only owner can activate
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the project owner can activate it");
        }

        // 4. BUSINESS RULE: Project must have at least one active (non-completed) task
        var tasks = taskRepository.findByProjectId(projectId);
        boolean hasActiveTask = tasks.stream().anyMatch(t -> !t.isCompleted());

        if (!hasActiveTask) {
            throw new BusinessException("Cannot activate project without at least one active task");
        }

        // 5. Activate project (domain logic)
        project.activate();

        // 6. Save
        Project activatedProject = projectRepository.save(project);

        // 7. BUSINESS RULE: Generate audit log
        auditLogPort.logUpdate(
                "Project",
                activatedProject.getId(),
                currentUser.getUsername(),
                String.format("Activated project: %s", activatedProject.getName()));

        // 8. BUSINESS RULE: Generate notification
        notificationPort.sendProjectCreatedNotification(
                activatedProject.getId(),
                activatedProject.getName(),
                currentUser.getEmail());

        return activatedProject;
    }
}
