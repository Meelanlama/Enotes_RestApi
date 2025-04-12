package com.milan.service.impl;

import com.milan.config.security.CustomUserDetails;
import com.milan.dto.*;
import com.milan.model.AccountStatus;
import com.milan.model.Role;
import com.milan.model.User;
import com.milan.repository.RoleRepository;
import com.milan.repository.UserRepository;
import com.milan.service.JwtService;
import com.milan.service.AuthService;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;

    private final ModelMapper mapper;

    private final RoleRepository roleRepo;

    private final Validation validation;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public Boolean registerUser(UserDto userDto,String url) throws Exception {

        logger.info("Starting registration process for email={}", userDto.getEmail());

        //validate role before register
        validation.userValidation(userDto);
        User saveUser = mapper.map(userDto, User.class);

        setRole(userDto,saveUser);

        AccountStatus status = AccountStatus.builder()
                .isActive(false)
                .verificationCode(UUID.randomUUID().toString())
                .build();

        saveUser.setStatus(status);
        saveUser.setPassword(passwordEncoder.encode(saveUser.getPassword()));

        logger.debug("User object prepared for saving: {}", saveUser);

        User savedUser = userRepo.save(saveUser);
        if(savedUser != null) {
            //send email
            logger.info("User registration successful for email={}", savedUser.getEmail());
            emailSend(savedUser,url);
            return true;
        } else {
            logger.error("User registration failed for email={}", userDto.getEmail());
        }
        return false;
    }

    private void emailSend(User savedUser, String url) throws Exception {
        try {
            String emailMessage = "Hi,<b>[[username]]</b> "
                    + "<br> Your account has been registered successfully.<br>"
                    + "<br> Click the below link verify & Activate your account <br>"
                    + "<a href ='[[url]]'>Click Here</a> <br><br>"
                    + "Thanks,<br>Enotes.com";

            emailMessage = emailMessage.replace("[[username]]", savedUser.getFirstName());
            emailMessage = emailMessage.replace("[[url]]",
                    url + "/api/v1/home/verify?userId=" + savedUser.getId() + "&&verificationCode=" + savedUser.getStatus().getVerificationCode());

            EmailRequest emailRequest = EmailRequest.builder()
                    .to(savedUser.getEmail())
                    .title("Confirm Your Registration")
                    .subject("Account Register Successful")
                    .message(emailMessage)
                    .build();

            logger.info("Sending email to: {}", savedUser.getEmail());
            emailService.sendEmail(emailRequest);

        } catch (Exception e) {
            logger.error("Failed to send email for user {}: {}", savedUser.getEmail(), e.getMessage(), e);
        }
    }

    private void setRole(UserDto userDto,User saveUser) {
        List<Integer> roleId = userDto.getRoles().stream().map(r -> r.getId()).toList();
        List<Role> roles = roleRepo.findAllById(roleId);
        saveUser.setRoles(roles);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        logger.info("Login attempt for email={}", loginRequest.getEmail());

        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (loginRequest.getEmail(), loginRequest.getPassword()));

            if (authenticate.isAuthenticated()) {
                CustomUserDetails customUserDetails = (CustomUserDetails) authenticate.getPrincipal();

                String token = jwtService.generateToken(customUserDetails.getUser());

                LoginResponse loginResponse = LoginResponse.builder()
                        .user(mapper.map(customUserDetails.getUser(), UserResponse.class))
                        .token(token)
                        .build();

                logger.info("Login successful for email={}", loginRequest.getEmail());
                return loginResponse;
            } else {
                logger.warn("Login failed: Invalid credentials for email={}", loginRequest.getEmail());
            }
        } catch (Exception e) {
            logger.error("Error during login for email={}: {}", loginRequest.getEmail(), e.getMessage(), e);
        }

        return null;
    }


}
