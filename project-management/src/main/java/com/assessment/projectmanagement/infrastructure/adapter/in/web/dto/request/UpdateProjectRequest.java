package com.assessment.projectmanagement.infrastructure.adapter.in.web.dto.request;

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
@Schema(description = "Request to update an existing project")
public class UpdateProjectRequest {

    @NotBlank(message = "Project name is required")
    @Size(max = 100, message = "Project name must be less than 100 characters")
    @Schema(description = "Project name", example = "Updated Project Name")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    @Schema(description = "Project description", example = "Updated project description")
    private String description;

    @Schema(description = "Project start date", example = "2024-01-01T09:00:00")
    private LocalDateTime startDate;

    @Schema(description = "Project end date", example = "2024-12-31T17:00:00")
    private LocalDateTime endDate;
}
