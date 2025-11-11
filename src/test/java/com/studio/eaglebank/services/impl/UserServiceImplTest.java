package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.repositories.UserRepository;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.UserService;
import com.studio.eaglebank.services.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.studio.eaglebank.services.mappers.AddressMapperTest.getAddress;
import static com.studio.eaglebank.services.mappers.AddressMapperTest.getAddressEntity;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getCreateUserRequest;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getUserEntity;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getUserResponse;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private UserMapper userMapperMock;

    private UserService unit;

    @BeforeEach
    public void setUp() {
        unit = new UserServiceImpl(userMapperMock, userRepositoryMock);
    }

    @Test
    void createNewUser() {

        // Given
        Address address = getAddress();
        CreateUserRequest createUserRequest = getCreateUserRequest(address);

        AddressEntity addressEntity = getAddressEntity();
        UserEntity entity = getUserEntity(addressEntity);

        UserResponse expected = getUserResponse(address);

        when(userMapperMock.mapRequestToEntity(createUserRequest)).thenReturn(entity);
        when(userRepositoryMock.save(entity)).thenReturn(entity);
        when(userMapperMock.mapEntityToResponse(entity)).thenReturn(expected);

        // When
        UserResponse actual = unit.createNewUser(createUserRequest);

        // Then
        assertThat(actual).isEqualTo(expected);

    }
}