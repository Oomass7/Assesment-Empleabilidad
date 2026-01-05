package com.assessment.projectmanagement.domain.port.out;

import com.assessment.projectmanagement.domain.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Output port for User repository operations
 */
public interface UserRepositoryPort {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findByRole(String role);

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
