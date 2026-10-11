package dev.jenny.clara.folder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.folder.exceptions.ChapterNotReachedException;
import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.PuzzleRepository;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    private static final String MISSING_CODE = "99_NO_EXISTE";
    private static final String EXISTING_CODE = "01_TOMAS";
    private static final String FOLDER_NAME = "01_TOMAS";
    private static final int MIN_CHAPTER_TO_OPEN = 2;
    private static final int CURRENT_CHAPTER_BELOW_OPENING = 1;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);

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
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(progress);

        assertThrows(ChapterNotReachedException.class,
                () -> service.getVisibleFolder(authentication, EXISTING_CODE));
    }
}
