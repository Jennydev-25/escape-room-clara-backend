package dev.jenny.clara.register;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
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

import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.recaptcha.exceptions.InvalidRecaptchaException;
import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.role.InterfaceRoleService;
import dev.jenny.clara.role.RoleEntity;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @InjectMocks
    private RegisterServiceImpl service;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RecaptchaService recaptchaService;

    @Mock
    private InterfaceRoleService roleService;

    @Test
    void testRegister_ShouldSaveUserAndReturnSuccessResponse() {
        RegisterRequestDTO dtoRequest = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword",
                "valid-captcha-token");
        RoleEntity role = RoleEntity.builder().name("USER").build();

        when(userRepository.findByEmail("clara@pruebas.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPassword")).thenReturn("hashedPassword");
        when(roleService.assignDefaultRole()).thenReturn(role);

        RegisterResponseDTO response = service.register(dtoRequest);

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());
        UserEntity savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail(), is(equalTo("clara@pruebas.com")));
        assertThat(savedUser.getPasswordHash(), is(equalTo("hashedPassword")));
        assertThat(savedUser.getAlias(), is(equalTo("clara")));
        assertThat(savedUser.getRole(), is(equalTo(role)));
        assertThat(response.message(), is(equalTo("User stored successfully")));
    }

    @Test
    void testRegister_ShouldThrowException_WhenEmailAlreadyExists() {
        RegisterRequestDTO dtoRequest = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword",
                "valid-captcha-token");
        UserEntity existingUser = UserEntity.builder().email("clara@pruebas.com").build();

        when(userRepository.findByEmail("clara@pruebas.com")).thenReturn(Optional.of(existingUser));

        assertThrows(EmailAlreadyExistsException.class, () -> service.register(dtoRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegister_ShouldThrowException_WhenRecaptchaIsInvalid() {
        RegisterRequestDTO dtoRequest = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword",
                "invalid-captcha-token");

        doThrow(new InvalidRecaptchaException("Invalid recaptcha token."))
                .when(recaptchaService).verifyOrThrow("invalid-captcha-token");

        assertThrows(InvalidRecaptchaException.class, () -> service.register(dtoRequest));

        verify(userRepository, never()).save(any());
    }
}