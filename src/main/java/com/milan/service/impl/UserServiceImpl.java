package com.milan.service.impl;

import com.milan.dto.EmailRequest;
import com.milan.dto.PasswordChangeRequest;
import com.milan.dto.PasswordResetRequest;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.User;
import com.milan.repository.UserRepository;
import com.milan.service.UserService;
import com.milan.util.CommonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final EmailService emailService;

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

    @Override
    public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {

        User user = userRepository.findByEmail(email);
        if(ObjectUtils.isEmpty(user))
        {
            throw new ResourceNotFoundException("Invalid Email");
        }

        // Generate unique password reset token
        String passwordResetToken = UUID.randomUUID().toString();
        user.getStatus().setPasswordResetToken(passwordResetToken);
        User updateUser = userRepository.save(user);

        String url = CommonUtil.getUrl(request);
        sendEmailRequest(updateUser,url);
    }

    private void sendEmailRequest(User user, String url) throws Exception {

        String message = "Hi <b>[[username]]</b> "
                +"<br><p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=[[url]]>Change your password</a></p>"
                + "Thanks,<br>Enotes.com";

        message = message.replace("[[username]]", user.getFirstName());
        message = message.replace("[[url]]", url + "/api/v1/home/verify-link?userId=" + user.getId() + "&&token="
                + user.getStatus().getPasswordResetToken());

        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .title("Password Reset")
                .subject("Password Reset link")
                .message(message).build();

        // send password reset email to user
        emailService.sendEmail(emailRequest);
    }

    @Override
    public void verifyPasswordResetLink(Integer userId, String token) throws ResourceNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("Invalid user"));

        //check if token matches from link and db
        verifyPasswordResetToken(user.getStatus().getPasswordResetToken(),token);
    }

    private void verifyPasswordResetToken(String existToken, String reqToken) {

        // request token not null
        if(StringUtils.hasText(reqToken))
        {
            // password already reset, if its empty
            if(!StringUtils.hasText(existToken))
            {
                throw new IllegalArgumentException("Password reset token doesn't exist");
            }
            // user req token changes
            if(!existToken.equals(reqToken))
            {
                throw new IllegalArgumentException("Invalid URL");
            }
        }else {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) throws Exception {
        User user = userRepository.findById(passwordResetRequest.getUid()).orElseThrow(()->new ResourceNotFoundException("Invalid user"));
        String encodePassword = passwordEncoder.encode(passwordResetRequest.getNewPassword());
        user.setPassword(encodePassword);
        user.getStatus().setPasswordResetToken(null);
        userRepository.save(user);
    }

}
