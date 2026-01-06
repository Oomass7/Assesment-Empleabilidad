package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;

import java.time.LocalDateTime;

/**
 * Use case for updating an existing task
 */
public interface UpdateTaskUseCase {

    Task updateTask(UpdateTaskCommand command);

    record UpdateTaskCommand(
            Long taskId,
            String title,
            String description,
            com.assessment.projectmanagement.domain.enums.TaskPriority priority,
            LocalDateTime dueDate,
            Long assignedToId) {
    }
}
