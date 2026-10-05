package dev.jenny.clara.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.register.mappers.RegisterMapper;
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

        UserEntity user = RegisterMapper.toEntity(request, hashedPassword, alias);

        userRepository.save(user);

        return new RegisterResponseDTO("User stored successfully");
    }
}