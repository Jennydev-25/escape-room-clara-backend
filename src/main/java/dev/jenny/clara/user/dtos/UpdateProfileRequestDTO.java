package dev.jenny.clara.user.dtos;

import dev.jenny.clara.user.validation.EmailMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@EmailMatches
public record UpdateProfileRequestDTO(
        @NotBlank(message = "El alias es obligatorio") String alias,

        @NotBlank(message = "El email es obligatorio") @Email(message = "El formato del email no es válido") String email,

        @NotBlank(message = "Confirma tu email") String emailConfirmation,

        @NotNull(message = "El avatar es obligatorio") Integer avatarId) {
}