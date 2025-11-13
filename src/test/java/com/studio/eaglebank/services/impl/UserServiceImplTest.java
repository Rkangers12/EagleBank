package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.config.exceptions.UserNotFoundException;
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

import java.util.Optional;

import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddressEntity;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntity;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserResponse;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    public void createNewUser() {

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

    @Test
    public void shouldFetchUser() {

        // Given
        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);

        String userId = USER_ID;
        when(userRepositoryMock.findByPublicId(userId)).thenReturn(Optional.of(userEntity));

        // Then
        UserEntity actual = unit.fetchUser(userId);

        // When
        assertThat(actual).isEqualTo(userEntity);
    }

    @Test
    public void shouldNotFetchUserDoesNotExist() {

        // Given
        String userId = "usr-xxxxxxx";
        when(userRepositoryMock.findByPublicId(userId)).thenReturn(Optional.empty());

        // Then
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> unit.fetchUser(userId));
        assertThat(ex.getMessage()).isEqualTo("User does not exist");
    }
}