package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository;

import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for AuditLogEntity
 */
@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {
}
