package dev.jenny.clara.email;

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

import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;
import dev.jenny.clara.email.exceptions.EmailChapterNotReachedException;
import dev.jenny.clara.email.exceptions.EmailNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    private static final String SENDER = "Marina Ortiz";
    private static final String SUBJECT = "Lo de esta noche";
    private static final String BODY = "Nos vemos luego en el puerto.";
    private static final Long EMAIL_ID = 1L;
    private static final Long MISSING_EMAIL_ID = 99L;
    private static final int MIN_CHAPTER = 1;
    private static final int LOCKED_MIN_CHAPTER = 3;
    private static final int CURRENT_CHAPTER = 2;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);
    private static final String USER_EMAIL = "clara@pruebas.com";
    private static final Long USER_ID = 10L;
    private static final String NOT_FOUND_MESSAGE = "No se encontró ningún correo con el id " + MISSING_EMAIL_ID;

    @Mock
    private EmailRepository emailRepository;

    @Mock
    private EmailReadRepository emailReadRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterfaceProgressService progressService;

    private EmailServiceImpl service;

    private Authentication authentication;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        service = new EmailServiceImpl(emailRepository, emailReadRepository, userRepository, progressService);
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

    @Test
    void testListInbox_ShouldMarkEmailAsRead_WhenAReadAlreadyExists() {
        stubAuthenticatedUser();

        EmailEntity email = EmailEntity.builder()
                .id(EMAIL_ID).sender(SENDER).subject(SUBJECT).body(BODY).minChapter(MIN_CHAPTER).build();

        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(emailRepository.findByMinChapterLessThanEqualOrderByMinChapterAsc(CURRENT_CHAPTER))
                .thenReturn(List.of(email));
        when(emailReadRepository.findByUserId(USER_ID)).thenReturn(List.of(
                EmailReadEntity.builder().user(user).email(email).readAt(LocalDateTime.now()).build()));

        List<EmailSummaryDTO> result = service.listInbox(authentication);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).read(), is(true));
    }

    @Test
    void testListInbox_ShouldMarkEmailAsUnread_WhenNoReadExists() {
        stubAuthenticatedUser();

        EmailEntity email = EmailEntity.builder()
                .id(EMAIL_ID).sender(SENDER).subject(SUBJECT).body(BODY).minChapter(MIN_CHAPTER).build();

        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(emailRepository.findByMinChapterLessThanEqualOrderByMinChapterAsc(CURRENT_CHAPTER))
                .thenReturn(List.of(email));
        when(emailReadRepository.findByUserId(USER_ID)).thenReturn(List.of());

        List<EmailSummaryDTO> result = service.listInbox(authentication);

        assertThat(result.get(0).read(), is(false));
    }

    @Test
    void testGetDetail_ShouldThrowEmailNotFoundException_WhenEmailDoesNotExist() {
        when(emailRepository.findById(MISSING_EMAIL_ID)).thenReturn(Optional.empty());

        EmailNotFoundException exception = assertThrows(EmailNotFoundException.class,
                () -> service.getDetail(authentication, MISSING_EMAIL_ID));

        assertThat(exception.getMessage(), is(equalTo(NOT_FOUND_MESSAGE)));
    }

    @Test
    void testGetDetail_ShouldThrowEmailChapterNotReachedException_WhenChapterNotReached() {
        stubAuthenticatedUser();

        EmailEntity email = EmailEntity.builder()
                .id(EMAIL_ID).sender(SENDER).subject(SUBJECT).body(BODY).minChapter(LOCKED_MIN_CHAPTER).build();

        when(emailRepository.findById(EMAIL_ID)).thenReturn(Optional.of(email));
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));

        assertThrows(EmailChapterNotReachedException.class, () -> service.getDetail(authentication, EMAIL_ID));
    }

    @Test
    void testGetDetail_ShouldCreateEmailRead_WhenNotReadYet() {
        stubAuthenticatedUser();

        EmailEntity email = EmailEntity.builder()
                .id(EMAIL_ID).sender(SENDER).subject(SUBJECT).body(BODY).minChapter(MIN_CHAPTER).build();

        when(emailRepository.findById(EMAIL_ID)).thenReturn(Optional.of(email));
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(emailReadRepository.findByUserIdAndEmailId(USER_ID, EMAIL_ID)).thenReturn(Optional.empty());

        EmailDetailDTO result = service.getDetail(authentication, EMAIL_ID);

        assertThat(result.id(), is(equalTo(EMAIL_ID)));
        verify(emailReadRepository).save(any(EmailReadEntity.class));
    }

    @Test
    void testGetDetail_ShouldNotCreateDuplicateEmailRead_WhenAlreadyRead() {
        stubAuthenticatedUser();

        EmailEntity email = EmailEntity.builder()
                .id(EMAIL_ID).sender(SENDER).subject(SUBJECT).body(BODY).minChapter(MIN_CHAPTER).build();

        when(emailRepository.findById(EMAIL_ID)).thenReturn(Optional.of(email));
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(emailReadRepository.findByUserIdAndEmailId(USER_ID, EMAIL_ID)).thenReturn(Optional.of(
                EmailReadEntity.builder().user(user).email(email).readAt(LocalDateTime.now()).build()));

        service.getDetail(authentication, EMAIL_ID);

        verify(emailReadRepository, never()).save(any(EmailReadEntity.class));
    }
}
