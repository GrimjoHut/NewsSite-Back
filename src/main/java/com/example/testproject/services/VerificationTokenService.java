package com.example.testproject.services;

import com.example.testproject.models.entities.User;
import com.example.testproject.models.entities.VerificationToken;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.UserRepository;
import com.example.testproject.repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    public String createVerificationToken(User user) {
        User savedUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, savedUser, LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);
        return token;
    }

    public boolean validateVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        user.getRoles().add(RoleEnum.USER);

        userRepository.save(user); // Сохраните пользователя как активированного
        tokenRepository.delete(verificationToken); // Удалите использованный токен
        userRepository.flush();
        return true;
    }
}
