package com.assessment.projectmanagement.infrastructure.adapter.in.web.controller;

import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.in.task.CompleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.in.task.CreateTaskUseCase;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request.CreateTaskRequest;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.ApiResponse;
import com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.response.TaskResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Task operations
 * This is an INPUT adapter in hexagonal architecture
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TaskController {

    private final CreateTaskUseCase createTaskUseCase;
    private final CompleteTaskUseCase completeTaskUseCase;

    /**
     * Create a new task in a project
     * POST /api/projects/{projectId}/tasks
     */
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody CreateTaskRequest request) {

        // Convert request to command
        CreateTaskUseCase.CreateTaskCommand command = new CreateTaskUseCase.CreateTaskCommand(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueDate(),
                projectId,
                request.getAssignedToId());

        // Execute use case
        Task task = createTaskUseCase.createTask(command);

        // Convert domain model to response DTO
        TaskResponse response = mapToResponse(task);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Task created successfully", response));
    }

    /**
     * Complete a task
     * PATCH /api/tasks/{id}/complete
     */
    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<ApiResponse<TaskResponse>> completeTask(@PathVariable Long id) {
        Task task = completeTaskUseCase.completeTask(id);
        TaskResponse response = mapToResponse(task);
        return ResponseEntity.ok(ApiResponse.success("Task completed successfully", response));
    }

    /**
     * Helper method to convert Task domain model to TaskResponse DTO
     */
    private TaskResponse mapToResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus().name())
                .priority(task.getPriority().name())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .projectId(task.getProject() != null ? task.getProject().getId() : null)
                .projectName(task.getProject() != null ? task.getProject().getName() : null)
                .assignedToId(task.getAssignedTo() != null ? task.getAssignedTo().getId() : null)
                .assignedToUsername(task.getAssignedTo() != null ? task.getAssignedTo().getUsername() : null)
                .overdue(task.isOverdue())
                .build();
    }
}
