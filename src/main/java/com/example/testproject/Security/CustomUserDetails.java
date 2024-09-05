package com.example.testproject.Security;

import com.example.testproject.models.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role.name())
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Логику можно изменить при необходимости
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Логику можно изменить при необходимости
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Логику можно изменить при необходимости
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled(); // Это поле у вас уже есть
    }

    public User getUser() {
        return this.user;
    }
}
