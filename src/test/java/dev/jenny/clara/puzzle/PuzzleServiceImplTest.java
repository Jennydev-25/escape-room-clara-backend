package dev.jenny.clara.puzzle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.expectedanswer.ExpectedAnswerRepository;
import dev.jenny.clara.folder.FolderEntity;
import dev.jenny.clara.folder.FolderRepository;
import dev.jenny.clara.progress.InterfaceProgressService;
import dev.jenny.clara.progress.dtos.ProgressResponseDTO;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.dtos.ValidatedAnswerResponseDTO;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;
import dev.jenny.clara.puzzle.validator.PuzzleValidator;
import dev.jenny.clara.puzzle.validator.PuzzleValidatorResolver;

@ExtendWith(MockitoExtension.class)
class PuzzleServiceImplTest {

    private static final Long MISSING_PUZZLE_ID = 99L;
    private static final Long EXISTING_PUZZLE_ID = 1L;
    private static final String ANSWER_VALUE = "any-value";
    private static final PuzzleType PUZZLE_TYPE = PuzzleType.SIMPLE_PASSWORD;
    private static final int PUZZLE_MIN_CHAPTER = 1;
    private static final String TEST_HUD = "";
    private static final long TEST_SECONDS = 0L;
    private static final String TEST_NOTE = "";
    private static final LocalDateTime TEST_UPDATED_AT = LocalDateTime.of(2026, 1, 1, 12, 0);
    private static final String NEW_HUD_LETTER = "M";
    private static final String UNLOCKED_FOLDER_CODE = "02_FRAUDE";

    @Mock
    private PuzzleRepository puzzleRepository;

    @Mock
    private ExpectedAnswerRepository expectedAnswerRepository;

    @Mock
    private PuzzleValidatorResolver puzzleValidatorResolver;

    @Mock
    private PuzzleValidator puzzleValidator;

    @Mock
    private InterfaceProgressService progressService;

    @Mock
    private FolderRepository folderRepository;

    private PuzzleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PuzzleServiceImpl(puzzleRepository, expectedAnswerRepository, puzzleValidatorResolver,
                progressService, folderRepository);
    }

    @Test
    void testValidateAnswer_ShouldThrowPuzzleNotFoundException_WhenPuzzleDoesNotExist() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(puzzleRepository.findById(MISSING_PUZZLE_ID)).thenReturn(Optional.empty());

        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO(ANSWER_VALUE);

        assertThrows(PuzzleNotFoundException.class,
                () -> service.validateAnswer(authentication, MISSING_PUZZLE_ID, answer));
    }

    @Test
    void testValidateAnswer_ShouldReturnNotCorrect_WhenAnswerIsWrong() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        PuzzleEntity puzzle = PuzzleEntity.builder()
                .id(EXISTING_PUZZLE_ID)
                .type(PUZZLE_TYPE)
                .build();

        when(puzzleRepository.findById(EXISTING_PUZZLE_ID)).thenReturn(Optional.of(puzzle));

        List<ExpectedAnswerEntity> expectedAnswers = List.of(ExpectedAnswerEntity.builder().build());
        when(expectedAnswerRepository.findByPuzzleId(EXISTING_PUZZLE_ID)).thenReturn(expectedAnswers);

        when(puzzleValidatorResolver.resolve(PUZZLE_TYPE)).thenReturn(puzzleValidator);
        when(puzzleValidator.isCorrect(any(PuzzleEntity.class), any(PuzzleAnswerRequestDTO.class), eq(expectedAnswers)))
                .thenReturn(false);

        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO(ANSWER_VALUE);

        ValidatedAnswerResponseDTO result = service.validateAnswer(authentication, EXISTING_PUZZLE_ID, answer);

        assertThat(result.correct(), is(equalTo(false)));
    }

    @Test
    void testValidateAnswer_ShouldAdvanceChapterAndReturnNewHudLetter_WhenAnswerIsCorrectAndChapterMatches() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);

        PuzzleEntity puzzle = PuzzleEntity.builder()
                .id(EXISTING_PUZZLE_ID)
                .type(PUZZLE_TYPE)
                .minChapter(PUZZLE_MIN_CHAPTER)
                .build();

        when(puzzleRepository.findById(EXISTING_PUZZLE_ID)).thenReturn(Optional.of(puzzle));

        List<ExpectedAnswerEntity> expectedAnswers = List.of(ExpectedAnswerEntity.builder().build());
        when(expectedAnswerRepository.findByPuzzleId(EXISTING_PUZZLE_ID)).thenReturn(expectedAnswers);

        when(puzzleValidatorResolver.resolve(PUZZLE_TYPE)).thenReturn(puzzleValidator);
        when(puzzleValidator.isCorrect(any(PuzzleEntity.class), any(PuzzleAnswerRequestDTO.class), eq(expectedAnswers)))
                .thenReturn(true);

        ProgressResponseDTO currentProgress = new ProgressResponseDTO(PUZZLE_MIN_CHAPTER, TEST_HUD, false,
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, null);
        when(progressService.getOrCreateProgress(authentication)).thenReturn(currentProgress);

        ProgressResponseDTO advancedProgress = new ProgressResponseDTO(PUZZLE_MIN_CHAPTER + 1, TEST_HUD, false,
                TEST_SECONDS, TEST_NOTE, TEST_UPDATED_AT, NEW_HUD_LETTER);
        when(progressService.advanceChapter(authentication, PUZZLE_MIN_CHAPTER + 1)).thenReturn(advancedProgress);

        FolderEntity unlockedFolder = FolderEntity.builder().code(UNLOCKED_FOLDER_CODE).build();
        when(folderRepository.findByMinChapterToOpen(PUZZLE_MIN_CHAPTER + 1)).thenReturn(List.of(unlockedFolder));

        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO(ANSWER_VALUE);

        ValidatedAnswerResponseDTO result = service.validateAnswer(authentication, EXISTING_PUZZLE_ID, answer);

        assertThat(result.correct(), is(equalTo(true)));
        assertThat(result.updatesChapter(), is(equalTo(true)));
        assertThat(result.newHudLetter(), is(equalTo(NEW_HUD_LETTER)));
        assertThat(result.unlocks(), is(equalTo(List.of(UNLOCKED_FOLDER_CODE))));
    }
}
