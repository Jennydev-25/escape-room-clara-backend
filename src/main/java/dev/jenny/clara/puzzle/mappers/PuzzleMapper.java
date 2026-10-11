package dev.jenny.clara.puzzle.mappers;

import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;

public class PuzzleMapper {

    private PuzzleMapper() {
        throw new UnsupportedOperationException("No se puede instanciar esta clase de utilidad");
    }

    public static PuzzleResponseDTO toDTO(PuzzleEntity entity) {
        return new PuzzleResponseDTO(
                entity.getId(),
                entity.getType(),
                entity.getMinChapter(),
                entity.getDisplayOrder(),
                entity.getContent(),
                entity.getHint1(),
                entity.getHint2(),
                entity.getHint3());
    }
}
