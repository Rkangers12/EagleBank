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
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddressEntity;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {

    @Mock
    private AddressMapper addressMapperMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private UserMapper unit;

    @BeforeEach
    public void setUp() {
        unit = new UserMapper(addressMapperMock, passwordEncoderMock);
    }

    @Test
    void shouldMapAllFieldsAndGeneratePublicId() {

        // Given
        Address address = getAddress();
        AddressEntity addressEntity = getAddressEntity();

        CreateUserRequest request = getCreateUserRequest(address);

        when(addressMapperMock.mapAddressToAddressEntity(address)).thenReturn(addressEntity);
        when(passwordEncoderMock.encode("password")).thenReturn("encoded password");

        // When
        UserEntity actual = unit.mapRequestToEntity(request);

        // Then
        assertThat(actual.getPublicId()).isNotNull();
        assertThat(actual.getPublicId()).startsWith("usr-");
        assertThat(actual.getName()).isEqualTo("Amelia Thompson");
        assertThat(actual.getPassword()).isEqualTo("encoded password");
        assertThat(actual.getPhoneNumber()).isEqualTo("+447912345678");
        assertThat(actual.getEmail()).isEqualTo("amelia.thompson@example.com");
        assertThat(actual.getAddress()).isEqualTo(addressEntity);
    }

    @Test
    void shouldMapEntityToUserResponse() {

        // Given
        AddressEntity addressEntity = getAddressEntity();
        Address address = getAddress();

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
}