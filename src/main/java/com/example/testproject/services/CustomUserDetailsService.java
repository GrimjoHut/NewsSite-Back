package com.example.testproject.services;

import com.example.testproject.Security.CustomUserDetails;
import com.example.testproject.exceptions.custom.UserNotFoundException;
import com.example.testproject.models.entities.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userService.findByNickname(username);
        if (user.isPresent())
            return new CustomUserDetails(user.get());
        else throw new UserNotFoundException();
    }
}
