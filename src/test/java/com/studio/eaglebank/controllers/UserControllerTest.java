package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.config.exceptions.DuplicateResourceException;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
import com.studio.eaglebank.services.UserService;
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

import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequestBadRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    public MockMvc mvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userServiceMock;

    @BeforeEach
    public void setUp() {
        UserController unit = new UserController(userServiceMock);
        mvc = MockMvcBuilders.standaloneSetup(unit)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void shouldCreateANewUser() throws Exception {

        // Given
        Address address = getAddress();
        CreateUserRequest createUserRequest = getCreateUserRequest(address);

        UserResponse expected = getUserResponse(address);

        when(userServiceMock.createNewUser(createUserRequest)).thenReturn(expected);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andReturn()
                .getResponse();


        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(expected));
    }

    @Test
    public void shouldNotCreateNewUserEmailClash() throws Exception {

        // Given
        CreateUserRequest userRequest = getCreateUserRequest(getAddress());

        when(userServiceMock.createNewUser(userRequest))
                .thenThrow(new DuplicateResourceException("This email is already taken"));

        // When
        mvc.perform(post("v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("This email is already taken"));
    }

    @Test
    public void shouldFailToCreateUserBadRequest() throws Exception {

        // Given
        CreateUserRequest badRequest = getCreateUserRequestBadRequest();

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}