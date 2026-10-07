package dev.jenny.clara.refreshtoken;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.role.RoleEntity;
import dev.jenny.clara.role.RoleRepository;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@DataJpaTest
@ActiveProfiles("h2")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testSave_ShouldGenerateId() {
        RoleEntity role = roleRepository.findByName("USER").orElseThrow();

        UserEntity user = userRepository.save(UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashed-password")
                .alias("clara")
                .role(role)
                .build());

        RefreshTokenEntity token = repository.save(RefreshTokenEntity.builder()
                .token("some-token")
                .user(user)
                .expiryDate(Instant.now().plus(1, ChronoUnit.DAYS))
                .build());

        assertThat(token.getId(), is(notNullValue()));
    }
}
