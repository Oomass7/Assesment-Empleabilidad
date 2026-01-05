package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;

import java.util.List;

/**
 * Use case for retrieving task information
 */
public interface GetTaskUseCase {

    Task getTaskById(Long taskId);

    List<Task> getAllTasks();

    List<Task> getTasksByProject(Long projectId);

    List<Task> getTasksByAssignedUser(Long userId);

    List<Task> getTasksByStatus(String status);
}
