package com.studio.eaglebank.services;

import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;

import java.util.Optional;

public interface UserService {

    UserResponse createNewUser(CreateUserRequest userRequest);

    Optional<UserEntity> fetchUser(String userId);
}
