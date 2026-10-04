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

import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class NewPasswordMatchesValidatorTest {
    private static final String TEST_CURRENT_PASSWORD = "oldPass123";

    private final NewPasswordMatchesValidator validator = new NewPasswordMatchesValidator();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

    @Test
    void testIsValid_ShouldReturnTrue_WhenRequestIsNull() {
        boolean result = validator.isValid(null, null);

        assertThat(result, is(equalTo(true)));
    }

    @ParameterizedTest
    @MethodSource("nullPasswordCombinations")
    void testIsValid_ShouldReturnTrue_WhenNewPasswordOrConfirmationIsNull(String newPassword,
            String newPasswordConfirmation) {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(TEST_CURRENT_PASSWORD, newPassword,
                newPasswordConfirmation);

        boolean result = validator.isValid(dto, null);

        assertThat(result, is(equalTo(true)));
    }

    private static Stream<Arguments> nullPasswordCombinations() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("newPass123", null),
                Arguments.of(null, "newPass123"));
    }
    
    @Test
    void testIsValid_ShouldReturnTrue_WhenNewPasswordsMatch() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO(TEST_CURRENT_PASSWORD, "newPass123",
                "newPass123");

        boolean result = validator.isValid(dto, null);

        assertThat(result, is(equalTo(true)));
    }
}