package com.milan.controller;

import com.milan.dto.PasswordChangeRequest;
import com.milan.dto.UserResponse;
import com.milan.model.User;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final ModelMapper mapper;

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile()
    {
        User loggedInUser = CommonUtil.getLoggedInUser();
        UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);
        return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
    }

    @GetMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request)
    {
        userService.changePassword(request);
        return CommonUtil.createBuildResponseMessage("Password changed successfully", HttpStatus.OK);
    }

}
