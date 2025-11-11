package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.config.exceptions.UserNotFoundException;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.repositories.UserRepository;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.UserService;
import com.studio.eaglebank.services.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse createNewUser(CreateUserRequest userRequest) {

        UserEntity userEntity = userMapper.mapRequestToEntity(userRequest);
        UserEntity newUser = userRepository.save(userEntity);
        return userMapper.mapEntityToResponse(newUser);
    }

    @Override
    public UserEntity fetchUser(String userId) {

        Optional<UserEntity> user = userRepository.findByPublicId(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User does not exist");
        }

        return user.get();
    }
}
