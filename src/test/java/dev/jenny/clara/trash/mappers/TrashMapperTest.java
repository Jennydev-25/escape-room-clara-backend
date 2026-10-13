package dev.jenny.clara.trash.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.trash.DiscardedFileEntity;
import dev.jenny.clara.trash.dtos.DiscardedFileDTO;

class TrashMapperTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "borrador_reportaje_puerto_v1.txt";
    private static final String TEST_TYPE = "TXT";
    private static final String TEST_BODY = "[BORRADOR — sin terminar]";
    private static final int TEST_MIN_CHAPTER = 1;

    @Test
    void testToDTO_ShouldMapAllDiscardedFileFields() {
        DiscardedFileEntity file = DiscardedFileEntity.builder()
                .id(TEST_ID)
                .name(TEST_NAME)
                .type(TEST_TYPE)
                .body(TEST_BODY)
                .minChapter(TEST_MIN_CHAPTER)
                .build();

        DiscardedFileDTO dto = TrashMapper.toDTO(file);

        assertThat(dto, isA(DiscardedFileDTO.class));
        assertThat(dto.id(), is(equalTo(TEST_ID)));
        assertThat(dto.name(), is(equalTo(TEST_NAME)));
        assertThat(dto.type(), is(equalTo(TEST_TYPE)));
        assertThat(dto.body(), is(equalTo(TEST_BODY)));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<TrashMapper> constructor = TrashMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
