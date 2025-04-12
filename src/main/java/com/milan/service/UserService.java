package com.milan.service;

import com.milan.dto.PasswordChangeRequest;

public interface UserService {

    public void changePassword(PasswordChangeRequest request);
}
