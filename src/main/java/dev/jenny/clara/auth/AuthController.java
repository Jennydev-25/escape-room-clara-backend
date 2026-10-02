package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.auth.dtos.RefreshTokenRequestDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "${api-endpoint}/auth")
public class AuthController {

    private final InterfaceAuthService authService;

    public AuthController(InterfaceAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(Authentication authentication) {
        return authService.login(authentication);
    }

    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        return authService.refresh(dto.refreshToken());
    }
}