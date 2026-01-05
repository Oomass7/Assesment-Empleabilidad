package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository;

import com.assessment.projectmanagement.domain.enums.ProjectStatus;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for ProjectEntity
 */
@Repository
public interface ProjectJpaRepository extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByOwnerId(Long ownerId);

    List<ProjectEntity> findByStatus(ProjectStatus status);
}
