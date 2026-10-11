package dev.jenny.clara.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class EmailRepositoryTest {

    // Remitente que no existe en el seed narrativo de data.sql (evita falsos positivos)
    private static final String SENDER = "Remitente De Prueba";
    private static final String SUBJECT = "Lo de esta noche";
    private static final String BODY = "Nos vemos luego en el puerto.";
    private static final int MIN_CHAPTER = 1;
    private static final int LATER_MIN_CHAPTER = 2;
    private static final int CHAPTER_WITHOUT_EMAILS = 0;

    @Autowired
    private EmailRepository emailRepository;

    @Test
    void testFindByMinChapterLessThanEqualOrderByMinChapterAsc_ShouldReturnVisibleEmails_WhenChapterMatches() {
        emailRepository.save(EmailEntity.builder()
                .sender(SENDER)
                .subject(SUBJECT)
                .body(BODY)
                .minChapter(MIN_CHAPTER)
                .build());
        emailRepository.save(EmailEntity.builder()
                .sender(SENDER)
                .subject(SUBJECT)
                .body(BODY)
                .minChapter(LATER_MIN_CHAPTER)
                .build());

        List<EmailEntity> result = emailRepository.findByMinChapterLessThanEqualOrderByMinChapterAsc(MIN_CHAPTER);

        // No comprobamos el total (el seed narrativo aporta otros correos en ese capitulo):
        // comprobamos que el nuestro aparece y que ninguno supera el capitulo pedido
        boolean containsTestEmail = result.stream()
                .anyMatch(email -> email.getSender().equals(SENDER) && email.getMinChapter() == MIN_CHAPTER);

        assertThat(containsTestEmail, is(true));
        assertThat(result.stream().anyMatch(email -> email.getMinChapter() > MIN_CHAPTER), is(false));
    }

    @Test
    void testFindByMinChapterLessThanEqualOrderByMinChapterAsc_ShouldReturnEmpty_WhenNoEmailReachedYet() {
        List<EmailEntity> result = emailRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAsc(CHAPTER_WITHOUT_EMAILS);

        assertThat(result.isEmpty(), is(true));
    }
}
