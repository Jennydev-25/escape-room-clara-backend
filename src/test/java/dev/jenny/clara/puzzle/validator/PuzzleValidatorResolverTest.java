package dev.jenny.clara.puzzle.validator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.puzzle.PuzzleType;

@ExtendWith(MockitoExtension.class)
class PuzzleValidatorResolverTest {

    @Mock
    private PuzzleValidator singlePasswordValidator;

    @Mock
    private PuzzleValidator cipherValidator;

    @Test
    void testResolve_ShouldReturnTheValidatorThatSupportsTheGivenPuzzleType() {
        when(singlePasswordValidator.supports(PuzzleType.CONTRASENA_SIMPLE)).thenReturn(true);
        when(cipherValidator.supports(PuzzleType.CONTRASENA_SIMPLE)).thenReturn(false);

        PuzzleValidatorResolver resolver = new PuzzleValidatorResolver(
                List.of(singlePasswordValidator, cipherValidator));

        PuzzleValidator result = resolver.resolve(PuzzleType.CONTRASENA_SIMPLE);

        assertThat(result, is(sameInstance(singlePasswordValidator)));
    }
}
