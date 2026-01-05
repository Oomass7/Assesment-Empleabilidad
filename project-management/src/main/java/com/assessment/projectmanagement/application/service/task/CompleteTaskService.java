package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.exception.BusinessException;
import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.task.CompleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

/**
 * Service implementing CompleteTaskUseCase
 * BUSINESS RULE: Completed tasks cannot be modified
 * BUSINESS RULE: Must generate audit and notification
 */
@RequiredArgsConstructor
public class CompleteTaskService implements CompleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;
    private final NotificationPort notificationPort;

    @Override
    public Task completeTask(Long taskId) {
        // 1. Get current user
        User currentUser = currentUserPort.getCurrentUser();

        // 2. Find task
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", taskId));

        // 3. BUSINESS RULE: Only project owner can complete tasks
        if (!task.getProject().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the project owner can complete tasks");
        }

        // 4. BUSINESS RULE: Cannot complete an already completed task
        if (task.isCompleted()) {
            throw new BusinessException("Task is already completed");
        }

        // 5. Complete task (domain logic)
        task.complete();

        // 6. Save
        Task completedTask = taskRepository.save(task);

        // 7. BUSINESS RULE: Generate audit log
        auditLogPort.logUpdate(
                "Task",
                completedTask.getId(),
                currentUser.getUsername(),
                String.format("Completed task: %s", completedTask.getTitle()));

        // 8. BUSINESS RULE: Generate notification
        notificationPort.sendTaskCompletedNotification(
                completedTask.getId(),
                completedTask.getTitle(),
                task.getProject().getOwner().getEmail());

        return completedTask;
    }
}
