package dev.jenny.clara.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.register.exceptions.EmailAlreadyExistsException;
import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;
import dev.jenny.clara.user.exceptions.UserNotFoundException;
import dev.jenny.clara.user.mappers.UserMapper;

@Service
public class UserServiceImpl implements InterfaceUserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponseDTO getProfile(Authentication authentication) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(
                        "No se encontró ningún usuario con ese email " + authentication.getName()));
        return UserMapper.toDTO(user);
    }

    @Override
    public UserProfileResponseDTO updateProfile(Authentication authentication, UpdateProfileRequestDTO request) {
        UserEntity user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(
                        "No se encontró ningún usuario con ese email " + authentication.getName()));

        if (!request.email().equals(user.getEmail())
                && userRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailAlreadyExistsException("El email " + request.email() + " ya está registrado.");
        }

        user.setAlias(request.alias());
        user.setEmail(request.email());
        user.setAvatarId(request.avatarId());

        userRepository.save(user);

        return UserMapper.toDTO(user);
    }

}