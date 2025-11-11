package com.studio.eaglebank.controllers;

import com.studio.eaglebank.auth.JwtService;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createANewUser(@Valid @RequestBody CreateUserRequest userRequest) {

        return ResponseEntity.ok(userService.createNewUser(userRequest));
    }
}
