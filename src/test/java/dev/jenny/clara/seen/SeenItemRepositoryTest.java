package dev.jenny.clara.seen;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;

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
class SeenItemRepositoryTest {

    private static final String FOLDER_KEY = "folder:leeme";

    @Autowired
    private SeenItemRepository seenItemRepository;

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

    @Test
    void testFindByUserId_ShouldReturnOnlyItemsOfThatUser() {
        UserEntity clara = createUser("clara@pruebas.com");
        UserEntity marina = createUser("marina@pruebas.com");

        seenItemRepository.save(SeenItemEntity.builder()
                .user(clara)
                .itemKey(FOLDER_KEY)
                .seenAt(LocalDateTime.now())
                .build());
        seenItemRepository.save(SeenItemEntity.builder()
                .user(marina)
                .itemKey(FOLDER_KEY)
                .seenAt(LocalDateTime.now())
                .build());

        List<SeenItemEntity> result = seenItemRepository.findByUserId(clara.getId());

        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    void testExistsByUserIdAndItemKey_ShouldReturnTrue_WhenUserSawIt() {
        UserEntity clara = createUser("clara@pruebas.com");

        seenItemRepository.save(SeenItemEntity.builder()
                .user(clara)
                .itemKey(FOLDER_KEY)
                .seenAt(LocalDateTime.now())
                .build());

        boolean result = seenItemRepository.existsByUserIdAndItemKey(clara.getId(), FOLDER_KEY);

        assertThat(result, is(true));
    }
}
