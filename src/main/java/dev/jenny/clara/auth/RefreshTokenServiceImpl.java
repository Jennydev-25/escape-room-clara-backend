package dev.jenny.clara.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.stereotype.Service;

import dev.jenny.clara.user.User;

@Service
public class RefreshTokenServiceImpl implements InterfaceRefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Override
    public RefreshTokenEntity createRefreshToken(User user) {
        repository.deleteByUser(user);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity(
                UUID.randomUUID().toString(),
                user,
                Instant.now().plus(365, ChronoUnit.DAYS));

        return repository.save(refreshToken);
    }

}