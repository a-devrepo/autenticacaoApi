package br.com.nca.helpers;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTHelper {
    
    @Value("${jwt.secretkey}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private String jwtExpiration;

    private Date now() {
        return new Date();
    }
    
    public Date getExpiration() {
        return new Date(now().getTime() + Long.parseLong(jwtExpiration));
    }
    
    public Date getCurrentDate() {
        return now();
    }
    
    public String getToken(UUID userID) {
        Date issuedAt = now();
        Date expiration = getExpiration();

        return Jwts.builder()
                .setSubject(userID.toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}