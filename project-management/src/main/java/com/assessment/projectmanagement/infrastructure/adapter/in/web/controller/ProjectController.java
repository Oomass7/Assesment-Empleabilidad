package com.assessment.projectmanagement.infrastructure.adapter.in.web.controller;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.in.project.CreateProjectUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.CreateProjectRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ProjectResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Project operations
 * This is an INPUT adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final CreateProjectUseCase createProjectUseCase;
    // TODO: Inject other use cases as you implement them

    /**
     * Create a new project
     */
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
        ProjectResponse response = ProjectResponse.builder()
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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Project created successfully", response));
    }

    // TODO: Add more endpoints:
    // @GetMapping("/{id}") - Get project by ID
    // @GetMapping - Get all projects
    // @PutMapping("/{id}") - Update project
    // @DeleteMapping("/{id}") - Delete project
    // @PatchMapping("/{id}/activate") - Activate project
    // @PatchMapping("/{id}/complete") - Complete project
}
