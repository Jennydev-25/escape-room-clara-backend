package dev.jenny.clara.folder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.folder.dtos.FolderSummaryDTO;
import dev.jenny.clara.folder.exceptions.ChapterNotReachedException;
import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleRepository;
import dev.jenny.clara.puzzle.PuzzleType;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    private static final String MISSING_CODE = "99_NO_EXISTE";
    private static final String EXISTING_CODE = "01_TOMAS";
    private static final String FOLDER_NAME = "01_TOMAS";
    private static final Long FOLDER_ID = 1L;
    private static final int MIN_CHAPTER_TO_OPEN = 2;
    private static final int CURRENT_CHAPTER_BELOW_OPENING = 1;
    private static final int CURRENT_CHAPTER_REACHED = 2;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);
    private static final Long VISIBLE_PUZZLE_ID = 5L;
    private static final PuzzleType VISIBLE_PUZZLE_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int VISIBLE_PUZZLE_DISPLAY_ORDER = 1;

    @Mock
    private FolderRepository folderRepository;

    @Mock
    private PuzzleRepository puzzleRepository;

    @Mock
    private InterfaceProgressService progressService;

    private FolderServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new FolderServiceImpl(folderRepository, puzzleRepository, progressService);
    }

    @Test
    void testGetVisibleFolder_ShouldThrowFolderNotFoundException_WhenCodeDoesNotExist() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(folderRepository.findByCode(MISSING_CODE)).thenReturn(Optional.empty());

        assertThrows(FolderNotFoundException.class,
                () -> service.getVisibleFolder(authentication, MISSING_CODE));
    }

    @Test
    void testGetVisibleFolder_ShouldThrowChapterNotReachedException_WhenCurrentChapterIsBelowMinChapterToOpen() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        FolderEntity folder = FolderEntity.builder()
                .code(EXISTING_CODE)
                .name(FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build();

        when(folderRepository.findByCode(EXISTING_CODE)).thenReturn(Optional.of(folder));

        ProgressResponseDTO progress = new ProgressResponseDTO(CURRENT_CHAPTER_BELOW_OPENING, TEST_HUD, false,
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null, false);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progress);

        assertThrows(ChapterNotReachedException.class,
                () -> service.getVisibleFolder(authentication, EXISTING_CODE));
    }

    @Test
    void testGetVisibleFolder_ShouldReturnFolderWithVisiblePuzzles_WhenChapterIsReached() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        FolderEntity folder = FolderEntity.builder()
                .id(FOLDER_ID)
                .code(EXISTING_CODE)
                .name(FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build();

        when(folderRepository.findByCode(EXISTING_CODE)).thenReturn(Optional.of(folder));

        ProgressResponseDTO progress = new ProgressResponseDTO(CURRENT_CHAPTER_REACHED, TEST_HUD, false,
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null, false);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progress);

        PuzzleEntity visiblePuzzle = PuzzleEntity.builder()
                .id(VISIBLE_PUZZLE_ID)
                .type(VISIBLE_PUZZLE_TYPE)
                .displayOrder(VISIBLE_PUZZLE_DISPLAY_ORDER)
                .build();

        when(puzzleRepository.findByFolderIdAndMinChapterLessThanEqualOrderByDisplayOrderAsc(FOLDER_ID,
                CURRENT_CHAPTER_REACHED)).thenReturn(List.of(visiblePuzzle));

        FolderResponseDTO result = service.getVisibleFolder(authentication, EXISTING_CODE);

        assertThat(result.code(), is(equalTo(EXISTING_CODE)));
        assertThat(result.name(), is(equalTo(FOLDER_NAME)));
        assertThat(result.puzzles().size(), is(equalTo(1)));
        assertThat(result.puzzles().get(0).id(), is(equalTo(VISIBLE_PUZZLE_ID)));
    }

    @Test
    void testListVisibleFolders_ShouldReturnReachedFolders_WhenChapterIsReached() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        FolderEntity reachedFolder = FolderEntity.builder()
                .id(FOLDER_ID)
                .code(EXISTING_CODE)
                .name(FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build();

        ProgressResponseDTO progress = new ProgressResponseDTO(CURRENT_CHAPTER_REACHED, TEST_HUD, false,
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null, false);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progress);
        when(folderRepository.findByMinChapterToOpenLessThanEqualOrderByMinChapterToOpenAsc(CURRENT_CHAPTER_REACHED))
                .thenReturn(List.of(reachedFolder));

        List<FolderSummaryDTO> result = service.listVisibleFolders(authentication);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).code(), is(equalTo(EXISTING_CODE)));
        assertThat(result.get(0).name(), is(equalTo(FOLDER_NAME)));
    }
}
