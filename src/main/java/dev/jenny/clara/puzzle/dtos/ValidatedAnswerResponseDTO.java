package dev.jenny.clara.puzzle.dtos;

import java.util.List;

public record ValidatedAnswerResponseDTO(
        boolean correct,
        Boolean updatesChapter,
        String newHudLetter,
        List<String> unlocks) {
}
