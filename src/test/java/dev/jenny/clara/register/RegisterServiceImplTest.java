package dev.jenny.clara.register;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @InjectMocks
    private RegisterServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegister_ShouldSaveUserAndReturnSuccessResponse() {
        RegisterRequestDTO dtoRequest = new RegisterRequestDTO("clara@pruebas.com", "plainPassword");

        when(userRepository.findByEmail("clara@pruebas.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");

        RegisterResponseDTO response = service.register(dtoRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail(), is(equalTo("clara@pruebas.com")));
        assertThat(savedUser.getPasswordHash(), is(equalTo("hashedPassword")));
        assertThat(savedUser.getAlias(), is(equalTo("clara")));
        assertThat(savedUser.getRole(), is(equalTo(Role.USER)));
        assertThat(response.message(), is(equalTo("User stored successfully")));
    }
}