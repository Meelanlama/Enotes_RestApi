package com.milan.controller;

import com.milan.dto.LoginRequest;
import com.milan.dto.LoginResponse;
import com.milan.dto.UserDto;
import com.milan.endpoint.AuthEndpoint;
import com.milan.service.AuthService;
import com.milan.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    @Override
    public ResponseEntity<?> registerUser(UserDto userDto, HttpServletRequest request) throws Exception {
        String url = CommonUtil.getUrl(request);

        logger.info("Registering new user: email={}, url={}", userDto.getEmail(), url);

        Boolean register = authService.registerUser(userDto, url);

        if (register) {
            logger.info("User registration successful for email={}", userDto.getEmail());
            return CommonUtil.createBuildResponseMessage("Register success", HttpStatus.CREATED);
        }

        logger.error("User registration failed for email={}", userDto.getEmail());
        return CommonUtil.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) throws Exception {

        logger.info("Login attempt for email={}", loginRequest.getEmail());

        LoginResponse loginResponse = authService.login(loginRequest);

        if (ObjectUtils.isEmpty(loginResponse)) {
            logger.warn("Login failed: Invalid credentials for email={}", loginRequest.getEmail());
            return CommonUtil.createErrorResponseMessage("Invalid credential", HttpStatus.BAD_REQUEST);
        }

        logger.info("Login successful for email={}", loginRequest.getEmail());
        return CommonUtil.createBuildResponse(loginResponse, HttpStatus.OK);
    }

}

