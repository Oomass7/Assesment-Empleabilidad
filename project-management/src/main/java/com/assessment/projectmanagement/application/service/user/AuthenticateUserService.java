package com.assessment.projectmanagement.application.service.user;

import com.assessment.projectmanagement.domain.port.in.user.AuthenticateUserUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.out.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Service implementing AuthenticateUserUseCase
 */
@RequiredArgsConstructor
public class AuthenticateUserService implements AuthenticateUserUseCase {

        private final AuthenticationManager authenticationManager;
        private final JwtTokenProvider tokenProvider;

        @Override
        public AuthenticationResult authenticate(AuthenticationCommand command) {
                // 1. Authenticate user
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                command.email(),
                                                command.password()));

                // 2. Generate JWT token
                String token = tokenProvider.generateToken(authentication);

                // 3. Get role
                String role = authentication.getAuthorities().stream()
                                .findFirst()
                                .map(GrantedAuthority::getAuthority)
                                .orElse("ROLE_DEVELOPER")
                                .replace("ROLE_", "");

                // 4. Return result
                return new AuthenticationResult(
                                token,
                                authentication.getName(), // Return actual username from UserDetails
                                role);
        }
}
