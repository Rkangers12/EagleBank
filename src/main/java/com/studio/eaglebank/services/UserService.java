package com.studio.eaglebank.services;

import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;

public interface UserService {

    UserResponse createNewUser(CreateUserRequest userRequest);
}
