package com.milan.endpoint;

import com.milan.dto.LoginRequest;
import com.milan.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "AUTHENTICATION", description = "API for register & login users ")
@RequestMapping("/api/v1/auth")
public interface AuthEndpoint {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",description = "Register successful"),
            @ApiResponse(responseCode = "500",description = "Internal server error"),
            @ApiResponse(responseCode = "400",description = "Bad Request")})
    @Operation(summary = "Register User API", tags = {"Auth,Home"})
    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserDto userDto, HttpServletRequest request) throws Exception;

    @Operation(summary = "Create a new user", description = "Saves user data",tags = {"Auth,Home"})
    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception;
}
