package com.milan.endpoint;

import com.milan.dto.PasswordChangeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/user")
public interface UserEndpoint {

    @GetMapping("/profile")
    ResponseEntity<?> getProfile();

    @GetMapping("/change-password")
    ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request);
}
