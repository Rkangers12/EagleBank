package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Override
    public UserResponse authenticateUser(UserAuthRequest userAuthRequest) {
        return null;
    }
}
