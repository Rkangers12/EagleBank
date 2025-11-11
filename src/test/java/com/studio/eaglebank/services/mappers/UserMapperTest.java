package com.studio.eaglebank.services.mappers;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock
    private AddressMapper addressMapperMock;

    private UserMapper unit;

    @BeforeEach
    public void setUp() {
        unit = new UserMapper(addressMapperMock);
    }

    @Test
    void mapRequestToEntity_shouldMapAllFieldsAndGeneratePublicId() {

        // Given
        Address address = AddressMapperTest.getAddress();
        AddressEntity addressEntity = AddressMapperTest.getAddressEntity();

        CreateUserRequest request = getCreateUserRequest(address);

        when(addressMapperMock.mapAddressToAddressEntity(address)).thenReturn(addressEntity);

        // When
        UserEntity actual = unit.mapRequestToEntity(request);

        // Then
        assertThat(actual.getPublicId()).isNotNull();
        assertThat(actual.getPublicId()).startsWith("usr-");
        assertThat(actual.getName()).isEqualTo("Amelia Thompson");
        assertThat(actual.getPhoneNumber()).isEqualTo("+447912345678");
        assertThat(actual.getEmail()).isEqualTo("amelia.thompson@example.com");
        assertThat(actual.getAddress()).isEqualTo(addressEntity);
    }


    @Test
    void mapEntityToResponse_shouldMapEntityToUserResponse() {

        // Given
        AddressEntity addressEntity = AddressMapperTest.getAddressEntity();
        Address address = AddressMapperTest.getAddress();

        UserEntity entity = getUserEntity(addressEntity);

        when(addressMapperMock.mapAddressEntityToAddress(addressEntity)).thenReturn(address);

        // When
        UserResponse actual = unit.mapEntityToResponse(entity);

        // Then
        assertThat(actual.id()).isEqualTo("usr-abc123ef");
        assertThat(actual.name()).isEqualTo("Amelia Thompson");
        assertThat(actual.phoneNumber()).isEqualTo("+447912345678");
        assertThat(actual.email()).isEqualTo("amelia.thompson@example.com");
        assertThat(actual.address()).isEqualTo(address);
    }

    public static UserEntity getUserEntity(AddressEntity addressEntity) {
        return UserEntity.builder()
                .publicId("usr-abc123ef")
                .name("Amelia Thompson")
                .address(addressEntity)
                .phoneNumber("+447912345678")
                .email("amelia.thompson@example.com")
                .build();
    }

    public static CreateUserRequest getCreateUserRequest(Address address) {
        return new CreateUserRequest(
                "Amelia Thompson",
                address,
                "+447912345678",
                "amelia.thompson@example.com"
        );
    }
}