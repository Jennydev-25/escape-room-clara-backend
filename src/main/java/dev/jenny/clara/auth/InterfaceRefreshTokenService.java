package dev.jenny.clara.auth;

import dev.jenny.clara.user.User;

public interface InterfaceRefreshTokenService {

    RefreshTokenEntity createRefreshToken(User user);

    RefreshTokenEntity verifyExpiration(RefreshTokenEntity token);

}