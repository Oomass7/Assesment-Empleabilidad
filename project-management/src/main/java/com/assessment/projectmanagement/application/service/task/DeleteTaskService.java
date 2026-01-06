package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.port.in.task.DeleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteTaskService implements DeleteTaskUseCase {

    private final TaskRepositoryPort taskRepository;
    private final AuditLogPort auditLogPort;
    private final CurrentUserPort currentUserPort;

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }

        taskRepository.deleteById(id);

        auditLogPort.logDeletion(
                "TASK",
                id,
                currentUserPort.getCurrentUsername(),
                "Task deleted");
    }
}
