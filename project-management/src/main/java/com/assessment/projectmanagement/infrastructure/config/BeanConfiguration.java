package com.assessment.projectmanagement.infrastructure.config;

import com.assessment.projectmanagement.application.service.project.ActivateProjectService;
import com.assessment.projectmanagement.application.service.project.CreateProjectService;
import com.assessment.projectmanagement.application.service.project.GetProjectService;
import com.assessment.projectmanagement.application.service.task.CompleteTaskService;
import com.assessment.projectmanagement.application.service.task.CreateTaskService;
import com.assessment.projectmanagement.application.service.task.GetProjectTasksService;
import com.assessment.projectmanagement.application.service.user.AuthenticateUserService;
import com.assessment.projectmanagement.application.service.user.RegisterUserService;
import com.assessment.projectmanagement.application.service.user.GetAllUsersService;
import com.assessment.projectmanagement.application.service.project.InviteMemberService;
import com.assessment.projectmanagement.application.service.project.GetProjectMembersService;
import com.assessment.projectmanagement.application.service.project.GetProjectAuditLogsService;
import com.assessment.projectmanagement.domain.port.in.project.ActivateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.project.CreateProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.task.CompleteTaskUseCase;
import com.assessment.projectmanagement.domain.port.in.task.CreateTaskUseCase;
import com.assessment.projectmanagement.domain.port.in.task.GetTasksByProjectUseCase;
import com.assessment.projectmanagement.domain.port.in.user.AuthenticateUserUseCase;
import com.assessment.projectmanagement.domain.port.in.user.RegisterUserUseCase;
import com.assessment.projectmanagement.domain.port.in.user.GetAllUsersUseCase;
import com.assessment.projectmanagement.domain.port.in.project.InviteMemberUseCase;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectMembersUseCase;
import com.assessment.projectmanagement.domain.port.in.project.GetProjectAuditLogsUseCase;
import com.assessment.projectmanagement.domain.port.in.task.UpdateTaskUseCase;
import com.assessment.projectmanagement.application.service.task.UpdateTaskService;
import com.assessment.projectmanagement.domain.port.out.*;
import com.assessment.projectmanagement.infrastructure.adapter.out.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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

        @Bean
        public ActivateProjectUseCase activateProjectUseCase(
                        ProjectRepositoryPort projectRepository,
                        TaskRepositoryPort taskRepository,
                        CurrentUserPort currentUserPort,
                        AuditLogPort auditLogPort,
                        NotificationPort notificationPort) {
                return new ActivateProjectService(
                                projectRepository,
                                taskRepository,
                                currentUserPort,
                                auditLogPort,
                                notificationPort);
        }

        @Bean
        public GetProjectUseCase getProjectUseCase(
                        ProjectRepositoryPort projectRepository,
                        CurrentUserPort currentUserPort) {
                return new GetProjectService(
                                projectRepository,
                                currentUserPort);
        }

        @Bean
        public InviteMemberUseCase inviteMemberUseCase(
                        ProjectRepositoryPort projectRepository,
                        UserRepositoryPort userRepository,
                        CurrentUserPort currentUserPort,
                        AuditLogPort auditLogPort) {
                return new InviteMemberService(
                                projectRepository,
                                userRepository,
                                currentUserPort,
                                auditLogPort);
        }

        @Bean
        public GetProjectMembersUseCase getProjectMembersUseCase(
                        ProjectRepositoryPort projectRepository) {
                return new GetProjectMembersService(projectRepository);
        }

        @Bean
        public GetProjectAuditLogsUseCase getProjectAuditLogsUseCase(
                        AuditLogPort auditLogPort,
                        TaskRepositoryPort taskRepository) {
                return new GetProjectAuditLogsService(auditLogPort, taskRepository);
        }

        /**
         * Task Use Cases
         */
        @Bean
        public CreateTaskUseCase createTaskUseCase(
                        TaskRepositoryPort taskRepository,
                        ProjectRepositoryPort projectRepository,
                        UserRepositoryPort userRepository,
                        CurrentUserPort currentUserPort,
                        AuditLogPort auditLogPort) {
                return new CreateTaskService(
                                taskRepository,
                                projectRepository,
                                userRepository,
                                currentUserPort,
                                auditLogPort);
        }

        @Bean
        public CompleteTaskUseCase completeTaskUseCase(
                        TaskRepositoryPort taskRepository,
                        CurrentUserPort currentUserPort,
                        AuditLogPort auditLogPort,
                        NotificationPort notificationPort) {
                return new CompleteTaskService(
                                taskRepository,
                                currentUserPort,
                                auditLogPort,
                                notificationPort);
        }

        @Bean
        public GetTasksByProjectUseCase getTasksByProjectUseCase(
                        TaskRepositoryPort taskRepository,
                        ProjectRepositoryPort projectRepository,
                        CurrentUserPort currentUserPort) {
                return new GetProjectTasksService(
                                taskRepository,
                                projectRepository,
                                currentUserPort);
        }

        @Bean
        public UpdateTaskUseCase updateTaskUseCase(
                        TaskRepositoryPort taskRepository,
                        CurrentUserPort currentUserPort,
                        UserRepositoryPort userRepository,
                        AuditLogPort auditLogPort) {
                return new UpdateTaskService(
                                taskRepository,
                                currentUserPort,
                                userRepository,
                                auditLogPort);
        }

        /**
         * User Use Cases
         */
        @Bean
        public RegisterUserUseCase registerUserUseCase(
                        UserRepositoryPort userRepository,
                        PasswordEncoder passwordEncoder) {
                return new RegisterUserService(
                                userRepository,
                                passwordEncoder);
        }

        @Bean
        public AuthenticateUserUseCase authenticateUserUseCase(
                        AuthenticationManager authenticationManager,
                        JwtTokenProvider tokenProvider) {
                return new AuthenticateUserService(
                                authenticationManager,
                                tokenProvider);
        }

        @Bean
        public GetAllUsersUseCase getAllUsersUseCase(UserRepositoryPort userRepository) {
                return new GetAllUsersService(userRepository);
        }
}
