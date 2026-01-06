package com.assessment.projectmanagement.application.service.task;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.task.GetTasksByProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetProjectTasksService implements GetTasksByProjectUseCase {

    private final TaskRepositoryPort taskRepository;
    private final ProjectRepositoryPort projectRepository;
    private final CurrentUserPort currentUserPort;

    @Override
    public List<Task> getTasksByProjectId(Long projectId) {
        User currentUser = currentUserPort.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the project owner can view its tasks");
        }

        return taskRepository.findByProjectId(projectId);
    }
}
