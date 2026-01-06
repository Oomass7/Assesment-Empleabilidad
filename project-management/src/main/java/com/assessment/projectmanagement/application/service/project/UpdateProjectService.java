package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.in.project.UpdateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UpdateProjectService implements UpdateProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final AuditLogPort auditLogPort;
    private final CurrentUserPort currentUserPort;

    @Override
    @Transactional
    public Project updateProject(UpdateProjectCommand command) {
        Project project = projectRepository.findById(command.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + command.projectId()));

        project.updateDetails(
                command.name(),
                command.description(),
                command.startDate(),
                command.endDate());

        Project updatedProject = projectRepository.save(project);

        auditLogPort.logUpdate(
                "PROJECT",
                project.getId(),
                currentUserPort.getCurrentUsername(),
                "Project updated");

        return updatedProject;
    }
}
