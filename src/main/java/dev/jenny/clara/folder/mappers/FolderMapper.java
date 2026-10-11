package dev.jenny.clara.folder.mappers;

import java.util.List;

import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.dtos.PuzzleSummaryDTO;

public class FolderMapper {

    private FolderMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static FolderResponseDTO toDTO(FolderEntity folder, List<PuzzleEntity> visiblePuzzles) {
        List<PuzzleSummaryDTO> puzzles = visiblePuzzles.stream()
                .map(puzzle -> new PuzzleSummaryDTO(puzzle.getId(), puzzle.getTitle(), puzzle.getType(), puzzle.getDisplayOrder()))
                .toList();

        return new FolderResponseDTO(folder.getCode(), folder.getName(), puzzles);
    }
}
