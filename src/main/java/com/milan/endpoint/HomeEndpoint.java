package com.milan.endpoint;

import com.milan.dto.PasswordResetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/api/v1/home")
public interface HomeEndpoint {

    @GetMapping("/verify")
    ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception;

    @GetMapping("/send-email-reset-link")
    ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
            throws Exception;

    @GetMapping("/verify-link")
    ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code)
            throws Exception;

    @PostMapping("/reset-password")
    ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) throws Exception;
}