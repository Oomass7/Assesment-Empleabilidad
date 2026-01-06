package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.mapper;

import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Task domain model and TaskEntity
 */
@Component
public class TaskEntityMapper {

    private final UserEntityMapper userEntityMapper;

    public TaskEntityMapper(UserEntityMapper userEntityMapper) {
        this.userEntityMapper = userEntityMapper;
    }

    /**
     * Convert domain model to entity (without project to avoid circular dependency)
     */
    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }

        return TaskEntity.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .completedAt(task.getCompletedAt())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .assignedTo(userEntityMapper.toEntity(task.getAssignedTo()))
                .project(task.getProject() != null
                        ? com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.ProjectEntity
                                .builder()
                                .id(task.getProject().getId())
                                .build()
                        : null)
                .build();
    }

    /**
     * Convert entity to domain model (without project to avoid circular dependency)
     */
    public Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }

        return Task.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .priority(entity.getPriority())
                .dueDate(entity.getDueDate())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .assignedTo(userEntityMapper.toDomain(entity.getAssignedTo()))
                .project(entity.getProject() != null ? com.assessment.projectmanagement.domain.model.Project.builder()
                        .id(entity.getProject().getId())
                        .name(entity.getProject().getName())
                        .status(entity.getProject().getStatus())
                        .owner(entity.getProject().getOwner() != null
                                ? com.assessment.projectmanagement.domain.model.User.builder()
                                        .id(entity.getProject().getOwner().getId())
                                        .email(entity.getProject().getOwner().getEmail())
                                        .username(entity.getProject().getOwner().getUsername())
                                        .build()
                                : null)
                        .build() : null)
                .build();
    }
}
