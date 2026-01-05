package com.assessment.projectmanagement.infrastructure.adapter.in.web.controller;

import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.user.AuthenticateUserUseCase;
import com.assessment.projectmanagement.domain.port.in.user.RegisterUserUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.LoginRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.RegisterRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Authentication
 * Handles user registration and login
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication and registration endpoints")
public class AuthController {

        private final RegisterUserUseCase registerUserUseCase;
        private final AuthenticateUserUseCase authenticateUserUseCase;

        /**
         * Register a new user
         * POST /api/auth/register
         */
        @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token for immediate authentication")
        @PostMapping("/register")
        public ResponseEntity<ApiResponse<AuthResponse>> register(
                        @Valid @RequestBody RegisterRequest request) {

                // Convert request to command
                RegisterUserUseCase.RegisterUserCommand command = new RegisterUserUseCase.RegisterUserCommand(
                                request.getUsername(),
                                request.getEmail(),
                                request.getPassword(),
                                request.getRole());

                // Execute use case
                User user = registerUserUseCase.registerUser(command);

                // Auto-login after registration
                AuthenticateUserUseCase.AuthenticationCommand authCommand = new AuthenticateUserUseCase.AuthenticationCommand(
                                user.getUsername(),
                                request.getPassword());

                AuthenticateUserUseCase.AuthenticationResult authResult = authenticateUserUseCase
                                .authenticate(authCommand);

                // Create response
                AuthResponse response = new AuthResponse(
                                authResult.token(),
                                authResult.username(),
                                authResult.role());

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.success("User registered successfully", response));
        }

        /**
         * Login user
         * POST /api/auth/login
         */
        @Operation(summary = "Login user", description = "Authenticates a user and returns a JWT token for accessing protected endpoints")
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<AuthResponse>> login(
                        @Valid @RequestBody LoginRequest request) {

                // Convert request to command
                AuthenticateUserUseCase.AuthenticationCommand command = new AuthenticateUserUseCase.AuthenticationCommand(
                                request.getUsername(),
                                request.getPassword());

                // Execute use case
                AuthenticateUserUseCase.AuthenticationResult result = authenticateUserUseCase.authenticate(command);

                // Create response
                AuthResponse response = new AuthResponse(
                                result.token(),
                                result.username(),
                                result.role());

                return ResponseEntity.ok(ApiResponse.success("Login successful", response));
        }
}
