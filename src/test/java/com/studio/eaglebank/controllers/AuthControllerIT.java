package com.studio.eaglebank.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studio.eaglebank.config.GlobalExceptionHandler;
import com.studio.eaglebank.config.constants.ErrorObject;
import com.studio.eaglebank.domain.repositories.UserRepository;
import com.studio.eaglebank.domain.requests.UserAuthRequest;
import com.studio.eaglebank.domain.responses.UserAuthResponse;
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

import static com.studio.eaglebank.testdata.AuthTestDataHelper.getUserAuthRequestData;
import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static com.studio.eaglebank.testdata.UserTestDataHelper.getUserEntityIT;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class AuthControllerIT {

    public static final String UNABLE_TO_AUTHORISE_MSG = "Unable to authorise user incorrect userID or password";
    public static final String REQUEST_MISSING_DATA_MSG = "The request didn't supply all the necessary data";
    public static String ENCODED_PASSWORD = "$2a$10$o0ULgL2ikxZ2P5CRpyqya.fBu2XqSatgQ.Md8t.wzlLnH5Bv5.9xu";

    public MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public AuthController authController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }


    @Test
    public void shouldAuthenticateUser() throws Exception {

        // Given
        String userId = "usr-generic1";
        userRepository.save(getUserEntityIT(userId, ENCODED_PASSWORD, "generic@example.com"));
        UserAuthRequest authRequest = getUserAuthRequestData("encoded password", userId);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andReturn()
                .getResponse();

        UserAuthResponse actual = objectMapper.readValue(response.getContentAsString(), UserAuthResponse.class);

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(actual.userId()).isEqualTo(authRequest.userId());
    }

    @Test
    public void shouldNotAuthenticateUserIncorrectPassword() throws Exception {

        // Given
        userRepository.save(getUserEntityIT(USER_ID, ENCODED_PASSWORD, "generic2@example.com"));
        UserAuthRequest authRequest = getUserAuthRequestData("incorrect password", USER_ID);

        // When
        mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(UNABLE_TO_AUTHORISE_MSG));
    }

    @Test
    public void shouldNotAuthenticateUserIncorrectUser() throws Exception {

        // Given
        UserAuthRequest authRequest = getUserAuthRequestData("encoded password", "usr-unknown");

        // When
        mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(UNABLE_TO_AUTHORISE_MSG));
    }

    @Test
    public void shouldNotAuthenticateUserMissingDetails() throws Exception {

        // Given
        UserAuthRequest authRequest = getUserAuthRequestData(null, null);

        // When
        MockHttpServletResponse response = mvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        ErrorObject errorObject = objectMapper.readValue(response.getContentAsString(), ErrorObject.class);

        assertThat(errorObject.message()).isEqualTo(REQUEST_MISSING_DATA_MSG);
    }
}
