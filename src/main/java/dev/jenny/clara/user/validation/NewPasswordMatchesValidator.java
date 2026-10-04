package dev.jenny.clara.user.validation;

import dev.jenny.clara.user.dtos.ChangePasswordRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NewPasswordMatchesValidator implements ConstraintValidator<NewPasswordMatches, ChangePasswordRequestDTO> {

    @Override
    public boolean isValid(ChangePasswordRequestDTO request, ConstraintValidatorContext context) {
        throw new UnsupportedOperationException("Todavía no implementado");
    }
}