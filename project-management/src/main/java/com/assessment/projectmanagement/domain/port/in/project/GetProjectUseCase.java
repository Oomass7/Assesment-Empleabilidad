package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.List;

/**
 * Use case for retrieving project information
 */
public interface GetProjectUseCase {

    Project getProjectById(Long projectId);

    List<Project> getAllProjects();

    List<Project> getProjectsByOwner(Long ownerId);

    List<Project> getProjectsByStatus(String status);
}
