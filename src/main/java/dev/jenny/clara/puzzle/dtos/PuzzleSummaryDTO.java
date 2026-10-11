package dev.jenny.clara.puzzle.dtos;

import dev.jenny.clara.puzzle.PuzzleType;

public record PuzzleSummaryDTO(Long id, PuzzleType type, int displayOrder) {
}
