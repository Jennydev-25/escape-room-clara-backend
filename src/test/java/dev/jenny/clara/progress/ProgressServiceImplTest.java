package dev.jenny.clara.progress;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.progress.dtos.AccumulateTimeRequestDTO;
import dev.jenny.clara.progress.dtos.UpdateNoteRequestDTO;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.user.UserEntity;
import dev.jenny.clara.user.UserRepository;

@ExtendWith(MockitoExtension.class)
class ProgressServiceImplTest {

    private static final String TEST_EMAIL = "clara@example.com";
    private static final Long TEST_USER_ID = 1L;
    private static final int TEST_CHAPTER = 2;
    private static final String TEST_HUD = "AB";
    private static final long TEST_SECONDS = 300L;
    private static final String TEST_NOTE = "nota";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);
    private static final int DEFAULT_CHAPTER = 1;
    private static final String DEFAULT_HUD = "";
    private static final long DEFAULT_SECONDS = 0L;
    private static final long ADD_SECONDS = 50L;
    private static final long EXPECTED_TOTAL_SECONDS = TEST_SECONDS + ADD_SECONDS;
    private static final String UPDATED_NOTE = "nota actualizada";

    @Mock
    private ProgressRepository progressRepository;

    @Mock
    private UserRepository userRepository;

    private ProgressServiceImpl service;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().id(TEST_USER_ID).email(TEST_EMAIL).build();
        service = new ProgressServiceImpl(progressRepository, userRepository);
    }

    @Test
    void testGetOrCreateProgress_ShouldReturnExistingProgress_WhenAlreadyExists() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        ProgressEntity existingProgress = ProgressEntity.builder()
                .user(user)
                .currentChapter(TEST_CHAPTER)
                .hudLetters(TEST_HUD)
                .investigationSubmitted(false)
                .timeSpentSeconds(TEST_SECONDS)
                .freeNote(TEST_NOTE)
                .updatedAt(TEST_UPDATED_AT)
                .build();

        when(progressRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingProgress));

        ProgressResponseDTO result = service.getOrCreateProgress(authentication);

        assertThat(result.currentChapter(), is(equalTo(TEST_CHAPTER)));
        assertThat(result.hudLetters(), is(equalTo(TEST_HUD)));
        assertThat(result.timeSpentSeconds(), is(equalTo(TEST_SECONDS)));
        assertThat(result.freeNote(), is(equalTo(TEST_NOTE)));
        verify(progressRepository, never()).save(any(ProgressEntity.class));
    }

    @Test
    void testGetOrCreateProgress_ShouldCreateDefaultProgress_WhenNoneExists() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(progressRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.empty());
        when(progressRepository.save(any(ProgressEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProgressResponseDTO result = service.getOrCreateProgress(authentication);

        assertThat(result.currentChapter(), is(equalTo(DEFAULT_CHAPTER)));
        assertThat(result.hudLetters(), is(equalTo(DEFAULT_HUD)));
        assertThat(result.investigationSubmitted(), is(false));
        assertThat(result.timeSpentSeconds(), is(equalTo(DEFAULT_SECONDS)));
        verify(progressRepository).save(any(ProgressEntity.class));
    }

    @Test
    void testAccumulateTime_ShouldAddSecondsToExistingProgress() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        ProgressEntity existingProgress = ProgressEntity.builder()
                .user(user)
                .currentChapter(TEST_CHAPTER)
                .hudLetters(TEST_HUD)
                .investigationSubmitted(false)
                .timeSpentSeconds(TEST_SECONDS)
                .freeNote(TEST_NOTE)
                .updatedAt(TEST_UPDATED_AT)
                .build();

        when(progressRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingProgress));
        when(progressRepository.save(any(ProgressEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AccumulateTimeRequestDTO dto = new AccumulateTimeRequestDTO(ADD_SECONDS);

        ProgressResponseDTO result = service.accumulateTime(authentication, dto);

        assertThat(result.timeSpentSeconds(), is(equalTo(EXPECTED_TOTAL_SECONDS)));
        verify(progressRepository).save(any(ProgressEntity.class));
    }

    @ParameterizedTest
    @ValueSource(strings = { UPDATED_NOTE, "" })
    void testUpdateNote_ShouldReplaceFreeNoteOnExistingProgress(String newNote) {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(TEST_EMAIL);
        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));

        ProgressEntity existingProgress = ProgressEntity.builder()
                .user(user)
                .currentChapter(TEST_CHAPTER)
                .hudLetters(TEST_HUD)
                .investigationSubmitted(false)
                .timeSpentSeconds(TEST_SECONDS)
                .freeNote(TEST_NOTE)
                .updatedAt(TEST_UPDATED_AT)
                .build();

        when(progressRepository.findByUserId(TEST_USER_ID)).thenReturn(Optional.of(existingProgress));
        when(progressRepository.save(any(ProgressEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UpdateNoteRequestDTO dto = new UpdateNoteRequestDTO(newNote);

        ProgressResponseDTO result = service.updateNote(authentication, dto);

        assertThat(result.freeNote(), is(equalTo(newNote)));
        verify(progressRepository).save(any(ProgressEntity.class));
    }
}
