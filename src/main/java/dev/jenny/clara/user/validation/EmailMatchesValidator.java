package dev.jenny.clara.user.validation;

import dev.jenny.clara.user.dtos.UpdateProfileRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailMatchesValidator implements ConstraintValidator<EmailMatches, UpdateProfileRequestDTO> {

    @Override
    public boolean isValid(UpdateProfileRequestDTO request, ConstraintValidatorContext context) {
        return true;
    }
}