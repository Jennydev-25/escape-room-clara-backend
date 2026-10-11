package dev.jenny.clara.folder.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.dtos.FolderResponseDTO;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleType;

class FolderMapperTest {

    private static final String TEST_CODE = "01_TOMAS";
    private static final String TEST_NAME = "01_TOMAS";
    private static final int TEST_MIN_CHAPTER_TO_OPEN = 2;
    private static final Long TEST_PUZZLE_ID = 1L;
    private static final PuzzleType TEST_PUZZLE_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int TEST_DISPLAY_ORDER = 1;
    private static final String TEST_PUZZLE_TITLE = "Libreta de Tomás";

    @Test
    void testToDTO_ShouldMapFolderFieldsAndVisiblePuzzles() {
        FolderEntity folder = FolderEntity.builder()
                .code(TEST_CODE)
                .name(TEST_NAME)
                .minChapterToOpen(TEST_MIN_CHAPTER_TO_OPEN)
                .build();

        PuzzleEntity puzzle = PuzzleEntity.builder()
                .id(TEST_PUZZLE_ID)
                .title(TEST_PUZZLE_TITLE)
                .type(TEST_PUZZLE_TYPE)
                .displayOrder(TEST_DISPLAY_ORDER)
                .build();

        FolderResponseDTO dto = FolderMapper.toDTO(folder, List.of(puzzle));

        assertThat(dto, isA(FolderResponseDTO.class));
        assertThat(dto.code(), is(equalTo(TEST_CODE)));
        assertThat(dto.name(), is(equalTo(TEST_NAME)));
        assertThat(dto.puzzles().size(), is(equalTo(1)));
        assertThat(dto.puzzles().get(0).id(), is(equalTo(TEST_PUZZLE_ID)));
        assertThat(dto.puzzles().get(0).title(), is(equalTo(TEST_PUZZLE_TITLE)));
        assertThat(dto.puzzles().get(0).type(), is(equalTo(TEST_PUZZLE_TYPE)));
        assertThat(dto.puzzles().get(0).displayOrder(), is(equalTo(TEST_DISPLAY_ORDER)));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<FolderMapper> constructor = FolderMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
