package com.assessment.projectmanagement.domain.port.in.user;

import com.assessment.projectmanagement.domain.model.User;

import java.util.List;

/**
 * Use case for retrieving user information
 */
public interface GetUserUseCase {

    User getUserById(Long userId);

    User getUserByUsername(String username);

    List<User> getAllUsers();

    List<User> getUsersByRole(String role);
}
