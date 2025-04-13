package com.milan.controller;

import com.milan.dto.PasswordResetRequest;
import com.milan.endpoint.HomeEndpoint;
import com.milan.exception.ResourceNotFoundException;
import com.milan.service.HomeService;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController implements HomeEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final HomeService homeService;
    private final UserService userService;

    @Override
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String verificationCode)
            throws ResourceNotFoundException {

        logger.info("Verifying user account for userId={} with code={}", userId, verificationCode);
        Boolean success = homeService.verifyAccount(userId, verificationCode);

        if (success) {
            logger.info("Account verified successfully for userId={}", userId);
            return CommonUtil.createBuildResponse("Account verification successful", HttpStatus.OK);
        }

        logger.warn("Account verification failed for userId={}", userId);
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception {

        logger.info("Request received to send password reset link to email={}", email);
        userService.sendEmailPasswordReset(email, request);
        logger.info("Password reset email sent to {}", email);
        return CommonUtil.createBuildResponseMessage("Email sent. Check Email For Password Reset", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String token)
            throws Exception {

        logger.info("Verifying password reset link for userId={} with token={}", userId, token);
        userService.verifyPasswordResetLink(userId, token);
        logger.info("Password reset link verified for userId={}", userId);
        return CommonUtil.createBuildResponseMessage("Verification successful", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {

        logger.info("Resetting password for userId={} (token={})", passwordResetRequest.getUid());
        userService.resetPassword(passwordResetRequest);
        logger.info("Password reset successfully for userId={}", passwordResetRequest.getUid());
        return CommonUtil.createBuildResponseMessage("Password reset success", HttpStatus.OK);
    }
}
