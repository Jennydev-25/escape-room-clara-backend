package dev.jenny.clara.trash;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.trash.dtos.DiscardedFileDTO;

@ExtendWith(MockitoExtension.class)
class TrashServiceImplTest {

    private static final String NAME = "borrador_reportaje_puerto_v1.txt";
    private static final String TYPE = "TXT";
    private static final String BODY = "[BORRADOR — sin terminar]";
    private static final Long FILE_ID = 1L;
    private static final int MIN_CHAPTER = 1;
    private static final int CURRENT_CHAPTER = 2;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);

    @Mock
    private DiscardedFileRepository discardedFileRepository;

    @Mock
    private InterfaceProgressService progressService;

    private TrashServiceImpl service;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        service = new TrashServiceImpl(discardedFileRepository, progressService);
        authentication = org.mockito.Mockito.mock(Authentication.class);
    }

    private ProgressResponseDTO progressAtChapter(int chapter) {
        return new ProgressResponseDTO(chapter, TEST_HUD, false, TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null);
    }

    @Test
    void testListDiscardedFiles_ShouldReturnVisibleFiles_WhenChapterMatches() {
        DiscardedFileEntity file = DiscardedFileEntity.builder()
                .id(FILE_ID).name(NAME).type(TYPE).body(BODY).minChapter(MIN_CHAPTER).build();

        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(discardedFileRepository.findByMinChapterLessThanEqualOrderByMinChapterAscDisplayOrderAsc(CURRENT_CHAPTER))
                .thenReturn(List.of(file));

        List<DiscardedFileDTO> result = service.listDiscardedFiles(authentication);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).id(), is(equalTo(FILE_ID)));
        assertThat(result.get(0).name(), is(equalTo(NAME)));
    }

    @Test
    void testListDiscardedFiles_ShouldReturnEmpty_WhenNoFileReachedYet() {
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progressAtChapter(CURRENT_CHAPTER));
        when(discardedFileRepository.findByMinChapterLessThanEqualOrderByMinChapterAscDisplayOrderAsc(CURRENT_CHAPTER))
                .thenReturn(List.of());

        List<DiscardedFileDTO> result = service.listDiscardedFiles(authentication);

        assertThat(result.isEmpty(), is(true));
    }
}
