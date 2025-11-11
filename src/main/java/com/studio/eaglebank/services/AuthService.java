package com.studio.eaglebank.services;

import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;

public interface AuthService {

    UserAuthResponse authenticateUser(UserAuthRequest userAuthRequest);
}
