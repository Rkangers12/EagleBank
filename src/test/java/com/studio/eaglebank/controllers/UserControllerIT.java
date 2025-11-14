package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
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
import static com.studio.eaglebank.testdata.UserTestDataHelper.getAddress;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequest;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getCreateUserRequestIT;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntityIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
                .andExpect(content().string("This email is already taken"))
                .andReturn().getResponse();
    }
}
