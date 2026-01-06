package com.assessment.projectmanagement.application.service.user;

import com.assessment.projectmanagement.domain.model.User;
import com.assessment.projectmanagement.domain.port.in.user.GetAllUsersUseCase;
import com.assessment.projectmanagement.domain.port.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class GetAllUsersService implements GetAllUsersUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
