package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.config.constants.ErrorResponse;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.repositories.UserRepository;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static com.studio.eaglebank.controllers.AuthControllerIT.ENCODED_PASSWORD;
import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_I2;
import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequestIT;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntityIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIT {

    public MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserController userController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void createANewUser() throws Exception {

        // Given
        Address address = getAddress();
        CreateUserRequest createUserRequest = getCreateUserRequest(address);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createUserRequest)))
                .andReturn()
                .getResponse();

        // Then
        Optional<UserEntity> savedUser = userRepository.findAll().stream().findFirst();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(savedUser.isPresent());
        assertThat(savedUser.get().getEmail().equals("amelia.thompson@example.com"));
    }

    @Test
    public void shouldNotCreateNewUserEmailClash() throws Exception {

        // Given
        String userId = "usr-generic2";
        userRepository.save(getUserEntityIT(userId, ENCODED_PASSWORD, "user.clash@email.com"));
        CreateUserRequest userRequest = getCreateUserRequestIT("user.clash@email.com");


        // When
        mvc.perform(post("/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string(objectMapper.writeValueAsString(new ErrorResponse("This email is already taken"))))
                .andReturn().getResponse();
    }
    @Test
    public void shouldFetchUserDetailsWhenAuthorised() throws Exception {

        // Given
        String email = "authorised.user@email.com";
        userRepository.save(getUserEntityIT(USER_I2, ENCODED_PASSWORD, email));

        // When / Then
        mvc.perform(get("/v1/users/{userId}", USER_I2)
                        .requestAttr("userId", USER_I2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_I2))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    public void shouldNotFetchUserDetailsWhenForbidden() throws Exception {

        // Given
        userRepository.save(getUserEntityIT(USER_ID, ENCODED_PASSWORD, "forbidden.user@email.com"));
        String authUser = "usr-someone-else";

        // When - Then
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
    public void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {

        // When - Then
        mvc.perform(get("/v1/users/{userId}", USER_ID)
                        .requestAttr("userId", USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        objectMapper.writeValueAsString(
                                new ErrorResponse("User does not exist"))
                ));
    }
}
