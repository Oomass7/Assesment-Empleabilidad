package com.assessment.projectmanagement.domain.port.in.project;

public interface InviteMemberUseCase {
    void inviteMember(Long projectId, Long userId);
}
