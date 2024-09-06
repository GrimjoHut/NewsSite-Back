package com.example.testproject.services;

import com.example.testproject.exceptions.custom.VerificationTokenNotFoundException;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.entities.VerificationToken;
import com.example.testproject.models.enums.RoleEnum;
import com.example.testproject.repositories.UserRepository;
import com.example.testproject.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;

    private final UserRepository userRepository;

    public VerificationToken findByToken(String token){
        return tokenRepository.findByToken(token)
                .orElseThrow(VerificationTokenNotFoundException::new);
    }

    public String createVerificationToken(User user) {
        User savedUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, savedUser, LocalDateTime.now().plusHours(24));
        tokenRepository.save(verificationToken);
        return token;
    }

    public boolean validateVerificationToken(String token) {
        VerificationToken verificationToken = this.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        user.getRoles().add(RoleEnum.ROLE_USER);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        userRepository.flush();
        return true;
    }
}
