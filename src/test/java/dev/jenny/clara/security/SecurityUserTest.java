package dev.jenny.clara.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Stream;

import dev.jenny.clara.role.RoleEntity;
import dev.jenny.clara.user.UserEntity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class SecurityUserTest {

    @Test
    void testGetUsername_ShouldReturnUserEmail() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(RoleEntity.builder().name("USER").build())
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        String result = securityUser.getUsername();

        assertThat(result, is(equalTo("clara@pruebas.com")));
    }

    @Test
    void testGetPassword_ShouldReturnUserPasswordHash() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(RoleEntity.builder().name("USER").build())
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        String result = securityUser.getPassword();

        assertThat(result, is(equalTo("hashedPassword")));
    }

    @ParameterizedTest
    @MethodSource("rolesAndExpectedAuthorities")
    void testGetAuthorities_ShouldReturnRoleWithPrefix(RoleEntity role, String expectedAuthority) {
        UserEntity user = UserEntity.builder()
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

    @Test
    void testAccountStatusMethods_ShouldAlwaysReturnTrue() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(RoleEntity.builder().name("USER").build())
                .createdAt(LocalDateTime.now())
                .build();
        SecurityUser securityUser = new SecurityUser(user);

        assertThat(securityUser.isAccountNonExpired(), is(equalTo(true)));
        assertThat(securityUser.isAccountNonLocked(), is(equalTo(true)));
        assertThat(securityUser.isCredentialsNonExpired(), is(equalTo(true)));
        assertThat(securityUser.isEnabled(), is(equalTo(true)));
    }

    private static Stream<Arguments> rolesAndExpectedAuthorities() {
        return Stream.of(
                Arguments.of(RoleEntity.builder().name("USER").build(), "ROLE_USER"),
                Arguments.of(RoleEntity.builder().name("ADMIN").build(), "ROLE_ADMIN"));
    }
}