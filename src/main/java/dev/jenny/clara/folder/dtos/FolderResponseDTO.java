package dev.jenny.clara.folder.dtos;

import java.util.List;

import dev.jenny.clara.puzzle.dtos.PuzzleSummaryDTO;

public record FolderResponseDTO(String code, String name, List<PuzzleSummaryDTO> puzzles) {
}
