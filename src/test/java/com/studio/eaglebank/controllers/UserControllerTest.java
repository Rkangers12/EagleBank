package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.config.constants.ErrorResponse;
import com.studio.eaglebank.config.exceptions.DuplicateResourceException;
import com.studio.eaglebank.config.exceptions.ForbiddenResourceException;
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

import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequestBadRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    }

    @Test
    public void shouldNotCreateNewUserEmailClash() throws Exception {

        // Given
        CreateUserRequest userRequest = getCreateUserRequest(getAddress());

        when(userServiceMock.createNewUser(userRequest))
                .thenThrow(new DuplicateResourceException("This email is already taken"));

        // When
        mvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string(objectMapper.writeValueAsString(new ErrorResponse("This email is already taken"))));
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

    @Test
    public void shouldFetchUserDetailsWhenAuthorised() throws Exception {

        // Given
        Address address = getAddress();
        UserResponse expected = getUserResponse(address);

        when(userServiceMock.fetchUserDetails(USER_ID, USER_ID)).thenReturn(expected);

        // When / Then
        mvc.perform(get("/v1/users/{userId}", USER_ID)
                        .requestAttr("userId", USER_ID)) // set by JwtAuthFilter in real life
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    @Test
    public void shouldNotFetchUserDetailsWhenForbidden() throws Exception {

        // Given
        String authUser = "usr-different-user";

        when(userServiceMock.fetchUserDetails(authUser, USER_ID))
                .thenThrow(new ForbiddenResourceException("The user is not allowed to access the transaction"));

        // When / Then
        mvc.perform(get("/v1/users/{userId}", USER_ID)
                        .requestAttr("userId", authUser))
                .andExpect(status().isForbidden())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(
                                new ErrorResponse("The user is not allowed to access the transaction")
                        )
                ));
    }

    @Test
    public void shouldFailToFetchUserDetailsBadRequestWhenUserIdInvalid() throws Exception {

        // Given
        String invalidUserId = "abc"; //

        // When
        MockHttpServletResponse response = mvc.perform(get("/v1/users/{userId}", invalidUserId)
                        .requestAttr("userId", invalidUserId))
                .andReturn()
                .getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}