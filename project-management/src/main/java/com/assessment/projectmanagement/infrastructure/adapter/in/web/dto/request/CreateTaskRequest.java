package com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request;

import com.assessment.projectmanagement.domain.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new task")
public class CreateTaskRequest {

    @NotBlank(message = "Task title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    @Schema(description = "Task title", example = "Implement Auth")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @Schema(description = "Task description", example = "Setup Spring Security")
    private String description;

    @Schema(description = "Task priority", example = "HIGH")
    private TaskPriority priority;

    @Schema(description = "Due date", example = "2024-02-01T23:59:59")
    private LocalDateTime dueDate;

    @Schema(description = "ID of the user this task is assigned to", example = "1")
    private Long assignedToId;
}
