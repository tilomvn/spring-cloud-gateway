package com.spring.cloud.gateway.common;

import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTProvider {

    final String SECRET;

    /**
     * Millisecond
     */
    final Long EXPIRE;

    final String TOKEN_PREFIX;

    JWTProvider(@Value("${security.jwt.secret}") String secret, @Value("${security.jwt.expire}") Long expire,
                @Value("${security.jwt.prefix:Bearer}") String tokenPrefix) {
        this.SECRET = secret;
        this.EXPIRE = expire;
        this.TOKEN_PREFIX = tokenPrefix;
    }

    public String generate(String userId) {
        return JWT.create()
                .withSubject(userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRE))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    public String verify(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX + " ", ""))
                .getSubject();
    }
}
