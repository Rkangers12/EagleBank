package com.studio.eaglebank.domain.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserAuthRequest(

        @NotBlank(message = "UserId is required")
        @Size(min = 2, max = 50, message = "UserId must be between 2 and 50 characters")
        String userId,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
        String password
) {}
