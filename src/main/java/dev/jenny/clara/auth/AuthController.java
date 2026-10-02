package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;

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
}