package com.milan.controller;

import com.milan.dto.LoginRequest;
import com.milan.dto.LoginResponse;
import com.milan.dto.UserDto;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception {

        //get url
        String url = CommonUtil.getUrl(request);

        Boolean register = userService.registerUser(userDto,url);
        if (register) {
            return CommonUtil.createBuildResponseMessage("Register success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Register failed", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {

        LoginResponse loginResponse = userService.login(loginRequest);
        if (ObjectUtils.isEmpty(loginResponse)) {
            return CommonUtil.createErrorResponseMessage("Invalid credential", HttpStatus.BAD_REQUEST);
        }
        return CommonUtil.createBuildResponse(loginResponse,HttpStatus.OK);
    }

}
