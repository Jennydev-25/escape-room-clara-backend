package dev.jenny.clara.puzzle.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleType;
import dev.jenny.clara.puzzle.dtos.PuzzleResponseDTO;

class PuzzleMapperTest {

    private static final Long TEST_ID = 1L;
    private static final PuzzleType TEST_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int TEST_MIN_CHAPTER = 1;
    private static final int TEST_DISPLAY_ORDER = 1;
    private static final String TEST_CONTENT = "contenido de prueba";
    private static final String TEST_HINT1 = "revisa lo que ya has investigado";
    private static final String TEST_HINT2 = "mira en la libreta de Tomás";
    private static final String TEST_HINT3 = "la respuesta empieza por C";

    @Test
    void testToDTO_ShouldMapEntityFieldsToResponseDTO() {
        PuzzleEntity entity = PuzzleEntity.builder()
                .id(TEST_ID)
                .type(TEST_TYPE)
                .minChapter(TEST_MIN_CHAPTER)
                .displayOrder(TEST_DISPLAY_ORDER)
                .content(TEST_CONTENT)
                .hint1(TEST_HINT1)
                .hint2(TEST_HINT2)
                .hint3(TEST_HINT3)
                .build();

        PuzzleResponseDTO dto = PuzzleMapper.toDTO(entity);

        assertThat(dto, isA(PuzzleResponseDTO.class));
        assertThat(dto.id(), is(equalTo(entity.getId())));
        assertThat(dto.type(), is(equalTo(entity.getType())));
        assertThat(dto.minChapter(), is(equalTo(entity.getMinChapter())));
        assertThat(dto.displayOrder(), is(equalTo(entity.getDisplayOrder())));
        assertThat(dto.content(), is(equalTo(entity.getContent())));
        assertThat(dto.hint1(), is(equalTo(entity.getHint1())));
        assertThat(dto.hint2(), is(equalTo(entity.getHint2())));
        assertThat(dto.hint3(), is(equalTo(entity.getHint3())));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<PuzzleMapper> constructor = PuzzleMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
