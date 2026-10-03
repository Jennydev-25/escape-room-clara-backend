package dev.jenny.clara.progress.dtos;

import java.time.LocalDateTime;

public record ProgressResponseDTO(
        int currentChapter,
        String hudLetters,
        boolean investigationSubmitted,
        long timeSpentSeconds,
        String freeNote,
        LocalDateTime updatedAt) {
}
