package com.studio.eaglebank.controllers;

import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createANewUser(@Valid @RequestBody CreateUserRequest userRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createNewUser(userRequest));
    }

    @GetMapping("{userId}")
    public ResponseEntity<UserResponse> fetchUserDetails(HttpServletRequest request,
                                                         @PathVariable
                                                         @Pattern(regexp = "^usr-[A-Za-z0-9]+$",
                                                                 message = "userId must match pattern usr-[A-Za-z0-9]+")
                                                         String userId) {

        String authUser = (String) request.getAttribute("userId");
        return ResponseEntity.ok(userService.fetchUserDetails(authUser, userId));
    }
}
