package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;

public interface InterfaceAuthService {

    LoginResponseDTO login(Authentication authentication);

}