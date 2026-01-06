package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.task.AssignTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AssignTaskService implements AssignTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final UserRepositoryPort userRepository;
    private final AuditLogPort auditLogPort;
    private final CurrentUserPort currentUserPort;

    @Override
    @Transactional
    public Task assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        task.assignTo(user);

        Task updatedTask = taskRepository.save(task);

        auditLogPort.logUpdate(
                "TASK",
                taskId,
                currentUserPort.getCurrentUsername(),
                "Task assigned to user: " + user.getUsername());

        return updatedTask;
    }
}
