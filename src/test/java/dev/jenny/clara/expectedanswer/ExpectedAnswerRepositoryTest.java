package dev.jenny.clara.expectedanswer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.FolderRepository;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.PuzzleRepository;
import dev.jenny.clara.puzzle.PuzzleType;

@DataJpaTest
@ActiveProfiles("h2")
class ExpectedAnswerRepositoryTest {

    private static final String EXPECTED_ANSWER_ONE_HASH = "hashed-una-sola-prueba";
    private static final String EXPECTED_ANSWER_TWO_HASH = "hashed-cuidado-con-ramon";
    private static final int EXPECTED_SIZE_FOR_PUZZLE_ONE = 1;

    @Autowired
    private ExpectedAnswerRepository expectedAnswerRepository;

    @Autowired
    private PuzzleRepository puzzleRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Test
    void testFindByPuzzleId_ShouldReturnOnlyAnswersForThatPuzzle() {
        FolderEntity folder = folderRepository.save(FolderEntity.builder()
                .code("00_LEEME")
                .name("00_LEEME")
                .minChapterToOpen(1)
                .build());

        PuzzleEntity puzzleOne = puzzleRepository.save(PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.CONTRASENA_SIMPLE)
                .minChapter(1)
                .displayOrder(1)
                .build());

        PuzzleEntity puzzleTwo = puzzleRepository.save(PuzzleEntity.builder()
                .folder(folder)
                .type(PuzzleType.CIFRADO)
                .minChapter(2)
                .displayOrder(2)
                .build());

        expectedAnswerRepository.save(ExpectedAnswerEntity.builder()
                .puzzle(puzzleOne)
                .answerHash(EXPECTED_ANSWER_ONE_HASH)
                .build());

        expectedAnswerRepository.save(ExpectedAnswerEntity.builder()
                .puzzle(puzzleTwo)
                .answerHash(EXPECTED_ANSWER_TWO_HASH)
                .build());

        List<ExpectedAnswerEntity> result = expectedAnswerRepository.findByPuzzleId(puzzleOne.getId());

        assertThat(result.size(), is(EXPECTED_SIZE_FOR_PUZZLE_ONE));
        assertThat(result.get(0).getAnswerHash(), is(EXPECTED_ANSWER_ONE_HASH));
    }
}
