package com.studio.eaglebank.domain.requests;

import com.studio.eaglebank.domain.models.Address;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(

        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be at least 8 characters")
        String password,

        @Valid
        Address address,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^(\\+44\\s?7\\d{3}|07\\d{3})\\s?\\d{3}\\s?\\d{3}$",
                message = "Phone number must be a valid UK mobile number format"
        )
        String phoneNumber,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        String email
) {}