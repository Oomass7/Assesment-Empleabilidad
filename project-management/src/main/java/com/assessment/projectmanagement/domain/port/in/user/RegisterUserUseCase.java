package com.assessment.projectmanagement.domain.port.in.user;

import com.assessment.projectmanagement.domain.model.User;

/**
 * Use case for user registration
 */
public interface RegisterUserUseCase {

    User registerUser(RegisterUserCommand command);

    record RegisterUserCommand(
            String username,
            String email,
            String password,
            String role) {
    }
}
