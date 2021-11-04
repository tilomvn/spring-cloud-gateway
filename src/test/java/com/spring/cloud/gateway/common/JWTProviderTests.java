package com.spring.cloud.gateway.common;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JWTProviderTests {

    @InjectMocks
    JWTProvider jwtProvider = new JWTProvider("Rfk45@#!FFfwef", 10000l, "Bearer");

    String token;

    @Test
    @Order(1)
    void generateTest() {
        token = jwtProvider.generate("CIF");
        assertTrue(token.matches("[a-zA-Z0-9_\\.\\_-]*"));
    }

    @Test
    @Order(2)
    void verifyTokenValidTest() {
        String userId = jwtProvider.verify(token);
        assertEquals("CIF", userId);
    }

    @Test
    @Order(3)
    void verifyTokenInvalidTest() {
        assertThrows(JWTVerificationException.class,() -> jwtProvider.verify("Bearer ashdkjahskdasd.asdkjhaiuydiuyw"));
    }

    @Test
    @Order(4)
    void verifyTokenExpiredTest() {
        assertThrows(TokenExpiredException.class, () -> jwtProvider.verify("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDSUYiLCJleHAiOjE2MzU5ODgzNjF9.aWHOmooIicOI4MAj6mjP4PXjIU17OA_VnE1ymH-m6dcdP6ZHTprzQuQYVWAATA2ExnNeSDYM4J5NT9aq_nV4YA"));
    }
}
