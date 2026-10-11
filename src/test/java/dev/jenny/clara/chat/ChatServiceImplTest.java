package dev.jenny.clara.chat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.chat.dtos.ChatMessageResponseDTO;
import dev.jenny.clara.chat.dtos.ContactSummaryDTO;
import dev.jenny.clara.chat.exceptions.ContactNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    private static final String CONTACT = "Diego Barreda";
    private static final String MISSING_CONTACT = "Nadie";
    private static final String CHARACTER = "Clara";
    private static final String CONTENT_1 = "Buenas tardes, Sr. Barreda.";
    private static final String CONTENT_2 = "Le agradecería que tuviera cuidado.";
    private static final Long MESSAGE_ID_1 = 1L;
    private static final Long MESSAGE_ID_2 = 2L;
    private static final int MIN_CHAPTER = 1;
    private static final int CURRENT_CHAPTER = 2;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);
    private static final String USER_EMAIL = "clara@pruebas.com";
    private static final Long USER_ID = 10L;
    private static final String NOT_FOUND_MESSAGE = "No se encontró ningún contacto con el nombre " + MISSING_CONTACT;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatMessageReadRepository chatMessageReadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterfaceProgressService progressService;

    private ChatServiceImpl service;

    private Authentication authentication;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        service = new ChatServiceImpl(chatMessageRepository, chatMessageReadRepository, userRepository,
                progressService);
        authentication = org.mockito.Mockito.mock(Authentication.class);
        user = UserEntity.builder().id(USER_ID).email(USER_EMAIL).build();
    }

    private void stubAuthenticatedUser() {
        when(authentication.getName()).thenReturn(USER_EMAIL);
        when(userRepository.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
    }

    private ProgressResponseDTO progressAtChapter(int chapter) {
        return new ProgressResponseDTO(chapter, TEST_HUD, false, TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null);
    }

    private ChatMessageEntity message(Long id, String content, int displayOrder) {
        return ChatMessageEntity.builder()
                .id(id).contact(CONTACT).character(CHARACTER).content(content)
                .minChapter(MIN_CHAPTER).displayOrder(displayOrder).build();
    }

    @Test
    void testListContacts_ShouldReturnUnreadCount_WhenMessagesAreNotRead() {
        stubAuthenticatedUser();

        ChatMessageEntity message1 = message(MESSAGE_ID_1, CONTENT_1, 1);
        ChatMessageEntity message2 = message(MESSAGE_ID_2, CONTENT_2, 2);

        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(chatMessageRepository.findDistinctContactByMinChapterLessThanEqual(CURRENT_CHAPTER))
                .thenReturn(List.of(CONTACT));
        when(chatMessageRepository.findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT,
                CURRENT_CHAPTER)).thenReturn(List.of(message1, message2));
        when(chatMessageReadRepository.findByUserId(USER_ID)).thenReturn(List.of());

        List<ContactSummaryDTO> result = service.listContacts(authentication);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).contact(), is(equalTo(CONTACT)));
        assertThat(result.get(0).lastMessage(), is(equalTo(CONTENT_2)));
        assertThat(result.get(0).unread(), is(equalTo(2)));
    }

    @Test
    void testListContacts_ShouldReturnZeroUnread_WhenAllMessagesAlreadyRead() {
        stubAuthenticatedUser();

        ChatMessageEntity message1 = message(MESSAGE_ID_1, CONTENT_1, 1);

        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(chatMessageRepository.findDistinctContactByMinChapterLessThanEqual(CURRENT_CHAPTER))
                .thenReturn(List.of(CONTACT));
        when(chatMessageRepository.findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT,
                CURRENT_CHAPTER)).thenReturn(List.of(message1));
        when(chatMessageReadRepository.findByUserId(USER_ID)).thenReturn(List.of(
                ChatMessageReadEntity.builder().user(user).message(message1).readAt(LocalDateTime.now()).build()));

        List<ContactSummaryDTO> result = service.listContacts(authentication);

        assertThat(result.get(0).unread(), is(equalTo(0)));
    }

    @Test
    void testListConversation_ShouldThrowContactNotFoundException_WhenContactDoesNotExist() {
        when(chatMessageRepository.existsByContact(MISSING_CONTACT)).thenReturn(false);

        ContactNotFoundException exception = assertThrows(ContactNotFoundException.class,
                () -> service.listConversation(authentication, MISSING_CONTACT));

        assertThat(exception.getMessage(), is(equalTo(NOT_FOUND_MESSAGE)));
    }

    @Test
    void testListConversation_ShouldMarkUnreadMessagesAsRead_WhenNotReadYet() {
        stubAuthenticatedUser();

        ChatMessageEntity message1 = message(MESSAGE_ID_1, CONTENT_1, 1);

        when(chatMessageRepository.existsByContact(CONTACT)).thenReturn(true);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(chatMessageRepository.findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT,
                CURRENT_CHAPTER)).thenReturn(List.of(message1));
        when(chatMessageReadRepository.findByUserId(USER_ID)).thenReturn(List.of());

        List<ChatMessageResponseDTO> result = service.listConversation(authentication, CONTACT);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).read(), is(true));
        verify(chatMessageReadRepository).save(any(ChatMessageReadEntity.class));
    }

    @Test
    void testListConversation_ShouldNotCreateDuplicateRead_WhenAlreadyRead() {
        stubAuthenticatedUser();

        ChatMessageEntity message1 = message(MESSAGE_ID_1, CONTENT_1, 1);

        when(chatMessageRepository.existsByContact(CONTACT)).thenReturn(true);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(chatMessageRepository.findByContactAndMinChapterLessThanEqualOrderByDisplayOrderAsc(CONTACT,
                CURRENT_CHAPTER)).thenReturn(List.of(message1));
        when(chatMessageReadRepository.findByUserId(USER_ID)).thenReturn(List.of(
                ChatMessageReadEntity.builder().user(user).message(message1).readAt(LocalDateTime.now()).build()));

        service.listConversation(authentication, CONTACT);

        verify(chatMessageReadRepository, never()).save(any(ChatMessageReadEntity.class));
    }
}
