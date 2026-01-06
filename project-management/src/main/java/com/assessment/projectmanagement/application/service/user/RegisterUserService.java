package com.assessment.projectmanagement.application.service.user;

import com.assessment.projectmanagement.domain.enums.UserRole;
import com.assessment.projectmanagement.domain.exception.ValidationException;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.user.RegisterUserUseCase;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * Service implementing RegisterUserUseCase
 */
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterUserCommand command) {
        // 1. Validate input
        validateCommand(command);

        // 2. Check if username already exists
        if (userRepository.existsByUsername(command.username())) {
            throw new ValidationException("Username already exists");
        }

        // 3. Check if email already exists
        if (userRepository.existsByEmail(command.email())) {
            throw new ValidationException("Email already exists");
        }

        // 4. Create user
        // 4. Create user
        String roleToAssign = (command.role() != null && !command.role().isBlank()) ? command.role()
                : "PROJECT_MANAGER";

        User user = User.builder()
                .username(command.username())
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .role(UserRole.valueOf(roleToAssign))
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        // 5. Save
        return userRepository.save(user);
    }

    private void validateCommand(RegisterUserCommand command) {
        if (command.username() == null || command.username().trim().isEmpty()) {
            throw new ValidationException("Username is required");
        }

        if (command.email() == null || command.email().trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }

        if (command.password() == null || command.password().length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }

        // Role is optional, but if present must be valid
        if (command.role() != null && !command.role().isEmpty()) {
            try {
                UserRole.valueOf(command.role());
            } catch (IllegalArgumentException e) {
                throw new ValidationException("Invalid role. Must be: ADMIN, PROJECT_MANAGER, DEVELOPER, or GUEST");
            }
        }
    }
}
