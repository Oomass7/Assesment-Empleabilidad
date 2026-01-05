package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.Project;

import java.util.List;
import java.util.Optional;

/**
 * Output port for Project repository operations
 */
public interface ProjectRepositoryPort {

    Project save(Project project);

    Optional<Project> findById(Long id);

    List<Project> findAll();

    List<Project> findByOwnerId(Long ownerId);

    List<Project> findByStatus(String status);

    void deleteById(Long id);

    boolean existsById(Long id);
}
