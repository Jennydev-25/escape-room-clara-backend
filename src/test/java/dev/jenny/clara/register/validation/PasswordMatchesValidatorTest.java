package dev.jenny.clara.register.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class PasswordMatchesValidatorTest {

    private final PasswordMatchesValidator validator = new PasswordMatchesValidator();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

    @Test
    void testIsValid_ShouldReturnTrue_WhenPasswordsMatch() {
        RegisterRequestDTO dto = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "plainPassword");

        boolean result = validator.isValid(dto, null);

        assertThat(result, is(equalTo(true)));
    }

    @Test
    void testIsValid_ShouldReturnFalse_WhenPasswordsDoNotMatch() {
        RegisterRequestDTO dto = new RegisterRequestDTO("clara@pruebas.com", "plainPassword", "otherPassword");

        boolean result = validator.isValid(dto, context);

        assertThat(result, is(equalTo(false)));
    }
}