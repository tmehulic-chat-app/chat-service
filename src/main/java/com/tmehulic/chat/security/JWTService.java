package com.tmehulic.chat.security;

import com.tmehulic.chat.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

@Service
@AllArgsConstructor
public class JWTService {

    private JwtProperties jwtProperties;

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims =
                Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        return claimsResolver.apply(claims);
    }
}
