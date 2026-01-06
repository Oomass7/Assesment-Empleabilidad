package com.assessment.projectmanagement.domain.port.in.user;

/**
 * Use case for user authentication
 */
public interface AuthenticateUserUseCase {

        AuthenticationResult authenticate(AuthenticationCommand command);

        record AuthenticationCommand(
                        String email,
                        String password) {
        }

        record AuthenticationResult(
                        String token,
                        String username,
                        String role) {
        }
}
