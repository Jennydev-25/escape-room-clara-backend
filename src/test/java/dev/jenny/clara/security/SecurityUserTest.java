package dev.jenny.clara.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;

class SecurityUserTest {

    @Test
    void testGetUsername_ShouldReturnUserEmail() {
        User user = User.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        String result = securityUser.getUsername();

        assertThat(result, is(equalTo("clara@pruebas.com")));
    }
}