package com.assessment.projectmanagement.domain.port.in.task;

import com.assessment.projectmanagement.domain.model.Task;
import java.util.List;

public interface GetTasksByProjectUseCase {
    List<Task> getTasksByProjectId(Long projectId);
}
