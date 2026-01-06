package com.assessment.projectmanagement.domain.port.in.project;

import com.assessment.projectmanagement.domain.model.User;
import java.util.List;

public interface GetProjectMembersUseCase {
    List<User> getMembers(Long projectId);
}
