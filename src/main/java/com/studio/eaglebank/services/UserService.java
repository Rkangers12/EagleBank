package com.studio.eaglebank.services;

import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;

public interface UserService {

    UserResponse createNewUser(CreateUserRequest userRequest);

    UserEntity fetchUser(String userId);

    UserResponse fetchUserDetails(String authUser, String userId);
}
