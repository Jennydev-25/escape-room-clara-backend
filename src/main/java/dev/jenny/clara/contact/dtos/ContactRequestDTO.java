package dev.jenny.clara.contact.dtos;

import dev.jenny.clara.contact.ContactType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContactRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String name,

        @NotBlank(message = "El email es obligatorio") @Email(message = "El formato del email no es válido") String email,

        @NotNull(message = "El tipo de consulta es obligatorio") ContactType type,

        @NotBlank(message = "El mensaje es obligatorio") String message,

        @NotBlank(message = "El captcha es obligatorio") String recaptchaToken) {
}