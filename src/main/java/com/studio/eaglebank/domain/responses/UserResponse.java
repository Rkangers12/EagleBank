package com.studio.eaglebank.domain.responses;

import com.studio.eaglebank.domain.models.Address;

public record UserResponse(
        String id,
        String name,
        Address address,
        String phoneNumber,
        String email
) {}