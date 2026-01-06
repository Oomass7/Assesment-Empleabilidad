package com.assessment.projectmanagement.domain.port.in.user;

import com.assessment.projectmanagement.domain.model.User;
import java.util.List;

public interface GetAllUsersUseCase {
    List<User> getAllUsers();
}
