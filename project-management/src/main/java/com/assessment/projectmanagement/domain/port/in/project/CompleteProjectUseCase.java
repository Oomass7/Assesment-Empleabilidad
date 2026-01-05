package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.Project;

/**
 * Use case for completing a project
 */
public interface CompleteProjectUseCase {

    Project completeProject(Long projectId);
}
