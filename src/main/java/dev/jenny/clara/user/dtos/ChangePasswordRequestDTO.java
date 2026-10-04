package dev.jenny.clara.user.dtos;

import dev.jenny.clara.user.validation.NewPasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NewPasswordMatches
public record ChangePasswordRequestDTO(
        @NotBlank(message = "La contraseña actual es obligatoria") String currentPassword,

        @NotBlank(message = "La contraseña nueva es obligatoria") @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String newPassword,

        @NotBlank(message = "Confirma tu contraseña nueva") String newPasswordConfirmation) {
}