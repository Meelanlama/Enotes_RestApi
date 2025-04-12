package com.milan.service;

import com.milan.model.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    public String generateToken(User user);

    public String extractUsername(String token);

    public Boolean validateToken(String token, UserDetails userDetails);
}
