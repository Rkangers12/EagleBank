package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.domain.entities.AddressEntity;
import com.studio.eaglebank.domain.entities.UserEntity;
import com.studio.eaglebank.domain.models.Address;
import com.studio.eaglebank.domain.repositories.UserRepository;
import com.studio.eaglebank.domain.requests.CreateUserRequest;
import com.studio.eaglebank.domain.responses.UserResponse;
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

import static com.studio.eaglebank.services.mappers.AddressMapperTest.getAddress;
import static com.studio.eaglebank.services.mappers.AddressMapperTest.getAddressEntity;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getCreateUserRequest;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getUserEntity;
import static com.studio.eaglebank.services.mappers.UserMapperTest.getUserResponse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.contentOf;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class UserControllerIT {

    public MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired UserController userController;

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
}
