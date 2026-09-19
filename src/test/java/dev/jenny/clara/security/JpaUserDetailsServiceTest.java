package dev.jenny.clara.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @InjectMocks
    private JpaUserDetailsService service;

    @Mock
    private UserRepository userRepository;

    @Test
    void testLoadUserByUsername_ShouldReturnSecurityUser_WhenUserExists() {
        User user = User.builder()
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
}