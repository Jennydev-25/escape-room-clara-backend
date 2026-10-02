package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.auth.dtos.LoginResponseDTO;
import dev.jenny.clara.user.User;

@Service
public class AuthServiceImpl implements InterfaceAuthService {

    private final JwtService jwtService;
    private final InterfaceRefreshTokenService refreshTokenService;

    public AuthServiceImpl(JwtService jwtService, InterfaceRefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Override
    public LoginResponseDTO login(Authentication authentication) {
        String token = jwtService.generateToken(authentication);
        String refreshToken = refreshTokenService.createRefreshToken(authentication).getToken();
        return new LoginResponseDTO(token, refreshToken);
    }

    @Override
    public LoginResponseDTO refresh(String refreshToken) {
        RefreshTokenEntity validToken = refreshTokenService.findValidToken(refreshToken);
        User user = validToken.getUser();

        String newToken = jwtService.generateToken(user);
        String newRefreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new LoginResponseDTO(newToken, newRefreshToken);
    }

}