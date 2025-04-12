package com.milan.service;

import com.milan.dto.LoginRequest;
import com.milan.dto.LoginResponse;
import com.milan.dto.UserDto;

public interface AuthService {

    Boolean registerUser(UserDto userDto,String url) throws Exception;

    public LoginResponse login(LoginRequest loginRequest);

}
