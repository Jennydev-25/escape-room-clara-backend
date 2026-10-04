package dev.jenny.clara.user.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class EmailMatchesValidatorTest {

    private static final String TEST_ALIAS = "marta_v";
    private static final Integer TEST_AVATAR_ID = 3;

    private final EmailMatchesValidator validator = new EmailMatchesValidator();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

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

    @Test
    void testIsValid_ShouldReturnTrue_WhenEmailsMatch() {
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO(TEST_ALIAS, "marta@example.com", "marta@example.com",
                TEST_AVATAR_ID);

        boolean result = validator.isValid(dto, null);

        assertThat(result, is(equalTo(true)));
    }

    @Test
    void testIsValid_ShouldReturnFalse_WhenEmailsDoNotMatch() {
        UpdateProfileRequestDTO dto = new UpdateProfileRequestDTO(TEST_ALIAS, "marta@example.com",
                "otro@example.com", TEST_AVATAR_ID);

        boolean result = validator.isValid(dto, context);

        assertThat(result, is(equalTo(false)));
    }

    private static Stream<Arguments> nullEmailCombinations() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("marta@example.com", null),
                Arguments.of(null, "marta@example.com"));
    }
}