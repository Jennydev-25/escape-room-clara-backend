package dev.jenny.clara.puzzle.dtos;

import dev.jenny.clara.puzzle.PuzzleType;

public record PuzzleResponseDTO(
        Long id,
        PuzzleType type,
        int minChapter,
        int displayOrder,
        String content) {
}
