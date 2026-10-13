package dev.jenny.clara.contact.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String name,

        @NotBlank(message = "El email es obligatorio") @Email(message = "El formato del email no es válido") String email,

        @NotBlank(message = "El tipo de consulta es obligatorio") String type,

        @NotBlank(message = "El mensaje es obligatorio") String message,

        @NotBlank(message = "El captcha es obligatorio") String recaptchaToken) {
}