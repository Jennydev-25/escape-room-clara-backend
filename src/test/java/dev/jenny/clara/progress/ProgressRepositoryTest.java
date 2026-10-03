package dev.jenny.clara.progress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.user.Role;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@DataJpaTest
@ActiveProfiles("h2")
class ProgressRepositoryTest {

    private static final int INITIAL_CHAPTER = 1;
    private static final String EMPTY_HUD = "";
    private static final long ZERO_SECONDS = 0L;

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity createUser(String email) {
        return userRepository.save(UserEntity.builder()
                .email(email)
                .passwordHash("hashed-password")
                .alias(email.split("@")[0])
                .role(Role.USER)
                .build());
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void testFindByUserId_ShouldReturnProgressOnlyWhenExists(boolean progressExists) {
        UserEntity user = createUser("clara@pruebas.com");

        if (progressExists) {
            progressRepository.save(ProgressEntity.builder()
                    .user(user)
                    .currentChapter(INITIAL_CHAPTER)
                    .hudLetters(EMPTY_HUD)
                    .investigationSubmitted(false)
                    .timeSpentSeconds(ZERO_SECONDS)
                    .build());
        }

        Optional<ProgressEntity> found = progressRepository.findByUserId(user.getId());

        assertThat(found.isPresent(), is(progressExists));
    }
}
