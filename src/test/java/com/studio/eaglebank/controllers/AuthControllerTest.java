package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;
import com.studio.eaglebank.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.studio.eaglebank.testdata.AuthTestDataHelper.getUserAuthRequestData;
import static com.studio.eaglebank.testdata.AuthTestDataHelper.getUserAuthResponse;
import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    public MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private AuthService authServiceMock;

    @BeforeEach
    public void setUp() {
        AuthController unit = new AuthController(authServiceMock);
        mvc = MockMvcBuilders.standaloneSetup(unit)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldAuthenticateUser() throws Exception {

        // Given
        UserAuthRequest authRequest = getUserAuthRequestData("password", USER_ID);
        UserAuthResponse expected = getUserAuthResponse();

        when(authServiceMock.authenticateUser(authRequest)).thenReturn(expected);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @Test
    public void shouldNotAuthenticateUserMissingDetails() throws Exception {

        // Given
        UserAuthRequest authRequest = getUserAuthRequestData(null, USER_ID);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}