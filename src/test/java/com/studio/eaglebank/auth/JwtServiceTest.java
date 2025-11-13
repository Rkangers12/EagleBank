package com.studio.eaglebank.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;

import static com.studio.eaglebank.testdata.UserTestDataHelper.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {


    public static final String INSTANT_STRING_2007 = "2007-12-03T10:15:30.00Z";
    public static final String JWT_TOKEN_2007 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c3ItYWJjMTIzZWYiLCJpYXQiOjExOTY2NzY5MzAsImV4cCI6MTE5NjY4MDUzMH0.dWxwhYLqedV-wpjO0EcrSxKcif7l2n4_JcLAvzL3R8E";
    @Mock
    private Clock clockMock;

    private JwtService unit;

    @BeforeEach
    public void setUp() {
        unit = new JwtService(clockMock);
    }

    @Test
    void shouldGenerateToken() {

        // Given
        when(clockMock.instant()).thenReturn(Instant.parse(INSTANT_STRING_2007));

        // When
        String actual = unit.generateToken(USER_ID);

        // Then
        assertThat(actual).isEqualTo(JWT_TOKEN_2007);
    }

    @Test
    void shouldReturnTrueWhenTokenIsValid() {

        // Given
        when(clockMock.instant()).thenReturn(Instant.now());
        String jwtToken = unit.generateToken(USER_ID);

        // When
        boolean actual = unit.isTokenValid(jwtToken);

        // Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void shouldReturnFalseWhenTokenIsInValid() {

        // Given
        when(clockMock.instant()).thenReturn(Instant.parse(INSTANT_STRING_2007));
        String jwtToken = unit.generateToken(USER_ID);

        // When
        boolean actual = unit.isTokenValid(jwtToken);

        // Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    void shouldExtractUserId() {

        // Given
        when(clockMock.instant()).thenReturn(Instant.now());
        String jwtToken = unit.generateToken(USER_ID);

        // When
        String actual = unit.extractUserId(jwtToken);

        // Then
        assertThat(actual).isEqualTo(USER_ID);
    }
}