package dev.jenny.clara.user;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
                        "No se encontró ningún usuario con el email " + authentication.getName()));
        return UserMapper.toDTO(user);
    }

}