package com.truckhelper.application.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.truckhelper.core.models.UserId;

class JwtUtilTest {
    private static final String SECRET = "SECRET";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void encodeAndDecode() {
        UserId original = new UserId("1234");

        String token = jwtUtil.encode(original);

        assertThat(token).contains(".");

        UserId userId = jwtUtil.decode(token);

        assertThat(userId).isEqualTo(original);
    }

    @Test
    void decodeError() {
        assertThrows(JWTDecodeException.class, () -> {
            jwtUtil.decode("xxx");
        });
    }
}
