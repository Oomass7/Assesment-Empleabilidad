package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;

import java.time.LocalDateTime;

/**
 * Use case for creating a new task
 */
public interface CreateTaskUseCase {

    Task createTask(CreateTaskCommand command);

    record CreateTaskCommand(
            String title,
            String description,
            String priority,
            LocalDateTime dueDate,
            Long projectId,
            Long assignedToId) {
    }
}
