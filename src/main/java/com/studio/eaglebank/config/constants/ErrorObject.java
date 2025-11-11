package com.studio.eaglebank.config.constants;

import java.util.List;

public record ErrorObject(
        String message,
        List<ErrorDetails> details
) {}