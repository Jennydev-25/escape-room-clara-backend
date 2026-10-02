package dev.jenny.clara.auth;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.user.User;

public interface InterfaceRefreshTokenService {

    RefreshTokenEntity createRefreshToken(User user);

    RefreshTokenEntity createRefreshToken(Authentication authentication);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    RefreshTokenEntity findValidToken(String token);

}