package com.company.e_store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Ensure this is the correct Base64 encoded secret
    private final String secretKey = "nM4i4M/Hu+uWYq1F4z8boDUnE3clVTAHE+AV9dZQPBs=";

    private final long expiration = 1000 * 60 * 60; // 1 hour expiration

    // Decode the base64 key only once
    private byte[] getSigningKey() {
        return Base64.getDecoder().decode(secretKey);
    }

    // Generate token
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 1 hour expiry
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }


    // Extract email from the JWT token
    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    // Check if the token is still valid
    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            // Add more specific exception handling based on your needs
            System.out.println("Token validation failed: " + e.getMessage());
            return false;
        }
    }

    // Extract claims from the JWT token
    private Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Handle specific exception like ExpiredJwtException or MalformedJwtException if necessary
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}
