package com.milan.endpoint;

import com.milan.dto.PasswordResetRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/home")
@Tag(name = "HOME APIs", description = "All the Home APIs")
public interface HomeEndpoint {

    @Operation(summary = "Verification user account", description = "User Account verification by email after registering account",tags = {"Home"})
    @GetMapping("/verify")
    ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @Operation(summary = "Send Email for Password Reset", description = "User Can send their Email for password reset",tags = {"Home"})
    @GetMapping("/send-email-reset-link")
    ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception;

    @Operation(summary = "Verification password link",description = "User verifies the password link",tags = {"Home"})
    @GetMapping("/verify-link")
    ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code)
            throws Exception;

    @Operation(summary = "Reset Password", description = "User can now reset password",tags = {"Home"})
    @PostMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}