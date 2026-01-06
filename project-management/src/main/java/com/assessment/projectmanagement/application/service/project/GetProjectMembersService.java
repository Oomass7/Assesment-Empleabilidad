package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectMembersUseCase;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetProjectMembersService implements GetProjectMembersUseCase {

    private final ProjectRepositoryPort projectRepository;

    @Override
    public List<User> getMembers(Long projectId) {
        return projectRepository.findById(projectId)
                .map(Project::getMembers)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));
    }
}
