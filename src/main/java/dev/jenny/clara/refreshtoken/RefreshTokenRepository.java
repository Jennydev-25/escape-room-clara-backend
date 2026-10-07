package dev.jenny.clara.refreshtoken;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByToken(String token);

}
