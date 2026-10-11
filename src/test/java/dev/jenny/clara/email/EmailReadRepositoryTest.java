package dev.jenny.clara.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
class EmailReadRepositoryTest {

    private static final String SENDER = "Marina Ortiz";
    private static final String SUBJECT = "Lo de esta noche";
    private static final String BODY = "Nos vemos luego en el puerto.";
    private static final int MIN_CHAPTER = 1;

    @Autowired
    private EmailReadRepository emailReadRepository;

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity userRole;

    @BeforeEach
    void setUp() {
        userRole = roleRepository.findByName("USER").orElseThrow();
    }

    private UserEntity createUser(String email) {
        return userRepository.save(UserEntity.builder()
                .email(email)
                .passwordHash("hashed-password")
                .alias(email.split("@")[0])
                .role(userRole)
                .build());
    }

    private EmailEntity createEmail() {
        return emailRepository.save(EmailEntity.builder()
                .sender(SENDER)
                .subject(SUBJECT)
                .body(BODY)
                .minChapter(MIN_CHAPTER)
                .build());
    }

    @Test
    void testFindByUserIdAndEmailId_ShouldReturnRead_WhenItExists() {
        UserEntity user = createUser("clara@pruebas.com");
        EmailEntity email = createEmail();

        emailReadRepository.save(EmailReadEntity.builder()
                .user(user)
                .email(email)
                .readAt(LocalDateTime.now())
                .build());

        Optional<EmailReadEntity> result = emailReadRepository.findByUserIdAndEmailId(user.getId(), email.getId());

        assertThat(result.isPresent(), is(true));
    }

    @Test
    void testFindByUserIdAndEmailId_ShouldReturnEmpty_WhenItDoesNotExist() {
        UserEntity user = createUser("clara@pruebas.com");
        EmailEntity email = createEmail();

        Optional<EmailReadEntity> result = emailReadRepository.findByUserIdAndEmailId(user.getId(), email.getId());

        assertThat(result.isPresent(), is(false));
    }

    @Test
    void testFindByUserId_ShouldReturnAllReadsForThatUser() {
        UserEntity user = createUser("clara@pruebas.com");
        EmailEntity email = createEmail();

        emailReadRepository.save(EmailReadEntity.builder()
                .user(user)
                .email(email)
                .readAt(LocalDateTime.now())
                .build());

        List<EmailReadEntity> result = emailReadRepository.findByUserId(user.getId());

        assertThat(result.size(), is(equalTo(1)));
    }
}
