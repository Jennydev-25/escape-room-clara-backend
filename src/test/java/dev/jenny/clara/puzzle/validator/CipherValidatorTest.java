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
import dev.jenny.clara.puzzle.dtos.PlayerAnswerRequestDTO;

@ExtendWith(MockitoExtension.class)
class CipherValidatorTest {

    private static final String DECIPHERED_ANSWER = "CUIDADO CON RAMON";
    private static final String STORED_HASH = "hashed-cuidado-con-ramon";

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testIsCorrect_ShouldReturnTrue_WhenDecipheredAnswerMatchesStoredHash() {
        CipherValidator validator = new CipherValidator(passwordEncoder);
        PuzzleEntity puzzle = PuzzleEntity.builder().build();
        ExpectedAnswerEntity expectedAnswer = ExpectedAnswerEntity.builder().answerHash(STORED_HASH).build();
        PlayerAnswerRequestDTO answer = new PlayerAnswerRequestDTO(DECIPHERED_ANSWER);

        when(passwordEncoder.matches(DECIPHERED_ANSWER, STORED_HASH)).thenReturn(true);

        boolean result = validator.isCorrect(puzzle, answer, List.of(expectedAnswer));

        assertThat(result, is(equalTo(true)));
    }
}
