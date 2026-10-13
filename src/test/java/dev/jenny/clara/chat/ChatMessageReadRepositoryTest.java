package dev.jenny.clara.chat;

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
class ChatMessageReadRepositoryTest {

    private static final String CONTACT = "Diego Barreda";
    private static final String CHARACTER = "Clara";
    private static final String CONTENT = "Buenas tardes, Sr. Barreda.";
    private static final int MIN_CHAPTER = 1;

    @Autowired
    private ChatMessageReadRepository chatMessageReadRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

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

    private ChatMessageEntity createMessage() {
        return chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(MIN_CHAPTER)
                .displayOrder(1)
                .build());
    }

    @Test
    void testFindByUserId_ShouldReturnAllReadsForThatUser() {
        UserEntity user = createUser("clara@pruebas.com");
        ChatMessageEntity message = createMessage();

        chatMessageReadRepository.save(ChatMessageReadEntity.builder()
                .user(user)
                .message(message)
                .readAt(LocalDateTime.now())
                .build());

        List<ChatMessageReadEntity> result = chatMessageReadRepository.findByUserId(user.getId());

        assertThat(result.size(), is(equalTo(1)));
    }

    @Test
    void testFindByUserId_ShouldReturnEmpty_WhenUserHasNoReads() {
        UserEntity user = createUser("clara@pruebas.com");

        List<ChatMessageReadEntity> result = chatMessageReadRepository.findByUserId(user.getId());

        assertThat(result.isEmpty(), is(true));
    }
}
