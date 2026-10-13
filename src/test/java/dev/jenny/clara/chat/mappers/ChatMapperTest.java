package dev.jenny.clara.chat.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.chat.ChatMessageEntity;
import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;

class ChatMapperTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_CONTACT = "Diego Barreda";
    private static final String TEST_CHARACTER = "Clara";
    private static final String TEST_CONTENT = "Buenas tardes, Sr. Barreda.";
    private static final LocalDateTime TEST_SENT_AT = LocalDateTime.of(2025, 6, 3, 9, 15);
    private static final int TEST_MIN_CHAPTER = 1;
    private static final int TEST_DISPLAY_ORDER = 1;
    private static final int TEST_UNREAD = 2;

    @Test
    void testToMessageDTO_ShouldMapMessageFieldsAndReadFlag() {
        ChatMessageEntity message = ChatMessageEntity.builder()
                .id(TEST_ID)
                .contact(TEST_CONTACT)
                .character(TEST_CHARACTER)
                .content(TEST_CONTENT)
                .sentAt(TEST_SENT_AT)
                .minChapter(TEST_MIN_CHAPTER)
                .displayOrder(TEST_DISPLAY_ORDER)
                .build();

        ChatMessageResponseDTO dto = ChatMapper.toMessageDTO(message, true);

        assertThat(dto, isA(ChatMessageResponseDTO.class));
        assertThat(dto.id(), is(equalTo(TEST_ID)));
        assertThat(dto.contact(), is(equalTo(TEST_CONTACT)));
        assertThat(dto.character(), is(equalTo(TEST_CHARACTER)));
        assertThat(dto.content(), is(equalTo(TEST_CONTENT)));
        assertThat(dto.sentAt(), is(equalTo(TEST_SENT_AT)));
        assertThat(dto.displayOrder(), is(equalTo(TEST_DISPLAY_ORDER)));
        assertThat(dto.read(), is(true));
    }

    @Test
    void testToContactSummaryDTO_ShouldMapContactLastMessageAndUnreadCount() {
        ContactSummaryDTO dto = ChatMapper.toContactSummaryDTO(TEST_CONTACT, TEST_CONTENT, TEST_UNREAD);

        assertThat(dto, isA(ContactSummaryDTO.class));
        assertThat(dto.contact(), is(equalTo(TEST_CONTACT)));
        assertThat(dto.lastMessage(), is(equalTo(TEST_CONTENT)));
        assertThat(dto.unread(), is(equalTo(TEST_UNREAD)));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<ChatMapper> constructor = ChatMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
