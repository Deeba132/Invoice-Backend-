package com.deeba.botpersona.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

@Component
public class JWTUtil {
    
    @Value("${JWT_String}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}") // 24 hours
    private Long expiration;
    
    // ✅ Store username in SUBJECT (cleaner approach)
    public String generateToken(String userName) throws IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject(userName)  // ✅ Username in subject
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .withIssuer("Users")
                .sign(Algorithm.HMAC256(secret));
    }

    public String extractUsername(String token) throws JWTVerificationException {
        DecodedJWT jwt = verifyToken(token);
        return jwt.getSubject();  // ✅ Get username from subject
    }
    
    public boolean isTokenValid(String token, String username) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            String extractedUsername = decodedJWT.getSubject();  // ✅ Consistent!
            return extractedUsername.equals(username) && !isTokenExpired(decodedJWT);
        } catch (JWTVerificationException e) {
            System.err.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("Users")
                .build();
        return verifier.verify(token);
    }

    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        Date expiresAt = decodedJWT.getExpiresAt();
        return expiresAt != null && expiresAt.before(new Date());
    }
}