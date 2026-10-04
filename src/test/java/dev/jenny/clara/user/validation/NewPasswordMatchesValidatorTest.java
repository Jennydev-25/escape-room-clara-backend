package dev.jenny.clara.user.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class NewPasswordMatchesValidatorTest {

    private final NewPasswordMatchesValidator validator = new NewPasswordMatchesValidator();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ConstraintValidatorContext context;

    @Test
    void testIsValid_ShouldReturnTrue_WhenRequestIsNull() {
        boolean result = validator.isValid(null, null);

        assertThat(result, is(equalTo(true)));
    }
}