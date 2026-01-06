package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.port.in.project.DeleteProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteProjectService implements DeleteProjectUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final AuditLogPort auditLogPort;
    private final CurrentUserPort currentUserPort;

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }

        projectRepository.deleteById(id);

        auditLogPort.logDeletion(
                "PROJECT",
                id,
                currentUserPort.getCurrentUsername(),
                "Project deleted");
    }
}
