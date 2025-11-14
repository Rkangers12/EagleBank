package com.studio.eaglebank.services.mappers;

import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final AddressMapper addressMapper;
    private final PasswordEncoder passwordEncoder;

    public UserEntity mapRequestToEntity(CreateUserRequest request) {

        return UserEntity.builder()
                .publicId(generatePublicId())
                .name(request.name())
                .password(passwordEncoder.encode(request.password()))
                .address(addressMapper.mapAddressToAddressEntity(request.address()))
                .phoneNumber(request.phoneNumber())
                .email(request.email())
                .build();
    }


    private String generatePublicId() {
        return "usr-" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8);
    }

    public UserResponse mapEntityToResponse(UserEntity entity) {
        return new UserResponse(
                entity.getPublicId(),
                entity.getName(),
                addressMapper.mapAddressEntityToAddress(entity.getAddress()),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getCreatedTimestamp(),
                entity.getUpdatedTimestamp()
        );
    }
}
