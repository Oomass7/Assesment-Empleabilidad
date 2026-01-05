package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.Project;

import java.time.LocalDateTime;

/**
 * Use case for creating a new project
 */
public interface CreateProjectUseCase {

    Project createProject(CreateProjectCommand command);

    record CreateProjectCommand(
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate) {
    }
}
