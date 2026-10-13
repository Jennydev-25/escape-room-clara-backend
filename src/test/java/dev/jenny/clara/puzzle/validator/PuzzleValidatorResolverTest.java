package dev.jenny.clara.puzzle.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.puzzle.PuzzleType;
import dev.jenny.clara.puzzle.exceptions.UnsupportedPuzzleTypeException;

@ExtendWith(MockitoExtension.class)
class PuzzleValidatorResolverTest {

    @Mock
    private PuzzleValidator singlePasswordValidator;

    @Mock
    private PuzzleValidator cipherValidator;

    @Test
    void testResolve_ShouldReturnTheValidatorThatSupportsTheGivenPuzzleType() {
        when(singlePasswordValidator.supports(PuzzleType.SIMPLE_PASSWORD)).thenReturn(true);

        PuzzleValidatorResolver resolver = new PuzzleValidatorResolver(
                List.of(singlePasswordValidator, cipherValidator));

        PuzzleValidator result = resolver.resolve(PuzzleType.SIMPLE_PASSWORD);

        assertThat(result, is(sameInstance(singlePasswordValidator)));
    }

    @Test
    void testResolve_ShouldThrowUnsupportedPuzzleTypeException_WhenNoValidatorSupportsTheType() {
        when(singlePasswordValidator.supports(PuzzleType.CIPHER)).thenReturn(false);
        when(cipherValidator.supports(PuzzleType.CIPHER)).thenReturn(false);

        PuzzleValidatorResolver resolver = new PuzzleValidatorResolver(
                List.of(singlePasswordValidator, cipherValidator));

        assertThrows(UnsupportedPuzzleTypeException.class, () -> resolver.resolve(PuzzleType.CIPHER));
    }
}
