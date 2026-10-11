package dev.jenny.clara.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class EmailRepositoryTest {

    private static final String SENDER = "Marina Ortiz";
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

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).getMinChapter(), is(equalTo(MIN_CHAPTER)));
    }

    @Test
    void testFindByMinChapterLessThanEqualOrderByMinChapterAsc_ShouldReturnEmpty_WhenNoEmailReachedYet() {
        List<EmailEntity> result = emailRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAsc(CHAPTER_WITHOUT_EMAILS);

        assertThat(result.isEmpty(), is(true));
    }
}
