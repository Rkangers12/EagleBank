package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.auth.JwtService;
import com.studio.eaglebank.config.exceptions.ForbiddenResourceException;
import com.studio.eaglebank.config.exceptions.UnauthorisedException;
import com.studio.eaglebank.config.exceptions.UserNotFoundException;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;
import com.studio.eaglebank.services.AuthService;
import com.studio.eaglebank.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserAuthResponse authenticateUser(UserAuthRequest userAuthRequest) {

        try {
            UserEntity user = userService.fetchUser(userAuthRequest.userId());

            if (!passwordEncoder.matches(userAuthRequest.password(), user.getPassword())) {
                throw new ForbiddenResourceException("Unable to authorise user incorrect userID or password");
            }

            return new UserAuthResponse(user.getPublicId(), user.getEmail(),
                    jwtService.generateToken(user.getPublicId()));

        } catch (UserNotFoundException | ForbiddenResourceException ex) {
            throw new UnauthorisedException("Unable to authorise user incorrect userID or password");
        }
    }
}
