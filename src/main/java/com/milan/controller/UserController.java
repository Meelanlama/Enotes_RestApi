package com.milan.controller;

import com.milan.dto.PasswordChangeRequest;
import com.milan.dto.UserResponse;
import com.milan.model.User;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final ModelMapper mapper;
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        logger.info("Request received for fetching user profile.");

        try {
            User loggedInUser = CommonUtil.getLoggedInUser();
            UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);

            logger.info("Successfully retrieved profile for user: {}", loggedInUser.getEmail());
            return CommonUtil.createBuildResponse(userResponse, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while fetching user profile: {}", e.getMessage(), e);
            return CommonUtil.createErrorResponseMessage("Error fetching profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request) {
        logger.info("Request received to change password for user: {}");

        try {
            userService.changePassword(request);

            logger.info("Password changed successfully for user: {}");
            return CommonUtil.createBuildResponseMessage("Password changed successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error occurred while changing password for user {}: {}", e.getMessage(), e);
            return CommonUtil.createErrorResponseMessage("Error changing password", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
