package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.auth.dtos.LogoutResponseDTO;

public interface InterfaceAuthService {

    LoginResponseDTO login(Authentication authentication);

    LoginResponseDTO refresh(String refreshToken);

    LogoutResponseDTO logout(Authentication authentication, String refreshToken);

}