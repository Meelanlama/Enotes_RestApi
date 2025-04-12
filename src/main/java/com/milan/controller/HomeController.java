package com.milan.controller;

import com.milan.dto.PasswordResetRequest;
import com.milan.exception.ResourceNotFoundException;
import com.milan.service.HomeService;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    private final UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String verificationCode) throws ResourceNotFoundException {

        Boolean success = homeService.verifyAccount(userId, verificationCode);

        if (success) {
          return CommonUtil.createBuildResponse("Account verification successful", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/send-email-reset-link")
    public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception {
        userService.sendEmailPasswordReset(email, request);
        return CommonUtil.createBuildResponseMessage("Email sent. Check Email For Password Reset", HttpStatus.OK);
    }

    @GetMapping("/verify-link")
    public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer userId, @RequestParam String token)
            throws Exception {
        userService.verifyPasswordResetLink(userId, token);
        return CommonUtil.createBuildResponseMessage("Verification successful", HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception {
        userService.resetPassword(passwordResetRequest);
        return CommonUtil.createBuildResponseMessage("Password reset success", HttpStatus.OK);
    }

}
