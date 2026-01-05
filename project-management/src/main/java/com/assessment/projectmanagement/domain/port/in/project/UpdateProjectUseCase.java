package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.Project;

import java.time.LocalDateTime;

/**
 * Use case for updating an existing project
 */
public interface UpdateProjectUseCase {

    Project updateProject(UpdateProjectCommand command);

    record UpdateProjectCommand(
            Long projectId,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate) {
    }
}
