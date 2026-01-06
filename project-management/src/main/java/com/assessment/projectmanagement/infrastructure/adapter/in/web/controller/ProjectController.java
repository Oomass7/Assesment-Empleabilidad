package com.assessment.projectmanagement.infrastructure.adapter.in.web.controller;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.in.project.ActivateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.project.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.CreateProjectRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ProjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import com.assessment.projectmanagement.domain.port.in.project.InviteMemberUseCase;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectMembersUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.InviteMemberRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.UserResponse;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectAuditLogsUseCase;
import com.assessment.projectmanagement.domain.model.AuditLog;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.AuditLogResponse;

/**
 * REST Controller for Project operations
 * This is an INPUT adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {

        private final CreateProjectUseCase createProjectUseCase;
        private final ActivateProjectUseCase activateProjectUseCase;
        private final GetProjectUseCase getProjectUseCase;
        private final InviteMemberUseCase inviteMemberUseCase;
        private final GetProjectMembersUseCase getProjectMembersUseCase;
        private final GetProjectAuditLogsUseCase getProjectAuditLogsUseCase;

        /**
         * Create a new project
         * POST /api/projects
         */
        @Operation(summary = "Create a new project", description = "Creates a new project in DRAFT status. User must be authenticated.")
        @PostMapping
        public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
                        @Valid @RequestBody CreateProjectRequest request) {

                // Convert request to command
                CreateProjectUseCase.CreateProjectCommand command = new CreateProjectUseCase.CreateProjectCommand(
                                request.getName(),
                                request.getDescription(),
                                request.getStartDate(),
                                request.getEndDate());

                // Execute use case
                Project project = createProjectUseCase.createProject(command);

                // Convert domain model to response DTO
                ProjectResponse response = mapToResponse(project);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Project created successfully", response));
        }

        /**
         * Get all projects for current user
         * GET /api/projects
         */
        @Operation(summary = "Get all projects", description = "Retrieves a list of all projects belonging to the current user.")
        @GetMapping
        public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
                List<Project> projects = getProjectUseCase.getAllProjects();

                List<ProjectResponse> responses = projects.stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());

                return ResponseEntity.ok(ApiResponse.success(responses));
        }

        /**
         * Get project by ID
         * GET /api/projects/{id}
         */
        @Operation(summary = "Get project by ID", description = "Retrieves details of a specific project.")
        @GetMapping("/{id}")
        public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
                Project project = getProjectUseCase.getProjectById(id);
                ProjectResponse response = mapToResponse(project);
                return ResponseEntity.ok(ApiResponse.success(response));
        }

        /**
         * Activate a project
         * PATCH /api/projects/{id}/activate
         */
        @Operation(summary = "Activate project", description = "Activates a project. Fails if the project has no active tasks.")
        @PatchMapping("/{id}/activate")
        public ResponseEntity<ApiResponse<ProjectResponse>> activateProject(@PathVariable Long id) {
                Project project = activateProjectUseCase.activateProject(id);
                ProjectResponse response = mapToResponse(project);
                return ResponseEntity.ok(ApiResponse.success("Project activated successfully", response));
        }

        @Operation(summary = "Invite member", description = "Invites a user to the project.")
        @PostMapping("/{id}/members")
        public ResponseEntity<ApiResponse<Void>> inviteMember(@PathVariable Long id,
                        @RequestBody InviteMemberRequest request) {
                inviteMemberUseCase.inviteMember(id, request.getUserId());
                return ResponseEntity.ok(ApiResponse.success("Member invited successfully", null));
        }

        @Operation(summary = "Get project members", description = "Get list of members in the project.")
        @GetMapping("/{id}/members")
        public ResponseEntity<ApiResponse<List<UserResponse>>> getMembers(@PathVariable Long id) {
                List<User> members = getProjectMembersUseCase.getMembers(id);
                List<UserResponse> response = members.stream()
                                .map(u -> UserResponse.builder()
                                                .id(u.getId())
                                                .username(u.getUsername())
                                                .email(u.getEmail())
                                                .build())
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success(response));
        }

        @GetMapping("/{id}/audit")
        @Operation(summary = "Get project audit logs")
        public ResponseEntity<ApiResponse<List<AuditLogResponse>>> getAuditLogs(@PathVariable Long id) {
                List<AuditLog> logs = getProjectAuditLogsUseCase.getAuditLogs(id);
                List<AuditLogResponse> response = logs.stream()
                                .map(AuditLogResponse::fromDomain)
                                .collect(Collectors.toList());
                return ResponseEntity.ok(ApiResponse.success(response));
        }

        /**
         * Helper method to convert Project domain model to ProjectResponse DTO
         */
        private ProjectResponse mapToResponse(Project project) {
                return ProjectResponse.builder()
                                .id(project.getId())
                                .name(project.getName())
                                .description(project.getDescription())
                                .status(project.getStatus().name())
                                .startDate(project.getStartDate())
                                .endDate(project.getEndDate())
                                .createdAt(project.getCreatedAt())
                                .updatedAt(project.getUpdatedAt())
                                .ownerId(project.getOwner().getId())
                                .ownerUsername(project.getOwner().getUsername())
                                .completionPercentage(project.getCompletionPercentage())
                                .build();
        }
}
