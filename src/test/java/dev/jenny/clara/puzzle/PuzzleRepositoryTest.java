package dev.jenny.clara.puzzle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.FolderRepository;

@DataJpaTest
@ActiveProfiles("h2")
class PuzzleRepositoryTest {

    private static final int CURRENT_CHAPTER = 2;
    private static final int MIN_CHAPTER_VISIBLE_FIRST = 2;
    private static final int MIN_CHAPTER_VISIBLE_SECOND = 2;
    private static final int MIN_CHAPTER_NOT_VISIBLE_YET = 3;
    private static final int DISPLAY_ORDER_FIRST = 1;
    private static final int DISPLAY_ORDER_SECOND = 2;
    private static final int DISPLAY_ORDER_NOT_VISIBLE = 3;

    @Autowired
    private PuzzleRepository puzzleRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Test
    void testFindByFolderIdAndMinChapterLessThanEqualOrderByDisplayOrderAsc_ShouldReturnOnlyVisiblePuzzlesInOrder() {
        FolderEntity folder = folderRepository.save(FolderEntity.builder()
                .code("01_TOMAS")
                .name("01_TOMAS")
                .minChapterToOpen(2)
                .build());

        PuzzleEntity secondVisible = puzzleRepository.save(PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.SIMPLE_PASSWORD)
                .minChapter(MIN_CHAPTER_VISIBLE_SECOND)
                .displayOrder(DISPLAY_ORDER_SECOND)
                .build());

        PuzzleEntity firstVisible = puzzleRepository.save(PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.SIMPLE_PASSWORD)
                .minChapter(MIN_CHAPTER_VISIBLE_FIRST)
                .displayOrder(DISPLAY_ORDER_FIRST)
                .build());

        puzzleRepository.save(PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.CIPHER)
                .minChapter(MIN_CHAPTER_NOT_VISIBLE_YET)
                .displayOrder(DISPLAY_ORDER_NOT_VISIBLE)
                .build());

        List<PuzzleEntity> result = puzzleRepository
                .findByFolderIdAndMinChapterLessThanEqualOrderByDisplayOrderAsc(folder.getId(), CURRENT_CHAPTER);

        assertThat(result.stream().map(PuzzleEntity::getId).toList(),
                contains(firstVisible.getId(), secondVisible.getId()));
    }
}
