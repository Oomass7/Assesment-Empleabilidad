package com.assessment.projectmanagement.domain.port.in.task;

/**
 * Use case for deleting a task
 */
public interface DeleteTaskUseCase {

    void deleteTask(Long taskId);
}
