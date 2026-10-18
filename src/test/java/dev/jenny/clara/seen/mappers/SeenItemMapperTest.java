package dev.jenny.clara.seen.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.seen.SeenItemEntity;
import dev.jenny.clara.seen.dtos.SeenItemResponseDTO;

class SeenItemMapperTest {

    private static final String TEST_KEY = "folder:leeme";
    private static final LocalDateTime TEST_SEEN_AT = LocalDateTime.of(2026, 10, 15, 18, 30);

    @Test
    void testToDTO_ShouldMapKeyAndSeenAt() {
        SeenItemEntity item = SeenItemEntity.builder()
                .itemKey(TEST_KEY)
                .seenAt(TEST_SEEN_AT)
                .build();

        SeenItemResponseDTO dto = SeenItemMapper.toDTO(item);

        assertThat(dto, isA(SeenItemResponseDTO.class));
        assertThat(dto.key(), is(equalTo(TEST_KEY)));
        assertThat(dto.seenAt(), is(equalTo(TEST_SEEN_AT)));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<SeenItemMapper> constructor = SeenItemMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
