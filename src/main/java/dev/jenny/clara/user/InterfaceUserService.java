package dev.jenny.clara.user;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import dev.jenny.clara.user.dtos.CompleteProfileRequestDTO;
import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import dev.jenny.clara.user.dtos.UserProfileResponseDTO;

public interface InterfaceUserService {

    UserProfileResponseDTO getProfile(Authentication authentication);

    UserProfileResponseDTO completeProfile(Authentication authentication, CompleteProfileRequestDTO request);

    UserProfileResponseDTO updateProfile(Authentication authentication, UpdateProfileRequestDTO request);

    void changePassword(Authentication authentication, ChangePasswordRequestDTO request);
}