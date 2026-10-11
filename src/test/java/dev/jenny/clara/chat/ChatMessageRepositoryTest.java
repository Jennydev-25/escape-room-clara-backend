package dev.jenny.clara.chat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class ChatMessageRepositoryTest {

    private static final String CONTACT = "Diego Barreda";
    private static final String OTHER_CONTACT = "Mamá";
    private static final String CHARACTER = "Clara";
    private static final String CONTENT = "Buenas tardes, Sr. Barreda.";
    private static final int MIN_CHAPTER = 1;
    private static final int LATER_MIN_CHAPTER = 2;
    private static final int CHAPTER_WITHOUT_MESSAGES = 0;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    void testFindByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc_ShouldReturnVisibleMessagesInOrder_WhenChapterMatches() {
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(MIN_CHAPTER)
                .displayOrder(2)
                .build());
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(MIN_CHAPTER)
                .displayOrder(1)
                .build());
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(LATER_MIN_CHAPTER)
                .displayOrder(3)
                .build());
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(OTHER_CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(MIN_CHAPTER)
                .displayOrder(1)
                .build());

        List<ChatMessageEntity> result = chatMessageRepository
                .findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT, MIN_CHAPTER);

        assertThat(result.size(), is(equalTo(2)));
        assertThat(result.get(0).getDisplayOrder(), is(equalTo(1)));
        assertThat(result.get(1).getDisplayOrder(), is(equalTo(2)));
    }

    @Test
    void testFindByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc_ShouldReturnEmpty_WhenNoMessageReachedYet() {
        List<ChatMessageEntity> result = chatMessageRepository
                .findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT, CHAPTER_WITHOUT_MESSAGES);

        assertThat(result.isEmpty(), is(true));
    }
}
