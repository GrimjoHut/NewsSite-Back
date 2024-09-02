package com.example.testproject.services;

import com.example.testproject.models.DTO.LoginDTO;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.UserRepository;
import com.example.testproject.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.testproject.security.JWTResponse;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailSenderService mailSender;

    public ResponseEntity<String> createUser(LoginDTO loginDTO) {
        if (userRepository.findByNickname(loginDTO.getNickname()).isPresent()) {
            return ResponseEntity.status(HttpStatus.FOUND).body("User already exists");
        }

        User user = new User();
        user.setNickname(loginDTO.getNickname());
        user.setPassword(passwordEncoder.encode(loginDTO.getPassword()));
        user.setEmail(loginDTO.getEmail());
        String token = verificationTokenService.createVerificationToken(user);
        mailSender.sendVerifyCode(user, token);

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Please check email and verify code");
    }

    public ResponseEntity<String> verifyUser(String token){
        if (verificationTokenService.validateVerificationToken(token)) return ResponseEntity.status(HttpStatus.OK).body("Good");
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token not found");
    }

    public ResponseEntity<?> login(LoginDTO loginDTO) {
        Optional<User> tempUser = userRepository.findByNickname(loginDTO.getNickname());

        if (tempUser.isPresent()) {
            User user = tempUser.get();
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                // Генерация нового токена при каждом успешном входе
                String token = JWTUtil.generateToken(user.getNickname());
                return ResponseEntity.ok(new JWTResponse(token));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong login or password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
    public ResponseEntity<String> giveRole(String actingUserNickname, String targetUserNickname, RoleEnum role) {
        Optional<User> actingUserOpt = userRepository.findByNickname(actingUserNickname);
        Optional<User> targetUserOpt = userRepository.findByNickname(targetUserNickname);

        if (actingUserOpt.isPresent() && targetUserOpt.isPresent()) {
            User actingUser = actingUserOpt.get();
            User targetUser = targetUserOpt.get();

            if (!actingUser.getRoles().stream().anyMatch(r -> r.canAssign(role))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to assign this role");
            }

            if (targetUser.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.OK).body("User already has this role");
            } else {
                targetUser.getRoles().add(role);
                if (role == RoleEnum.ADMIN && !targetUser.getRoles().contains(RoleEnum.MODER)) {
                    targetUser.getRoles().add(RoleEnum.MODER);
                }
                userRepository.save(targetUser);
                return ResponseEntity.status(HttpStatus.OK).body("Role was given");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    public ResponseEntity<String> takeRole(String actingUserNickname, String targetUserNickname, RoleEnum role) {
        Optional<User> actingUserOpt = userRepository.findByNickname(actingUserNickname);
        Optional<User> targetUserOpt = userRepository.findByNickname(targetUserNickname);

        if (actingUserOpt.isPresent() && targetUserOpt.isPresent()) {
            User actingUser = actingUserOpt.get();
            User targetUser = targetUserOpt.get();

            if (!actingUser.getRoles().stream().anyMatch(r -> r.canRevoke(role))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to revoke this role");
            }

            if (!targetUser.getRoles().contains(role)) {
                return ResponseEntity.status(HttpStatus.OK).body("User doesn't have this role");
            } else {
                targetUser.getRoles().remove(role);
                if (role == RoleEnum.MODER && targetUser.getRoles().contains(RoleEnum.ADMIN)) {
                    targetUser.getRoles().remove(RoleEnum.ADMIN);
                }
                userRepository.save(targetUser);
                return ResponseEntity.status(HttpStatus.OK).body("Role was taken");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

}
