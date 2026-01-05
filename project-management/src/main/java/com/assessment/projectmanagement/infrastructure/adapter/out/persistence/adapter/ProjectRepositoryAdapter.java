package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.adapter;

import com.assessment.projectmanagement.domain.model.Project;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.mapper.ProjectEntityMapper;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository.ProjectJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing ProjectRepositoryPort using JPA
 * This is the bridge between domain and infrastructure
 */
@Component
@RequiredArgsConstructor
public class ProjectRepositoryAdapter implements ProjectRepositoryPort {

    private final ProjectJpaRepository jpaRepository;
    private final ProjectEntityMapper mapper;

    @Override
    public Project save(Project project) {
        ProjectEntity entity = mapper.toEntity(project);
        ProjectEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByOwnerId(Long ownerId) {
        return jpaRepository.findByOwnerId(ownerId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByStatus(String status) {
        return jpaRepository.findByStatus(
                com.assessment.projectmanagement.domain.enums.ProjectStatus.valueOf(status)).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}
