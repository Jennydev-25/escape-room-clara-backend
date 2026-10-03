package dev.jenny.clara.progress.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.progress.ProgressEntity;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;

class ProgressMapperTest {

    private static final int TEST_CHAPTER = 3;
    private static final String TEST_HUD = "ABC";
    private static final boolean TEST_SUBMITTED = true;
    private static final long TEST_SECONDS = 120L;
    private static final String TEST_NOTE = "mi nota";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 10, 0);

    @Test
    void testToDTO_ShouldMapEntityFieldsToResponseDTO() {
        ProgressEntity entity = ProgressEntity.builder()
                .currentChapter(TEST_CHAPTER)
                .hudLetters(TEST_HUD)
                .investigationSubmitted(TEST_SUBMITTED)
                .timeSpentSeconds(TEST_SECONDS)
                .freeNote(TEST_NOTE)
                .updatedAt(TEST_UPDATED_AT)
                .build();

        ProgressResponseDTO dto = ProgressMapper.toDTO(entity);

        assertThat(dto, isA(ProgressResponseDTO.class));
        assertThat(dto.currentChapter(), is(equalTo(entity.getCurrentChapter())));
        assertThat(dto.hudLetters(), is(equalTo(entity.getHudLetters())));
        assertThat(dto.investigationSubmitted(), is(equalTo(entity.isInvestigationSubmitted())));
        assertThat(dto.timeSpentSeconds(), is(equalTo(entity.getTimeSpentSeconds())));
        assertThat(dto.freeNote(), is(equalTo(entity.getFreeNote())));
        assertThat(dto.updatedAt(), is(equalTo(entity.getUpdatedAt())));
    }
}
