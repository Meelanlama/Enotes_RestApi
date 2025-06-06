package com.milan.service.impl;

import com.milan.exception.JwtTokenExpiredException;
import com.milan.model.User;
import com.milan.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public String generateToken(User user) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRoles());
        claims.put("status", user.getStatus().getIsActive());

        String token = Jwts.builder()
                .claims().add(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //10 hours
                .and()
                .signWith(getKey())
                .compact();

        return token;
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    public String role(String token)
    {
        Claims claims = extractAllClaims(token);
        String role=(String)claims.get("role");
        return role;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(decryptKey(secretKey))
                    .build().parseSignedClaims(token).getPayload();
        }
        catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException("Token is Expired");
        }catch (JwtException e) {
            throw new JwtTokenExpiredException("Invalid Jwt token");
        }catch (Exception e) {
            throw e;
        }
    }

    private SecretKey decryptKey(String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {

        String username = extractUsername(token);
        Boolean isExpired=isTokenExpired(token);
        if(username.equalsIgnoreCase(userDetails.getUsername()) && !isExpired)
        {
            return true;
        }
        return false;
    }

    private Boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        Date expiredDate = claims.getExpiration();
        return expiredDate.before(new Date());
    }
}
