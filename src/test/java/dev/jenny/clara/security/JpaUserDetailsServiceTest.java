package dev.jenny.clara.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @InjectMocks
    private JpaUserDetailsService service;

    @Mock
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername_ShouldReturnSecurityUser_WhenUserExists() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashedPassword")
                .alias("clara")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("clara@pruebas.com")).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername("clara@pruebas.com");

        assertThat(result.getUsername(), is(equalTo("clara@pruebas.com")));
        assertThat(result.getPassword(), is(equalTo("hashedPassword")));
    }

    @Test
    void testLoadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        when(userRepository.findByEmail("noexiste@pruebas.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("noexiste@pruebas.com"));
    }
}