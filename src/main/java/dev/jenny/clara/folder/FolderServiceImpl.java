package dev.jenny.clara.folder;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.folder.exceptions.ChapterNotReachedException;
import dev.jenny.clara.folder.exceptions.FolderNotFoundException;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.PuzzleRepository;

@Service
public class FolderServiceImpl implements InterfaceFolderService {

    private final FolderRepository folderRepository;
    private final PuzzleRepository puzzleRepository;
    private final InterfaceProgressService progressService;

    public FolderServiceImpl(FolderRepository folderRepository, PuzzleRepository puzzleRepository,
            InterfaceProgressService progressService) {
        this.folderRepository = folderRepository;
        this.puzzleRepository = puzzleRepository;
        this.progressService = progressService;
    }

    @Override
    public FolderResponseDTO getVisibleFolder(Authentication authentication, String code) {
        FolderEntity folder = folderRepository.findByCode(code)
                .orElseThrow(() -> new FolderNotFoundException("No se encontró ninguna carpeta con el código " + code));

        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        if (progress.currentChapter() < folder.getMinChapterToOpen()) {
            throw new ChapterNotReachedException(
                    "La carpeta " + code + " todavía no está disponible en el capítulo actual");
        }

        return null;
    }
}
