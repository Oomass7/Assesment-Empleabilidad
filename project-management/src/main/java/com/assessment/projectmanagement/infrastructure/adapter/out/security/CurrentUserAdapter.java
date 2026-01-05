package com.assessment.projectmanagement.infrastructure.adapter.out.security;

import com.assessment.projectmanagement.domain.exception.UnauthorizedException;
import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Adapter for getting current authenticated user from Spring Security context
 */
@Component
@RequiredArgsConstructor
public class CurrentUserAdapter implements CurrentUserPort {

    private final UserRepositoryPort userRepository;

    @Override
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Current user not found"));
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found");
        }

        return authentication.getName();
    }

    @Override
    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}
