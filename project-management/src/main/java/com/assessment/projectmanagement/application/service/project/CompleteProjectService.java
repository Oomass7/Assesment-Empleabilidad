package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.in.project.CompleteProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompleteProjectService implements CompleteProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final AuditLogPort auditLogPort;
    private final CurrentUserPort currentUserPort;

    @Override
    @Transactional
    public Project completeProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        project.complete();

        Project updatedProject = projectRepository.save(project);

        auditLogPort.logUpdate(
                "PROJECT",
                projectId,
                currentUserPort.getCurrentUsername(),
                "Project marked as COMPLETED");

        return updatedProject;
    }
}
