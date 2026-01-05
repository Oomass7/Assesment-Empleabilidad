package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository;

import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for TaskEntity
 */
@Repository
public interface TaskJpaRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findByProjectId(Long projectId);

    List<TaskEntity> findByAssignedToId(Long userId);

    List<TaskEntity> findByStatus(TaskStatus status);
}
