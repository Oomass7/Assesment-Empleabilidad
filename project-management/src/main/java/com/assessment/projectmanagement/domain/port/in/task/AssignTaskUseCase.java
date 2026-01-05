package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;

/**
 * Use case for assigning a task to a user
 */
public interface AssignTaskUseCase {

    Task assignTask(Long taskId, Long userId);
}
