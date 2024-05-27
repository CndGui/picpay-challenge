package com.guilhermerodrigues.picpaychallenge.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public class TokenService {
    @Value("${api.jwt.secret}")
    private String secret;
    public String generateToken(UserDetails user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                .withIssuer("auth")
                .withSubject(user.getUsername())
                .sign(algorithm);

            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("auth")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception) {
            throw new BadRequestException("Invalid token.");
        }
    }

    public String getToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Missing or invalid Authorization header.");
        }

        return authorizationHeader.replace("Bearer ", "");
    }
}
