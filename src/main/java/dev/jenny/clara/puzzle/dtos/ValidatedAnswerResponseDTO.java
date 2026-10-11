package dev.jenny.clara.puzzle.dtos;

public record ValidatedAnswerResponseDTO(
        boolean correct,
        Boolean updatesChapter,
        String newHudLetter,
        String unlocks) {
}
