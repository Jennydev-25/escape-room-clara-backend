package dev.jenny.clara.progress.mappers;

import dev.jenny.clara.progress.ProgressEntity;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;

public class ProgressMapper {

    public static ProgressResponseDTO toDTO(ProgressEntity entity) {
        return new ProgressResponseDTO(
                entity.getCurrentChapter(),
                entity.getHudLetters(),
                entity.isInvestigationSubmitted(),
                entity.getTimeSpentSeconds(),
                entity.getFreeNote(),
                entity.getUpdatedAt());
    }
}
