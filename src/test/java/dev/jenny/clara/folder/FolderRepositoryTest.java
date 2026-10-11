package dev.jenny.clara.folder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("h2")
class FolderRepositoryTest {

    private static final String EXISTING_CODE = "01_TOMAS";
    private static final String FOLDER_NAME = "01_TOMAS";
    private static final int MIN_CHAPTER_TO_OPEN = 2;
    private static final String MISSING_CODE = "99_NO_EXISTE";

    @Autowired
    private FolderRepository folderRepository;

    @Test
    void testFindByCode_ShouldReturnFolder_WhenCodeExists() {
        folderRepository.save(FolderEntity.builder()
                .code(EXISTING_CODE)
                .name(FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build());

        Optional<FolderEntity> result = folderRepository.findByCode(EXISTING_CODE);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get().getCode(), is(equalTo(EXISTING_CODE)));
    }

    @Test
    void testFindByCode_ShouldReturnEmpty_WhenCodeDoesNotExist() {
        Optional<FolderEntity> result = folderRepository.findByCode(MISSING_CODE);

        assertThat(result.isPresent(), is(false));
    }
}
