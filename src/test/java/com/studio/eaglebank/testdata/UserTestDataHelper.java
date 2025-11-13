package com.studio.eaglebank.testdata;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;
import com.studio.eaglebank.domain.responses.UserResponse;

public class UserTestDataHelper {


    public static final String USER_ID = "usr-abc123ef";

    public static Address getAddress() {
        return new Address(
                "23 Maple Crescent",
                "Flat 4B",
                "",
                "Nottingham",
                "Nottinghamshire",
                "NG1 3EZ"
        );
    }

    public static AddressEntity getAddressEntity() {
        return AddressEntity.builder()
                .line1("23 Maple Crescent")
                .line2("Flat 4B")
                .line3("")
                .town("Nottingham")
                .county("Nottinghamshire")
                .postcode("NG1 3EZ")
                .build();
    }

    public static UserEntity getUserEntity(AddressEntity addressEntity) {
        return UserEntity.builder()
                .publicId(USER_ID)
                .name("Amelia Thompson")
                .password("encoded password")
                .address(addressEntity)
                .phoneNumber("+447912345678")
                .email("amelia.thompson@example.com")
                .build();
    }

    public static CreateUserRequest getCreateUserRequest(Address address) {
        return new CreateUserRequest(
                "Amelia Thompson",
                "password",
                address,
                "+447912345678",
                "amelia.thompson@example.com"
        );
    }

    public static CreateUserRequest getCreateUserRequestBadRequest() {
        return new CreateUserRequest(
                null,
                null,
                getAddress(),
                "+447912345678",
                "amelia.thompson@example.com"
        );
    }

    public static UserResponse getUserResponse(Address address) {
        return new UserResponse(
                USER_ID,
                "Amelia Thompson",
                address,
                "+447912345678",
                "amelia.thompson@example.com"
        );
    }

    public static UserAuthResponse getUserAuthResponse() {
        return new UserAuthResponse(USER_ID, "amelia.thompson@example.com", "jwtToken-random-token");
    }
}
