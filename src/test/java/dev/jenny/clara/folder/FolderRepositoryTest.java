package dev.jenny.clara.folder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;
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
    // Capitulo fuera de rango real (el seed de data.sql solo usa 1 y 2) para no chocar con esos datos
    private static final int MIN_CHAPTER_TO_OPEN = 97;
    private static final String MISSING_CODE = "99_NO_EXISTE";
    private static final String ANOTHER_CODE = "02_FRAUDE";
    private static final String ANOTHER_FOLDER_NAME = "02_FRAUDE";
    private static final int CHAPTER_WITHOUT_FOLDERS = 98;

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

    @Test
    void testFindByMinChapterToOpen_ShouldReturnFolders_WhenMinChapterToOpenMatches() {
        folderRepository.save(FolderEntity.builder()
                .code(EXISTING_CODE)
                .name(FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build());
        folderRepository.save(FolderEntity.builder()
                .code(ANOTHER_CODE)
                .name(ANOTHER_FOLDER_NAME)
                .minChapterToOpen(MIN_CHAPTER_TO_OPEN)
                .build());

        List<FolderEntity> result = folderRepository.findByMinChapterToOpen(MIN_CHAPTER_TO_OPEN);

        assertThat(result.size(), is(equalTo(2)));
    }

    @Test
    void testFindByMinChapterToOpen_ShouldReturnEmpty_WhenNoFolderOpensAtThatChapter() {
        List<FolderEntity> result = folderRepository.findByMinChapterToOpen(CHAPTER_WITHOUT_FOLDERS);

        assertThat(result.isEmpty(), is(true));
    }
}
