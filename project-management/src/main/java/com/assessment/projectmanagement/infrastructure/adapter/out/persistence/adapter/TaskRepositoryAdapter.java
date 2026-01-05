package com.assessment.projectmanagement.infrastructure.adapter.out.persistence.adapter;

import com.assessment.projectmanagement.domain.enums.TaskStatus;
import com.assessment.projectmanagement.domain.model.Task;
import com.assessment.projectmanagement.domain.port.out.TaskRepositoryPort;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.mapper.TaskEntityMapper;
import com.assessment.projectmanagement.infrastructure.adapter.out.persistence.repository.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter implementing TaskRepositoryPort using JPA
 */
@Component
@RequiredArgsConstructor
public class TaskRepositoryAdapter implements TaskRepositoryPort {

    private final TaskJpaRepository jpaRepository;
    private final TaskEntityMapper mapper;

    @Override
    public Task save(Task task) {
        var entity = mapper.toEntity(task);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Task> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Task> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByProjectId(Long projectId) {
        return jpaRepository.findByProjectId(projectId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByAssignedUserId(Long userId) {
        return jpaRepository.findByAssignedToId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByStatus(String status) {
        return jpaRepository.findByStatus(TaskStatus.valueOf(status)).stream()
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
