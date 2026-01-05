package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository;

import com.assessment.projectmanagement.domain.enums.UserRole;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for UserEntity
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRole(UserRole role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
