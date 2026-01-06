package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.task.UpdateTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateTaskService implements UpdateTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final CurrentUserPort currentUserPort;
    private final UserRepositoryPort userRepository;
    private final AuditLogPort auditLogPort;

    @Override
    public Task updateTask(UpdateTaskCommand command) {
        User currentUser = currentUserPort.getCurrentUser();
        Long taskId = command.taskId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        Project project = task.getProject();
        if (!project.isMember(currentUser)) {
            throw new UnauthorizedException("Only project members can edit tasks");
        }

        if (command.assignedToId() != null) {
            User assignee = userRepository.findById(command.assignedToId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", command.assignedToId()));

            // Check if assignee is member
            if (!project.isMember(assignee)) {
                throw new BusinessException("Assignee must be a member of the project");
            }

            task.assignTo(assignee);
        }

        task.updateDetails(command.title(), command.description(), command.priority(), command.dueDate());

        Task updatedTask = taskRepository.save(task);

        auditLogPort.logUpdate("Task", taskId, currentUser.getUsername(), "Updated task details");

        return updatedTask;
    }
}
