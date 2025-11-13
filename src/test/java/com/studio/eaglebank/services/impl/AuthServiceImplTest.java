package com.studio.eaglebank.services.impl;

import com.studio.eaglebank.auth.JwtService;
import com.studio.eaglebank.config.exceptions.UnauthorisedException;
import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;
import com.studio.eaglebank.services.AuthService;
import com.studio.eaglebank.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddressEntity;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserAuthResponse;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    public static final String UNAUTHORISED_USER_ERROR_MESSAGE = "Unable to authorise user incorrect userID or password";

    @Mock
    private JwtService jwtServiceMock;

    @Mock
    private UserService userServiceMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private AuthService unit;

    @BeforeEach
    public void setUp() {
        unit = new AuthServiceImpl(jwtServiceMock, userServiceMock, passwordEncoderMock);
    }

    @Test
    public void shouldSuccessfullyAuthenticateUser() {

        // Given
        UserAuthRequest userAuthRequest = new UserAuthRequest(USER_ID, "password");

        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);

        UserAuthResponse expected = getUserAuthResponse();

        when(userServiceMock.fetchUser(USER_ID))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoderMock.matches(userAuthRequest.password(), userEntity.getPassword()))
                .thenReturn(true);
        when(jwtServiceMock.generateToken(userEntity.getPublicId())).thenReturn("jwtToken-random-token");

        // When
        UserAuthResponse actual = unit.authenticateUser(userAuthRequest);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldFailToAuthenticateUserUnableToFindUser() {

        // Given
        UserAuthRequest userAuthRequest = new UserAuthRequest(USER_ID, "password");
        
        when(userServiceMock.fetchUser(USER_ID))
                .thenThrow(new UnauthorisedException(UNAUTHORISED_USER_ERROR_MESSAGE));

        // When
        UnauthorisedException ex = assertThrows(UnauthorisedException.class,
                () -> unit.authenticateUser(userAuthRequest));

        // Then
        assertThat(ex.getMessage()).isEqualTo(UNAUTHORISED_USER_ERROR_MESSAGE);
    }
    
    @Test
    public void shouldFailToAuthenticateUserIncorrectPassword() {

        // Given
        UserAuthRequest userAuthRequest = new UserAuthRequest(USER_ID, "password");

        AddressEntity addressEntity = getAddressEntity();
        UserEntity userEntity = getUserEntity(addressEntity);


        when(userServiceMock.fetchUser(USER_ID))
                .thenReturn(Optional.of(userEntity));
        when(passwordEncoderMock.matches(userAuthRequest.password(), userEntity.getPassword()))
                .thenReturn(false);

        // When
        UnauthorisedException ex = assertThrows(UnauthorisedException.class,
                () -> unit.authenticateUser(userAuthRequest));

        // Then
        assertThat(ex.getMessage()).isEqualTo(UNAUTHORISED_USER_ERROR_MESSAGE);
    }
}