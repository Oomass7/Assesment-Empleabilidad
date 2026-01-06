package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository;

import java.util.List;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for AuditLogEntity
 */
@Repository
public interface AuditLogJpaRepository extends JpaRepository<AuditLogEntity, Long> {
    List<AuditLogEntity> findByEntityTypeAndEntityId(String entityType, Long entityId);

    List<AuditLogEntity> findByEntityTypeAndEntityIdIn(String entityType, List<Long> entityIds);
}
