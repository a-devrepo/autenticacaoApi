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

    private Date currentDate = new Date();
    
    public Date getExpiration() {
        return new Date(currentDate.getTime() + Integer.parseInt(jwtExpiration));
    }
    
    public Date getCurrentDate() {
        return currentDate;
    }
    
    public String getToken(UUID userID) {
        
        return Jwts.builder()
                .setSubject(userID.toString())
                .setIssuedAt(currentDate)
                .setExpiration(getExpiration())
                .signWith(SignatureAlgorithm.HS256,jwtSecret.getBytes(StandardCharsets.UTF_8))
                .compact();
    }
}