package dev.jenny.clara.trash;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.trash.dtos.DiscardedFileDTO;
import dev.jenny.clara.trash.mappers.TrashMapper;

@Service
public class TrashServiceImpl implements InterfaceTrashService {

    private final DiscardedFileRepository discardedFileRepository;
    private final InterfaceProgressService progressService;

    public TrashServiceImpl(DiscardedFileRepository discardedFileRepository, InterfaceProgressService progressService) {
        this.discardedFileRepository = discardedFileRepository;
        this.progressService = progressService;
    }

    @Override
    public List<DiscardedFileDTO> listDiscardedFiles(Authentication authentication) {
        ProgressResponseDTO progress = progressService.getOrCreateProgress(authentication);

        List<DiscardedFileEntity> visibleFiles = discardedFileRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAscDisplayOrderAsc(progress.currentChapter());

        return visibleFiles.stream()
                .map(TrashMapper::toDTO)
                .toList();
    }
}
