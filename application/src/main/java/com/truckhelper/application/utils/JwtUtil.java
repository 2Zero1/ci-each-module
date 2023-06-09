package com.truckhelper.application.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.truckhelper.core.models.UserId;

@Component
public class JwtUtil {
    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String encode(UserId userId) {
        return JWT.create()
                .withClaim("userId", userId.toString())
                .sign(algorithm);
    }

    public UserId decode(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        String value = verify.getClaim("userId").asString();
        return new UserId(value);
    }
}
