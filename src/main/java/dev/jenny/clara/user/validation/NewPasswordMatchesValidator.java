package dev.jenny.clara.user.validation;

import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NewPasswordMatchesValidator implements ConstraintValidator<NewPasswordMatches, ChangePasswordRequestDTO> {

    @Override
    public boolean isValid(ChangePasswordRequestDTO request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String newPassword = request.newPassword();
        String newPasswordConfirmation = request.newPasswordConfirmation();

        if (newPassword == null || newPasswordConfirmation == null) {
            return true;
        }

        boolean matches = newPassword.equals(newPasswordConfirmation);

        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("newPasswordConfirmation")
                    .addConstraintViolation();
        }

        return matches;
    }
}