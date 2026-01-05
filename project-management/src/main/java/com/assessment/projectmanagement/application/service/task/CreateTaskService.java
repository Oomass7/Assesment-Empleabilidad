package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.enums.TaskPriority;
import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.exception.ValidationException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.task.CreateTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Service implementing CreateTaskUseCase
 */
@RequiredArgsConstructor
public class CreateTaskService implements CreateTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;

    @Override
    public Task createTask(CreateTaskCommand command) {
        // 1. Validate input
        validateCommand(command);

        // 2. Get current user
        User currentUser = currentUserPort.getCurrentUser();

        // 3. Find project
        Project project = projectRepository.findById(command.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project", command.projectId()));

        // 4. BUSINESS RULE: Only owner can create tasks in their projects
        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the project owner can create tasks");
        }

        // 5. Find assigned user if specified
        User assignedUser = null;
        if (command.assignedToId() != null) {
            assignedUser = userRepository.findById(command.assignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", command.assignedToId()));
        }

        // 6. Create task domain model
        Task task = Task.builder()
                .title(command.title())
                .description(command.description())
                .status(TaskStatus.TODO)
                .priority(TaskPriority.valueOf(command.priority()))
                .dueDate(command.dueDate())
                .project(project)
                .assignedTo(assignedUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 7. Save
        Task savedTask = taskRepository.save(task);

        // 8. Audit log
        auditLogPort.logCreation(
                "Task",
                savedTask.getId(),
                currentUser.getUsername(),
                String.format("Created task: %s in project: %s",
                        savedTask.getTitle(), project.getName()));

        return savedTask;
    }

    private void validateCommand(CreateTaskCommand command) {
        if (command.title() == null || command.title().trim().isEmpty()) {
            throw new ValidationException("Task title is required");
        }

        if (command.projectId() == null) {
            throw new ValidationException("Project ID is required");
        }

        if (command.priority() == null) {
            throw new ValidationException("Task priority is required");
        }

        try {
            TaskPriority.valueOf(command.priority());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid task priority. Must be: LOW, MEDIUM, HIGH, or CRITICAL");
        }
    }
}
