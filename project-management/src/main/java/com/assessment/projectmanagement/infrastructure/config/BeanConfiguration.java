package com.assessment.projectmanagement.infrastructure.config;

import com.assessment.projectmanagement.application.service.project.CreateProjectService;
import com.assessment.projectmanagement.domain.port.in.project.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.out.AuditLogPort;
import com.assessment.projectmanagement.domain.port.out.CurrentUserPort;
import com.assessment.projectmanagement.domain.port.out.NotificationPort;
import com.assessment.projectmanagement.domain.port.out.ProjectRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration for dependency injection
 * This is where we wire the hexagonal architecture together
 */
@Configuration
public class BeanConfiguration {

    /**
     * Project Use Cases
     */
    @Bean
    public CreateProjectUseCase createProjectUseCase(
            ProjectRepositoryPort projectRepository,
            CurrentUserPort currentUserPort,
            AuditLogPort auditLogPort,
            NotificationPort notificationPort) {
        return new CreateProjectService(
                projectRepository,
                currentUserPort,
                auditLogPort,
                notificationPort);
    }

    // TODO: Add more use case beans as you implement them
    // Example:
    // @Bean
    // public UpdateProjectUseCase updateProjectUseCase(...) {
    // return new UpdateProjectService(...);
    // }

    /**
     * Task Use Cases
     */
    // TODO: Add task use case beans

    /**
     * User Use Cases
     */
    // TODO: Add user use case beans
}
