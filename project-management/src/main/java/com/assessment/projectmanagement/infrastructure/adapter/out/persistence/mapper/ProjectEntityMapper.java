package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.mapper;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Project domain model and ProjectEntity
 * This keeps the domain model clean from JPA annotations
 */
@Component
public class ProjectEntityMapper {

    private final UserEntityMapper userEntityMapper;
    private final TaskEntityMapper taskEntityMapper;

    public ProjectEntityMapper(UserEntityMapper userEntityMapper, TaskEntityMapper taskEntityMapper) {
        this.userEntityMapper = userEntityMapper;
        this.taskEntityMapper = taskEntityMapper;
    }

    /**
     * Convert domain model to entity
     */
    public ProjectEntity toEntity(Project project) {
        if (project == null) {
            return null;
        }

        return ProjectEntity.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .owner(userEntityMapper.toEntity(project.getOwner()))
                .members(project.getMembers() != null ? project.getMembers().stream()
                        .map(userEntityMapper::toEntity)
                        .collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>())
                .tasks(project.getTasks() != null ? project.getTasks().stream()
                        .map(taskEntityMapper::toEntity)
                        .collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }

    /**
     * Convert entity to domain model
     */
    public Project toDomain(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }

        return Project.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .owner(userEntityMapper.toDomain(entity.getOwner()))
                .members(entity.getMembers() != null ? entity.getMembers().stream()
                        .map(userEntityMapper::toDomain)
                        .collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>())
                .tasks(entity.getTasks() != null ? entity.getTasks().stream()
                        .map(taskEntityMapper::toDomain)
                        .collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }
}
