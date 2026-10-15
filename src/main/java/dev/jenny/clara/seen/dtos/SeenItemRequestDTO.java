package dev.jenny.clara.seen.dtos;

import jakarta.validation.constraints.NotBlank;

public record SeenItemRequestDTO(
        @NotBlank(message = "La clave es obligatoria") String key) {
}
