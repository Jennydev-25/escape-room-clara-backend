package dev.jenny.clara.email.mappers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import dev.jenny.clara.email.EmailEntity;
import dev.jenny.clara.email.dtos.EmailDetailDTO;
import dev.jenny.clara.email.dtos.EmailSummaryDTO;

class EmailMapperTest {

    private static final Long TEST_ID = 1L;
    private static final String TEST_SENDER = "Marina Ortiz";
    private static final String TEST_RECIPIENT = "Clara Vega";
    private static final String TEST_SUBJECT = "Lo de esta noche";
    private static final String TEST_BODY = "Nos vemos luego en el puerto.";
    private static final LocalDate TEST_SENT_DATE = LocalDate.of(2025, 6, 3);
    private static final int TEST_MIN_CHAPTER = 1;

    @Test
    void testToSummaryDTO_ShouldMapEmailFieldsAndReadFlag() {
        EmailEntity email = EmailEntity.builder()
                .id(TEST_ID)
                .sender(TEST_SENDER)
                .subject(TEST_SUBJECT)
                .body(TEST_BODY)
                .minChapter(TEST_MIN_CHAPTER)
                .build();

        EmailSummaryDTO dto = EmailMapper.toSummaryDTO(email, true);

        assertThat(dto, isA(EmailSummaryDTO.class));
        assertThat(dto.id(), is(equalTo(TEST_ID)));
        assertThat(dto.sender(), is(equalTo(TEST_SENDER)));
        assertThat(dto.subject(), is(equalTo(TEST_SUBJECT)));
        assertThat(dto.read(), is(true));
    }

    @Test
    void testToDetailDTO_ShouldMapAllEmailFields() {
        EmailEntity email = EmailEntity.builder()
                .id(TEST_ID)
                .sender(TEST_SENDER)
                .recipient(TEST_RECIPIENT)
                .sentDate(TEST_SENT_DATE)
                .subject(TEST_SUBJECT)
                .body(TEST_BODY)
                .minChapter(TEST_MIN_CHAPTER)
                .build();

        EmailDetailDTO dto = EmailMapper.toDetailDTO(email);

        assertThat(dto, isA(EmailDetailDTO.class));
        assertThat(dto.id(), is(equalTo(TEST_ID)));
        assertThat(dto.sender(), is(equalTo(TEST_SENDER)));
        assertThat(dto.subject(), is(equalTo(TEST_SUBJECT)));
        assertThat(dto.recipient(), is(equalTo(TEST_RECIPIENT)));
        assertThat(dto.sentDate(), is(equalTo(TEST_SENT_DATE)));
        assertThat(dto.body(), is(equalTo(TEST_BODY)));
    }

    @Test
    void testConstructor_ShouldThrowException_WhenInstantiatedViaReflection() throws Exception {
        Constructor<EmailMapper> constructor = EmailMapper.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertThat(exception.getCause(), isA(UnsupportedOperationException.class));
    }
}
