package dev.jenny.clara.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import dev.jenny.clara.user.User;

public class SecurityUser {

    private final User user;

    public SecurityUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return user.getEmail();
    }

    public String getPassword() {
        return user.getPasswordHash();
    }

    public List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }
}