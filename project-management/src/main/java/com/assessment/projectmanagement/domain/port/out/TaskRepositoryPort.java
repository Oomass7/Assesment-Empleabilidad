package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Task repository operations
 */
public interface TaskRepositoryPort {

    Task save(Task task);

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findByProjectId(Long projectId);

    List<Task> findByAssignedUserId(Long userId);

    List<Task> findByStatus(String status);

    void deleteById(Long id);

    boolean existsById(Long id);
}
