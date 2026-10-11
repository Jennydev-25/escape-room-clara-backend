package dev.jenny.clara.trash;

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
class DiscardedFileRepositoryTest {

    private static final String NAME = "borrador_reportaje_puerto_v1.txt";
    private static final String TYPE = "TXT";
    private static final String BODY = "[BORRADOR — sin terminar]";
    private static final int MIN_CHAPTER = 1;
    private static final int LATER_MIN_CHAPTER = 2;
    private static final int CHAPTER_WITHOUT_FILES = 0;

    @Autowired
    private DiscardedFileRepository discardedFileRepository;

    @Test
    void testFindByMinChapterLessThanEqualOrderByMinChapterAsc_ShouldReturnVisibleFiles_WhenChapterMatches() {
        discardedFileRepository.save(DiscardedFileEntity.builder()
                .name(NAME).type(TYPE).body(BODY).minChapter(MIN_CHAPTER).build());
        discardedFileRepository.save(DiscardedFileEntity.builder()
                .name(NAME).type(TYPE).body(BODY).minChapter(LATER_MIN_CHAPTER).build());

        List<DiscardedFileEntity> result = discardedFileRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAsc(MIN_CHAPTER);

        assertThat(result.size(), is(equalTo(1)));
        assertThat(result.get(0).getMinChapter(), is(equalTo(MIN_CHAPTER)));
    }

    @Test
    void testFindByMinChapterLessThanEqualOrderByMinChapterAsc_ShouldReturnEmpty_WhenNoFileReachedYet() {
        List<DiscardedFileEntity> result = discardedFileRepository
                .findByMinChapterLessThanEqualOrderByMinChapterAsc(CHAPTER_WITHOUT_FILES);

        assertThat(result.isEmpty(), is(true));
    }
}
