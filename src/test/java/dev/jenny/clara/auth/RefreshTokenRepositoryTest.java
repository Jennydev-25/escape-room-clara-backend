package dev.jenny.clara.auth;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.User;
import dev.jenny.clara.user.UserRepository;

@DataJpaTest
@ActiveProfiles("h2")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSave_ShouldGenerateId() {
        User user = userRepository.save(User.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashed-password")
                .alias("clara")
                .role(Role.USER)
                .build());

        RefreshTokenEntity token = repository.save(new RefreshTokenEntity(
                "some-token",
                user,
                Instant.now().plus(1, ChronoUnit.DAYS)));

        assertThat(token.getId(), is(notNullValue()));
    }
}