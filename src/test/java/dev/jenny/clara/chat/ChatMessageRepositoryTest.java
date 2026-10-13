package dev.jenny.clara.chat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class ChatMessageRepositoryTest {

    // Nombres que no existen en el seed narrativo de data.sql (evita falsos positivos por contactos reales)
    private static final String CONTACT = "Contacto De Prueba";
    private static final String OTHER_CONTACT = "Contacto Sin Mensajes";
    private static final String CHARACTER = "Clara";
    private static final String CONTENT = "Mensaje de prueba.";
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

    @Test
    void testFindDistinctContactByMinChapterLessThanEqual_ShouldReturnDistinctVisibleContacts_WhenChapterMatches() {
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
                .minChapter(MIN_CHAPTER)
                .displayOrder(2)
                .build());
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(OTHER_CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(LATER_MIN_CHAPTER)
                .displayOrder(1)
                .build());

        List<String> result = chatMessageRepository.findDistinctContactByMinChapterLessThanEqual(MIN_CHAPTER);

        // No comprobamos el total (el seed narrativo aporta otros contactos en ese capitulo):
        // comprobamos que el nuestro aparece y que el de un capitulo posterior no se filtra de mas
        assertThat(result, hasItem(CONTACT));
        assertThat(result, not(hasItem(OTHER_CONTACT)));
    }

    @Test
    void testExistsByContact_ShouldReturnTrue_WhenContactHasMessages() {
        chatMessageRepository.save(ChatMessageEntity.builder()
                .contact(CONTACT)
                .character(CHARACTER)
                .content(CONTENT)
                .minChapter(MIN_CHAPTER)
                .displayOrder(1)
                .build());

        assertThat(chatMessageRepository.existsByContact(CONTACT), is(true));
    }

    @Test
    void testExistsByContact_ShouldReturnFalse_WhenContactHasNoMessages() {
        assertThat(chatMessageRepository.existsByContact(OTHER_CONTACT), is(false));
    }
}
