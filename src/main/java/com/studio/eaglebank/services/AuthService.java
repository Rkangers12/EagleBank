package com.studio.eaglebank.services;

import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserResponse;

public interface AuthService {

    UserResponse authenticateUser(UserAuthRequest userAuthRequest);
}
