package com.milan.service;

import com.milan.exception.ResourceNotFoundException;

public interface HomeService {
    Boolean verifyAccount(Integer userId, String verificationCode) throws ResourceNotFoundException;
}
