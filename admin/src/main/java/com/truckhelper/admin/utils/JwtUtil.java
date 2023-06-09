package com.truckhelper.admin.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.truckhelper.core.models.AdminId;

@Component
public class JwtUtil {
    private final Algorithm algorithm;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String encode(AdminId adminId) {
        return JWT.create()
                .withClaim("adminId", adminId.toString())
                .sign(algorithm);
    }

    public AdminId decode(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT verify = verifier.verify(token);
        String value = verify.getClaim("adminId").asString();
        return new AdminId(value);
    }
}
