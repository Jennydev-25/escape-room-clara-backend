package dev.jenny.clara.user.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;

class EmailMatchesValidatorTest {

    private static final String TEST_ALIAS = "marta_v";
    private static final Integer TEST_AVATAR_ID = 3;

    private final EmailMatchesValidator validator = new EmailMatchesValidator();

    @Test
    void testIsValid_ShouldReturnTrue_WhenRequestIsNull() {
        boolean result = validator.isValid(null, null);

        assertThat(result, is(equalTo(true)));
    }

    @ParameterizedTest
    @MethodSource("nullEmailCombinations")
    void testIsValid_ShouldReturnTrue_WhenEmailOrEmailConfirmationIsNull(String email, String emailConfirmation) {
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO(TEST_ALIAS, email, emailConfirmation,
                TEST_AVATAR_ID);

        boolean result = validator.isValid(dto, null);

        assertThat(result, is(equalTo(true)));
    }

    private static Stream<Arguments> nullEmailCombinations() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("marta@example.com", null),
                Arguments.of(null, "marta@example.com"));
    }
}