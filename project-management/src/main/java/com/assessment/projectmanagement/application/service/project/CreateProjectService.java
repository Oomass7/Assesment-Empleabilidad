package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.domain.exception.ValidationException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.project.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Application service implementing CreateProjectUseCase
 * This is where the use case orchestration happens
 */
@RequiredArgsConstructor
public class CreateProjectService implements CreateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    @Override
    public Project createProject(CreateProjectCommand command) {
        // 1. Validate input
        validateCommand(command);

        // 2. Get current user
        User currentUser = currentUserPort.getCurrentUser();

        // 3. Check authorization
        if (!currentUser.canManageProjects()) {
            throw new ValidationException("User does not have permission to create projects");
        }

        // 4. Create domain model
        Project project = Project.builder()
                .name(command.name())
                .description(command.description())
                .status(ProjectStatus.ACTIVE)
                .startDate(command.startDate())
                .endDate(command.endDate())
                .owner(currentUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 5. Save to repository
        Project savedProject = projectRepository.save(project);

        // 6. Log audit
        auditLogPort.logCreation(
                "Project",
                savedProject.getId(),
                currentUser.getUsername(),
                String.format("Created project: %s", savedProject.getName()));

        // 7. Send notification
        notificationPort.sendProjectCreatedNotification(
                savedProject.getId(),
                savedProject.getName(),
                currentUser.getEmail());

        return savedProject;
    }

    private void validateCommand(CreateProjectCommand command) {
        if (command.name() == null || command.name().trim().isEmpty()) {
            throw new ValidationException("Project name is required");
        }

        if (command.startDate() != null && command.endDate() != null) {
            if (command.endDate().isBefore(command.startDate())) {
                throw new ValidationException("End date cannot be before start date");
            }
        }
    }
}
