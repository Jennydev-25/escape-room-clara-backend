package dev.jenny.clara.register;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@Service
public class RegisterServiceImpl implements InterfaceRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public RegisterResponseDTO register(RegisterRequestDTO request) {
        userRepository.findByEmail(request.email()).ifPresent(existingUser -> {
            throw new EmailAlreadyExistsException("Email " + request.email() + " is already registered.");
        });

        String hashedPassword = passwordEncoder.encode(request.password());
        String alias = request.email().split("@")[0];

        User user = User.builder()
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