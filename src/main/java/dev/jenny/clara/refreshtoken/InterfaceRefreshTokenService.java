package dev.jenny.clara.refreshtoken;

import org.springframework.security.core.Authentication;

import dev.jenny.clara.user.UserEntity;

public interface InterfaceRefreshTokenService {

    RefreshTokenEntity createRefreshToken(UserEntity user);

    RefreshTokenEntity createRefreshToken(Authentication authentication);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

    RefreshTokenEntity findValidToken(String token);

}
