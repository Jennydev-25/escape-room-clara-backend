package dev.jenny.clara.register;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class RegisterServiceImpl implements InterfaceRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecaptchaService recaptchaService;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RecaptchaService recaptchaService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.recaptchaService = recaptchaService;
    }

    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        recaptchaService.verifyOrThrow(request.recaptchaToken());

        userRepository.findByEmail(request.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("El email " + request.email() + " ya está registrado.");
        });

        String hashedPassword = passwordEncoder.encode(request.password());
        String alias = request.email().split("@")[0];

        UserEntity user = UserEntity.builder()
                .email(request.email())
                .passwordHash(hashedPassword)
                .alias(alias)
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        return new RegisterResponseDTO("User stored successfully");
    }
}