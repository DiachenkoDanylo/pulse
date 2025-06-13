package com.diachenko.dev_pulse_jira_service.config.security;
/*  Dev_Pulse
    14.05.2025
    @author DiachenkoDanylo
*/

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String ACCESS_KEY;

    @Value("${security.jwt.refresh-key}")
    private String REFRESH_KEY;

    @Value("${security.jwt.expiration-access}")
    private Long ACCESS_EXPIRATION;

    @Value("${security.jwt.expiration-refresh}")
    private Long REFRESH_EXPIRATION;

    public String generateToken(UserDetails userDetails, TokenType type) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(type.equals(TokenType.ACCESS) ?
                        new Date(System.currentTimeMillis() + ACCESS_EXPIRATION) :
                        new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(getSigningKey(type), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token, TokenType type) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails, TokenType type) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, type);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }

    private Key getSigningKey(TokenType type) {
        if (type.equals(TokenType.ACCESS)) {
            byte[] keyBytes = ACCESS_KEY.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }
        byte[] keyBytes = REFRESH_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public enum TokenType {
        ACCESS, REFRESH
    }

}
