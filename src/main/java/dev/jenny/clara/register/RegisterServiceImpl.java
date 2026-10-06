package dev.jenny.clara.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.jenny.clara.recaptcha.RecaptchaService;
import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import dev.jenny.clara.register.dtos.RegisterResponseDTO;
import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.register.mappers.RegisterMapper;
import dev.jenny.clara.role.InterfaceRoleService;
import dev.jenny.clara.role.RoleEntity;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@Service
public class RegisterServiceImpl implements InterfaceRegisterService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RecaptchaService recaptchaService;
    private final InterfaceRoleService roleService;

    public RegisterServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
            RecaptchaService recaptchaService, InterfaceRoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.recaptchaService = recaptchaService;
        this.roleService = roleService;
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
        RoleEntity role = roleService.assignDefaultRole();

        UserEntity user = RegisterMapper.toEntity(request, hashedPassword, alias, role);

        userRepository.save(user);

        return new RegisterResponseDTO("User stored successfully");
    }
}
