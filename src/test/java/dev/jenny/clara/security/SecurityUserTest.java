package dev.jenny.clara.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

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

    @Test
    void testGetPassword_ShouldReturnUserPasswordHash() {
        User user = User.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        String result = securityUser.getPassword();

        assertThat(result, is(equalTo("hashedPassword")));
    }

    @ParameterizedTest
    @MethodSource("rolesAndExpectedAuthorities")
    void testGetAuthorities_ShouldReturnRoleWithPrefix(Role role, String expectedAuthority) {
        User user = User.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        Collection<? extends GrantedAuthority> result = securityUser.getAuthorities();

        assertThat(result, contains(new SimpleGrantedAuthority(expectedAuthority)));
    }

    private static Stream<Arguments> rolesAndExpectedAuthorities() {
        return Stream.of(
                Arguments.of(Role.USER, "ROLE_USER"),
                Arguments.of(Role.ADMIN, "ROLE_ADMIN"));
    }
}