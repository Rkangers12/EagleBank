package com.studio.eaglebank.domain.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record Address(

        @NotBlank(message = "Address line 1 is required")
        @Size(min = 3, max = 100, message = "Address line 1 must be between 3 and 100 characters")
        String line1,

        @Size(max = 100, message = "Address line 2 must not exceed 100 characters")
        String line2,

        @Size(max = 100, message = "Address line 3 must not exceed 100 characters")
        String line3,

        @NotBlank(message = "Town is required")
        @Size(min = 2, max = 50, message = "Town must be between 2 and 50 characters")
        String town,

        @NotBlank(message = "County is required")
        @Size(min = 2, max = 50, message = "County must be between 2 and 50 characters")
        String county,

        @NotBlank(message = "Postcode is required")
        @Pattern(
                regexp = "^[A-Z]{1,2}[0-9][0-9A-Z]?\\s?[0-9][A-Z]{2}$",
                message = "Postcode must be a valid UK postcode format"
        )
        String postcode
) { }