package com.assessment.projectmanagement.application.service.project;

import com.assessment.projectmanagement.domain.exception.ResourceNotFoundException;
import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.project.InviteMemberUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InviteMemberService implements InviteMemberUseCase {

    private final ProjectRepositoryPort projectRepository;
    private final UserRepositoryPort userRepository;
    private final CurrentUserPort currentUserPort;
    private final AuditLogPort auditLogPort;

    @Override
    public void inviteMember(Long projectId, Long userId) {
        User currentUser = currentUserPort.getCurrentUser();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedException("Only the project owner can invite members");
        }

        User userToInvite = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        project.addMember(userToInvite);

        projectRepository.save(project);

        auditLogPort.logUpdate("Project", projectId, currentUser.getUsername(),
                "Invited user " + userToInvite.getUsername() + " to project " + project.getName());
    }
}
