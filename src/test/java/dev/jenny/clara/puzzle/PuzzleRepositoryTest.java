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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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
    private static final String FIRST_FILE = "/tomas/tomas_elorza.png";
    private static final String SECOND_FILE = "/tomas/libreta_tomas.png";

    @Autowired
    private PuzzleRepository puzzleRepository;

    @Autowired
    private FolderRepository folderRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Test
    void testFindById_ShouldReturnFilesOrderedByDisplayOrder_WhenPuzzleHasFilesSavedOutOfOrder() {
        FolderEntity folder = folderRepository.save(FolderEntity.builder()
                .code("01_TOMAS")
                .name("01_TOMAS")
                .minChapterToOpen(2)
                .build());

        PuzzleEntity puzzle = PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.SIMPLE_CONTENT)
                .minChapter(MIN_CHAPTER_VISIBLE_FIRST)
                .displayOrder(DISPLAY_ORDER_FIRST)
                .build();
        puzzle.getFiles().add(PuzzleFileEntity.builder()
                .puzzle(puzzle)
                .path(SECOND_FILE)
                .displayOrder(DISPLAY_ORDER_SECOND)
                .build());
        puzzle.getFiles().add(PuzzleFileEntity.builder()
                .puzzle(puzzle)
                .path(FIRST_FILE)
                .displayOrder(DISPLAY_ORDER_FIRST)
                .build());
        PuzzleEntity saved = puzzleRepository.saveAndFlush(puzzle);
        entityManager.clear();

        PuzzleEntity result = puzzleRepository.findById(saved.getId()).orElseThrow();

        assertThat(result.getFiles().stream().map(PuzzleFileEntity::getPath).toList(),
                contains(FIRST_FILE, SECOND_FILE));
    }
}
