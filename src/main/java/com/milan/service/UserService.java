package com.milan.service;

import com.milan.dto.PasswordChangeRequest;
import com.milan.dto.PasswordResetRequest;
import com.milan.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    public void changePassword(PasswordChangeRequest request);

    void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception;

    void verifyPasswordResetLink(Integer userId, String token) throws ResourceNotFoundException;

    void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception;

}
