package dev.jenny.clara.register.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import dev.jenny.clara.register.validation.PasswordMatches;

@PasswordMatches
public record RegisterRequestDTO(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "Confirma tu contraseña")
        String confirmPassword) {
}
