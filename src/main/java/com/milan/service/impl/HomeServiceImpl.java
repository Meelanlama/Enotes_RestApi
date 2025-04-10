package com.milan.service.impl;

import com.milan.exception.ResourceNotFoundException;
import com.milan.exception.SuccessException;
import com.milan.model.AccountStatus;
import com.milan.model.User;
import com.milan.repository.UserRepository;
import com.milan.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {

    private final UserRepository userRepo;

    @Override
    public Boolean verifyAccount(Integer userId, String verificationCode) throws ResourceNotFoundException {

        //first find if user id is in db or not
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Invalid user"));

        //if again user try to verify through that url
        if(user.getStatus().getVerificationCode()==null){
            throw new SuccessException("Account already verified.");
        }

        //if user id is available and verification code in url is same as stored in db of that user
        if(user.getStatus().getVerificationCode().equals(verificationCode)) {

          AccountStatus status = user.getStatus();
          status.setIsActive(true);
          status.setVerificationCode(null);

          //update user
          userRepo.save(user);
            return true;
        }
        return false;
    }
}
