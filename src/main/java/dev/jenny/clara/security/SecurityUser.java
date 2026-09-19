package dev.jenny.clara.security;

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
}