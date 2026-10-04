package dev.jenny.clara.user.validation;

import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailMatchesValidator implements ConstraintValidator<EmailMatches, UpdateProfileRequestDTO> {

    @Override
    public boolean isValid(UpdateProfileRequestDTO request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String email = request.email();
        String emailConfirmation = request.emailConfirmation();

        if (email == null || emailConfirmation == null) {
            return true;
        }

        boolean matches = email.equals(emailConfirmation);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("emailConfirmation")
                    .addConstraintViolation();
        }

        return matches;
    }
}