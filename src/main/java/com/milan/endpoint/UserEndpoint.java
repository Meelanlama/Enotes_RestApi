package com.milan.endpoint;

import com.milan.dto.PasswordChangeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "User", description = "Authentication User Operation APIs")
@RequestMapping("/api/v1/user")
public interface UserEndpoint {

    @Operation(summary = "Get logged in User Profile", description = "Get User Profile")
    @GetMapping("/profile")
    ResponseEntity<?> getProfile();

    @Operation(summary = "CHANGE PASSWORD OF USER ACCOUNT")
    @GetMapping("/change-password")
    ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request);
}
