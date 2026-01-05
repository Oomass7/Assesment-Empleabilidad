package com.assessment.projectmanagement.domain.port.in.project;

/**
 * Use case for deleting a project
 */
public interface DeleteProjectUseCase {

    void deleteProject(Long projectId);
}
