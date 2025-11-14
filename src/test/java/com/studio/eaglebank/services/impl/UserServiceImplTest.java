package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.config.exceptions.DuplicateResourceException;
import com.studio.eaglebank.config.exceptions.ForbiddenResourceException;
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
import org.springframework.dao.DataIntegrityViolationException;

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
    public void shouldCreateNewUser() {

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
    public void shouldNotCreateNewUserAlreadyTaken() {

        // Given
        CreateUserRequest userRequest = getCreateUserRequest(getAddress());
        UserEntity existingUser = getUserEntity(getAddressEntity());

        when(userMapperMock.mapRequestToEntity(userRequest)).thenReturn(existingUser);
        when(userRepositoryMock.save(existingUser))
                .thenThrow(new DataIntegrityViolationException("violated key constraint"));

        // When - Then
        DuplicateResourceException ex = assertThrows(DuplicateResourceException.class,
                () -> unit.createNewUser(userRequest));
        assertThat(ex.getMessage()).isEqualTo("This email is already taken");
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

    @Test
    public void shouldFetchUserDetailsWhenAuthorised() {

        // Given
        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);
        UserResponse expected = getUserResponse(getAddress());

        when(userRepositoryMock.findByPublicId(USER_ID)).thenReturn(Optional.of(userEntity));
        when(userMapperMock.mapEntityToResponse(userEntity)).thenReturn(expected);

        // When
        UserResponse actual = unit.fetchUserDetails(USER_ID, USER_ID);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldThrowForbiddenWhenAuthUserDiffersFromRequestedUser() {

        // Given
        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);

        when(userRepositoryMock.findByPublicId(USER_ID)).thenReturn(Optional.of(userEntity));

        // When - Then
        ForbiddenResourceException ex = assertThrows(
                ForbiddenResourceException.class,
                () -> unit.fetchUserDetails("usr-different-user", USER_ID)
        );

        assertThat(ex.getMessage())
                .isEqualTo("The user is not allowed to access the transaction");
    }

    @Test
    public void shouldThrowUserNotFoundWhenFetchingDetailsForNonExistingUser() {

        // Given
        when(userRepositoryMock.findByPublicId(USER_ID)).thenReturn(Optional.empty());

        // When - Then
        UserNotFoundException ex = assertThrows(
                UserNotFoundException.class,
                () -> unit.fetchUserDetails(USER_ID, USER_ID)
        );

        assertThat(ex.getMessage()).isEqualTo("User does not exist");
    }
}