package com.milan.service.impl;

import com.milan.dto.PasswordChangeRequest;
import com.milan.model.User;
import com.milan.repository.UserRepository;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void changePassword(PasswordChangeRequest request) {

        //Get details of log in user
        User loggedInUser = CommonUtil.getLoggedInUser();

       if(!passwordEncoder.matches(request.getOldPassword(),loggedInUser.getPassword())){
           throw new IllegalArgumentException("Old password doesn't match");
        }

        String encodePassword = passwordEncoder.encode(request.getNewPassword());
        loggedInUser.setPassword(encodePassword);

        userRepository.save(loggedInUser);
    }
}
