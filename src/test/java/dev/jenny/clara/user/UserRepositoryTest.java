package dev.jenny.clara.user;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByEmail() {
        UserEntity user = UserEntity.builder()
                .email("clara@pruebas.com")
                .passwordHash("hashed-password")
                .alias("clara")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);

        Optional<UserEntity> found = userRepository.findByEmail("clara@pruebas.com");

        assertThat(found.isPresent(), is(true));
        assertThat(found.get().getEmail(), is(equalTo("clara@pruebas.com")));
    }
}