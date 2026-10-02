package dev.jenny.clara.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jenny.clara.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

    void deleteByUser(User user);

}