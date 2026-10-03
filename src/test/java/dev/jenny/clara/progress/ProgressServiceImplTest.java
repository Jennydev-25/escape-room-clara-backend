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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

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
}
