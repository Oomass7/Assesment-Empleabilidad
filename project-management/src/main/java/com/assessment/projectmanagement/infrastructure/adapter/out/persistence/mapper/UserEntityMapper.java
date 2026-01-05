package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.mapper;

import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between User domain model and UserEntity
 */
@Component
public class UserEntityMapper {

    /**
     * Convert domain model to entity
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Convert entity to domain model
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .role(entity.getRole())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
