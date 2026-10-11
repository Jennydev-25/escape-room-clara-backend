package dev.jenny.clara.folder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.puzzle.PuzzleRepository;

@ExtendWith(MockitoExtension.class)
class FolderServiceImplTest {

    private static final String MISSING_CODE = "99_NO_EXISTE";

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
}
