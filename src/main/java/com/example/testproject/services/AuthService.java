package com.example.testproject.services;

import com.example.testproject.exceptions.GeneralException;
import com.example.testproject.exceptions.custom.UserCreationConflict;
import com.example.testproject.exceptions.custom.UserNotFoundException;
import com.example.testproject.exceptions.custom.WrongAuthorizationParametrsRequest;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.models.requests.LoginRequest;
import com.example.testproject.models.models.requests.UserRequest;
import com.example.testproject.security.JWTResponse;
import com.example.testproject.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService mailSender;

    public JWTResponse login(LoginRequest loginRequest) {
        Optional<User> tempUser = userService.findByNickname(loginRequest.getNickname());
        if (!tempUser.isPresent()) throw new UserNotFoundException();
        User user = tempUser.get();
        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = JWTUtil.generateToken(user.getNickname());
            return new JWTResponse(token);
        } else {
            throw new WrongAuthorizationParametrsRequest();
        }
    }

    public User createUser(UserRequest userRequest) {
        if (userService.findByNickname(userRequest.getNickname()).isPresent())
            throw new UserCreationConflict();
        User user = UserRequest.mapFromEntity(userRequest);
        String token = verificationTokenService.createVerificationToken(user);
        mailSender.sendVerifyCode(user, token);
        userService.saveUser(user);
        return user;
    }

}
