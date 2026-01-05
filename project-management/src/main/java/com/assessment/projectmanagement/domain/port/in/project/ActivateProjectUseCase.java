package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.Project;

/**
 * Use case for activating a project
 */
public interface ActivateProjectUseCase {

    Project activateProject(Long projectId);
}
