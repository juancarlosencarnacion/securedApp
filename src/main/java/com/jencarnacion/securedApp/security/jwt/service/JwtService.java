package com.jencarnacion.securedApp.security.jwt.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // private String secretKey = "Q2VxZ0tJZ0ZyZUxqRk1Yb3pNNU9zVnJQeE9aU0hCUnB4c0ZqT2lQb0R6cWZJbw==";

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(UserDetails user) {
    return Jwts.builder()
            .setClaims(new HashMap<>())
            .setSubject(user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() +  1000 * 60 * 60))
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            .compact();
}

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String email = extractEmail(token);
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignKey() {
        // byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // return Keys.hmacShaKeyFor(keyBytes);

        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("JWT secret is not configured");
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }
}
