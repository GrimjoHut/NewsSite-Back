package com.example.testproject.Security;

import com.example.testproject.models.entities.Community;
import com.example.testproject.models.entities.User;
import com.example.testproject.models.enums.CommunityRoleEnum;
import com.example.testproject.models.enums.RoleEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Set<CommunityRoleEnum> getCommunityAuthorities(Community community) {
        return user.getRoleSystems().stream()
                .filter(roleSystem -> roleSystem.getCommunity().equals(community))
                .flatMap(roleSystem -> roleSystem.getRoles().stream()) // Получаем Stream ролей
                .collect(Collectors.toSet()); // Собираем роли как Set
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> role.name()) // Преобразуем RoleEnum в строки
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
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public User getUser() {
        return this.user;
    }
}
