package dev.jenny.clara.puzzle.dtos;

import java.util.List;

import dev.jenny.clara.puzzle.PuzzleType;

public record PuzzleResponseDTO(
        Long id,
        PuzzleType type,
        int minChapter,
        int displayOrder,
        List<String> files,
        String question) {
}
