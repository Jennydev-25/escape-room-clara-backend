package dev.jenny.clara.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import dev.jenny.clara.user.User;

@Service
public class RefreshTokenServiceImpl implements InterfaceRefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long expirationDays;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository,
            @Value("${jwt.refresh-token.expiration-days}") long expirationDays) {
        this.repository = repository;
        this.expirationDays = expirationDays;
    }

    @Override
    public RefreshTokenEntity createRefreshToken(User user) {
        repository.deleteByUser(user);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(
                UUID.randomUUID().toString(),
                user,
                Instant.now().plus(expirationDays, ChronoUnit.DAYS));

        return repository.save(refreshToken);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new InvalidRefreshTokenException("El refresh token ha caducado");
        }

        return token;
    }

}