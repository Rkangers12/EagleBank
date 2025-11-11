package com.studio.eaglebank.config.constants;

public record ErrorDetails(
        String field,
        String message,
        String type
) {}