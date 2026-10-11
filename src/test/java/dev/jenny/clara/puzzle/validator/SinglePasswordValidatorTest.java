package dev.jenny.clara.puzzle.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.jenny.clara.expectedanswer.ExpectedAnswerEntity;
import dev.jenny.clara.puzzle.PuzzleEntity;
import dev.jenny.clara.puzzle.dtos.PuzzleAnswerRequestDTO;

@ExtendWith(MockitoExtension.class)
class SinglePasswordValidatorTest {

    private static final String EXPECTED_ANSWER = "UNA SOLA PRUEBA";
    private static final String STORED_HASH = "hashed-una-sola-prueba";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testIsCorrect_ShouldReturnTrue_WhenAnswerMatchesStoredHash() {
        SinglePasswordValidator validator = new SinglePasswordValidator(passwordEncoder);
        PuzzleEntity puzzle = PuzzleEntity.builder().build();
        ExpectedAnswerEntity expectedAnswer = ExpectedAnswerEntity.builder().answerHash(STORED_HASH).build();
        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO(EXPECTED_ANSWER);

        when(passwordEncoder.matches(EXPECTED_ANSWER, STORED_HASH)).thenReturn(true);

        boolean result = validator.isCorrect(puzzle, answer, List.of(expectedAnswer));

        assertThat(result, is(equalTo(true)));
    }

    @Test
    void testIsCorrect_ShouldReturnTrue_WhenAnswerIsLowercase() {
        SinglePasswordValidator validator = new SinglePasswordValidator(passwordEncoder);
        PuzzleEntity puzzle = PuzzleEntity.builder().build();
        ExpectedAnswerEntity expectedAnswer = ExpectedAnswerEntity.builder().answerHash(STORED_HASH).build();
        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO("una sola prueba");

        when(passwordEncoder.matches(EXPECTED_ANSWER, STORED_HASH)).thenReturn(true);

        boolean result = validator.isCorrect(puzzle, answer, List.of(expectedAnswer));

        assertThat(result, is(equalTo(true)));
    }

    @Test
    void testIsCorrect_ShouldReturnTrue_WhenAnswerHasSurroundingWhitespace() {
        SinglePasswordValidator validator = new SinglePasswordValidator(passwordEncoder);
        PuzzleEntity puzzle = PuzzleEntity.builder().build();
        ExpectedAnswerEntity expectedAnswer = ExpectedAnswerEntity.builder().answerHash(STORED_HASH).build();
        PuzzleAnswerRequestDTO answer = new PuzzleAnswerRequestDTO("  " + EXPECTED_ANSWER + "  ");

        when(passwordEncoder.matches(EXPECTED_ANSWER, STORED_HASH)).thenReturn(true);

        boolean result = validator.isCorrect(puzzle, answer, List.of(expectedAnswer));

        assertThat(result, is(equalTo(true)));
    }
}
