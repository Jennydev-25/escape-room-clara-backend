package dev.jenny.clara.user.dtos;

import jakarta.validation.constraints.NotBlank;

public record CompleteProfileRequestDTO(
        @NotBlank(message = "El nombre completo es obligatorio") String fullName,

        @NotBlank(message = "El alias es obligatorio") String alias) {
}
