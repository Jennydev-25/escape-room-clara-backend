package dev.jenny.clara.register.validation;

import dev.jenny.clara.register.dtos.RegisterRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequestDTO> {

    @Override
    public boolean isValid(RegisterRequestDTO request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String password = request.password();
        String confirmPassword = request.confirmPassword();

        if (password == null || confirmPassword == null) {
            return true;
        }

        boolean matches = password.equals(confirmPassword);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
        }

        return matches;
    }
}