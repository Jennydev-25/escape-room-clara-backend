package dev.jenny.clara.puzzle;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;
import dev.jenny.clara.puzzle.exceptions.PuzzleNotFoundException;

@ExtendWith(MockitoExtension.class)
class PuzzleServiceImplTest {

    private static final Long MISSING_PUZZLE_ID = 99L;
    private static final String ANSWER_VALUE = "any-value";

    @Mock
    private PuzzleRepository puzzleRepository;

    private PuzzleServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new PuzzleServiceImpl(puzzleRepository);
    }

    @Test
    void testValidateAnswer_ShouldThrowPuzzleNotFoundException_WhenPuzzleDoesNotExist() {
        Authentication authentication = org.mockito.Mockito.mock(Authentication.class);
        when(puzzleRepository.findById(MISSING_PUZZLE_ID)).thenReturn(Optional.empty());

        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO(ANSWER_VALUE);

        assertThrows(PuzzleNotFoundException.class,
                () -> service.validateAnswer(authentication, MISSING_PUZZLE_ID, answer));
    }
}
