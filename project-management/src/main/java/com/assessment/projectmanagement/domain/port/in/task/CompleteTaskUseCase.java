package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;

/**
 * Use case for completing a task
 */
public interface CompleteTaskUseCase {

    Task completeTask(Long taskId);
}
