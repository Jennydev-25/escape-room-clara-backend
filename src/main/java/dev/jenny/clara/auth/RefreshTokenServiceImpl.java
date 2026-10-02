package dev.jenny.clara.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.auth.exceptions.InvalidRefreshTokenException;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@Service
public class RefreshTokenServiceImpl implements InterfaceRefreshTokenService {

    private final RefreshTokenRepository repository;
    private final UserRepository userRepository;
    private final long expirationDays;

    public RefreshTokenServiceImpl(RefreshTokenRepository repository, UserRepository userRepository,
            @Value("${jwt.refresh-token.expiration-days}") long expirationDays) {
        this.repository = repository;
        this.userRepository = userRepository;
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
    public RefreshTokenEntity createRefreshToken(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();
        return createRefreshToken(user);
    }

    @Override
    public RefreshTokenEntity verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(token);
            throw new InvalidRefreshTokenException("El refresh token ha caducado");
        }

        return token;
    }

    @Override
    public RefreshTokenEntity findValidToken(String token) {
        RefreshTokenEntity refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("El refresh token no existe"));

        return verifyExpiration(refreshToken);
    }

}