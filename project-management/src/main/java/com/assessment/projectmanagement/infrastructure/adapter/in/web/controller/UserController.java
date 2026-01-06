package com.assessment.projectmanagement.infrastructure.adapter.in.web.controller;

import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.user.GetAllUsersUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final GetAllUsersUseCase getAllUsersUseCase;

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = getAllUsersUseCase.getAllUsers();

        List<UserResponse> response = users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
