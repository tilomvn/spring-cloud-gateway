package com.spring.cloud.gateway.common;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AuthenticationServiceTests {

    @Mock
    JWTProvider jwtProvider;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void authorizationTest() {
        when(jwtProvider.verify(anyString())).thenReturn("CIF");
        AuthenticationService.AuthorizationResponse response =
                authenticationService.authorize("Bearer askdhiauyshdas.ASasiudy@13298dakjhkjhsd");
        assertEquals("CIF", response.getMessage());
        assertEquals(true, response.getAuthResult());
    }

    @Test
    void authorizationInvalidTest() {
        when(jwtProvider.verify(anyString())).thenThrow(JWTVerificationException.class);

        AuthenticationService.AuthorizationResponse response =
                authenticationService.authorize("Bearer askdhiauyshdas.ASasiudy@13298dakjhkjhsd");

        assertEquals("Invalid Token", response.getMessage());
        assertEquals(false, response.getAuthResult());
    }

    @Test
    void authorizationTokenExpiredTest() {
        when(jwtProvider.verify(anyString())).thenThrow(TokenExpiredException.class);

        AuthenticationService.AuthorizationResponse response =
                authenticationService.authorize("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJDSUYiLCJleHAiOjE2MzU5ODgzNjF9.aWHOmooIicOI4MAj6mjP4PXjIU17OA_VnE1ymH-m6dcdP6ZHTprzQuQYVWAATA2ExnNeSDYM4J5NT9aq_nV4YA");

        assertEquals("Token Expired", response.getMessage());
        assertEquals(false, response.getAuthResult());
    }
}
