package com.milan.controller;

import com.milan.exception.ResourceNotFoundException;
import com.milan.service.HomeService;
import com.milan.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeService homeService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Integer userId, @RequestParam String verificationCode) throws ResourceNotFoundException {

        Boolean success = homeService.verifyAccount(userId, verificationCode);

        if (success) {
          return CommonUtil.createBuildResponse("Account verification successful", HttpStatus.OK);
        }
        return CommonUtil.createErrorResponseMessage("Invalid verification link", HttpStatus.BAD_REQUEST);
    }
}
