package com.spring.cloud.gateway.common;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    final JWTProvider jwtProvider;

    AuthenticationService(JWTProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public AuthorizationResponse authorize(String jwtToken) {
        String userId;
        try {
            userId = jwtProvider.verify(jwtToken);
        } catch (TokenExpiredException e) {
            return new AuthorizationResponse(false, "Token Expired");
        } catch (JWTVerificationException e) {
            return new AuthorizationResponse(false, "Invalid Token");
        }
        return new AuthorizationResponse(true, userId);
    }

    public static class AuthorizationResponse {
        String message;
        boolean authResult;

        public AuthorizationResponse(boolean authResult, String message) {
            this.authResult = authResult;
            this.message = message;
        }

        public boolean getAuthResult() { return authResult; }
        public String getMessage() { return message; }
    }
}
